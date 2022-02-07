package ru.javawebinar.topjava.web;

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
import java.util.List;

public class MealServlet extends HttpServlet {
    private MealDao mealDao = new MemoryBaseMealDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action != null) {
            if (action.equals("Delete")) {
                mealDao.delete(Integer.parseInt(request.getParameter("id")));
                response.sendRedirect("meals");
                return;
            } else if (action.equals("CreateOrUpdate")) {
                Meal mealToEdit = mealDao.getById(Integer.parseInt(request.getParameter("id")));
                if (mealToEdit == null) {
                    mealToEdit = new Meal(0,null, null, 0);
                }
                request.setAttribute("mealToEdit", mealToEdit);
                request.getRequestDispatcher("meal.jsp").forward(request, response);
                return;
            }
        }
        List<MealTo> mealToList = MealsUtil.filteredByStreams(mealDao.getAll(), LocalTime.MIN, LocalTime.MAX, MealsUtil.CALORIES_PER_DAY);
        request.setAttribute("mealToList", mealToList);
        request.setAttribute("dateTimeFormatter", TimeUtil.dateTimeFormatter);
        request.getRequestDispatcher("meals.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        mealDao.add(new Meal(
                Integer.parseInt(request.getParameter("id")),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories"))
        ));
        response.sendRedirect("meals");
    }
}
