package nl.hu.cisq1.lingo.trainer.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.words.data.SpringWordRepository;
import nl.hu.cisq1.lingo.words.domain.Word;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(CiTestConfiguration.class)
@AutoConfigureMockMvc
class TrainerControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SpringWordRepository repository;

    @Test
    void startNewGame() throws Exception {
        when(repository.findRandomWordByLength(5)).thenReturn(Optional.of(new Word("braam")));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:8080/game/start");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId", greaterThanOrEqualTo(0)))
                .andExpect(jsonPath("$.status").value("ONGOING"))
                .andExpect(jsonPath("$.score").value(0))
                .andExpect(jsonPath("$.roundsWon").value(0))
                .andExpect(jsonPath("$.hints", hasSize(1)))
                .andExpect(jsonPath("$.feedback", hasSize(0)));
    }

    @Test
    void showGame() throws Exception {
        when(repository.findRandomWordByLength(5)).thenReturn(Optional.of(new Word("braam")));

        RequestBuilder gameRequest = MockMvcRequestBuilders
                .post("http://localhost:8080/game/start");

        MockHttpServletResponse response = mockMvc.perform(gameRequest).andReturn().getResponse();
        Integer id = JsonPath.read(response.getContentAsString(), "$.gameId");

        RequestBuilder guessRequest = MockMvcRequestBuilders
                .get("http://localhost:8080/game/" + id);

        mockMvc.perform(guessRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId", greaterThanOrEqualTo(0)))
                .andExpect(jsonPath("$.status").value("ONGOING"))
                .andExpect(jsonPath("$.score").value(0))
                .andExpect(jsonPath("$.roundsWon").value(0))
                .andExpect(jsonPath("$.hints", hasSize(1)))
                .andExpect(jsonPath("$.feedback", hasSize(0)));
    }

    @Test
    void makeGuess() throws Exception {
        when(repository.findRandomWordByLength(5)).thenReturn(Optional.of(new Word("braam")));
        when(repository.checkWordExists("kroon")).thenReturn(true);

        RequestBuilder gameRequest = MockMvcRequestBuilders
                .post("http://localhost:8080/game/start");

        MockHttpServletResponse response = mockMvc.perform(gameRequest).andReturn().getResponse();
        Integer id = JsonPath.read(response.getContentAsString(), "$.gameId");


        ObjectMapper mapper = new ObjectMapper();
        String word = mapper.writeValueAsString(new nl.hu.cisq1.lingo.trainer.presentation.dto.Word("braam"));

        RequestBuilder guessRequest = MockMvcRequestBuilders
                .patch("http://localhost:8080/game/" + id + "/guess")
                .content(word)
                .contentType("application/json");

        mockMvc.perform(guessRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId", greaterThanOrEqualTo(0)))
                .andExpect(jsonPath("$.status").value("ONGOING"))
                .andExpect(jsonPath("$.score").value(0))
                .andExpect(jsonPath("$.roundsWon").value(0))
                .andExpect(jsonPath("$.hints", hasSize(2)))
                .andExpect(jsonPath("$.feedback", hasSize(1)));
    }

    @Test
    void startNewRound() throws Exception {
        when(repository.findRandomWordByLength(5)).thenReturn(Optional.of(new Word("braam")));
        when(repository.findRandomWordByLength(6)).thenReturn(Optional.of(new Word("straat")));
        when(repository.checkWordExists("braam")).thenReturn(true);

        RequestBuilder gameRequest = MockMvcRequestBuilders
                .post("http://localhost:8080/game/start");

        MockHttpServletResponse response = mockMvc.perform(gameRequest).andReturn().getResponse();
        Integer id = JsonPath.read(response.getContentAsString(), "$.gameId");

        ObjectMapper mapper = new ObjectMapper();
        String word = mapper.writeValueAsString(new nl.hu.cisq1.lingo.trainer.presentation.dto.Word("braam"));

        RequestBuilder guessRequest = MockMvcRequestBuilders
                .patch("http://localhost:8080/game/" + id + "/guess")
                .content(word)
                .contentType("application/json");

        mockMvc.perform(guessRequest)
                .andExpect(jsonPath("$.status").value("WAITING_FOR_NEW_ROUND"))
                .andExpect(jsonPath("$.roundsWon").value(1));

        RequestBuilder roundRequest = MockMvcRequestBuilders
                .post("http://localhost:8080/game/"+ id + "/newRound");

        mockMvc.perform(roundRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId", greaterThanOrEqualTo(0)))
                .andExpect(jsonPath("$.status").value("ONGOING"))
                .andExpect(jsonPath("$.score").value(25))
                .andExpect(jsonPath("$.roundsWon").value(1))
                .andExpect(jsonPath("$.hints", hasSize(1)))
                .andExpect(jsonPath("$.feedback", hasSize(0)));
    }
}