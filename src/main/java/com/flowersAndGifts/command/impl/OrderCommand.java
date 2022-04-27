package com.flowersAndGifts.command.impl;

import com.flowersAndGifts.command.Command;
import com.flowersAndGifts.exception.ControllerException;
import com.flowersAndGifts.exception.ServiceException;
import com.flowersAndGifts.model.*;
import com.flowersAndGifts.service.OrderService;
import com.flowersAndGifts.service.impl.OrderServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderCommand implements Command {
    private final OrderService orderService = new OrderServiceImpl();

    @Override
    public void getProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        try {
            req.getRequestDispatcher(req.getServletPath().substring(1)+".jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public void postProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        HttpSession session = req.getSession();

        String country = req.getParameter("country");
        String addressParameter = req.getParameter("address");
        String city = req.getParameter("city");
        String postcode = req.getParameter("postcode");

        Address address = new Address(country, addressParameter, city, postcode);
        Order order = new Order(address);

        User user = (User) session.getAttribute("user");
        if(user!=null){
            order.setUser_id(user.getId());
        }
        else {
            order.setUser_id(0L);
        }

        Map<Product, Long> map = (Map<Product, Long>) session.getAttribute("cartMap");
        List<ProductOrder> productOrderList = new ArrayList<>();
        for (Product product:map.keySet()) {
            productOrderList.add(new ProductOrder(product, Math.toIntExact(map.get(product))));
        }
        order.setProductOrder(productOrderList);

        try {
            orderService.makeOrder(order);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }

        session.removeAttribute("cartMap");
        session.removeAttribute("cart");

        if(user!=null) {
            try {
                resp.sendRedirect("account");
            } catch (IOException e) {
                throw new ControllerException(e);
            }
        }
        else {
            try {
                resp.sendRedirect("cart");
            } catch (IOException e) {
                throw new ControllerException(e);
            }
        }
    }
}
