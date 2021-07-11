package com.instructionnetwork.termini;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.instructionnetwork.termini.exceptions.AppointmentNotFoundException;
import com.instructionnetwork.termini.exceptions.NoAppointmentsDefinedException;
import com.instructionnetwork.termini.model.Appointment;
import com.instructionnetwork.termini.repositories.AppointmentRepository;
import com.instructionnetwork.termini.services.AppointmentService;
import org.json.JSONObject;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
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

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@FixMethodOrder(MethodSorters.JVM)
public class AppointmentControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    AppointmentRepository appointmentRepository;
    @MockBean
    private AppointmentService appointmentService;

    ObjectMapper mapper = new ObjectMapper();
    MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", java.nio.charset.Charset.forName("UTF-8"));
    Appointment a1 = new Appointment(1, "11.11.2021", "10:00", "18:00", "Kakanj", 55, true ,2, 2);
    Appointment a2 = new Appointment(2, "21.12.2021", "07:00", "20:00", "Sarajevo", 20, true, 1, 2);
    ArrayList<Appointment> appointments = new ArrayList<>();

    private String getFreshToken() {
        String freshToken = "";
        try {
            RequestBuilder requestBuilder = MockMvcRequestBuilders.post("http://localhost:8081/login")
                                            .contentType(MEDIA_TYPE_JSON_UTF8)
                                            .content("{\"username\":\"iprazina\",\"password\":\"password\"}");
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
    public void getAppointmentTest() throws Exception {
        Mockito.when(appointmentService.getAppointmentByIdService(1)).thenReturn(a1);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/appointmentById/1").contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String excepted = "{\"id\":1," +
                "\"date\":\"11.11.2021\"," +
                "\"start_time\":\"10:00\"," +
                "\"end_time\":\"18:00\"," +
                "\"location\":\"Kakanj\"," +
                "\"price\":55," +
                "\"available\":true," +
                "\"instructor_id\":2," +
                "\"subject_id\":2}";
        JSONAssert.assertEquals(excepted, result.getResponse().getContentAsString(), true);
    }

    @Test
    public void getInvalidAppointmentTest() throws Exception {
        Integer idTermina = 2121;
        String expected = "Appointment with ID '" + idTermina + "' doesn't exist!";
        Mockito.when(appointmentService.getAppointmentByIdService(idTermina)).thenThrow(new AppointmentNotFoundException(expected));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/appointmentById/" + idTermina);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    @Test
    public void getAllAppointmentsTest() throws Exception {
        appointments.add(a1);
        appointments.add(a2);
        Mockito.when(appointmentService.getAllAppointmentsService()).thenReturn(appointments);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/allAppointments").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected = "[{\"id\":1,\"date\":\"11.11.2021\",\"start_time\":\"10:00\",\"end_time\":\"18:00\"," +
                "\"location\":\"Kakanj\",\"price\":55,\"available\":true,\"instructor_id\":2,\"subject_id\":2}," +
                "{\"id\":2,\"date\":\"21.12.2021\",\"start_time\":\"07:00\",\"end_time\":\"20:00\",\"location\":\"Sarajevo\"," +
                "\"price\":20,\"available\":true,\"instructor_id\":1,\"subject_id\":2}]\n";
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
    }

    @Test
    public void getInvalidAllAppointmentsTest() throws Exception {
        Mockito.when(appointmentService.getAllAppointmentsService()).thenThrow(NoAppointmentsDefinedException.class);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/allAppointments");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    @Test
    public void putInvalidAppointmentTest() throws Exception {
        String freshToken = getFreshToken();
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/appointment/555")
                .header("Access-Control-Request-Method", "PUT")
                .header("Authorization", "Bearer " + freshToken).accept(MEDIA_TYPE_JSON_UTF8);
        mockMvc.perform(builder).andExpect(status().is4xxClientError());
    }

    @Test
    public void postInvalidAppointmentTest() throws Exception {
        String freshToken = getFreshToken();
        Appointment mockAppointment = new Appointment(1, "26.06.2021", "12:30", "13:00", "541654", 20, true, 1, 1);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/appointment")
                .header("Access-Control-Request-Method", "POST")
                .header("Authorization", "Bearer " + freshToken)
                .content(mapper.writeValueAsString(mockAppointment))
                .accept(MEDIA_TYPE_JSON_UTF8);
        mockMvc.perform(builder).andExpect(status().is4xxClientError());
    }

    @Test
    public void deleteInvalidAppointmentTest() throws Exception {
        try {
            Mockito.when(appointmentService.getAppointmentByIdService(555)).thenThrow(new AppointmentNotFoundException("Appointment with ID '555' doesn't exist!"));
        }
        catch (AppointmentNotFoundException e) {
            String freshToken = getFreshToken();
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/appointment/555")
                                        .header("Access-Control-Request-Method", "DELETE")
                                        .header("Authorization", "Bearer " + freshToken)).andReturn();
            assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        }
    }

}
