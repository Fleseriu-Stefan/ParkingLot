package com.parking.parkinglot.servlets;

import com.parking.parkinglot.ejb.CarsBean;
import com.parking.parkinglot.ejb.UsersBean;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "AddCar", value = "/AddCar")
public class AddCar extends HttpServlet {

    @Inject
    private UsersBean usersBean;

    @Inject
    private CarsBean carsBean;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        showForm(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String licensePlate = trim(request.getParameter("license_plate"));
        String parkingSpot = trim(request.getParameter("parking_spot"));
        String ownerId = trim(request.getParameter("owner_id"));

        request.setAttribute("licensePlate", licensePlate);
        request.setAttribute("parkingSpot", parkingSpot);
        request.setAttribute("selectedOwnerId", ownerId);

        Long userId = null;
        try {
            if (ownerId != null && !ownerId.isEmpty()) {
                userId = Long.valueOf(ownerId);
            }
        } catch (NumberFormatException ignored) {
            // The owner must be a numeric user ID.
        }

        if (licensePlate == null || licensePlate.isEmpty() || licensePlate.length() > 100
                || parkingSpot == null || parkingSpot.isEmpty() || parkingSpot.length() > 100
                || userId == null || userId <= 0) {
            request.setAttribute("formError", "Please complete all fields with valid values.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            showForm(request, response);
            return;
        }

        try {
            carsBean.createCar(licensePlate, parkingSpot, userId);
        } catch (IllegalArgumentException ex) {
            request.setAttribute("formError", "Please choose an existing owner.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            showForm(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/Cars");
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("users", usersBean.findAllUsers());
        request.setAttribute("activePage", "Cars");
        request.getRequestDispatcher("/WEB-INF/pages/addCar.jsp").forward(request, response);
    }

    private static String trim(String value) {
        return value == null ? null : value.trim();
    }
}
