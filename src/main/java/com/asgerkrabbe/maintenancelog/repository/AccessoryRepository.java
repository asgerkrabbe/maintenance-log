package com.asgerkrabbe.maintenancelog.repository;

import com.asgerkrabbe.maintenancelog.entity.Accessory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessoryRepository extends JpaRepository<Accessory, Long> {
}