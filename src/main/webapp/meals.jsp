<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .excess {
            color: red;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <hr/>
    <h2>Meals</h2>

    <form action="meals" method="get">
        <input type="text" value="filter" name="action" hidden>
        <table cellpadding="5">
            <thead>
            <tr align="left">
                <th width="150">От даты (включая)</th>
                <th width="150">До даты (включая)</th>
                <th width="100">От времени (включая)</th>
                <th width="100">До времени (исключая)</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td><input type="date" name="startDate" value="${param.get("startDate")}"></td>
                <td><input type="date" name="endDate" value="${param.get("endDate")}"></td>
                <td><input type="time" name="startTime" value="${param.get("startTime")}"></td>
                <td><input type="time" name="endTime" value="${param.get("endTime")}"></td>
            </tr>
            <tr>
                <td colspan="2"></td>
                <td colspan="2">
                    <a href="meals"><input type="button" value="Отменить"></a>
                    <input type="submit" value="Отфильтровать">
                </td>
            </tr>
            </tbody>
        </table>
    </form>

    <a href="meals?action=create">Add Meal</a>
    <br><br>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr class="${meal.excess ? 'excess' : 'normal'}">
                <td>
                        <%--                        ${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--                        <%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--                        ${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>