package ru.javawebinar.topjava.repository.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(JdbcUserRepository.class);
    private static final RowMapper<User> ROW_MAPPER = new UserRowMapper();
    private static final JdbcValidator VALIDATOR = JdbcValidator.getInstance();
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        VALIDATOR.validate(user);
        log.debug("JdbcUserRepository#save user:{}", user);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password,
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) != 0) {
            namedParameterJdbcTemplate.update("""
                    DELETE FROM user_roles
                    WHERE user_id=:id
                    """, parameterSource);
        } else {
            return null;
        }
        jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, user.id());
                        ps.setString(2, List.copyOf(user.getRoles()).get(i).toString());
                    }

                    @Override
                    public int getBatchSize() {
                        return user.getRoles().size();
                    }
                });
        return VALIDATOR.validate(user);
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        log.trace("JdbcUserRepository#delete id:{}", id);
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        log.trace("JdbcUserRepository#get id:{}", id);
        List<User> users = jdbcTemplate.query("""
                SELECT u.id, u.name, u.email, u.password, u.calories_per_day, u.enabled, u.registered, STRING_AGG(r.role, ',') as roles
                FROM users u
                JOIN user_roles r on u.id = r.user_id
                WHERE u.id = ?
                GROUP BY u.id
                """, ROW_MAPPER, id);
        return VALIDATOR.validate(DataAccessUtils.singleResult(users));
    }

    @Override
    public User getByEmail(String email) {
        log.trace("JdbcUserRepository#getByEmail email:{}", email);
        List<User> users = jdbcTemplate.query("""
                SELECT u.id, u.name, u.email, u.password, u.calories_per_day, u.enabled, u.registered, STRING_AGG(r.role, ',') as roles
                FROM users u
                JOIN user_roles r on u.id = r.user_id
                WHERE u.email = ?
                GROUP BY u.id
                """, ROW_MAPPER, email);
        return VALIDATOR.validate(DataAccessUtils.singleResult(users));
    }

    @Override
    public List<User> getAll() {
        log.trace("JdbcUserRepository#getAll");
        return VALIDATOR.validate(jdbcTemplate.query("""
                SELECT u.id, u.name, u.email, u.password, u.calories_per_day, u.enabled, u.registered, STRING_AGG(r.role, ',') as roles
                FROM users u
                LEFT JOIN user_roles r on u.id = r.user_id
                GROUP BY u.id
                ORDER BY u.name, u.email
                """, ROW_MAPPER));
    }

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int columnNumber) throws SQLException {
            Set<Role> roleSet = new HashSet<>();
            User user = new User(rs.getInt("id"), rs.getString("name"), rs.getString("email"),
                    rs.getString("password"), rs.getInt("calories_per_day"), rs.getBoolean("enabled"),
                    rs.getDate("registered"), roleSet);
            String rolesString = rs.getString("roles");
            if (rolesString != null) {
                for (String role : rolesString.split(",")) {
                    roleSet.add(Role.valueOf(role));
                }
                user.setRoles(roleSet);
            }
            return user;
        }
    }
}
