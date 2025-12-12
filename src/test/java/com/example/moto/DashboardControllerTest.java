package com.example.moto;

import com.example.moto.controller.DashboardController;
import com.example.moto.repository.MaintenanceTaskInstanceRepository;
import com.example.moto.repository.MotorcycleRepository;
import com.example.moto.service.MaintenanceTaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DashboardController.class)
class DashboardControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MotorcycleRepository motorcycleRepository;
    @MockBean
    private MaintenanceTaskInstanceRepository taskInstanceRepository;
    @MockBean
    private MaintenanceTaskService maintenanceTaskService;

    @Test
    @WithMockUser
    void dashboardAccessible() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
    }
}
