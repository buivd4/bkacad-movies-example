<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<html>
<head>
    <title>Add Movie</title>
</head>
<body>
    <h2>Add Movie</h2>
    <form action="${pageContext.request.contextPath}/movie/add" method="post">
        <label>Title:</label>
        <input type="text" name="title" required><br>
        <label>Genre:</label>
        <input type="text" name="genre" required><br>
        <label>Year of Release:</label>
        <input type="number" name="yearOfRelease" required><br>
        <input type="submit" value="Add Movie">
    </form>
    <br>
    <a href="${pageContext.request.contextPath}/movie/list">Back to List</a>
</body>
</html>
