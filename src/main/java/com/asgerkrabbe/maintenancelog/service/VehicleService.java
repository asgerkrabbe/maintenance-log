package com.asgerkrabbe.maintenancelog.service;

import com.asgerkrabbe.maintenancelog.entity.*;
import java.util.List;
import java.util.Optional;

public interface VehicleService {
    Vehicle createVehicle(Vehicle vehicle, String username);
    List<Vehicle> getVehiclesByUser(String username);
    Optional<Vehicle> getVehicleById(Long id, String username);
    Vehicle updateVehicle(Vehicle vehicle, String username);
    void deleteVehicle(Long id, String username);
}