package com.flowersAndGifts.command.impl;

import com.flowersAndGifts.command.interfaces.Command;
import com.flowersAndGifts.exception.ControllerException;
import com.flowersAndGifts.exception.ServiceException;
import com.flowersAndGifts.model.Address;
import com.flowersAndGifts.model.Order;
import com.flowersAndGifts.model.Page;
import com.flowersAndGifts.model.User;
import com.flowersAndGifts.service.OrderService;
import com.flowersAndGifts.service.impl.OrderServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.flowersAndGifts.command.Authentication.*;
import static com.flowersAndGifts.command.CommandHelper.*;
import static com.flowersAndGifts.validator.ControllerValidator.isValidString;

public class OrdersCommand implements Command {
    private final OrderService orderService = new OrderServiceImpl();

    @Override
    public void getProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        HttpSession session = req.getSession(false);
        checkSession(session);

        User user = (User) session.getAttribute("user");
        needToBeLoggedIn(user);
        needToBeEmployee(user);

        Order orderFilter = new Order(new Address(
                req.getParameter("country"),
                req.getParameter("address"),
                req.getParameter("city"),
                req.getParameter("postcode")));

        Page<Order> orderPage = new Page<>(getPage(req), 8, req.getParameter("sortBy"), req.getParameter("direction"), orderFilter);
        try {
            orderPage= orderService.allUnsentOrdersByPage(orderPage);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }

        session.setAttribute("page", orderPage.getPageNumber());
        session.setAttribute("allPages", orderPage.allPages());
        session.setAttribute("orders", orderPage.getElements());

        sendRequestDispatcher(req, resp);
    }

    @Override
    public void postProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        HttpSession session = req.getSession(false);
        checkSession(session);

        User user = (User) session.getAttribute("user");
        needToBeLoggedIn(user);
        needToBeEmployee(user);

        Order order = new Order(new Address());
        order.setId(Long.parseLong(isValidString(req.getParameter("id"), "id")));
        try {
            order = orderService.showOrder(order);
            order.setStatus(!order.getStatus());
            orderService.changeOrderStatus(order);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }

        sendRedirect(resp, "orders");
    }
}
