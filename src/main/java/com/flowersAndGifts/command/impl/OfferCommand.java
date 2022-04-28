package com.flowersAndGifts.command.impl;

import com.flowersAndGifts.command.interfaces.Command;
import com.flowersAndGifts.exception.ControllerException;
import com.flowersAndGifts.exception.ServiceException;
import com.flowersAndGifts.model.Page;
import com.flowersAndGifts.model.Product;
import com.flowersAndGifts.service.ProductService;
import com.flowersAndGifts.service.impl.ProductServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static com.flowersAndGifts.command.CommandHelper.*;
import static com.flowersAndGifts.validator.ControllerValidator.isValidString;

public class OfferCommand implements Command {
    ProductService productService = new ProductServiceImpl();

    @Override
    public void getProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        Product productFilter = new Product();
        productFilter.setName(req.getParameter("name"));

        Page<Product> productPage = new Page<>(getPage(req), 8, req.getParameter("sortBy"), req.getParameter("direction"), productFilter);
        try {
            productPage = productService.allActiveProductsByPage(productPage);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }

        HttpSession session = req.getSession(false);
        if (session == null) {
            session = req.getSession();
        }

        session.setAttribute("page", productPage.getPageNumber());
        session.setAttribute("allPages", productPage.allPages());
        session.setAttribute("products", productPage.getElements());

        sendRequestDispatcher(req, resp);
    }

    @Override
    public void postProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        Long id = Long.parseLong(isValidString(req.getParameter("id"), "id"));
        HttpSession session = req.getSession(false);
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

        sendRedirect(resp, "offer");
    }
}
