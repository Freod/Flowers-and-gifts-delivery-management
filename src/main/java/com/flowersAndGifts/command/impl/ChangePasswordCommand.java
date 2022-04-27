package com.flowersAndGifts.command.impl;

import com.flowersAndGifts.command.Command;
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

public class ChangePasswordCommand implements Command {
    private static final Logger LOGGER = Logger.getLogger(ChangePasswordCommand.class.getName());
    private UserService userService = new UserServiceImpl();

    @Override
    public void getProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        LOGGER.info("");

        HttpSession session = req.getSession(false);
        if (session == null) {
            throw new ControllerException("You must be logged in.");
        }

        String email = req.getParameter("email");
        if (email != null) {
            User user = (User) session.getAttribute("user");
            if (Role.ADMIN.compareTo(user.getRole()) != 0) {
                throw new ControllerException("Only admin can be here.");
            }
            session.setAttribute("email", email);
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

        HttpSession session = req.getSession(false);
        if (session == null) {
            throw new ControllerException("You must be logged in.");
        }

        String email = req.getParameter("email");
        String password = req.getParameter("password");
        User user = new User();
        if (email != null && email != "") {
            User admin = (User) session.getAttribute("user");
            if (Role.ADMIN.compareTo(admin.getRole()) != 0) {
                throw new ControllerException("Only admin can be here.");
            }
            user.setEmail(email);
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

        if (email != null && email != "") {
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
