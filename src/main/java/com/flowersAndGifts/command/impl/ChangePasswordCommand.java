package com.flowersAndGifts.command.impl;

import com.flowersAndGifts.command.interfaces.Command;
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

import static com.flowersAndGifts.command.Authentication.*;
import static com.flowersAndGifts.command.CommandHelper.sendRequestDispatcher;
import static com.flowersAndGifts.validator.ControllerValidator.isValidString;

public class ChangePasswordCommand implements Command {
    private final UserService userService = new UserServiceImpl();

    @Override
    public void getProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        HttpSession session = req.getSession(false);
        checkSession(session);

        User user = (User) session.getAttribute("user");
        needToBeLoggedIn(user);

        String email = req.getParameter("email");
        if (email != null && !email.isEmpty()) {
            needToBeAdmin(user);
            session.setAttribute("email", email);
        }

        sendRequestDispatcher(req, resp);
    }

    @Override
    public void postProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        HttpSession session = req.getSession(false);
        checkSession(session);

        String email = req.getParameter("email");
        String password = isValidString(req.getParameter("password"), "password");
        User user = new User();
        if (email != null && !email.isEmpty()) {
            User admin = (User) session.getAttribute("user");
            needToBeAdmin(admin);
            user.setEmail(isValidString(email, "email"));
            try {
                user = userService.userAccount(user);
            } catch (ServiceException e) {
                throw new ControllerException(e);
            }
        } else {
            user = (User) session.getAttribute("user");
        }

        session.removeAttribute("email");
        session.removeAttribute("password");
        user.setPassword(password);

        try {
            userService.changePassword(user);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }

        if (email != null && !email.isEmpty()) {
            try {
                req.getRequestDispatcher("users").forward(req, resp);
            } catch (IOException | ServletException e) {
                throw new ControllerException(e);
            }
        } else {
            try {
                resp.sendRedirect("account");
            } catch (IOException e) {
                throw new ControllerException(e);
            }
        }
    }
}
