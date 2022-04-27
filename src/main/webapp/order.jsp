<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<html lang="en">
<head>
    <link rel="stylesheet" type="text/css" href="static/css/style.css">
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order</title>
</head>
<body>
<%@include file="navbar.jsp" %>
<div class="content">
    <form action="" method="post">
        <span>Country:</span>
        <input type="text" name="country" id="country"/><br>
        <span>Address:</span>
        <input type="text" name="address" id="address"/><br>
        <span>City:</span>
        <input type="text" name="city" id="city"/><br>
        <span>Postcode:</span>
        <input type="text" name="postcode" id="postcode"/><br>
        <input type="submit" value="Order">
    </form>
</div>
</body>
</html>