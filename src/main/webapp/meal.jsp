<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>

<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Add meal/Edit meal</h2>

<form action="meals" method="post">
    <input name="id" type="number" value="${mealToEdit.id}" hidden>
    <table>
        <tr>
            <td width="150" >DateTime:</td>
            <td width="300"><input name="dateTime" type="datetime-local" value="${mealToEdit.dateTime}"/></td>
        </tr>
        <tr>
            <td>Description:</td>
            <td><input name="description" type="text" size="30" value="${mealToEdit.description}"></td>
        </tr>
        <tr>
            <td>Calories:</td>
            <td><input name="calories" type="number" min="0" value="${mealToEdit.calories}"></td>
        </tr>
        <tr>
            <td>
                <button type="submit">Save</button>
                <a href="meals"><button type="button">Cancel</button></a>
        </tr>
    </table>
</form>

</body>
</html>