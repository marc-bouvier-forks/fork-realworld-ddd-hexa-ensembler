package com.jitterted.moborg.adapter.in.web;

import com.jitterted.moborg.domain.Huddle;
import com.jitterted.moborg.domain.HuddleId;
import com.jitterted.moborg.domain.HuddleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class WebConfigurationTest {

    @MockBean
    HuddleService huddleService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getOfDashboardEndpointReturns200Ok() throws Exception {
        mockMvc.perform(get("/dashboard"))
               .andExpect(status().isOk());
    }

    @Test
    public void getOfHuddleDetailEndpointReturns200Ok() throws Exception {
        createStubServiceReturningHuddleWithIdOf(13L);

        mockMvc.perform(get("/huddle/13"))
               .andExpect(status().isOk());
    }

    @Test
    public void postToScheduleHuddleEndpointRedirects() throws Exception {
        mockMvc.perform(post("/schedule")
                                .param("name", "test")
                                .param("date", "2021-04-30")
                                .param("time", "09:00"))
               .andExpect(status().is3xxRedirection());
    }

    @Test
    public void postToRegisterParticipantEndpointRedirects() throws Exception {
        createStubServiceReturningHuddleWithIdOf(23L);
        mockMvc.perform(post("/register")
                                .param("huddleId", "23")
                                .param("name", "participant")
                                .param("githubUsername", "mygithub"))
               .andExpect(status().is3xxRedirection());
    }

    private void createStubServiceReturningHuddleWithIdOf(long id) {
        Huddle dummyHuddle = new Huddle("dummy", ZonedDateTime.now());
        HuddleId huddleId = HuddleId.of(id);
        dummyHuddle.setId(huddleId);
        when(huddleService.findById(huddleId))
                .thenReturn(Optional.of(dummyHuddle));
    }

}
