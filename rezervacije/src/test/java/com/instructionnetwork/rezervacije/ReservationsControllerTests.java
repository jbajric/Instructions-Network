package com.instructionnetwork.rezervacije;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.instructionnetwork.rezervacije.exceptions.NoReservationsDefinedException;
import com.instructionnetwork.rezervacije.model.Reservations;
import com.instructionnetwork.rezervacije.services.ReservationsServices;
import org.json.JSONObject;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@FixMethodOrder(MethodSorters.JVM)
public class ReservationsControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ReservationsServices reservationsServices;

    private ObjectMapper mapper = new ObjectMapper();
    private MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);
    private String getFreshToken() {
        String freshToken = "";
        try {
            RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/login")
                                            .contentType(MEDIA_TYPE_JSON_UTF8)
                                            .content("{\"username\":\"nmusic\",\"password\":\"password\"}");
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            JSONObject json = new JSONObject(result.getResponse().getContentAsString());
            freshToken = json.getString("accessToken");
            return freshToken;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return freshToken;
    }

    @Test
    public void getAllReservationsTest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/allReservations").contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    @Test
    public void getInvalidAllReservationsTest() throws Exception {
        Mockito.when(reservationsServices.getAllReservationsService()).thenThrow(NoReservationsDefinedException.class);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/allReservations");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    @Test
    public void putInvalidReservationsTest() throws Exception {
        String freshToken = getFreshToken();
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/reservation/111")
                                                .header("Access-Control-Request-Method", "PUT")
                                                .header("Authorization", "Bearer " + freshToken).accept(MEDIA_TYPE_JSON_UTF8);
        mockMvc.perform(builder).andExpect(status().is4xxClientError());
    }

    @Test
    public void postInvalidReservationsTest() throws Exception {
        String freshToken = getFreshToken();
        Reservations mockReservation = new Reservations(17, 17, 17);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/reservation")
                                                .header("Access-Control-Request-Method", "POST")
                                                .header("Authorization", "Bearer " + freshToken)
                                                .content(mapper.writeValueAsString(mockReservation))
                                                .accept(MEDIA_TYPE_JSON_UTF8);
        mockMvc.perform(builder).andExpect(status().is4xxClientError());
    }

    @Test
    public void deleteInvalidReservationsTest() throws Exception {
        try {
            Mockito.when(reservationsServices.getReservationService(753)).thenThrow(new NoReservationsDefinedException("Reservation with ID '753' doesn't exist!"));
        }
        catch (NoReservationsDefinedException e) {
            String freshToken = getFreshToken();
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/reservation/753")
                                .header("Access-Control-Request-Method", "DELETE")
                                .header("Authorization", "Bearer " + freshToken)).andReturn();
            assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        }
    }

}
