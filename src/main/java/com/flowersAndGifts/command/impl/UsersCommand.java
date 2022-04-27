package com.flowersAndGifts.command.impl;

import com.flowersAndGifts.command.interfaces.Command;
import com.flowersAndGifts.exception.ControllerException;
import com.flowersAndGifts.exception.ServiceException;
import com.flowersAndGifts.model.Page;
import com.flowersAndGifts.model.Role;
import com.flowersAndGifts.model.User;
import com.flowersAndGifts.service.UserService;
import com.flowersAndGifts.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.flowersAndGifts.command.Authentication.*;
import static com.flowersAndGifts.command.CommandHelper.sendRequestDispatcher;

public class UsersCommand implements Command {
    private final UserService userService = new UserServiceImpl();

    @Override
    public void getProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        HttpSession session = req.getSession(false);
        checkSession(session);

        User user = (User) session.getAttribute("user");
        needToBeLoggedIn(user);
        needToBeAdmin(user);

        String roleString = req.getParameter("role");
        Role role = roleString == null || roleString.equals("all") || roleString.isEmpty() ? null : Role.valueOf(roleString.toUpperCase());
        User userFilter = new User(
                req.getParameter("email"),
                req.getParameter("firstname"),
                req.getParameter("lastname"),
                role
        );

        String pageString = req.getParameter("page");
        int page = pageString == null || pageString.isEmpty() ? 1 : Integer.parseInt(pageString);

        Page<User> userPage = new Page<>(page, 10, req.getParameter("sortBy"), req.getParameter("direction"), userFilter);
        try {
            userPage = userService.allUsersByPage(userPage);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }

        session.setAttribute("page", userPage.getPageNumber());
        session.setAttribute("allPages", userPage.allPages());
        session.setAttribute("users", userPage.getElements());

        sendRequestDispatcher(req, resp);
    }

    @Override
    public void postProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {

    }
}
