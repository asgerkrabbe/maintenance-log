package com.example.moto;

import com.example.moto.entity.Motorcycle;
import com.example.moto.repository.MotorcycleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RepositoryTests {
    @Autowired
    private MotorcycleRepository motorcycleRepository;

    @Test
    void saveMotorcycle() {
        Motorcycle moto = new Motorcycle();
        moto.setNickname("Test Bike");
        moto.setMake("Make");
        moto.setModel("Model");
        moto.setYear(2020);
        moto.setCurrentOdometerKm(1000);
        Motorcycle saved = motorcycleRepository.save(moto);
        assertThat(saved.getId()).isNotNull();
        assertThat(motorcycleRepository.findById(saved.getId())).isPresent();
    }
}
