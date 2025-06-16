package ru.otus.hw.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.config.SecurityConfiguration;
import ru.otus.hw.controller.GenreRestController;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenreRestController.class)
@Import(SecurityConfiguration.class)
public class GenreResourceSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreService genreService;

    @Autowired
    private ObjectMapper mapper;

    private List<GenreDto> genreDtoList = List.of(
            new GenreDto(1L, "Genre_1"),
            new GenreDto(2L, "Genre_2"),
            new GenreDto(3L, "Genre_3"));

    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    @Test
    public void testOnlyForAuthenticatedUsers() throws Exception {
        when(genreService.findAll()).thenReturn(genreDtoList);

        when(genreService.findById(1L)).thenReturn(Optional.ofNullable(genreDtoList.get(0)));

        mockMvc.perform(get("/api/v1/genres")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/genres/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @Test
    public void testPostHttpMethodOnlyForAdmin() throws Exception {

        GenreDto genreDto = new GenreDto(1L, "Genre1");
        given(genreService.save(any())).willReturn(genreDto);
        String expectedResult = mapper.writeValueAsString(genreDto);

        mockMvc.perform(post("/api/v1/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedResult))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    @Test
    public void testPostHttpMethodAccessDeniedForNotAdmin() throws Exception {

        GenreDto genreDto = new GenreDto(1L, "Genre1");
        given(genreService.save(any())).willReturn(genreDto);
        String expectedResult = mapper.writeValueAsString(genreDto);

        mockMvc.perform(post("/api/v1/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedResult))
                .andExpect(status().isForbidden());
    }


    @Test
    public void redirectsToLoginFormIfNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/genres")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/api/v1/genres/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(post("/api/v1/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                mapper.writeValueAsString(genreDtoList.get(0))))
                .andExpect(status().is3xxRedirection());
    }
}
