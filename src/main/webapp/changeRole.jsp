<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<html lang="en">
<head>
    <link rel="stylesheet" type="text/css" href="static/css/style.css">
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
</head>
<body>
<%@include file="navbar.jsp" %>
<div class="content">
    <form action="changeRole" method="post">
        <select name="role">
            <c:forEach var="r" items="${roles}">
                <option value="${r}">${r}</option>
            </c:forEach>
        </select><br>
        <input type="submit" value="Change">
        <input type="hidden" name="email" value="${email}">
    </form>
</div>
</body>
</html>