<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Account</title>
</head>
<body>
<%@include file="navbar.jsp" %>
<div>
    <span>Firstname: ${user.email}</span><br>
    <span>Lastname: ${user.email}</span><br>
    <span>Email: ${user.email}</span><br>
    <a href="changePassword">Change password</a>
</div>
<c:if test="${user.role == 'CUSTOMER'}">
    <div>
        <form method="get" action="account">
            Country
            <input type="text" name="country">
            Address
            <input type="text" name="address">
            City
            <input type="text" name="city">
            Postcode
            <input type="text" name="postcode">
            Order by:
            <select name="sortBy">
                <option value="id">ID</option>
                <option value="country">COUNTRY</option>
                <option value="address">ADDRESS</option>
                <option value="city">CITY</option>
                <option value="postcode">POSTCODE</option>
                <option value="send">STATUS</option>
            </select>
            <select name="direction">
                <option value="ASC">ASC</option>
                <option value="DESC">DESC</option>
            </select>
            <input type="submit" value="Filter">
        </form>
        <table>
            <tr>
                <th>Country</th>
                <th>Address</th>
                <th>City</th>
                <th>Postcode</th>
                <th>Products</th>
                <th>Status</th>
            </tr>
            <c:forEach var="order" items="${orders}">
                <tr>
                    <td>${order.address.country}</td>
                    <td>${order.address.address}</td>
                    <td>${order.address.city}</td>
                    <td>${order.address.postcode}</td>
                    <td>
                        <c:forEach var="productorder" items="${order.productOrder}">
                            <span>${productorder.product.name} - ${productorder.amount}</span><br>
                        </c:forEach>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${order.status}">Sent</c:when>
                            <c:otherwise>Unsent</c:otherwise>
                        </c:choose>
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
</c:if>
</body>
</html>