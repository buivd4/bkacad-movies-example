<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<html>
<head>
    <title>Edit Movie</title>
</head>
<body>
    <h2>Edit Movie</h2>
    <c:if test="${movie ne null}">
        <form action="${pageContext.request.contextPath}/movie/edit" method="post">
            <label>Title:</label>
            <input type="text" name="title" value="${movie.title}" required><br>
            <input type="hidden" name="id" value="${movie.id}">
            <input type="submit" value="Save Changes">
        </form>
    </c:if>
    <br>
    <a href="${pageContext.request.contextPath}/movies">Back to List</a>
</body>
</html>
