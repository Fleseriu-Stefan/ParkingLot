package com.parking.parkinglot.ejb;

import com.parking.parkinglot.common.CarDto;
import com.parking.parkinglot.entities.Car;
import com.parking.parkinglot.entities.User;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class CarsBean {

    private static final Logger LOG = Logger.getLogger(CarsBean.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    public List<CarDto> findAllCars() {
        LOG.info("findAllCars");
        try {
            List<Car> cars = entityManager.createQuery("SELECT c FROM Car c", Car.class).getResultList();
            return copyCarsToDto(cars);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    public void createCar(String licensePlate, String parkingSpot, Long userId) {
        User owner = entityManager.find(User.class, userId);
        if (owner == null) {
            throw new IllegalArgumentException("Owner does not exist");
        }

        Car car = new Car();
        car.setLicensePlate(licensePlate);
        car.setParkingSpot(parkingSpot);
        car.setOwner(owner);
        owner.getCars().add(car);
        entityManager.persist(car);
    }

    public CarDto findById(Long id) {
        Car car = entityManager.find(Car.class, id);
        return car == null ? null : copyCarToDto(car);
    }

    public void updateCar(Long carId, String licensePlate, String parkingSpot, Long userId) {
        Car car = entityManager.find(Car.class, carId);
        if (car == null) {
            throw new IllegalArgumentException("Car does not exist");
        }
        User newOwner = entityManager.find(User.class, userId);
        if (newOwner == null) {
            throw new IllegalArgumentException("Owner does not exist");
        }

        car.setLicensePlate(licensePlate);
        car.setParkingSpot(parkingSpot);
        User oldOwner = car.getOwner();
        if (!oldOwner.getId().equals(newOwner.getId())) {
            oldOwner.getCars().remove(car);
            newOwner.getCars().add(car);
            car.setOwner(newOwner);
        }
    }

    public void deleteCarsByIds(Collection<Long> carIds) {
        for (Long carId : new LinkedHashSet<>(carIds)) {
            Car car = entityManager.find(Car.class, carId);
            if (car != null) {
                car.getOwner().getCars().remove(car);
                entityManager.remove(car);
            }
        }
    }

    private List<CarDto> copyCarsToDto(List<Car> cars) {
        List<CarDto> carDtos = new ArrayList<>(cars.size());
        for (Car car : cars) {
            carDtos.add(copyCarToDto(car));
        }
        return carDtos;
    }

    private CarDto copyCarToDto(Car car) {
        return new CarDto(car.getId(), car.getLicensePlate(), car.getParkingSpot(),
                car.getOwner().getUsername(), car.getOwner().getId());
    }
}
