<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<html lang="en">
<head>
    <link rel="stylesheet" type="text/css" href="static/css/style.css">
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add products</title>
</head>
<body>
<%@include file="navbar.jsp" %>
<div class="content">
    <form action="addProduct" method="post">
        <span>Name:</span>
        <input type="text" name="name" id="name"><br>
        <span>Price:</span>
        <input type="text" name="price" id="price"><br>
        <span>Image</span>
        <input type="submit" value="Add product">
    </form>
</div>
</body>
</html>