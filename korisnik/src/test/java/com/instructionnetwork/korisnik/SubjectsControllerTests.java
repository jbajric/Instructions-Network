package com.instructionnetwork.korisnik;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.instructionnetwork.korisnik.exceptions.*;
import com.instructionnetwork.korisnik.model.*;
import com.instructionnetwork.korisnik.services.InstructorsServices;
import com.instructionnetwork.korisnik.services.StudentsServices;
import com.instructionnetwork.korisnik.services.SubjectsServices;
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
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebAppConfiguration
@FixMethodOrder(MethodSorters.JVM)
public class SubjectsControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SubjectsServices subjectsServices;
    @MockBean
    private InstructorsServices instructorsServices;
    @MockBean
    private StudentsServices studentsServices;

    Subjects mockSubjectAlgorithms = new Subjects(1, "Algorithms", null ,null);
    Subjects mockSubjectEnglish = new Subjects(2, "English", null ,null);
    HashSet<Role> mockInstructorRole = new HashSet<>(Arrays.asList(new Role[]{ new Role(1L, RoleName.INSTRUCTOR)}));
    List<InstructorsRatings> mockInstructorsRatingsList = new ArrayList<>();
    Instructors mockInstructorIrfan = new Instructors(1, "Irfan", "Prazina", "ip@gmail.com", "iprazina", "$2a$10$IkYWO5mUX97ei/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG", mockInstructorsRatingsList, mockInstructorRole);
    HashSet<Role> mockStudentRole = new HashSet<>(Arrays.asList(new Role[]{new Role(2L, RoleName.STUDENT)}));
    Students mockStudentNejira = new Students(1, "Nejira", "Music", "nm@gmail.com", "nmusic", "$2a$10$IkYWO5mUX97ei/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG", mockStudentRole);
    ObjectMapper mapper = new ObjectMapper();
    MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", java.nio.charset.Charset.forName("UTF-8"));

    private String getFreshToken(String username) {
        String freshToken = "";
        try {
            RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/login")
                    .contentType(MEDIA_TYPE_JSON_UTF8)
                    .content("{\"username\":\"" + username + "\",\"password\":\"password\"}");
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
    public void getSubjectTest() throws Exception {
        Mockito.when(subjectsServices.getSubjectService(1)).thenReturn(mockSubjectAlgorithms);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/subject/1").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected = "{\"id\":1," +
                            "\"subject_name\":\"Algorithms\"," +
                            "\"students\":null," +
                            "\"instructors\":null}";
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
    }

    @Test
    public void getInvalidSubjectTest() throws Exception {
        String expected = "Subject with ID '123' doesn't exist!";
        Mockito.when(subjectsServices.getSubjectService(123)).thenThrow(new SubjectNotFoundException(expected));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/subject/123");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        assertEquals(expected, result.getResolvedException().getMessage());
        }

    @Test
    public void getAllSubjectsTest() throws Exception {
        Subjects s1 = new Subjects(1, "Calculus", null ,null);
        Subjects s2 = new Subjects(2, "English", null ,null);
        ArrayList<Subjects> subjects = new ArrayList<>();
        subjects.add(s1);
        subjects.add(s2);
        Mockito.when(subjectsServices.getAllSubjectsService()).thenReturn(subjects);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/allSubjects").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected = "[{\"id\":1," +
                "\"subject_name\":\"Calculus\"," +
                "\"students\":null," +
                "\"instructors\":null}," +
                "{\"id\":2," +
                "\"subject_name\":\"English\"," +
                "\"students\":null," +
                "\"instructors\":null}]";
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
    }

    @Test
    public void getInvalidAllSubjectsTest() throws Exception {
        String expected = "No subjects defined!";
        Mockito.when(subjectsServices.getAllSubjectsService()).thenThrow(new NoSubjectsDefinedException(expected));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/allSubjects");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        assertEquals(expected, result.getResolvedException().getMessage());
    }

    @Test
    public void putInvalidSubjectTest() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/subject" + 555);
        mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void postSubjectTest() throws Exception {
        String freshToken = getFreshToken("iprazina");
        ArrayList<Subjects> subjects = new ArrayList<>();
        subjects.add(mockSubjectAlgorithms);
        subjects.add(mockSubjectEnglish);
        Mockito.when(subjectsServices.getSubjectService(1)).thenReturn(mockSubjectAlgorithms);
        Mockito.when(subjectsServices.getSubjectByNameService(mockSubjectAlgorithms.getSubject_name())).thenReturn(mockSubjectAlgorithms);
        Mockito.when(subjectsServices.getSubjectService(2)).thenReturn(mockSubjectEnglish);
        Mockito.when(subjectsServices.getSubjectByNameService(mockSubjectEnglish.getSubject_name())).thenReturn(mockSubjectEnglish);
        Mockito.when(instructorsServices.getInstructorService(1)).thenReturn(mockInstructorIrfan);
        Mockito.when(instructorsServices.getInstructorByUsernameService(mockInstructorIrfan.getUsername())).thenReturn(mockInstructorIrfan);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/subject/")
                                                                .header("Access-Control-Request-Method", "POST")
                                                                .header("Authorization", "Bearer " + freshToken)
                                                                .contentType(MEDIA_TYPE_JSON_UTF8)
                                                                .content(mapper.writeValueAsString(mockSubjectEnglish))
                                                                .accept(MEDIA_TYPE_JSON_UTF8);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Mockito.when(subjectsServices.getAllSubjectsService()).thenReturn(subjects);
        MvcResult result2 = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(HttpStatus.OK.value(), result2.getResponse().getStatus());
    }

    @Test
    public void postInvalidSubjectTest() throws Exception {
        Subjects s = new Subjects(1, "?)?!!!", null ,null);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/subject").content(mapper.writeValueAsString(s));
        mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void deleteSubjectTest() throws Exception {
        String freshToken = getFreshToken("iprazina");
        Mockito.when(instructorsServices.getInstructorService(1)).thenReturn(mockInstructorIrfan);
        Mockito.when(instructorsServices.getInstructorByUsernameService(mockInstructorIrfan.getUsername())).thenReturn(mockInstructorIrfan);
        Mockito.when(subjectsServices.getSubjectService(1)).thenReturn(mockSubjectAlgorithms);
        Mockito.when(subjectsServices.getSubjectByNameService(mockSubjectAlgorithms.getSubject_name())).thenReturn(mockSubjectAlgorithms);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/subject/1")
                .header("Access-Control-Request-Method", "DELETE")
                .header("Authorization", "Bearer " + freshToken)).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    @Test
    public void deleteInvalidSubjectTest() throws Exception {
        try{
            Mockito.when(subjectsServices.getSubjectService(123)).thenThrow(new SubjectNotFoundException("Subject with ID '123' doesn't exist!"));
        }
        catch (SubjectNotFoundException e) {
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/subject/123")).andReturn();
            assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        }
    }

    // --------------------------------------------------------------------------------
    // Netrivijalni web servisi
    // --------------------------------------------------------------------------------

    @Test
    public void getAllSubjectsNamesTest() throws Exception {
        String s1 = "Calculus", s2 = "Algorithms", s3 = "Computer Graphics";
        ArrayList<String> predmeti = new ArrayList<>();
        predmeti.add(s1);
        predmeti.add(s2);
        predmeti.add(s3);
        Mockito.when(subjectsServices.getAllSubjectNamesService()).thenReturn(predmeti);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/allSubjectNames").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String excepted = "[\"Calculus\",\"Algorithms\",\"Computer Graphics\"]";
        JSONAssert.assertEquals(excepted, result.getResponse().getContentAsString(), true);
    }

    @Test
    public void getInvalidAllSubjectsNamesTest() throws Exception {
        String expected = "No subjects defined!";
        Mockito.when(subjectsServices.getAllSubjectNamesService()).thenThrow(new NoSubjectsDefinedException(expected));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/allSubjectNames");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        assertEquals(expected, result.getResolvedException().getMessage());
    }

    @Test
    public void getStudentsSubjectsTest() throws Exception{
        HashSet<Role> mockStudentRole = new HashSet<>(Arrays.asList(new Role[]{new Role(2L, RoleName.STUDENT)}));
        HashSet<Role> mockInstructorRole = new HashSet<>(Arrays.asList(new Role[]{ new Role(1L, RoleName.INSTRUCTOR)}));
        Students mockStudentNejira = new Students(1, "Nejira", "Music", "nm@gmail.com", "nmusic", "$2a$10$IkYWO5mUX97ei/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG", mockStudentRole);
        ArrayList<Students> students = new ArrayList<>();
        students.add(mockStudentNejira);
        List<InstructorsRatings> mockInstructorsRatingsList = new ArrayList<>();
        Instructors mockInstructorIrfan = new Instructors(1, "Irfan", "Prazina", "ip@gmail.com", "iprazina", "$2a$10$IkYWO5mUX97ei/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG", mockInstructorsRatingsList, mockInstructorRole);
        ArrayList<Instructors> instructors = new ArrayList<>();
        instructors.add(mockInstructorIrfan);
        Subjects p = new Subjects(1, "Calculus", students, instructors);
        ArrayList<Subjects> subjects = new ArrayList<>();
        subjects.add(p);
        Mockito.when(subjectsServices.getStudentsBySubjectService(1)).thenReturn(subjects);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/subjectsByStudent/1").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String excepted = "[{\"id\":1,\"subject_name\":\"Calculus\",\"students\":[{\"id\":1,\"first_name\":\"Nejira\",\"last_name\":\"Music\"," +
                            "\"email\":\"nm@gmail.com\",\"username\":\"nmusic\",\"password\":\"$2a$10$IkYWO5mUX97ei\\/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG\"," +
                            "\"roles\":[{\"id\":2,\"name\":\"STUDENT\"}]}],\"instructors\":[{\"id\":1,\"first_name\":\"Irfan\",\"last_name\":\"Prazina\",\"email\":\"ip@gmail.com\"," +
                            "\"username\":\"iprazina\",\"password\":\"$2a$10$IkYWO5mUX97ei\\/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG\",\"roles\":[{\"id\":1,\"name\":\"INSTRUCTOR\"}]}]}]";
        JSONAssert.assertEquals(excepted, result.getResponse().getContentAsString(), true);

    }

    @Test
    public void getInvalidStudentsSubjectsTest() throws Exception{
        String expected = "Student doesn't have any subjects!";
        Mockito.when(subjectsServices.getStudentsBySubjectService(2)).thenThrow(new StudentsSubjectsNotFoundException(expected));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/subjectsByStudent/2");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        assertEquals(expected, result.getResolvedException().getMessage());
    }

    @Test
    public void getInstructorsSubjectsTest() throws Exception{
        HashSet<Role> mockStudentRole = new HashSet<>(Arrays.asList(new Role[]{new Role(2L, RoleName.STUDENT)}));
        HashSet<Role> mockInstructorRole = new HashSet<>(Arrays.asList(new Role[]{ new Role(1L, RoleName.INSTRUCTOR)}));
        Students mockStudentNejira = new Students(1, "Nejira", "Music", "nm@gmail.com", "nmusic", "$2a$10$IkYWO5mUX97ei/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG", mockStudentRole);
        ArrayList<Students> students = new ArrayList<>();
        students.add(mockStudentNejira);
        List<InstructorsRatings> mockInstructorsRatingsList = new ArrayList<>();
        Instructors mockInstructorIrfan = new Instructors(1, "Irfan", "Prazina", "ip@gmail.com", "iprazina", "$2a$10$IkYWO5mUX97ei/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG", mockInstructorsRatingsList, mockInstructorRole);
        ArrayList<Instructors> instructors = new ArrayList<>();
        instructors.add(mockInstructorIrfan);
        Subjects p = new Subjects(1, "Calculus", students, instructors);
        ArrayList<Subjects> subjects = new ArrayList<>();
        subjects.add(p);
        Mockito.when(subjectsServices.getInstructorsBySubjectService(1)).thenReturn(subjects);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/subjectsByInstructor/1").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String excepted = "[{\"id\":1,\"subject_name\":\"Calculus\",\"students\":[{\"id\":1,\"first_name\":\"Nejira\"," +
                            "\"last_name\":\"Music\",\"email\":\"nm@gmail.com\",\"username\":\"nmusic\",\"password\":\"$2a$10$IkYWO5mUX97ei\\/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG\"," +
                            "\"roles\":[{\"id\":2,\"name\":\"STUDENT\"}]}],\"instructors\":[{\"id\":1,\"first_name\":\"Irfan\",\"last_name\":\"Prazina\",\"email\":\"ip@gmail.com\"," +
                            "\"username\":\"iprazina\",\"password\":\"$2a$10$IkYWO5mUX97ei\\/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG\",\"roles\":[{\"id\":1,\"name\":\"INSTRUCTOR\"}]}]}]";
        JSONAssert.assertEquals(excepted, result.getResponse().getContentAsString(), true);

    }

    @Test
    public void getInvalidInstructorsSubjectsTest() throws Exception{
        String expected = "Instructor doesn't have any subjects!";
        Mockito.when(subjectsServices.getInstructorsBySubjectService(2)).thenThrow(new InstructorsSubjectsNotFoundException(expected));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/subjectsByInstructor/2");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        assertEquals(expected, result.getResolvedException().getMessage());
    }

}
