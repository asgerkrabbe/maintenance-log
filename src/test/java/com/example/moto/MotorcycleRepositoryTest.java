package com.example.moto;

import com.example.moto.entity.Motorcycle;
import com.example.moto.repository.MotorcycleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MotorcycleRepositoryTest {

    @Autowired
    private MotorcycleRepository repository;

    @Test
    void savesMotorcycle() {
        Motorcycle moto = new Motorcycle();
        moto.setNickname("Commuter");
        moto.setMake("Honda");
        moto.setModel("CB500");
        moto.setYear(2020);
        moto.setEngineSizeCc(500);
        moto.setCurrentOdometerKm(12000);
        moto.setPurchaseDate(LocalDate.of(2021, 1, 1));

        Motorcycle saved = repository.save(moto);
        assertThat(saved.getId()).isNotNull();
        assertThat(repository.findById(saved.getId())).isPresent();
    }
}
