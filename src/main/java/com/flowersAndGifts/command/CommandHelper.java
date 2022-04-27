package com.flowersAndGifts.command;

import com.flowersAndGifts.exception.ControllerException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CommandHelper {
    public static int getPage(HttpServletRequest req) {
        String pageString = req.getParameter("page");
        return pageString == null ? 1 : Integer.parseInt(pageString);
    }

    public static void sendRequestDispatcher(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        try {
            req.getRequestDispatcher(req.getServletPath().substring(1) + ".jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            throw new ControllerException(e);
        }
    }

    public static void sendRedirect(HttpServletResponse resp, final String string) throws ControllerException {
        try {
            resp.sendRedirect(string);
        } catch (
                IOException e) {
            throw new ControllerException(e);
        }
    }
}
