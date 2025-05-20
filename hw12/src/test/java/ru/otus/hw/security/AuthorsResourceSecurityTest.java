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
import ru.otus.hw.controller.AuthorRestController;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorRestController.class)
@Import(SecurityConfiguration.class)
public class AuthorsResourceSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AuthorService authorService;

    private List<AuthorDto> authorsList = List.of(
            new AuthorDto(1L, "Author_1"),
            new AuthorDto(2L, "Author_2"),
            new AuthorDto(3L, "Author_3"));

    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    @Test
    public void testOnlyForAuthenticatedUsers() throws Exception {
        when(authorService.findAll()).thenReturn(authorsList);

        when(authorService.findById(1L)).thenReturn(Optional.ofNullable(authorsList.get(0)));

        AuthorDto authorDto = new AuthorDto(1L, "Author1");
        given(authorService.save(any())).willReturn(authorDto);
        String expectedResult = mapper.writeValueAsString(authorDto);

        mockMvc.perform(get("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/authors/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedResult))
                .andExpect(status().isOk());
    }

    @Test
    public void redirectsToLoginFormIfNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/api/v1/authors/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(post("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                mapper.writeValueAsString(authorsList.get(0))))
                .andExpect(status().is3xxRedirection());
    }
}
