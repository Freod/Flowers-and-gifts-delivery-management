package com.flowersAndGifts.command.interfaces;

import com.flowersAndGifts.exception.ControllerException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Command {
    void getProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException;

    void postProcess(HttpServletRequest req, HttpServletResponse resp) throws ControllerException;
}