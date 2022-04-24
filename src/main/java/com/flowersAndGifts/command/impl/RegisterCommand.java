package com.flowersAndGifts.command.impl;

import com.flowersAndGifts.command.Command;
import com.flowersAndGifts.controller.MainController;
import com.flowersAndGifts.exception.ControllerException;
import com.flowersAndGifts.exception.ServiceException;
import com.flowersAndGifts.model.Role;
import com.flowersAndGifts.model.User;
import com.flowersAndGifts.service.UserService;
import com.flowersAndGifts.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

public class RegisterCommand implements Command {
    private static final Logger LOGGER = Logger.getLogger(RegisterCommand.class.getName());
    private UserService userService = new UserServiceImpl();

    @Override
    public void getProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        LOGGER.info("");
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            throw new ControllerException("You are logged in.");
        }
        try {
            req.getRequestDispatcher(req.getServletPath().substring(1)+".jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public void postProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        LOGGER.info("");
        User user = new User(
                req.getParameter("email"),
                req.getParameter("password"),
                req.getParameter("firstname"),
                req.getParameter("lastname"),
                Role.CUSTOMER, true
        );

        try {
            userService.register(user);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }

        try {
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        } catch (IOException | ServletException e) {
            throw new ControllerException(e);
        }
    }
}
