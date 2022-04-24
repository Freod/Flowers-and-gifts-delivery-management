package com.flowersAndGifts.command.impl;

import com.flowersAndGifts.command.Command;
import com.flowersAndGifts.controller.MainController;
import com.flowersAndGifts.exception.ControllerException;
import com.flowersAndGifts.exception.ServiceException;
import com.flowersAndGifts.model.User;
import com.flowersAndGifts.service.UserService;
import com.flowersAndGifts.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

public class LoginCommand implements Command {
    private static final Logger LOGGER = Logger.getLogger(LoginCommand.class.getName());
    private UserService userService = new UserServiceImpl();

    @Override
    public void getProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        LOGGER.info("");
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            throw new ControllerException("You are logged in.");
        }
        try {
            req.getRequestDispatcher(req.getServletPath().substring(1) + ".jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public void postProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        LOGGER.info("");

        User user = new User();
        user.setEmail(req.getParameter("email"));
        user.setPassword(req.getParameter("password"));

        try {
            user = userService.login(user);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }

        if(!user.getActive())
            throw new ControllerException("This account is inactive");

        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        session = req.getSession();
        session.setAttribute("user", user);

        try {
            resp.sendRedirect("account");
        } catch (IOException e) {
            throw new ControllerException(e);
        }
    }
}
