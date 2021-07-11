package com.instructionnetwork.korisnik;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.instructionnetwork.korisnik.exceptions.NoRatingsDefinedException;
import com.instructionnetwork.korisnik.exceptions.RatingNotFoundException;
import com.instructionnetwork.korisnik.model.*;
import com.instructionnetwork.korisnik.services.InstructorsRatingsServices;
import com.instructionnetwork.korisnik.services.InstructorsServices;
import com.instructionnetwork.korisnik.services.StudentsServices;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebAppConfiguration
public class InstructorsRatingsControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private InstructorsRatingsServices instructorsRatingsServices;

    private MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);
    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void getRatingsByInstructorTest() throws Exception {
        HashSet<Role> mockInstructorRole = new HashSet<>(Arrays.asList(new Role[]{ new Role(1L, RoleName.INSTRUCTOR)}));
        HashSet<Role> mockStudentRole = new HashSet<>(Arrays.asList(new Role[]{ new Role(2L, RoleName.STUDENT)}));
        ArrayList<InstructorsRatings> mockInstructorsRatingsList = new ArrayList<>();
        Instructors mockInstructorIrfan = new Instructors(1, "Irfan", "Prazina", "ip@gmail.com", "iprazina", "$2a$10$IkYWO5mUX97ei/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG", mockInstructorsRatingsList, mockInstructorRole);
        Students mockStudentNejira = new Students(1, "Nejira", "Music", "nm@gmail.com", "nmusic", "$2a$10$IkYWO5mUX97ei/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG", mockStudentRole);
        mockInstructorsRatingsList.add(new InstructorsRatings(1, 5, mockInstructorIrfan, mockStudentNejira));
        mockInstructorsRatingsList.add(new InstructorsRatings(1,1, mockInstructorIrfan, mockStudentNejira));
        String expected = "[{\"id\":1,\"rating\":5,\"instructors\":{\"id\":1,\"first_name\":\"Irfan\",\"last_name\":\"Prazina\"," +
                            "\"email\":\"ip@gmail.com\",\"username\":\"iprazina\",\"password\":\"$2a$10$IkYWO5mUX97ei\\/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG\"," +
                            "\"roles\":[{\"id\":1,\"name\":\"INSTRUCTOR\"}]},\"students\":{\"id\":1,\"first_name\":\"Nejira\",\"last_name\":\"Music\"," +
                            "\"email\":\"nm@gmail.com\",\"username\":\"nmusic\",\"password\":\"$2a$10$IkYWO5mUX97ei\\/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG\"," +
                            "\"roles\":[{\"id\":2,\"name\":\"STUDENT\"}]}},{\"id\":1,\"rating\":1,\"instructors\":{\"id\":1,\"first_name\":\"Irfan\",\"last_name\":\"Prazina\"," +
                            "\"email\":\"ip@gmail.com\",\"username\":\"iprazina\",\"password\":\"$2a$10$IkYWO5mUX97ei\\/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG\"," +
                            "\"roles\":[{\"id\":1,\"name\":\"INSTRUCTOR\"}]},\"students\":{\"id\":1,\"first_name\":\"Nejira\",\"last_name\":\"Music\"," +
                            "\"email\":\"nm@gmail.com\",\"username\":\"nmusic\",\"password\":\"$2a$10$IkYWO5mUX97ei\\/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG\"," +
                            "\"roles\":[{\"id\":2,\"name\":\"STUDENT\"}]}}]";

        Mockito.when(instructorsRatingsServices.getRatingsByInstructorService(1)).thenReturn(new ArrayList(mockInstructorIrfan.getInstructorsRatings()));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/rating/1").contentType(MEDIA_TYPE_JSON_UTF8);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
    }

    @Test
    public void getInvalidRatingsByInstructorTest() throws Exception {
        String expected = "Instructor with ID '789' doesn't have ratings!";
        Mockito.when(instructorsRatingsServices.getRatingsByInstructorService(789))
                .thenThrow(new RatingNotFoundException(expected));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/rating/789")
                .contentType(MEDIA_TYPE_JSON_UTF8);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        assertEquals(expected, result.getResolvedException().getMessage());
    }

    @Test
    public void getAllRatingsTest() throws Exception {
        HashSet<Role> mockInstructorRole = new HashSet<>(Arrays.asList(new Role[]{ new Role(1L, RoleName.INSTRUCTOR)}));
        HashSet<Role> mockStudentRole = new HashSet<>(Arrays.asList(new Role[]{ new Role(2L, RoleName.STUDENT)}));
        ArrayList<InstructorsRatings> mockInstructorsRatingsList = new ArrayList<InstructorsRatings>();
        Instructors mockInstructorIrfan = new Instructors(1, "Irfan", "Prazina", "ip@gmail.com", "iprazina", "$2a$10$IkYWO5mUX97ei/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG", mockInstructorsRatingsList, mockInstructorRole);
        Students mockStudentNejira = new Students(1, "Nejira", "Music", "nm@gmail.com", "nmusic", "$2a$10$IkYWO5mUX97ei/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG", mockStudentRole);
        mockInstructorsRatingsList.add(new InstructorsRatings(1, 5, mockInstructorIrfan, mockStudentNejira));
        mockInstructorsRatingsList.add(new InstructorsRatings(1,1, mockInstructorIrfan, mockStudentNejira));
        Mockito.when(instructorsRatingsServices.getAllRatingsService()).thenReturn(mockInstructorsRatingsList);
        String expected = "[{\"id\":1,\"rating\":5,\"instructors\":{\"id\":1,\"first_name\":\"Irfan\",\"last_name\":\"Prazina\",\"email\":\"ip@gmail.com\"," +
                            "\"username\":\"iprazina\",\"password\":\"$2a$10$IkYWO5mUX97ei\\/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG\",\"roles\":[{\"id\":1,\"name\":\"INSTRUCTOR\"}]}," +
                            "\"students\":{\"id\":1,\"first_name\":\"Nejira\",\"last_name\":\"Music\",\"email\":\"nm@gmail.com\",\"username\":\"nmusic\"," +
                            "\"password\":\"$2a$10$IkYWO5mUX97ei\\/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG\",\"roles\":[{\"id\":2,\"name\":\"STUDENT\"}]}}," +
                            "{\"id\":1,\"rating\":1,\"instructors\":{\"id\":1,\"first_name\":\"Irfan\",\"last_name\":\"Prazina\",\"email\":\"ip@gmail.com\",\"username\":\"iprazina\"," +
                            "\"password\":\"$2a$10$IkYWO5mUX97ei\\/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG\",\"roles\":[{\"id\":1,\"name\":\"INSTRUCTOR\"}]}," +
                            "\"students\":{\"id\":1,\"first_name\":\"Nejira\",\"last_name\":\"Music\",\"email\":\"nm@gmail.com\",\"username\":\"nmusic\"," +
                            "\"password\":\"$2a$10$IkYWO5mUX97ei\\/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG\",\"roles\":[{\"id\":2,\"name\":\"STUDENT\"}]}}]";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/allRatings")
                .contentType(MEDIA_TYPE_JSON_UTF8);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
    }

    @Test
    public void getAllInvalidRatingsTest() throws Exception {
        String expected = "No ratings defined!";
        Mockito.when(instructorsRatingsServices.getAllRatingsService()).thenThrow(new NoRatingsDefinedException(expected));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/allRatings")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        assertEquals(expected,result.getResolvedException().getMessage());
    }

    @Test
    public void putInvalidInstructorsRatingTest() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/ratings" + 555);
        mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void postInstructorsRatingTest() throws Exception {
        HashSet<Role> mockInstructorRole = new HashSet<>(Arrays.asList(new Role[]{ new Role(1L, RoleName.INSTRUCTOR)}));
        HashSet<Role> mockStudentRole = new HashSet<>(Arrays.asList(new Role[]{ new Role(2L, RoleName.STUDENT)}));
        ArrayList<InstructorsRatings> mockInstructorsRatingsList = new ArrayList<>();
        Instructors mockInstructorIrfan = new Instructors(1, "Irfan", "Prazina", "ip@gmail.com", "iprazina", "$2a$10$IkYWO5mUX97ei/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG", mockInstructorsRatingsList, mockInstructorRole);
        Students mockStudentNejira = new Students(1, "Nejira", "Music", "nm@gmail.com", "nmusic", "$2a$10$IkYWO5mUX97ei/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG", mockStudentRole);
        InstructorsRatings ir = new InstructorsRatings(1, 5, mockInstructorIrfan, mockStudentNejira);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/rating")
                .content(mapper.writeValueAsString(ir));
        mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void postInvalidInstructorsRatingTest() throws Exception {
        InstructorsRatings ir = new InstructorsRatings(1, 7896652, null ,null);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/rating")
                .content(mapper.writeValueAsString(ir));
        mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void deleteInvalidInstructorsRatingTest() throws Exception {
        try{
            Mockito.when(instructorsRatingsServices.getRatingsByInstructorService(123))
                    .thenThrow(new RatingNotFoundException("Instructor with ID '123' doesn't exist!"));
        }
        catch (RatingNotFoundException e) {
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/rating/123")).andReturn();
            assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        }
    }

    @Test
    public void getInstructorsAverageRatingTest() throws Exception {
        Double expected = 3.;
        HashSet<Role> mockInstructorRole = new HashSet<>(Arrays.asList(new Role[]{ new Role(1L, RoleName.INSTRUCTOR)}));
        HashSet<Role> mockStudentRole = new HashSet<>(Arrays.asList(new Role[]{ new Role(2L, RoleName.STUDENT)}));
        ArrayList<InstructorsRatings> mockInstructorsRatingsList = new ArrayList<InstructorsRatings>();
        Instructors mockInstructorIrfan = new Instructors(1, "Irfan", "Prazina", "ip@gmail.com", "iprazina", "$2a$10$IkYWO5mUX97ei/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG", mockInstructorsRatingsList, mockInstructorRole);
        Students mockStudentNejira = new Students(1, "Nejira", "Music", "nm@gmail.com", "nmusic", "$2a$10$IkYWO5mUX97ei/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG", mockStudentRole);
        mockInstructorsRatingsList.add(new InstructorsRatings(1, 5, mockInstructorIrfan, mockStudentNejira));
        mockInstructorsRatingsList.add(new InstructorsRatings(1,1, mockInstructorIrfan, mockStudentNejira));
        Mockito.when(instructorsRatingsServices.getAllRatingsService()).thenReturn(mockInstructorsRatingsList);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/averageRating/1").contentType(MEDIA_TYPE_JSON_UTF8);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    @Test
    public void getInvalidInstructorsAverageRatingTest() throws Exception {
        String expected = "Instructor with ID '123456' doesn't exist!";
        Mockito.when(instructorsRatingsServices.getInstructorsAverageRatingService(123456))
                .thenThrow(new NoRatingsDefinedException(expected));
        RequestBuilder requestBuilder =  MockMvcRequestBuilders.get("/averageRating/123456")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        assertEquals(expected,result.getResolvedException().getMessage());
    }

}