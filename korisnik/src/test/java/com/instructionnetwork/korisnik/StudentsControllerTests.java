package com.instructionnetwork.korisnik;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.instructionnetwork.korisnik.exceptions.NoStudentsDefinedException;
import com.instructionnetwork.korisnik.exceptions.StudentNotFoundException;
import com.instructionnetwork.korisnik.model.*;
import com.instructionnetwork.korisnik.services.StudentsServices;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebAppConfiguration
@FixMethodOrder(MethodSorters.JVM)
public class StudentsControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StudentsServices studentsServices;

    private ObjectMapper mapper = new ObjectMapper();
    private MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);
    HashSet<Role> mockStudentRole = new HashSet<>(Arrays.asList(new Role[]{new Role(2L, RoleName.STUDENT)}));
    Students mockStudentNejira = new Students(1, "Nejira", "Music", "nm@gmail.com", "nmusic", "$2a$10$IkYWO5mUX97ei/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG", mockStudentRole);
    Students mockStudentJasmin = new Students(2, "Jasmin", "Bajric", "jb@gmail.com", "jbajric", "$2a$10$IkYWO5mUX97ei/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG", mockStudentRole);
    Students mockStudentElma = new Students(3, "Elma", "Bejtovic","eb@gmail.com", "ebejtovic", "$2a$10$IkYWO5mUX97ei/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG", mockStudentRole);

    private String getFreshToken() {
        String freshToken = "";
        try {
            RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/login")
                                        .contentType(MEDIA_TYPE_JSON_UTF8)
                                        .content("{\"username\":\"jbajric\",\"password\":\"password\"}");
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
    public void getStudentTest() throws Exception {
        Mockito.when(studentsServices.getStudentService(1)).thenReturn(mockStudentNejira);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/student/1").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String excepted = "{\"id\":1,\"first_name\":\"Nejira\",\"last_name\":\"Music\",\"email\":\"nm@gmail.com\",\"username\":\"nmusic\"," +
                            "\"password\":\"$2a$10$IkYWO5mUX97ei\\/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG\",\"roles\":[{\"id\":2,\"name\":\"STUDENT\"}]}";
        JSONAssert.assertEquals(excepted, result.getResponse().getContentAsString(), true);
    }

    @Test
    public void getInvalidStudentTest() throws Exception {
        String expected = "Student with ID '555' doesn't exist!";
        Mockito.when(studentsServices.getStudentService(555)).thenThrow(new StudentNotFoundException(expected));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/student/555");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        assertEquals(expected, result.getResolvedException().getMessage());
    }

    @Test
    public void getAllStudentsTest() throws Exception {
        ArrayList<Students> students = new ArrayList<>();
        students.add(mockStudentNejira);
        students.add(mockStudentJasmin);
        students.add(mockStudentElma);
        Mockito.when(studentsServices.getAllStudentsService()).thenReturn(students);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/allStudents").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String excepted = "[{\"id\":1,\"first_name\":\"Nejira\",\"last_name\":\"Music\",\"email\":\"nm@gmail.com\"," +
                            "\"username\":\"nmusic\",\"password\":\"$2a$10$IkYWO5mUX97ei\\/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG\",\"roles\":[{\"id\":2,\"name\":\"STUDENT\"}]}," +
                            "{\"id\":2,\"first_name\":\"Jasmin\",\"last_name\":\"Bajric\",\"email\":\"jb@gmail.com\",\"username\":\"jbajric\"," +
                            "\"password\":\"$2a$10$IkYWO5mUX97ei\\/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG\",\"roles\":[{\"id\":2,\"name\":\"STUDENT\"}]}," +
                            "{\"id\":3,\"first_name\":\"Elma\",\"last_name\":\"Bejtovic\",\"email\":\"eb@gmail.com\",\"username\":\"ebejtovic\"," +
                            "\"password\":\"$2a$10$IkYWO5mUX97ei\\/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG\",\"roles\":[{\"id\":2,\"name\":\"STUDENT\"}]}]";
        JSONAssert.assertEquals(excepted, result.getResponse().getContentAsString(), true);
    }

    @Test
    public void getInvalidAllStudentsTest() throws Exception {
        String expected = "No students defined!";
        Mockito.when(studentsServices.getAllStudentsService()).thenThrow(new NoStudentsDefinedException(expected));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/allStudents");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        assertEquals(expected, result.getResolvedException().getMessage());
    }

    @Test
    public void putStudentTest() throws Exception {
        String freshToken = getFreshToken();
        Mockito.when(studentsServices.getStudentService(2)).thenReturn(mockStudentJasmin);
        Mockito.when(studentsServices.getStudentByUsernameService(mockStudentJasmin.getUsername())).thenReturn(mockStudentJasmin);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/student/" + 2)
                                        .header("Access-Control-Request-Method", "PUT")
                                        .header("Authorization", "Bearer " + freshToken)
                                        .contentType(MEDIA_TYPE_JSON_UTF8)
                                        .content(mapper.writeValueAsString(mockStudentNejira))
                                        .accept(MEDIA_TYPE_JSON_UTF8);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    @Test
    public void putInvalidStudentTest() throws Exception {
        String freshToken = getFreshToken();
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/student" + 853).header(HttpHeaders.AUTHORIZATION, "Bearer " + freshToken);
        mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void postStudentTest() throws Exception {
        String freshToken = getFreshToken();
        List<Students> students = new ArrayList<>();
        students.add(mockStudentNejira);
        students.add(mockStudentJasmin);
        Mockito.when(studentsServices.getStudentByIdService(1)).thenReturn(mockStudentNejira);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/registerStudent/")
                                        .header("Access-Control-Request-Method", "POST")
                                        .header("Authorization", "Bearer " + freshToken)
                                        .contentType(MEDIA_TYPE_JSON_UTF8)
                                        .content(mapper.writeValueAsString(mockStudentJasmin))
                                        .accept(MEDIA_TYPE_JSON_UTF8);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Mockito.when(studentsServices.getAllStudentsService()).thenReturn(students);
        MvcResult result2 = mockMvc.perform(MockMvcRequestBuilders.get("/allStudents")).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(HttpStatus.OK.value(), result2.getResponse().getStatus());
    }

    @Test
    public void postInvalidStudentTest() throws Exception {
        String freshToken = getFreshToken();
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/student")
                                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + freshToken)
                                                .content(mapper.writeValueAsString(mockStudentNejira));
        mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void deleteStudentTest() throws Exception {
        String freshToken = getFreshToken();
        ArrayList<Students> students = new ArrayList<>();
        students.add(mockStudentJasmin);
        Mockito.when(studentsServices.getStudentService(2)).thenReturn(mockStudentJasmin);
        Mockito.when(studentsServices.getStudentByUsernameService(mockStudentJasmin.getUsername())).thenReturn(mockStudentJasmin);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/student/2")
                                            .header("Access-Control-Request-Method", "DELETE")
                                            .header("Authorization", "Bearer " + freshToken)).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    @Test
    public void deleteInvalidStudentTest() throws Exception {
        try{
            Mockito.when(studentsServices.getStudentService(555)).thenThrow(new StudentNotFoundException("Student with ID '555' doesn't exist!"));
        } catch (StudentNotFoundException e) {
            String freshToken = getFreshToken();
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/student/555").header(HttpHeaders.AUTHORIZATION, "Bearer " + freshToken)).andReturn();
            assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        }
    }

    // --------------------------------------------------------------------------------
    // RestTemplate
    // --------------------------------------------------------------------------------

    @Test
    public void getStudentsFreeTimesTest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/student/freetimes/1").contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

}