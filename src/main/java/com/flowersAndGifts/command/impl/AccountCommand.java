package com.flowersAndGifts.command.impl;

import com.flowersAndGifts.command.Command;
import com.flowersAndGifts.controller.MainController;
import com.flowersAndGifts.exception.ControllerException;
import com.flowersAndGifts.exception.ServiceException;
import com.flowersAndGifts.model.Order;
import com.flowersAndGifts.model.Page;
import com.flowersAndGifts.model.Role;
import com.flowersAndGifts.model.User;
import com.flowersAndGifts.service.OrderService;
import com.flowersAndGifts.service.UserService;
import com.flowersAndGifts.service.impl.OrderServiceImpl;
import com.flowersAndGifts.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

public class AccountCommand implements Command {
    private static final Logger LOGGER = Logger.getLogger(AccountCommand.class.getName());
    private UserService userService = new UserServiceImpl();
    private OrderService orderService = new OrderServiceImpl();

    @Override
    public void getProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        LOGGER.info("");

        HttpSession session = req.getSession(false);
        if(session == null){
            throw new ControllerException("You must be logged in.");
        }

        User user = (User) session.getAttribute("user");
        if(user == null){
            throw new ControllerException("You must be logged in.");
        }

        if(user.getRole().equals(Role.CUSTOMER)){
            //        Page<Order> orderPage = new Page<>(1, 8, new Order());
        }


        try {
            req.getRequestDispatcher(req.getServletPath().substring(1)+".jsp").forward(req, resp);
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
