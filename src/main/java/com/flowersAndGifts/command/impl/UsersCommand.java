package com.flowersAndGifts.command.impl;

import com.flowersAndGifts.command.Command;
import com.flowersAndGifts.exception.ControllerException;
import com.flowersAndGifts.exception.ServiceException;
import com.flowersAndGifts.model.Page;
import com.flowersAndGifts.model.Role;
import com.flowersAndGifts.model.User;
import com.flowersAndGifts.service.UserService;
import com.flowersAndGifts.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UsersCommand implements Command {
    private UserService userService = new UserServiceImpl();

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

        int page;
        String pageString = req.getParameter("page");
        if(pageString==null){
            page = 1;
        }
        else {
            page = Integer.parseInt(pageString);
        }
        Page<User> userPage = new Page<>(page, 10, new User());
        try {
            userPage = userService.allUsersByPage(userPage);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }

        session.setAttribute("page", userPage.getPageNumber());
        session.setAttribute("allPages", userPage.allPages());
        session.setAttribute("users", userPage.getElements());

        try {
            req.getRequestDispatcher(req.getServletPath().substring(1)+".jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public void postProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        getProcess(req, resp);
    }
}
