<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>

<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>

<a href="meals?action=CreateOrUpdate&id=0">Add Meal</a>
<br><br>

<table border="1" cellpadding="5">
    <thead>
    <tr bgcolor="#f0ffff">
        <th width="120">Date</th>
        <th width="200">Description</th>
        <th width="50">Calories</th>
        <th width="100" colspan="2">Action</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="mealTo" items="${mealToList}">
        <tr style="${mealTo.excess ? "color: red" : "color: green"}">
            <td><c:out value="${mealTo.dateTime.format(dateTimeFormatter)}"/></td>
            <td><c:out value="${mealTo.description}"/></td>
            <td><c:out value="${mealTo.calories}"/></td>
            <td>
                <a href="meals?action=CreateOrUpdate&id=${mealTo.id}">Update</a>
            </td>
            <td>
                <a href="meals?action=Delete&id=${mealTo.id}">Delete</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>