package com.instructionnetwork.raspored;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.instructionnetwork.raspored.exceptions.FreeTimeNotFoundException;
import com.instructionnetwork.raspored.exceptions.NoFreeTimesDefinedException;
import com.instructionnetwork.raspored.model.FreeTime;
import com.instructionnetwork.raspored.model.Role;
import com.instructionnetwork.raspored.model.RoleName;
import com.instructionnetwork.raspored.model.Students;
import com.instructionnetwork.raspored.services.FreeTimeService;
import io.swagger.v3.core.util.Json;
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
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@FixMethodOrder(MethodSorters.JVM)
public class FreeTimeControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FreeTimeService freeTimeService;

    private String getFreshToken(String username) {
        String freshToken = "";
        try {
            RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/login")
                    .content("{\"username\":\"" + username + "\",\"password\":\"password\"}")
                    .contentType(MEDIA_TYPE_JSON_UTF8);
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

    ObjectMapper mapper = new ObjectMapper();
    MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", java.nio.charset.Charset.forName("UTF-8"));
    HashSet<Role> mockStudentRole = new HashSet<>(Arrays.asList(new Role[]{new Role(2L, RoleName.STUDENT)}));
    Students mockStudentNejira = new Students(1, "Nejira", "Music", "nm@gmail.com", "nmusic", "$2a$10$IkYWO5mUX97ei/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG", mockStudentRole);
    Students mockStudentJasmin = new Students(2, "Jasmin", "Bajric", "jb@gmail.com", "jbajric", "$2a$10$IkYWO5mUX97ei/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG", mockStudentRole);
    FreeTime ft1 = new FreeTime(1, "11.11.2021", "12:00", "14:00", mockStudentNejira.getId());
    FreeTime ft2 = new FreeTime(2, "12.12.2021", "07:00", "20:00", mockStudentJasmin.getId());
    ArrayList<FreeTime> freeTimes = new ArrayList<>();

    @Test
    public void getFreeTimeTest() throws Exception {
        Mockito.when(freeTimeService.getFreeTimeService(1)).thenReturn(ft1);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/freeTime/1").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String excepted = Json.pretty().writeValueAsString(ft1);
        JSONAssert.assertEquals(excepted, result.getResponse().getContentAsString(), true);
    }

    @Test
    public void getInvalidFreeTimes() throws Exception {
        String expected = "Freetime with ID '48486' doesn't exist!";
        Mockito.when(freeTimeService.getFreeTimeService(48486)).thenThrow(new FreeTimeNotFoundException(expected));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/freeTime/48486");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    @Test
    public void getAllFreeTimesTest() throws Exception {
        freeTimes.add(ft1);
        freeTimes.add(ft2);
        Mockito.when(freeTimeService.getAllFreeTimesService()).thenReturn(freeTimes);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/allFreeTimes").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected = "[" +
                            "{\"id\":1,\"date\":\"11.11.2021\",\"start_time\":\"12:00\",\"end_time\":\"14:00\",\"student_id\":1}," +
                            "{\"id\":2,\"date\":\"12.12.2021\",\"start_time\":\"07:00\",\"end_time\":\"20:00\",\"student_id\":2}" +
                          "]";
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
    }

    @Test
    public void getInvalidAllFreeTimesTest() throws Exception {
        String expected = "No freetimes defined!";
        Mockito.when(freeTimeService.getAllFreeTimesService()).thenThrow(new NoFreeTimesDefinedException(expected));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/allFreeTimes");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        assertEquals(expected, result.getResolvedException().getMessage());
    }


    @Test
    public void putInvalidFreeTimesTest() throws Exception {
        String freshToken = getFreshToken(mockStudentNejira.getUsername());
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/freeTime/963")
                                                                        .header("Access-Control-Request-Method", "PUT")
                                                                        .header("Authorization", "Bearer " + freshToken).accept(MEDIA_TYPE_JSON_UTF8);
        mockMvc.perform(builder).andExpect(status().is4xxClientError());
    }


    @Test
    public void postInvalidFreeTimesTest() throws Exception {
        String freshToken = getFreshToken(mockStudentNejira.getUsername());
        FreeTime mockFreeTime = new FreeTime(1, "11.11.2021", "1200", "14:00", mockStudentNejira.getId());
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/freeTime")
                                                                        .header("Access-Control-Request-Method", "POST")
                                                                        .header("Authorization", "Bearer " + freshToken)
                                                                        .content(mapper.writeValueAsString(mockFreeTime))
                                                                        .accept(MEDIA_TYPE_JSON_UTF8);
        mockMvc.perform(builder).andExpect(status().is4xxClientError());
    }

    @Test
    public void deleteInvalidFreeTimeTest() throws Exception {
        try {
            Mockito.when(freeTimeService.getFreeTimeService(999)).thenThrow(new FreeTimeNotFoundException("Freetime with ID '999' doesn't exist!"));
        }
        catch (FreeTimeNotFoundException e) {
            String freshToken = getFreshToken(mockStudentNejira.getUsername());
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/freeTime/999")
                                                                    .header("Access-Control-Request-Method", "DELETE")
                                                                    .header("Authorization", "Bearer " + freshToken)).andReturn();
            assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        }
    }

    @Test
    public void getFreeTimesByStudentTest() throws Exception {
        Integer studentId = 1;
        ArrayList<FreeTime> freeTimeArrayList = new ArrayList<>();
        freeTimeArrayList.add(ft1);
        Mockito.when(freeTimeService.getFreeTimesByStudentService(studentId)).thenReturn(freeTimeArrayList);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/freeTimesByStudent/" + studentId).accept(MEDIA_TYPE_JSON_UTF8);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String excepted = "[{\"id\":1,\"date\":\"11.11.2021\",\"start_time\":\"12:00\",\"end_time\":\"14:00\",\"student_id\":1}]";
        JSONAssert.assertEquals(excepted, result.getResponse().getContentAsString(), true);
    }

    @Test
    public void getInvalidFreeTimesByStudentTest() throws Exception {
        Integer studentId = 123;
        String expected = "No freetime for the student with ID 123!";
        Mockito.when(freeTimeService.getFreeTimesByStudentService(studentId)).thenThrow(new FreeTimeNotFoundException(expected));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/freeTimesByStudent/" + studentId);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }
}
