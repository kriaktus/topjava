package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping("/meals")
public class JspMealController {
    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    @Autowired
    private MealService service;

    @GetMapping("")
    public String getMeals(HttpServletRequest request, Model model) {
        log.info("JspMealController#getMeals filtered:{}", request.getParameterMap().size() != 0);
        List<MealTo> meals;
        if (request.getParameterMap().isEmpty()) {
            meals = MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay());
        } else {
            LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
            LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
            LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
            LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
            meals = MealsUtil.getFilteredTos(service.getBetweenInclusive(startDate, endDate, SecurityUtil.authUserId()),
                    SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
        }
        model.addAttribute("meals", meals);
        return "/meals";
    }

    @GetMapping({"/save"})
    public String sendToMealForm(HttpServletRequest request, Model model) {
        log.info("JspMealController#sendToMealForm id:{}", request.getParameter("id"));
        String paramId = request.getParameter("id");
        Meal meal = paramId == null ?
                new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                service.get(Integer.parseInt(paramId), SecurityUtil.authUserId());
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @PostMapping("")
    public String save(@ModelAttribute("meal") Meal meal) {
        log.info("JspMealController#save meal:{}", meal);
        if (meal.isNew()) {
            service.create(meal, SecurityUtil.authUserId());
        } else {
            service.update(meal, SecurityUtil.authUserId());
        }
        return "redirect:/meals";
    }

    @GetMapping({"/delete"})
    public String delete(@RequestParam("id") int id) {
        log.info("JspMealController#delete id:{}", id);
        service.delete(id, SecurityUtil.authUserId());
        return "redirect:/meals";
    }
}