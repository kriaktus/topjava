package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MemoryBaseMealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private MealDao mealDao = new MemoryBaseMealDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("MealServlet::doGet");
        String action = request.getParameter("action") != null ? request.getParameter("action") : "print-meal-list";
        log.debug("action "+action);
        switch (action) {
            case "delete": {
                mealDao.delete(MealsUtil.getIdFromRequest(request));
                log.debug("redirect to MealServlet");
                response.sendRedirect("meals");
                break;
            }
            case "update": {
                Meal mealToEdit = mealDao.getById(MealsUtil.getIdFromRequest(request));
                if (mealToEdit == null) {
                    log.error("no meal with such id in DB. redirect to MealServlet");
                    response.sendRedirect("meals");
                    return;
                }
                request.setAttribute("mealToEdit", mealToEdit);
                log.debug("forward to meal.jsp with " + mealToEdit );
                request.getRequestDispatcher("meal.jsp").forward(request, response);
                break;
            }
            case "create": {
                Meal mealToEdit = new Meal(null, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), null, 0);
                request.setAttribute("mealToEdit", mealToEdit);
                log.debug("forward to meal.jsp with " + mealToEdit );
                request.getRequestDispatcher("meal.jsp").forward(request, response);
                break;
            }
            default: {
                List<MealTo> mealToList = MealsUtil.filteredByStreams(mealDao.getAll(), LocalTime.MIN, LocalTime.MAX, MealsUtil.CALORIES_PER_DAY);
                request.setAttribute("mealToList", mealToList);
                request.setAttribute("dateTimeFormatter", TimeUtil.DATE_TIME_FORMATTER);
                log.debug("forward to meals.jsp");
                request.getRequestDispatcher("meals.jsp").forward(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("MealServlet::doPost");
        request.setCharacterEncoding("UTF-8");
        Meal saveMeal = mealDao.save(new Meal(
                request.getParameter("id").equals("") ? null : MealsUtil.getIdFromRequest(request),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories"))
        ));
        log.debug("save meal " + saveMeal + " in DB. redirect to MealServlet");
        response.sendRedirect("meals");
    }
}
