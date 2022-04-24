<nav>
    <div>
        <ul>
            <c:if test="${user.role != 'ADMIN'}">
                <li>Offer</li>
            </c:if>
            <c:if test="${not empty user.role && user.role=='EMPLOYEE'}">
                <li>Products</li>
            </c:if>
            <c:if test="${not empty user.role && user.role=='EMPLOYEE'}">
                <li>Orders</li>
            </c:if>
            <c:if test="${user.role != 'ADMIN'}">
                <li>Cart</li>
            </c:if>
            <c:if test="${not empty user.role && user.role=='EMPLOYEE'}">
                <li>Product</li>
            </c:if>
            <c:if test="${not empty user.role && user.role=='ADMIN'}">
                <li><a href="users">Users</a></li>
            </c:if>
            <c:if test="${not empty user.role}">
                <li><a href="account">Account</a></li>
            </c:if>
            <c:if test="${empty user.role}">
                <li><a href="login">Login</a></li>
            </c:if>
            <c:if test="${empty user.role}">
                <li><a href="register">Register</a></li>
            </c:if>
            <c:if test="${not empty user.role}">
                <li><a href="logout">Logout</a></li>
            </c:if>
        </ul>
    </div>
</nav>