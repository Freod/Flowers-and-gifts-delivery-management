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
        <table>
            <tr>
                <th>Country:</th>
                <th>Address</th>
                <th>City</th>
                <th>Postcode</th>
                <th>Status</th>
            </tr>
<%--            <c:forEach>--%>
<%--                <tr>--%>
<%--                    <td>${order.address.country}</td>--%>
<%--                    <td>${order.address.address}</td>--%>
<%--                    <td>${order.address.city}</td>--%>
<%--                    <td>${order.address.postcode}</td>--%>
<%--                    <td>${order.isSent}</td>--%>
<%--                </tr>--%>
<%--            </c:forEach>--%>
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