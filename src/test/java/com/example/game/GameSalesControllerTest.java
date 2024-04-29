package com.example.game;

import com.example.game.form.GameSalesPageForm;
import com.example.game.form.GameTotalSalesForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GameSalesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testImportCSVFile() throws Exception {
        long startTime = System.currentTimeMillis();
        mockMvc.perform(get("/gameSales/import"))
               .andExpect(status().isOk());
        long endTime = System.currentTimeMillis();
        assertTrue(endTime - startTime < 60000, "Import took longer than expected.");
    }


    @Test
    public void testGetGameSales() throws Exception {
        // Create a dummy GameSalesPageForm object
        GameSalesPageForm form = new GameSalesPageForm(); // Assuming this class has setters
         form.setPage(1);
         form.setSize(100);

        // Convert form to JSON
        String jsonContent = asJsonString(form);

        long startTime = System.currentTimeMillis();
        mockMvc.perform(post("/gameSales/getGameSales")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(jsonContent))
               .andExpect(status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.records", hasSize(100))); // test pagination
        long endTime = System.currentTimeMillis();
        assertTrue(endTime - startTime < 1000, "GetGameSales took longer than expected.");

    }

    @Test
    public void testGetTotalSales() throws Exception {
        // Create a dummy GameTotalSalesForm object
        GameTotalSalesForm form = new GameTotalSalesForm(); // Assuming this class has setters

        // Convert form to JSON
        String jsonContent = asJsonString(form);

        long startTime = System.currentTimeMillis();
        mockMvc.perform(post("/gameSales/getTotalSales")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(jsonContent))
               .andExpect(status().isOk());
        long endTime = System.currentTimeMillis();
        assertTrue(endTime - startTime < 1000, "GetTotalSales took longer than expected.");
    }

    // JSON utility function
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
