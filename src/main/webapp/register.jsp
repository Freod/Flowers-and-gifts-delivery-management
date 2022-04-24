<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registration</title>
</head>
<body>
<%@include file="navbar.jsp" %>
<form action="register" method="post">
    <span>Firstname:</span>
    <input type="text" name="firstname" id="firstname"><br>
    <span>Lastname:</span>
    <input type="text" name="lastname" id="lastname"><br>
    <span>Email:</span>
    <input type="text" name="email" id="email"><br>
    <span>Password:</span>
    <input type="password" name="password" id="password"><br>
    <input type="submit" value="Register">
</form>
</body>
</html>