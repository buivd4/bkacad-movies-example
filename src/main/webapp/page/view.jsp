<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<html>
<head>
    <title>View Movie</title>
</head>
<body>
    <h2>Movie Details</h2>
    <c:if test="${movie ne null}">
        <table border="1">
            <tr>
                <th>ID</th>
                <th>Title</th>
                <th>Genre</th>
                <th>Year of Release</th>
            </tr>
            <tr>
                <td>${movie.id}</td>
                <td>${movie.title}</td>
                <td>${movie.genre}</td>
                <td>${movie.yearOfRelease}</td>
            </tr>
        </table>
    </c:if>
    <br>
    <a href="${pageContext.request.contextPath}/movies">Back to List</a>
</body>
</html>
