package com.asgerkrabbe.maintenancelog.controller;

import com.asgerkrabbe.maintenancelog.entity.User;
import com.asgerkrabbe.maintenancelog.entity.Vehicle;
import com.asgerkrabbe.maintenancelog.entity.MaintenanceLog;
import com.asgerkrabbe.maintenancelog.entity.Accessory;
import com.asgerkrabbe.maintenancelog.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MaintenanceController {

    private final UserService userService;
    private final VehicleService vehicleService;
    private final MaintenanceLogService maintenanceLogService;
    private final AccessoryService accessoryService;

    @PostMapping("/users/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.register(user));
    }

    @GetMapping("/vehicles")
    public ResponseEntity<List<Vehicle>> getVehicles(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(vehicleService.getVehiclesByUser(username));
    }

    @PostMapping("/vehicles")
    public ResponseEntity<Vehicle> createVehicle(@RequestBody Vehicle vehicle, Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(vehicleService.createVehicle(vehicle, username));
    }

    @PutMapping("/vehicles/{vehicleId}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable Long vehicleId, @RequestBody Vehicle vehicle, Authentication authentication) {
        String username = authentication.getName();
        vehicle.setId(vehicleId);
        return ResponseEntity.ok(vehicleService.updateVehicle(vehicle, username));
    }

    @DeleteMapping("/vehicles/{vehicleId}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long vehicleId, Authentication authentication) {
        String username = authentication.getName();
        vehicleService.deleteVehicle(vehicleId, username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/vehicles/{vehicleId}/logs")
    public ResponseEntity<List<MaintenanceLog>> getLogs(@PathVariable Long vehicleId, Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(maintenanceLogService.getLogsByVehicle(vehicleId, username));
    }

    @PostMapping("/vehicles/{vehicleId}/logs")
    public ResponseEntity<MaintenanceLog> createLog(@PathVariable Long vehicleId, @RequestBody MaintenanceLog log, Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(maintenanceLogService.createLog(log, vehicleId, username));
    }

    @DeleteMapping("/logs/{logId}")
    public ResponseEntity<Void> deleteLog(@PathVariable Long logId, Authentication authentication) {
        String username = authentication.getName();
        maintenanceLogService.deleteLog(logId, username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/vehicles/{vehicleId}/accessories")
    public ResponseEntity<List<Accessory>> getAccessories(@PathVariable Long vehicleId, Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(accessoryService.getAccessoriesByVehicle(vehicleId, username));
    }

    @PostMapping("/vehicles/{vehicleId}/accessories")
    public ResponseEntity<Accessory> createAccessory(@PathVariable Long vehicleId, @RequestBody Accessory accessory, Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(accessoryService.createAccessory(accessory, vehicleId, username));
    }

    @PutMapping("/accessories/{accessoryId}")
    public ResponseEntity<Accessory> updateAccessory(@PathVariable Long accessoryId, @RequestBody Accessory accessory, Authentication authentication) {
        String username = authentication.getName();
        accessory.setId(accessoryId);
        return ResponseEntity.ok(accessoryService.updateAccessory(accessory, username));
    }

    @DeleteMapping("/accessories/{accessoryId}")
    public ResponseEntity<Void> deleteAccessory(@PathVariable Long accessoryId, Authentication authentication) {
        String username = authentication.getName();
        accessoryService.deleteAccessory(accessoryId, username);
        return ResponseEntity.noContent().build();
    }
}
