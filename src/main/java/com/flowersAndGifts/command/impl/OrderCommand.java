package com.flowersAndGifts.command.impl;

import com.flowersAndGifts.command.interfaces.Command;
import com.flowersAndGifts.exception.ControllerException;
import com.flowersAndGifts.exception.ServiceException;
import com.flowersAndGifts.model.*;
import com.flowersAndGifts.service.OrderService;
import com.flowersAndGifts.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.flowersAndGifts.command.CommandHelper.sendRedirect;
import static com.flowersAndGifts.command.CommandHelper.sendRequestDispatcher;

public class OrderCommand implements Command {
    private final OrderService orderService = ServiceFactory.getInstance().getOrderService();

    @Override
    public void getProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        sendRequestDispatcher(req, resp);
    }

    @Override
    public void postProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        HttpSession session = req.getSession();

        Order order = new Order(new Address(
                req.getParameter("country"),
                req.getParameter("address"),
                req.getParameter("city"),
                req.getParameter("postcode")));

        User user = (User) session.getAttribute("user");
        if (user != null) {
            order.setUser_id(user.getId());
        } else {
            order.setUser_id(0L);
        }

        Map<Product, Long> map = (Map<Product, Long>) session.getAttribute("cartMap");
        List<ProductOrder> productOrderList = new ArrayList<>();
        for (Product product : map.keySet()) {
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

        if (user != null) {
            sendRedirect(resp, "account");
        } else {
            sendRedirect(resp, "cart");
        }
    }
}
