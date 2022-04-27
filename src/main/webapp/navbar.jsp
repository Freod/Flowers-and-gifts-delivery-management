<nav>
    <div>
        <ul>
            <li><a href="index.jsp">Home</a></li>
            <c:if test="${user.role != 'ADMIN'}">
                <li><a href="offer">Offer</a></li>
            </c:if>
            <c:if test="${not empty user.role && user.role=='EMPLOYEE'}">
                <li><a href="products">Products</a></li>
            </c:if>
            <c:if test="${not empty user.role && user.role=='EMPLOYEE'}">
                <li><a href="orders">Orders</a></li>
            </c:if>
            <c:if test="${user.role != 'ADMIN' && user.role!='EMPLOYEE'}">
                <li><a href="cart">Cart</a></li>
            </c:if>
            <c:if test="${not empty user.role && user.role=='EMPLOYEE'}">
                <li><a href="addProduct">Add product</a></li>
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