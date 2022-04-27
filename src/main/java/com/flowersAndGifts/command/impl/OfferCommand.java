package com.flowersAndGifts.command.impl;

import com.flowersAndGifts.command.Command;
import com.flowersAndGifts.exception.ControllerException;
import com.flowersAndGifts.exception.ServiceException;
import com.flowersAndGifts.model.Page;
import com.flowersAndGifts.model.Product;
import com.flowersAndGifts.service.ProductService;
import com.flowersAndGifts.service.impl.ProductServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OfferCommand implements Command {
    ProductService productService = new ProductServiceImpl();

    @Override
    public void getProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        Product productFilter = new Product();
        productFilter.setName(req.getParameter("name"));

        String pageString = req.getParameter("page");
        int page = pageString == null ? 1 : Integer.parseInt(pageString);
        Page<Product> productPage = new Page<>(page, 6, req.getParameter("sortBy"), req.getParameter("direction"), productFilter);
        try {
            productPage = productService.allActiveProductsByPage(productPage);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }

        HttpSession session;
        session = req.getSession(false);
        if (session == null) {
            session = req.getSession();
        }

        session.setAttribute("page", productPage.getPageNumber());
        session.setAttribute("allPages", productPage.allPages());
        session.setAttribute("products", productPage.getElements());

        try {
            req.getRequestDispatcher(req.getServletPath().substring(1) + ".jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public void postProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        Long id = Long.parseLong(req.getParameter("id"));
        HttpSession session;
        session = req.getSession(false);
        if (session == null) {
            session = req.getSession();
        }

        List<Product> cart = (List<Product>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }
        Product product = new Product();
        product.setId(id);
        try {
            product = productService.takeProduct(product);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
        cart.add(product);

        session.setAttribute("cart", cart);

        getProcess(req, resp);
    }
}
