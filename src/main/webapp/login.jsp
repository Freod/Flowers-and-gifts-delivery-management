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
    <form action="login" method="post">
        <span>Email:</span>
        <input type="text" name="email" id="email"><br>
        <span>Password:</span>
        <input type="password" name="password" id="password"><br>
        <input type="submit" value="Login">
    </form>
</div>
</body>
</html>