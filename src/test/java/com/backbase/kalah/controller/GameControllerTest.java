package com.backbase.kalah.controller;

import com.backbase.kalah.TestRedisConfiguration;
import com.backbase.kalah.dto.GameResponse;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = TestRedisConfiguration.class)
@AutoConfigureMockMvc
public class GameControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void createCorrect() throws Exception {
        createNewGame();
    }

    @Test
    public void moveGameNotExist() throws Exception {
        performMove(100L, 1).andExpect(status().isNotFound());
    }

    @Test
    public void moveSimple() throws Exception {
        GameResponse response = createNewGame();
        performMove(response.getId(), 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.1", is("0")))
                .andExpect(jsonPath("$.status.7", is("1")))
                .andReturn();
    }

    @Test
    public void moveInvalidTurn() throws Exception {
        GameResponse response = createNewGame();
        performMove(response.getId(), 1).andExpect(status().isOk());
        performMove(response.getId(), 8).andExpect(status().isBadRequest());
    }

    @Test
    public void moveInvalidPitId() throws Exception {
        GameResponse response = createNewGame();
        performMove(response.getId(), 20).andExpect(status().isBadRequest());
    }

    @Test
    public void moveEmptyPitId() throws Exception {
        GameResponse response = createNewGame();
        performMove(response.getId(), 2).andExpect(status().isOk());
        performMove(response.getId(), 8).andExpect(status().isOk());
        performMove(response.getId(), 2).andExpect(status().isBadRequest());
    }

    @Test
    public void moveToEndGame() throws Exception {
        GameResponse response = createNewGame();
        int[] play = {1, 5, 8, 1, 9, 1, 12, 1, 13, 4, 12, 1, 8, 5, 10, 4, 11, 4, 12, 9, 1, 8, 2, 13, 2, 11, 4, 10, 6, 1};
        for (int move : play) {
            performMove(response.getId(), move).andExpect(status().isOk());
        }
    }

    @Test
    public void moveAfterGameOver() throws Exception {
        GameResponse response = createNewGame();
        int[] play = {1, 5, 8, 1, 9, 1, 12, 1, 13, 4, 12, 1, 8, 5, 10, 4, 11, 4, 12, 9, 1, 8, 2, 13, 2, 11, 4, 10, 6, 1};
        for (int move : play) {
            performMove(response.getId(), move).andExpect(status().isOk());
        }
        performMove(response.getId(), 2).andExpect(status().isBadRequest());

    }

    private GameResponse createNewGame() throws Exception {
        MvcResult mvcResult = mvc.perform(post("/games")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();
        return new Gson().fromJson(responseBody, GameResponse.class);
    }

    private ResultActions performMove(Long id, int pitId) throws Exception {
        return mvc.perform(put("/games/" + id + "/pits/" + pitId).contentType(MediaType.APPLICATION_JSON));

    }
}
