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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * test performance
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GameSalesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final int ONE_MILLION = 1000000;
    private final int ONE_SECOND = 1000;
    private final int ONE_MINUTE = 60000;

    @Test
    public void testImportCSVFile() throws Exception {
        long startTime = System.currentTimeMillis();
        mockMvc.perform(get("/gameSales/import"))
               .andExpect(status().isOk());
        long endTime = System.currentTimeMillis();
        assertTrue(endTime - startTime < ONE_MINUTE, "Import took longer than expected.");
    }


    @Test
    public void testGetGameSalesPagination() throws Exception {
        // test pagination with size 100
        GameSalesPageForm form = new GameSalesPageForm(); // Assuming this class has setters
         form.setPage(1);
         form.setSize(100);

        // Convert form to JSON
        String jsonContent = asJsonString(form);

        long startTime = System.currentTimeMillis();
        mockMvc.perform(post("/gameSales/getGameSales")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(jsonContent))
               .andExpect(MockMvcResultMatchers.jsonPath("$.data.size").value(100)) // test pagination
               .andExpect(MockMvcResultMatchers.jsonPath("$.data.total").value(ONE_MILLION))// test totalRecords
               .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
               .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("ok"));
        long endTime = System.currentTimeMillis();
        assertTrue(endTime - startTime < ONE_SECOND, "GetGameSales took longer than expected.");

        // test pagination with size 110
        form.setSize(110);
        jsonContent = asJsonString(form);

        mockMvc.perform(post("/gameSales/getGameSales")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(jsonContent))
               .andExpect(status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.data.size").value(110)) // test pagination
               .andExpect(MockMvcResultMatchers.jsonPath("$.data.total").value(ONE_MILLION))// test totalRecords
               .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
               .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("ok"));
    }


    @Test
    public void testGetGameSalesDateQuery() throws Exception {
        GameSalesPageForm form = new GameSalesPageForm(); // Assuming this class has setters
        form.setSaleDateGe(convertToDate("2024-04-21 00:00:17"));
        form.setSaleDateLe(convertToDate("2024-04-25 17:43:17"));

        String jsonContent = asJsonString(form);

        long startTime = System.currentTimeMillis();
        mockMvc.perform(post("/gameSales/getGameSales")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(jsonContent))
               .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
               .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("ok"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.data.total", lessThan(ONE_MILLION))); // test totalRecords

        long endTime = System.currentTimeMillis();
        assertTrue(endTime - startTime < ONE_SECOND, "GetGameSales took longer than expected.");
    }

    @Test
    public void testGetGameSalesPriceQuery() throws Exception {
        GameSalesPageForm form = new GameSalesPageForm(); // Assuming this class has setters
        form.setPriceGt(BigDecimal.ZERO);
        form.setPriceLt(new BigDecimal(999999));

        String jsonContent = asJsonString(form);

        mockMvc.perform(post("/gameSales/getGameSales")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(jsonContent))
               .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
               .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("ok"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.data.total").value(ONE_MILLION));

        // test api with a different price range
        form.setPriceLt(new BigDecimal(10));
        jsonContent = asJsonString(form);
        mockMvc.perform(post("/gameSales/getGameSales")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(jsonContent))
               .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
               .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("ok"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.data.total", lessThan(ONE_MILLION)));
    }

    @Test
    public void testGetTotalSales() throws Exception {
        GameTotalSalesForm form = new GameTotalSalesForm();
        form.setSaleDateGe(convertToDate("2024-04-21 00:00:17"));
        form.setSaleDateLe(convertToDate("2024-04-31 17:43:17"));
        String jsonContent = asJsonString(form);

        long startTime = System.currentTimeMillis();
        mockMvc.perform(post("/gameSales/getTotalSales")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(jsonContent))
               .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
               .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("ok"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalSalesNo", greaterThan(0)));

        long endTime = System.currentTimeMillis();
        assertTrue(endTime - startTime < ONE_SECOND, "GetTotalSales took longer than expected.");

        //game no starts from 1 to 100, test if will get 0 records if set game no as 101
        form.setGameNo(101);
        jsonContent = asJsonString(form);
        mockMvc.perform(post("/gameSales/getTotalSales")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(jsonContent))
               .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
               .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("ok"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalSalesNo").value(0));
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Date convertToDate(String s){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date date = dateFormat.parse(s);
            return date;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
