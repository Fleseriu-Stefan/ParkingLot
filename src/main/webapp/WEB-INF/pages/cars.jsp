
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<t:pageTemplate pageTitle="Cars">

    <h1>Cars</h1>
    <form method="POST" action="${pageContext.request.contextPath}/Cars" class="container">
        <a href="${pageContext.request.contextPath}/AddCar" class="btn btn-primary btn-lg">Add Car</a>
        <button type="submit" class="btn btn-danger">Delete Cars</button>

        <c:forEach items="${cars}" var="car">
            <div class="row">
                <div class="col"><input type="checkbox" name="car_ids" value="${car.id}" aria-label="Select car ${car.id}"></div>
                <div class="col">${car.licensePlate}</div>
                <div class="col">${car.parkingSpot}</div>
                <div class="col">${car.ownerName}</div>
                <div class="col"><a class="btn btn-secondary" href="${pageContext.request.contextPath}/EditCar?id=${car.id}">Edit Car</a></div>
            </div>
        </c:forEach>

        <h5>Free parking spots: ${numberOfFreeParkingSpots}</h5>
    </form>

</t:pageTemplate>
