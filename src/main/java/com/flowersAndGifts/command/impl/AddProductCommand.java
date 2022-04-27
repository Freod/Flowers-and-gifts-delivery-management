package com.flowersAndGifts.command.impl;

import com.flowersAndGifts.command.Command;
import com.flowersAndGifts.exception.ControllerException;
import com.flowersAndGifts.exception.ServiceException;
import com.flowersAndGifts.model.Product;
import com.flowersAndGifts.model.Role;
import com.flowersAndGifts.model.User;
import com.flowersAndGifts.service.ProductService;
import com.flowersAndGifts.service.impl.ProductServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AddProductCommand implements Command {
    private final ProductService productService = new ProductServiceImpl();

    @Override
    public void getProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            throw new ControllerException("You must be logged in.");
        }

        User user = (User) session.getAttribute("user");
        if (Role.EMPLOYEE.compareTo(user.getRole()) != 0) {
            throw new ControllerException("Only employee can be here.");
        }

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
        if (Role.EMPLOYEE.compareTo(user.getRole()) != 0) {
            throw new ControllerException("Only employee can be here.");
        }

        String name = req.getParameter("name");
        Double price = Double.parseDouble(req.getParameter("price"));
        //TODO: image

        Product product =  new Product(name, price, name, true);
        try {
            productService.addProduct(product);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }

        try {
            resp.sendRedirect("products");
        } catch (IOException e) {
            throw new ControllerException(e);
        }
    }
}
