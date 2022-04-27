package com.flowersAndGifts.command.impl;

import com.flowersAndGifts.command.Command;
import com.flowersAndGifts.exception.ControllerException;
import com.flowersAndGifts.exception.ServiceException;
import com.flowersAndGifts.model.*;
import com.flowersAndGifts.service.OrderService;
import com.flowersAndGifts.service.impl.OrderServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

public class AccountCommand implements Command {
    private static final Logger LOGGER = Logger.getLogger(AccountCommand.class.getName());
    private final OrderService orderService = new OrderServiceImpl();

    @Override
    public void getProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        HttpSession session = req.getSession(false);
        if (session == null) {
            throw new ControllerException("You must be logged in.");
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new ControllerException("You must be logged in.");
        }

        if (user.getRole().equals(Role.CUSTOMER)) {
            Order orderFilter = new Order(new Address(req.getParameter("country"), req.getParameter("address"), req.getParameter("city"), req.getParameter("postcode")));

            String pageString = req.getParameter("page");
            int page = pageString == null ? 1 : Integer.parseInt(pageString);
            Page<Order> orderPage = new Page<>(page, 8, req.getParameter("sortBy"), req.getParameter("direction"), orderFilter);
            try {
                orderPage = orderService.allOrdersByPageAndUserId(orderPage, user);
            } catch (ServiceException e) {
                throw new ControllerException(e);
            }
            session.setAttribute("page", orderPage.getPageNumber());
            session.setAttribute("allPages", orderPage.allPages());
            session.setAttribute("orders", orderPage.getElements());
        }

        try {
            req.getRequestDispatcher(req.getServletPath().substring(1) + ".jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public void postProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        LOGGER.warning("");
        throw new ControllerException("This path does not have a POST method");
    }
}
