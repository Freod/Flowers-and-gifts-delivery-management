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
import java.util.Arrays;
import java.util.List;

public class ChangeRoleCommand implements Command {
    private final UserService userService = new UserServiceImpl();

    @Override
    public void getProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            throw new ControllerException("You must be logged in.");
        }

        User user = (User) session.getAttribute("user");
        if (Role.ADMIN.compareTo(user.getRole()) != 0) {
            throw new ControllerException("Only admin can be here.");
        }

        String email = req.getParameter("email");
        List<Role> roles = Arrays.asList(Role.values());

        session.setAttribute("email", email);
        session.setAttribute("roles", roles);

        try {
            req.getRequestDispatcher(req.getServletPath().substring(1) + ".jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public void postProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            throw new ControllerException("You must be logged in.");
        }

        User user = (User) session.getAttribute("user");
        if (Role.ADMIN.compareTo(user.getRole()) != 0) {
            throw new ControllerException("Only admin can be here.");
        }

        String email = req.getParameter("email");
        String role = req.getParameter("role");

        user = new User();
        user.setEmail(email);
        try {
            user = userService.userAccount(user);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
        user.setRole(role);

        try {
            userService.changeRole(user);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }

        try {
            resp.sendRedirect("users");
        } catch (IOException e) {
            throw new ControllerException(e);
        }
    }
}
