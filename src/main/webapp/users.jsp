<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<html lang="en">
<head>
    <link rel="stylesheet" type="text/css" href="static/css/style.css">
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Users</title>
</head>
<body>
<%@include file="navbar.jsp" %>
<div class="content">
    <form method="get" action="users">
        Email:<input type="text" name="email">
        Firstname:<input type="text" name="firstname">
        Lastname:<input type="text" name="lastname">
        Role:
        <select name="role">
            <option value="all">ALL</option>
            <option value="customer">CUSTOMER</option>
            <option value="employee">EMPLOYEE</option>
            <option value="admin">ADMIN</option>
        </select>
        Order by:
        <select name="sortBy">
            <option value="id">ID</option>
            <option value="email">EMAIL</option>
            <option value="firstname">FIRSTNAME</option>
            <option value="lastname">LASTNAME</option>
            <option value="role">ROLE</option>
            <option value="active">ACTIVE</option>
        </select>
        <select name="direction">
            <option value="ASC">ASC</option>
            <option value="DESC">DESC</option>
        </select>
        <input type="submit" value="Filter">
    </form>
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
            <c:when test="${i==1 || i==allPages || i==page-2 || i==page-1 || i==page+1 || i==page+2}"><a
                    href="users?page=${i}">${i}</a></c:when>
        </c:choose>
    </c:forEach>
</div>
</body>
</html>