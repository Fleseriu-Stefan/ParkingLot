package com.parking.parkinglot;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;

@Singleton
@Startup
public class PersistenceInitializer {

    @PersistenceUnit(unitName = "default")
    private EntityManagerFactory entityManagerFactory;

    @PostConstruct
    public void initialize() {
        entityManagerFactory.getMetamodel().getEntities();
    }
}
