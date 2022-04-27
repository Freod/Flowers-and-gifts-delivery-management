package com.flowersAndGifts.command.impl;

import com.flowersAndGifts.command.interfaces.Command;
import com.flowersAndGifts.exception.ControllerException;
import com.flowersAndGifts.exception.ServiceException;
import com.flowersAndGifts.model.Product;
import com.flowersAndGifts.model.Role;
import com.flowersAndGifts.model.User;
import com.flowersAndGifts.service.ProductService;
import com.flowersAndGifts.service.UserService;
import com.flowersAndGifts.service.impl.ProductServiceImpl;
import com.flowersAndGifts.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.flowersAndGifts.command.Authentication.checkSession;
import static com.flowersAndGifts.command.Authentication.needToBeLoggedIn;
import static com.flowersAndGifts.command.CommandHelper.sendRedirect;
import static com.flowersAndGifts.validator.ControllerValidator.isValidString;

public class ChangeActiveCommand implements Command {
    private final UserService userService = new UserServiceImpl();
    private final ProductService productService = new ProductServiceImpl();

    @Override
    public void getProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {

    }

    @Override
    public void postProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException {
        HttpSession session = req.getSession(false);
        checkSession(session);

        User user = (User) session.getAttribute("user");
        needToBeLoggedIn(user);

        if (Role.ADMIN.equals(user.getRole())) {
            String email = isValidString(req.getParameter("email"), "email");

            User changedUser = new User();
            changedUser.setEmail(email);
            try {
                changedUser = userService.userAccount(changedUser);
                changedUser.setActive(!changedUser.getActive());
                userService.changeActive(changedUser);
            } catch (ServiceException e) {
                throw new ControllerException(e);
            }

            sendRedirect(resp, "users");
        } else if (Role.EMPLOYEE.equals(user.getRole())) {
            Product product = new Product();
            product.setId(Long.parseLong(isValidString(req.getParameter("id"),"id")));
            try {
                product = productService.takeProduct(product);
                product.setActive(!product.isActive());
                productService.updateProduct(product);
            } catch (ServiceException e) {
                throw new ControllerException(e);
            }

            sendRedirect(resp, "products");
        } else {
            throw new ControllerException("You don't have permission to be here.");
        }
    }
}
