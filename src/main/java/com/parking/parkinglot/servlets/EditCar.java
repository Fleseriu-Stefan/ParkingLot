package com.parking.parkinglot.servlets;

import com.parking.parkinglot.common.CarDto;
import com.parking.parkinglot.ejb.CarsBean;
import com.parking.parkinglot.ejb.UsersBean;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "EditCar", value = "/EditCar")
public class EditCar extends HttpServlet {

    @Inject
    private UsersBean usersBean;

    @Inject
    private CarsBean carsBean;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long carId = parseId(request.getParameter("id"));
        if (carId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid car ID");
            return;
        }
        CarDto car = carsBean.findById(carId);
        if (car == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Car not found");
            return;
        }

        request.setAttribute("carId", car.getId());
        request.setAttribute("licensePlate", car.getLicensePlate());
        request.setAttribute("parkingSpot", car.getParkingSpot());
        request.setAttribute("selectedOwnerId", car.getOwnerId());
        showForm(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long carId = parseId(request.getParameter("id"));
        Long ownerId = parseId(request.getParameter("owner_id"));
        String licensePlate = trim(request.getParameter("license_plate"));
        String parkingSpot = trim(request.getParameter("parking_spot"));

        if (carId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid car ID");
            return;
        }
        if (carsBean.findById(carId) == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Car not found");
            return;
        }

        request.setAttribute("carId", carId);
        request.setAttribute("licensePlate", licensePlate);
        request.setAttribute("parkingSpot", parkingSpot);
        request.setAttribute("selectedOwnerId", ownerId);

        if (licensePlate == null || licensePlate.isEmpty() || licensePlate.length() > 100
                || parkingSpot == null || parkingSpot.isEmpty() || parkingSpot.length() > 100
                || ownerId == null) {
            request.setAttribute("formError", "Please complete all fields with valid values.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            showForm(request, response);
            return;
        }

        try {
            carsBean.updateCar(carId, licensePlate, parkingSpot, ownerId);
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
        request.getRequestDispatcher("/WEB-INF/pages/editCar.jsp").forward(request, response);
    }

    private static Long parseId(String value) {
        if (value == null) {
            return null;
        }
        try {
            long id = Long.parseLong(value.trim());
            return id > 0 ? id : null;
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private static String trim(String value) {
        return value == null ? null : value.trim();
    }
}
