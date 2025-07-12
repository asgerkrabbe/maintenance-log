package com.asgerkrabbe.maintenancelog.service;

import com.asgerkrabbe.maintenancelog.entity.MaintenanceLog;
import java.util.List;

public interface MaintenanceLogService {
    MaintenanceLog createLog(MaintenanceLog log, Long vehicleId, String username);
    List<MaintenanceLog> getLogsByVehicle(Long vehicleId, String username);
    void deleteLog(Long id, String username);
}