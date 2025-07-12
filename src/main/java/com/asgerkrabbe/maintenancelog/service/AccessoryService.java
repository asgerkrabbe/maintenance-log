package com.asgerkrabbe.maintenancelog.service;

import com.asgerkrabbe.maintenancelog.entity.*;
import java.util.List;
import java.util.Optional;

public interface AccessoryService {
    Accessory createAccessory(Accessory accessory, Long vehicleId, String username);
    List<Accessory> getAccessoriesByVehicle(Long vehicleId, String username);
    void deleteAccessory(Long id, String username);
    Accessory updateAccessory(Accessory accessory, String username);
}