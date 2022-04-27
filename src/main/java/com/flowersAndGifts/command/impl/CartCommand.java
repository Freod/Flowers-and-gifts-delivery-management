package com.flowersAndGifts.command.impl;

import com.flowersAndGifts.command.interfaces.Command;
import com.flowersAndGifts.exception.ControllerException;
import com.flowersAndGifts.model.Product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.flowersAndGifts.command.CommandHelper.sendRequestDispatcher;

public class CartCommand implements Command {
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

        sendRequestDispatcher(req, resp);
    }

    @Override
    public void postProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {

    }
}
