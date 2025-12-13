package com.example.moto;

import com.example.moto.controller.DashboardController;
import com.example.moto.entity.Motorcycle;
import com.example.moto.service.MotorcycleService;
import com.example.moto.service.TaskInstanceService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DashboardController.class)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MotorcycleService motorcycleService;

    @MockBean
    private TaskInstanceService taskInstanceService;

    @Test
    @WithMockUser
    void dashboardLoads() throws Exception {
        Motorcycle moto = new Motorcycle();
        moto.setId(1L);
        moto.setNickname("Test");
        moto.setMake("Make");
        moto.setModel("Model");
        moto.setYear(2020);
        given(motorcycleService.findAll()).willReturn(List.of(moto));
        given(taskInstanceService.findByMotorcycle(Mockito.any())).willReturn(List.of());

        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }
}
