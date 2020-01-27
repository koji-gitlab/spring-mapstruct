package com.mitrais.app.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@PropertySource("classpath:application.properties")
@Sql(scripts = "classpath:db/test.sql")
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void saveEmptyJson_ThenReturn400() throws Exception {
        String jsonString = "{}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(jsonString, headers);

        // save
//        ResponseEntity<String> response = restTemplate.postForEntity("/api/user", entity, String.class);

        // then
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        mockMvc.perform(post("/api/user")
                .content(jsonString)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors").isMap())
                .andExpect(jsonPath("$.errors", hasKey("firstName")))
                .andExpect(jsonPath("$.errors", hasKey("lastName")))
                .andExpect(jsonPath("$.errors", hasKey("mobileNo")))
                .andExpect(jsonPath("$.errors", hasKey("email")))
        ;

    }

    @Test
    public void saveWrongMobilePhoneFormat_FutureDob_ThenReturn400() throws Exception {
        String jsonString = "{\"firstName\": \"Joko\", \"lastName\": \"Santoso\", \"email\": \"sxe2koji@yahoo.com\", \"mobileNo\": \"+60176199036\", \"dob\" : \"2021-01-01\"}";

        mockMvc.perform(post("/api/user")
                .content(jsonString)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors").isMap())
                .andExpect(jsonPath("$.errors", hasKey("mobileNo")))
                .andExpect(jsonPath("$.errors", hasKey("dob")))
        ;

    }

    @Test
    public void saveDuplicateEmail_ThenReturn409() throws Exception {

        mockMvc.perform(post("/api/user")
                .content(generateDuplicateEmail())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(jsonPath("$.errors").isMap())
                .andExpect(jsonPath("$.errors", hasKey("email")))
        ;

    }

    @Test
    public void saveDuplicateMobileNo_ThenReturn409() throws Exception {

        mockMvc.perform(post("/api/user")
                .content(generateDuplicateMobileNo())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(jsonPath("$.errors").isMap())
                .andExpect(jsonPath("$.errors", hasKey("mobileNo")))
        ;

    }

    @Test
    public void registerUser_ThenReturn200() throws Exception {
        String jsonString = "{\"firstName\": \"Joko\", \"lastName\": \"Santoso\", \"email\": \"sxe2koji@yahoo.com\", \"mobileNo\": \"081310169723\", \"dob\" : \"2001-01-01\"}";

        mockMvc.perform(post("/api/user")
                .content(jsonString)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasKey("firstName")))
                .andExpect(jsonPath("$", hasKey("lastName")))
                .andExpect(jsonPath("$", hasKey("email")))
                .andExpect(jsonPath("$", hasKey("mobileNo")))
                .andExpect(jsonPath("$", hasKey("dob")))
        ;

    }

    private String generateDuplicateEmail() {
        return "{\"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"johndoe@yahoo.com\", \"mobileNo\": \"081809712727\", \"dob\" : \"2001-01-01\"}";
    }

    private String generateDuplicateMobileNo() {
        return "{\"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"johndoe-test@yahoo.com\", \"mobileNo\": \"081809712728\", \"dob\" : \"2001-01-01\"}";
    }

}
