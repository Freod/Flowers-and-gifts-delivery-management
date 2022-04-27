package com.flowersAndGifts.command;

import com.flowersAndGifts.exception.ControllerException;
import com.flowersAndGifts.model.Role;
import com.flowersAndGifts.model.User;

import javax.servlet.http.HttpSession;

public class Authentication {
    public static void checkSession(final HttpSession session) throws ControllerException {
        if (session == null || session.getAttribute("user")==null) {
            throw new ControllerException("You must be logged in.");
        }
    }

    public static void needToBeLoggedIn(final User user) throws ControllerException {
        if (user == null) {
            throw new ControllerException("You must be logged in.");
        }
    }

    public static void checkLogged(final HttpSession session) throws ControllerException {
        if (session != null && session.getAttribute("user") != null) {
            throw new ControllerException("You are logged in.");
        }
    }

    public static void needToBeEmployee(final User user) throws ControllerException {
        if (Role.EMPLOYEE.equals(user.getRole())) {
            throw new ControllerException("Only employee can be here.");
        }
    }

    public static void needToBeAdmin(final User user) throws ControllerException {
        if (Role.ADMIN.equals(user.getRole())) {
            throw new ControllerException("Only admin can be here.");
        }
    }
}
