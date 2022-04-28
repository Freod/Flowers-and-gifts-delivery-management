<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<html lang="en">
<head>
    <link rel="stylesheet" type="text/css" href="static/css/style.css">
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Products</title>
</head>
<body>
<%@include file="navbar.jsp" %>
<div class="content">
    <form action="products" method="get">
        Name:
        <input type="text" name="name">
        Order by:
        <select name="sortBy">
            <option value="id">ID</option>
            <option value="name">NAME</option>
            <option value="price">PRICE</option>
            <option value="image">IMAGE</option>
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
            <th>Name</th>
            <th>Price</th>
            <th>Image</th>
            <th>Active</th>
        </tr>
        <c:forEach var="product" items="${products}">
            <tr>
                <td>${product.name}</td>
                <td>${product.price}</td>
                <td>${product.image}</td>
                <td>
                    <form action="changeActive" method="post">
                        <input type="hidden" name="id" value="${product.id}">
                        <input type="submit" value="${product.active}">
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
    <c:forEach var="i" begin="1" end="${allPages}">
        <c:choose>
            <c:when test="${i==page}">${i}</c:when>
            <c:when test="${i==1 || i==allPages || i==page-2 || i==page-1 || i==page+1 || i==page+2}"><a
                    href="products?page=${i}">${i}</a></c:when>
        </c:choose>
    </c:forEach>
</div>
</body>
</html>