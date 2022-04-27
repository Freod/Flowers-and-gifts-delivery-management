package com.flowersAndGifts.controller;

import com.flowersAndGifts.command.Command;
import com.flowersAndGifts.command.impl.*;
import com.flowersAndGifts.exception.ControllerException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class MainController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(MainController.class.getName());
    private Map<String, Command> commandMap;

    @Override
    public void init(){
        if (commandMap == null) {
            commandMap = new HashMap<>();
        }
        commandMap.put("login", new LoginCommand());
        commandMap.put("register", new RegisterCommand());
        commandMap.put("logout", new LogoutCommand());
        commandMap.put("account", new AccountCommand());
        commandMap.put("changePassword", new ChangePasswordCommand());
        commandMap.put("users", new UsersCommand());
        commandMap.put("changeActive", new ChangeActiveCommand());
        commandMap.put("changeRole", new ChangeRoleCommand());
        commandMap.put("offer", new OfferCommand());
        commandMap.put("cart", new CartCommand());
        commandMap.put("order", new OrderCommand());
        commandMap.put("orders", new OrdersCommand());
        commandMap.put("addProduct", new AddProductCommand());
        commandMap.put("products", new ProductsCommand());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info("");
        try {
            getProcess(req, resp);
        } catch (ControllerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info("");
        try {
            postProcess(req, resp);
        } catch (ControllerException e) {
            throw new RuntimeException(e);
        }
    }

    private void getProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        LOGGER.info(req.getServletPath());
        commandMap.get(req.getServletPath().substring(1)).getProcess(req, resp);
    }

    private void postProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        LOGGER.info(req.getServletPath());
        commandMap.get(req.getServletPath().substring(1)).postProcess(req, resp);
    }
}
