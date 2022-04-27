<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cart</title>
</head>
<body>
<div>
    <%@include file="navbar.jsp" %>
    <c:choose>
        <c:when test="${empty cart}"><h1>Your cart is empty</h1></c:when>
        <c:otherwise>
            <table>
                <tr>
                    <th>Name</th>
                    <th>Amount</th>
                    <th>Price</th>
                </tr>
                <c:forEach var="map" items="${cartMap}">
                    <tr>
                        <td>${map.key.name}</td>
                        <td>${map.value}</td>
                        <td>${map.key.price}</td>
                    </tr>
                </c:forEach>
            </table>
            <form action="order" method="get">
                <input type="submit" value="Order">
            </form>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>