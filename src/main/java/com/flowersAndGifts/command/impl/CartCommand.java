package com.flowersAndGifts.command.impl;

import com.flowersAndGifts.command.Command;
import com.flowersAndGifts.exception.ControllerException;
import com.flowersAndGifts.model.Product;
import com.flowersAndGifts.service.OrderService;
import com.flowersAndGifts.service.impl.OrderServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CartCommand implements Command {
    OrderService orderService = new OrderServiceImpl();

    @Override
    public void getProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        HttpSession session = req.getSession(false);
        if (session == null) {
            throw new ControllerException("Your cart is empty");
        }

        List<Product> cart = (List<Product>) session.getAttribute("cart");
        if(cart==null){
            throw new ControllerException("Your cart is empty");
        }
        Map<Product, Long> map = cart.stream().collect(Collectors.groupingBy(c -> c, Collectors.counting()));
        session.setAttribute("cartMap", map);

        try {
            req.getRequestDispatcher(req.getServletPath().substring(1) + ".jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public void postProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {

    }
}
