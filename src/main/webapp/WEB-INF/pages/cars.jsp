
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<t:pageTemplate pageTitle="Cars">

    <h1>Cars</h1>

    <div class="container">
        <c:forEach items="${cars}" var="car">
            <div class="row">
                <div class="col">${car.licensePlate}</div>
                <div class="col">${car.parkingSpot}</div>
                <div class="col">${car.ownerName}</div>
            </div>
        </c:forEach>

        <h5>Free parking spots: ${numberOfFreeParkingSpots}</h5>
    </div>

</t:pageTemplate>
