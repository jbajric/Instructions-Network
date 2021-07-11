package com.instructionnetwork.korisnik;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.instructionnetwork.korisnik.exceptions.InstructorNotFoundException;
import com.instructionnetwork.korisnik.exceptions.NoInstructorsDefinedException;
import com.instructionnetwork.korisnik.exceptions.NoInstructorsForStudentException;
import com.instructionnetwork.korisnik.model.*;
import com.instructionnetwork.korisnik.services.InstructorsServices;
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

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@FixMethodOrder(MethodSorters.JVM)
public class InstructorsControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private InstructorsServices instructorsServices;

    private ObjectMapper mapper = new ObjectMapper();
    private MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);

    HashSet<Role> mockInstructorRole = new HashSet<>(Arrays.asList(new Role[]{ new Role(1L, RoleName.INSTRUCTOR)}));
    List<InstructorsRatings> mockInstructorsRatingsList = new ArrayList<>();
    Instructors mockInstructorIrfan = new Instructors(1, "Irfan", "Prazina", "ip@gmail.com", "iprazina", "$2a$10$IkYWO5mUX97ei/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG", mockInstructorsRatingsList, mockInstructorRole);
    Instructors mockInstructorEmir = new Instructors(2, "Emir", "Cogo", "ec@gmail.com", "ecogo", "$2a$10$IkYWO5mUX97ei/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG", mockInstructorsRatingsList, mockInstructorRole);

    private String getFreshToken() {
        String freshToken = "";
        try {
            RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/login")
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

    // --------------------------------------------------------------------------------
    // CRUD
    // --------------------------------------------------------------------------------

    @Test
    public void getInstructorTest() throws Exception {
        Mockito.when(instructorsServices.getInstructorService(1)).thenReturn(mockInstructorIrfan);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/instructor/1").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String excepted = "{\"id\":1,\"first_name\":\"Irfan\",\"last_name\":\"Prazina\",\"email\":\"ip@gmail.com\"," +
                            "\"username\":\"iprazina\",\"password\":\"$2a$10$IkYWO5mUX97ei\\/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG\"," +
                            "\"roles\":[{\"id\":1,\"name\":\"INSTRUCTOR\"}]}";
        JSONAssert.assertEquals(excepted, result.getResponse().getContentAsString(), true);
    }

    @Test
    public void getInvalidInstructorTest() throws Exception {
        String expected = "Instructor with ID '777' doesn't exist!";
        Mockito.when(instructorsServices.getInstructorService(777)).thenThrow(new InstructorNotFoundException(expected));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/instructor/777").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        assertEquals(expected, result.getResolvedException().getMessage());
    }

    @Test
    public void getAllInstructorsTest() throws Exception {
        ArrayList<Instructors> instructors = new ArrayList<>();
        instructors.add(mockInstructorIrfan);
        instructors.add(mockInstructorEmir);
        Mockito.when(instructorsServices.getAllInstructorsService()).thenReturn(instructors);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/allInstructors").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String excepted = "[{\"id\":1,\"first_name\":\"Irfan\",\"last_name\":\"Prazina\",\"email\":\"ip@gmail.com\",\"username\":\"iprazina\"," +
                            "\"password\":\"$2a$10$IkYWO5mUX97ei\\/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG\",\"roles\":[{\"id\":1,\"name\":\"INSTRUCTOR\"}]}," +
                            "{\"id\":2,\"first_name\":\"Emir\",\"last_name\":\"Cogo\",\"email\":\"ec@gmail.com\",\"username\":\"ecogo\"," +
                            "\"password\":\"$2a$10$IkYWO5mUX97ei\\/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG\",\"roles\":[{\"id\":1,\"name\":\"INSTRUCTOR\"}]}]";
        JSONAssert.assertEquals(excepted, result.getResponse().getContentAsString(), true);
    }

    @Test
    public void getInvalidAllInstructorsTest() throws Exception {
        String expected = "No instructors defined!";
        Mockito.when(instructorsServices.getAllInstructorsService()).thenThrow(new NoInstructorsDefinedException(expected));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/allInstructors").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        assertEquals(expected,result.getResolvedException().getMessage());
    }

    @Test
    public void putInstructorTest() throws Exception {
        String freshToken = getFreshToken();
        Mockito.when(instructorsServices.getInstructorByUsernameService(mockInstructorIrfan.getUsername())).thenReturn(mockInstructorIrfan);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/instructor/" + 1)
                                        .header("Access-Control-Request-Method", "PUT")
                                        .header("Authorization", "Bearer " + freshToken)
                                        .contentType(MEDIA_TYPE_JSON_UTF8)
                                        .content(mapper.writeValueAsString(mockInstructorEmir))
                                        .accept(MEDIA_TYPE_JSON_UTF8);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    @Test
    public void putInvalidInstructorTest() throws Exception {
        String freshToken = getFreshToken();
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/instructor" + 555)
                                                .header("Access-Control-Request-Method", "PUT")
                                                .header("Authorization", "Bearer " + freshToken).accept(MEDIA_TYPE_JSON_UTF8);
        mockMvc.perform(builder).andExpect(status().isNotFound());
    }

    @Test
    public void postInstructorTest() throws Exception {
        String freshToken = getFreshToken();
        ArrayList<Instructors> instructors = new ArrayList<>();
        instructors.add(mockInstructorIrfan);
        instructors.add(mockInstructorEmir);
        Mockito.when(instructorsServices.getInstructorService(1)).thenReturn(mockInstructorIrfan);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/registerInstructor/")
                                        .header("Access-Control-Request-Method", "POST")
                                        .header("Authorization", "Bearer " + freshToken)
                                        .contentType(MEDIA_TYPE_JSON_UTF8)
                                        .content(mapper.writeValueAsString(mockInstructorEmir))
                                        .accept(MEDIA_TYPE_JSON_UTF8);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Mockito.when(instructorsServices.getAllInstructorsService()).thenReturn(instructors);
        MvcResult result2 = mockMvc.perform(MockMvcRequestBuilders.get("/allInstructors")).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(HttpStatus.OK.value(), result2.getResponse().getStatus());
    }

    @Test
    public void postInvalidInstructorTest() throws Exception {
        String freshToken = getFreshToken();
        Instructors i = new Instructors(1, "i", "p", "ip", "ip","p", mockInstructorsRatingsList, mockInstructorRole);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/registerInstructor")
                                                .header("Access-Control-Request-Method", "POST")
                                                .header("Authorization", "Bearer " + freshToken)
                                                .content(mapper.writeValueAsString(i))
                                                .accept(MEDIA_TYPE_JSON_UTF8);
        mockMvc.perform(builder).andExpect(status().is4xxClientError());
    }

    @Test
    public void deleteInstructorTest() throws Exception {
        String freshToken = getFreshToken();
        List<Instructors> instructors = new ArrayList<>();
        instructors.add(mockInstructorIrfan);
        Mockito.when(instructorsServices.getInstructorService(1)).thenReturn(mockInstructorIrfan);
        Mockito.when(instructorsServices.getInstructorByUsernameService(mockInstructorIrfan.getUsername())).thenReturn(mockInstructorIrfan);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/instructor/1")
                                            .header("Access-Control-Request-Method", "DELETE")
                                            .header("Authorization", "Bearer " + freshToken)).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    @Test
    public void deleteInvalidInstructorTest() throws Exception {
        try {
            Mockito.when(instructorsServices.getInstructorService(555)).thenThrow(new InstructorNotFoundException("Instructor with ID '555' doesn't exist!"));
        }
        catch (InstructorNotFoundException e) {
            String freshToken = getFreshToken();
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/instructor/555")
                                                .header("Access-Control-Request-Method", "DELETE")
                                                .header("Authorization", "Bearer " + freshToken)).andReturn();
            assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        }
    }

    // --------------------------------------------------------------------------------
    // Netrivijalni Web servisi
    // --------------------------------------------------------------------------------

    @Test
    public void getInstructorsForStudentTest() throws Exception{
        ArrayList<Instructors> instructors = new ArrayList<>();
        instructors.add(mockInstructorIrfan);
        Mockito.when(instructorsServices.getAllInstructorsByStudentService(1)).thenReturn(instructors);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/instructorsByStudent/1").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String excepted = "[{\"id\":1,\"first_name\":\"Irfan\",\"last_name\":\"Prazina\",\"email\":\"ip@gmail.com\",\"username\":\"iprazina\"," +
                            "\"password\":\"$2a$10$IkYWO5mUX97ei\\/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG\",\"roles\":[{\"id\":1,\"name\":\"INSTRUCTOR\"}]}]";
        JSONAssert.assertEquals(excepted, result.getResponse().getContentAsString(), true);
    }

    @Test
    public void getInvalidInstructorsForStudentTest() throws Exception{
        String expected = "No instructors assigned!";
        Mockito.when(instructorsServices.getAllInstructorsByStudentService(1)).thenThrow(new NoInstructorsForStudentException(expected));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/instructorsByStudent/1");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        assertEquals(expected, result.getResolvedException().getMessage());
    }

    // --------------------------------------------------------------------------------
    // RestTemplate
    // --------------------------------------------------------------------------------

   /* @Test
    public void getInstructorsAppointmentsTest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/instructor/appointments/1").contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }*/

    @Test
    public void deleteInstructorsAppointmentsTest() throws Exception {
        String freshToken = getFreshToken();
        RequestBuilder requestBuilder1 = MockMvcRequestBuilders
                .delete("/instructor/appointments/2")
                .header("Authorization", "Bearer " + freshToken);
        mockMvc.perform(requestBuilder1).andExpect(status().is4xxClientError());
    }

}