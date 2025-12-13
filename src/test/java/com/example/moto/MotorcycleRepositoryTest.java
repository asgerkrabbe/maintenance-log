package com.example.moto;

import com.example.moto.entity.Motorcycle;
import com.example.moto.repository.MotorcycleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MotorcycleRepositoryTest {
    @Autowired
    private MotorcycleRepository repository;

    @Test
    void saveAndFind() {
        Motorcycle moto = new Motorcycle();
        moto.setNickname("Test");
        moto.setMake("Make");
        moto.setModel("Model");
        moto.setYear(2020);
        moto.setCurrentOdometerKm(1000);
        repository.save(moto);

        var found = repository.findAll();
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getNickname()).isEqualTo("Test");
    }
}
