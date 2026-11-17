
package com.parking.parkinglot.servlets;

import com.parking.parkinglot.common.CarDto;
import com.parking.parkinglot.ejb.CarsBean;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "Cars", value = "/Cars")
public class Cars extends HttpServlet {

    @Inject
    private CarsBean carsBean;

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        List<CarDto> cars = carsBean.findAllCars();
        request.setAttribute("cars", cars);
        request.setAttribute("numberOfFreeParkingSpots", 10);
        request.setAttribute("activePage", "Cars");
        request.getRequestDispatcher("/WEB-INF/pages/cars.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        String[] selectedIds = request.getParameterValues("car_ids");
        List<Long> carIds = new ArrayList<>();
        if (selectedIds != null) {
            for (String selectedId : selectedIds) {
                try {
                    long id = Long.parseLong(selectedId);
                    if (id <= 0) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid car ID");
                        return;
                    }
                    carIds.add(id);
                } catch (NumberFormatException ex) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid car ID");
                    return;
                }
            }
        }

        carsBean.deleteCarsByIds(carIds);
        response.sendRedirect(request.getContextPath() + "/Cars");
    }
}
