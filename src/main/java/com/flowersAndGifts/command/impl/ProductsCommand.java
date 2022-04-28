package com.flowersAndGifts.command.impl;

import com.flowersAndGifts.command.interfaces.Command;
import com.flowersAndGifts.exception.ControllerException;
import com.flowersAndGifts.exception.ServiceException;
import com.flowersAndGifts.model.Page;
import com.flowersAndGifts.model.Product;
import com.flowersAndGifts.model.User;
import com.flowersAndGifts.service.ProductService;
import com.flowersAndGifts.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.flowersAndGifts.command.Authentication.*;
import static com.flowersAndGifts.command.CommandHelper.getPage;
import static com.flowersAndGifts.command.CommandHelper.sendRequestDispatcher;

public class ProductsCommand implements Command {
    private final ProductService productService = ServiceFactory.getInstance().getProductService();

    @Override
    public void getProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        HttpSession session = req.getSession(false);
        checkSession(session);

        User user = (User) session.getAttribute("user");
        needToBeLoggedIn(user);
        needToBeEmployee(user);

        Product productFilter = new Product();
        productFilter.setName(req.getParameter("name"));

        Page<Product> productPage = new Page<>(getPage(req), 10, req.getParameter("sortBy"), req.getParameter("direction"), productFilter);
        try {
            productPage = productService.allProductsByPage(productPage);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }

        session.setAttribute("page", productPage.getPageNumber());
        session.setAttribute("allPages", productPage.allPages());
        session.setAttribute("products", productPage.getElements());

        sendRequestDispatcher(req, resp);
    }

    @Override
    public void postProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {

    }
}
