package com.flowersAndGifts.command.impl;

import com.flowersAndGifts.command.interfaces.Command;
import com.flowersAndGifts.exception.ControllerException;
import com.flowersAndGifts.exception.ServiceException;
import com.flowersAndGifts.model.*;
import com.flowersAndGifts.service.OrderService;
import com.flowersAndGifts.service.impl.OrderServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.flowersAndGifts.command.Authentication.checkSession;
import static com.flowersAndGifts.command.Authentication.needToBeLoggedIn;
import static com.flowersAndGifts.command.CommandHelper.getPage;
import static com.flowersAndGifts.command.CommandHelper.sendRequestDispatcher;

public class AccountCommand implements Command {
    private final OrderService orderService = new OrderServiceImpl();

    @Override
    public void getProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        HttpSession session = req.getSession(false);
        checkSession(session);

        User user = (User) session.getAttribute("user");
        needToBeLoggedIn(user);

        if (user.getRole().equals(Role.CUSTOMER)) {
            Page<Order> orderPage = new Page<>(
                    getPage(req),
                    10,
                    req.getParameter("sortBy"),
                    req.getParameter("direction"),
                    new Order(new Address(
                            req.getParameter("country"),
                            req.getParameter("address"),
                            req.getParameter("city"),
                            req.getParameter("postcode"))));

            try {
                orderPage = orderService.allOrdersByPageAndUserId(orderPage, user);
            } catch (ServiceException e) {
                throw new ControllerException(e);
            }

            session.setAttribute("page", orderPage.getPageNumber());
            session.setAttribute("allPages", orderPage.allPages());
            session.setAttribute("orders", orderPage.getElements());
        }

        sendRequestDispatcher(req, resp);
    }

    @Override
    public void postProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {

    }
}
