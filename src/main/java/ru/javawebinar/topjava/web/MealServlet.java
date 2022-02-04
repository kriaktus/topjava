package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

public class MealServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<MealTo> mealToList = MealsUtil.filteredByStreams(MealsUtil.MemoryBase, LocalTime.MIN, LocalTime.MAX, MealsUtil.caloriesPerDay);
        request.setAttribute("mealToList", mealToList);
        request.setAttribute("dateTimeFormatter", TimeUtil.dateTimeFormatter);
        request.getRequestDispatcher("meals.jsp").forward(request, response);
    }
}
