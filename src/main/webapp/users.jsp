<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Users</title>
</head>
<body>
<div>
    <%@include file="navbar.jsp" %>
    <table>
        <tr>
            <th>Email</th>
            <th>Firstname</th>
            <th>Lastname</th>
            <th>Role</th>
            <th>Password</th>
            <th>Active</th>
        </tr>
        <c:forEach var="user" items="${users}">
            <tr>
                <td>${user.email}</td>
                <td>${user.firstname}</td>
                <td>${user.lastname}</td>
                <td>
                    <form action="changeRole" method="get">
                        <input type="hidden" name="email" value="${user.email}">
                        <input type="submit" value="${user.role}">
                    </form>
                </td>
                <td>
                    <form action="changePassword" method="get">
                        <input type="hidden" name="email" value="${user.email}">
                        <input type="submit" value="Change">
                    </form>
                </td>
                <td>
                    <form action="changeActive" method="post">
                        <input type="hidden" name="email" value="${user.email}">
                        <input type="submit" value="${user.active}">
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
    <c:forEach var="i" begin="1" end="${allPages}">
        <c:choose>
            <c:when test="${i==page}">${i}</c:when>
            <c:when test="${i==1 || i==allPages || i==page-2 || i==page-1 || i==page+1 || i==page+2}"><a href="users?page=${i}">${i}</a></c:when>
        </c:choose>
    </c:forEach>
</div>
</body>
</html>