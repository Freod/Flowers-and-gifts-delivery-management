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

public class ChangeActiveCommand implements Command {
    private UserService userService = new UserServiceImpl();

    @Override
    public void getProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {

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
        user = new User();
        user.setEmail(email);
        try {
            user = userService.userAccount(user);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
        user.setActive(!user.getActive());
        try {
            userService.changeActive(user);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }

        try {
            req.getRequestDispatcher("users").forward(req, resp);
        } catch (ServletException | IOException e) {
            throw new ControllerException(e);
        }
    }
}
