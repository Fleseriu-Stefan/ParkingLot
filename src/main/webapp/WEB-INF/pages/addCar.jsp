<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<t:pageTemplate pageTitle="Add Car">
    <h1>Add Car</h1>

    <c:if test="${not empty formError}">
        <div class="alert alert-danger" role="alert"><c:out value="${formError}" /></div>
    </c:if>

    <form class="needs-validation" method="POST" action="${pageContext.request.contextPath}/AddCar" novalidate>
        <div class="mb-3">
            <label for="license_plate" class="form-label">License Plate</label>
            <input type="text" class="form-control" id="license_plate" name="license_plate"
                   value="${fn:escapeXml(licensePlate)}" maxlength="100" required>
            <div class="invalid-feedback">Please enter a license plate.</div>
        </div>

        <div class="mb-3">
            <label for="parking_spot" class="form-label">Parking Spot</label>
            <input type="text" class="form-control" id="parking_spot" name="parking_spot"
                   value="${fn:escapeXml(parkingSpot)}" maxlength="100" required>
            <div class="invalid-feedback">Please enter a parking spot.</div>
        </div>

        <div class="mb-3">
            <label for="owner_id" class="form-label">Owner</label>
            <select class="form-select" id="owner_id" name="owner_id" required>
                <option value="">Choose...</option>
                <c:forEach items="${users}" var="user">
                    <option value="${user.id}" ${selectedOwnerId eq user.id ? 'selected' : ''}><c:out value="${user.username}" /></option>
                </c:forEach>
            </select>
            <div class="invalid-feedback">Please choose an owner.</div>
        </div>

        <button class="btn btn-primary btn-lg" type="submit">Save</button>
    </form>
</t:pageTemplate>
