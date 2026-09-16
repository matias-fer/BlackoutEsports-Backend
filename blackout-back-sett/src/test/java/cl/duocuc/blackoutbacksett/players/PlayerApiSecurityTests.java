package cl.duocuc.blackoutbacksett.players;

import cl.duocuc.blackoutbacksett.players.config.TestTokens;
import cl.duocuc.blackoutbacksett.players.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestTokens.class)
class PlayerApiSecurityTests {
    private static final String BODY = """
            {"handle":"Demon1","realName":"Max Mazanov","team":"NRG","countryCode":"US",
             "role":"Duelista","game":"valorant",
             "settings":{"dpi":800,"inGameSens":0.35,"windowsSens":6,"hz":360,
                         "resolution":"1920x1080","aspectRatio":"16:9"},
             "crosshair":{"code":"0;P;c;1","color":"#00FF00"},
             "gear":{"mouse":"G Pro X","keyboard":"Wooting 60HE","headset":"Cloud II"}}
            """;

    @Autowired MockMvc mvc;
    @Autowired PlayerRepository repository;

    @BeforeEach
    void clean() {
        repository.deleteAll();
    }

    @Test
    void adminYStaffPuedenAdministrarYFanPuedeLeer() throws Exception {
        mvc.perform(post("/api/players").header("Authorization", bearer(TestTokens.admin()))
                        .contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.id").value("demon1"));
        mvc.perform(get("/api/players/demon1").header("Authorization", bearer(TestTokens.fan())))
                .andExpect(status().isOk()).andExpect(jsonPath("$.handle").value("Demon1"));
        String updated = BODY.replace("\"NRG\"", "\"Blackout\"");
        mvc.perform(put("/api/players/demon1").header("Authorization", bearer(TestTokens.staff()))
                        .contentType(MediaType.APPLICATION_JSON).content(updated))
                .andExpect(status().isOk()).andExpect(jsonPath("$.team").value("Blackout"));
        mvc.perform(get("/api/games/valorant/players").header("Authorization", bearer(TestTokens.fan())))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value("demon1"));
        mvc.perform(delete("/api/players/demon1").header("Authorization", bearer(TestTokens.admin())))
                .andExpect(status().isNoContent());
    }

    @Test
    void fanYSesionEntraSinRolNoPuedenEscribir() throws Exception {
        mvc.perform(post("/api/players").header("Authorization", bearer(TestTokens.fan()))
                        .contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isForbidden());
        mvc.perform(post("/api/players").header("Authorization", bearer(TestTokens.entraSinRol()))
                        .contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isForbidden());
    }

    @Test
    void endpointsRechazanAusenciaOTokenInvalido() throws Exception {
        mvc.perform(get("/api/players")).andExpect(status().isUnauthorized());
        mvc.perform(post("/api/players").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isUnauthorized());
        mvc.perform(get("/api/players").header("Authorization", "Bearer incorrecto"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void swaggerYCorsSiguenDisponibles() throws Exception {
        mvc.perform(get("/v3/api-docs")).andExpect(status().isOk())
                .andExpect(jsonPath("$.components.securitySchemes.bearerAuth").exists());
        mvc.perform(options("/api/players").header("Origin", "http://localhost:5173")
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5173"));
    }

    private static String bearer(String token) {
        return "Bearer " + token;
    }
}
