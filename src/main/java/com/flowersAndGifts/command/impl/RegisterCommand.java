package com.flowersAndGifts.command.impl;

import com.flowersAndGifts.command.interfaces.Command;
import com.flowersAndGifts.exception.ControllerException;
import com.flowersAndGifts.exception.ServiceException;
import com.flowersAndGifts.model.Role;
import com.flowersAndGifts.model.User;
import com.flowersAndGifts.service.ServiceFactory;
import com.flowersAndGifts.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.flowersAndGifts.command.Authentication.checkLogged;
import static com.flowersAndGifts.command.CommandHelper.sendRedirect;
import static com.flowersAndGifts.command.CommandHelper.sendRequestDispatcher;
import static com.flowersAndGifts.validator.ControllerValidator.isValidString;

public class RegisterCommand implements Command {
    private final UserService userService = ServiceFactory.getInstance().getUserService();

    @Override
    public void getProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        HttpSession session = req.getSession(false);
        checkLogged(session);

        sendRequestDispatcher(req, resp);
    }

    @Override
    public void postProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        User user = new User(
                isValidString(req.getParameter("email"), "email"),
                isValidString(req.getParameter("password"), "password"),
                isValidString(req.getParameter("firstname"), "firstname"),
                isValidString(req.getParameter("lastname"), "lastname"),
                Role.CUSTOMER, true
        );

        try {
            userService.register(user);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }

        sendRedirect(resp, "index.jsp");
    }
}
