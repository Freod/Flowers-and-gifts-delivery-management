package com.flowersAndGifts.command.impl;

import com.flowersAndGifts.command.interfaces.Command;
import com.flowersAndGifts.exception.ControllerException;
import com.flowersAndGifts.exception.ServiceException;
import com.flowersAndGifts.model.Product;
import com.flowersAndGifts.model.User;
import com.flowersAndGifts.service.ProductService;
import com.flowersAndGifts.service.impl.ProductServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.flowersAndGifts.command.Authentication.*;
import static com.flowersAndGifts.command.CommandHelper.sendRedirect;
import static com.flowersAndGifts.command.CommandHelper.sendRequestDispatcher;
import static com.flowersAndGifts.validator.ControllerValidator.isValidString;

public class AddProductCommand implements Command {
    private final ProductService productService = new ProductServiceImpl();

    @Override
    public void getProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        HttpSession session = req.getSession(false);
        checkSession(session);

        User user = (User) session.getAttribute("user");
        needToBeLoggedIn(user);
        needToBeEmployee(user);

        sendRequestDispatcher(req, resp);
    }

    @Override
    public void postProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        HttpSession session = req.getSession(false);
        checkSession(session);

        User user = (User) session.getAttribute("user");
        needToBeLoggedIn(user);
        needToBeEmployee(user);

        //TODO: image
        Product product =  new Product(
                isValidString(req.getParameter("name"), "name"),
                Double.parseDouble(isValidString(req.getParameter("price"), "price")),
                isValidString(req.getParameter("name"), "name"),
                true);

        try {
            productService.addProduct(product);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }

        sendRedirect(resp, "products");
    }
}
