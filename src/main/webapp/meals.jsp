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

<table border="1">
    <thead>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th colspan="2">Action</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="mealTo" items="${mealToList}">
        <tr style="${mealTo.excess ? "color: red" : "color: green"}">
            <td><c:out value="${mealTo.dateTime.format(dateTimeFormatter)}"/></td>
            <td><c:out value="${mealTo.description}"/></td>
            <td><c:out value="${mealTo.calories}"/></td>
            <td>Update</td>
            <td>Delete</td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>