package com.flowersAndGifts.command.impl;

import com.flowersAndGifts.command.interfaces.Command;
import com.flowersAndGifts.exception.ControllerException;
import com.flowersAndGifts.exception.ServiceException;
import com.flowersAndGifts.model.Role;
import com.flowersAndGifts.model.User;
import com.flowersAndGifts.service.UserService;
import com.flowersAndGifts.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

import static com.flowersAndGifts.command.Authentication.*;
import static com.flowersAndGifts.command.CommandHelper.sendRedirect;
import static com.flowersAndGifts.command.CommandHelper.sendRequestDispatcher;
import static com.flowersAndGifts.validator.ControllerValidator.isValidString;

public class ChangeRoleCommand implements Command {
    private final UserService userService = new UserServiceImpl();

    @Override
    public void getProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        HttpSession session = req.getSession(false);
        checkSession(session);

        User user = (User) session.getAttribute("user");
        needToBeLoggedIn(user);
        needToBeAdmin(user);

        String email = isValidString(req.getParameter("email"), "email");
        List<Role> roles = Arrays.asList(Role.values());

        session.setAttribute("email", email);
        session.setAttribute("roles", roles);

        sendRequestDispatcher(req, resp);
    }

    @Override
    public void postProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        HttpSession session = req.getSession(false);
        checkSession(session);

        User user = (User) session.getAttribute("user");
        needToBeLoggedIn(user);
        needToBeAdmin(user);

        String email = isValidString(req.getParameter("email"), "email");
        String role = isValidString(req.getParameter("role"), "role");

        user = new User();
        user.setEmail(email);
        try {
            user = userService.userAccount(user);
            user.setRole(role);
            userService.changeRole(user);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }

        sendRedirect(resp, "users");
    }
}
