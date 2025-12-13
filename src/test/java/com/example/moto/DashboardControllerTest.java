package com.example.moto;

import com.example.moto.controller.DashboardController;
import com.example.moto.entity.MaintenanceTaskInstance;
import com.example.moto.entity.Motorcycle;
import com.example.moto.service.MaintenanceTaskService;
import com.example.moto.service.MotorcycleService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DashboardController.class)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MotorcycleService motorcycleService;

    @MockBean
    private MaintenanceTaskService maintenanceTaskService;

    @Test
    @WithMockUser
    void dashboardLoads() throws Exception {
        Mockito.when(motorcycleService.findAll()).thenReturn(Collections.singletonList(new Motorcycle()));
        Mockito.when(maintenanceTaskService.findUpcomingForMotorcycle(Mockito.anyLong()))
                .thenReturn(Collections.singletonList(new MaintenanceTaskInstance()));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }
}
