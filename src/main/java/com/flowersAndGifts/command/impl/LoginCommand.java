package com.flowersAndGifts.command.impl;

import com.flowersAndGifts.command.interfaces.Command;
import com.flowersAndGifts.exception.ControllerException;
import com.flowersAndGifts.exception.ServiceException;
import com.flowersAndGifts.model.User;
import com.flowersAndGifts.service.UserService;
import com.flowersAndGifts.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.flowersAndGifts.command.Authentication.checkLogged;
import static com.flowersAndGifts.command.CommandHelper.sendRedirect;
import static com.flowersAndGifts.command.CommandHelper.sendRequestDispatcher;
import static com.flowersAndGifts.validator.ControllerValidator.isValidString;

public class LoginCommand implements Command {
    private final UserService userService = new UserServiceImpl();

    @Override
    public void getProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        HttpSession session = req.getSession(false);
        checkLogged(session);

        sendRequestDispatcher(req, resp);
    }

    @Override
    public void postProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        User user = new User();

        user.setEmail(isValidString(req.getParameter("email"), "email"));
        user.setPassword(isValidString(req.getParameter("password"), "password"));

        try {
            user = userService.login(user);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }

        if (!user.getActive())
            throw new ControllerException("This account is inactive");

        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        session = req.getSession();
        session.setAttribute("user", user);

        sendRedirect(resp, "account");
    }
}
