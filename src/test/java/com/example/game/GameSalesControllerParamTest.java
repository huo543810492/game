package com.example.game;

import com.example.game.form.GameSalesPageForm;
import com.example.game.form.GameTotalSalesForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * test form validation
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GameSalesControllerParamTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void testGetGameSalesPriceParam() throws Exception {
        GameSalesPageForm form = new GameSalesPageForm();
         form.setPage(1);
         form.setSize(100);
         form.setPriceLt(new BigDecimal("-1"));
        form.setPriceGt(new BigDecimal("-122"));
        String jsonContent = asJsonString(form);

        mockMvc.perform(post("/gameSales/getGameSales")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(jsonContent))
               .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(500))
               .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.containsString("Validation failed for argument")));
    }


    @Test
    public void testGetTotalSalesParam() throws Exception {
        GameTotalSalesForm form = new GameTotalSalesForm();
        form.setGameNo(-1);
        String jsonContent = asJsonString(form);

        mockMvc.perform(post("/gameSales/getTotalSales")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(jsonContent))
               .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(500))
               .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.containsString("Validation failed for argument")));
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
