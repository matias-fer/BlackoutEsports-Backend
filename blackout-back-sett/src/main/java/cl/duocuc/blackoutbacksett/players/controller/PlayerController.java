package cl.duocuc.blackoutbacksett.players.controller;

import cl.duocuc.blackoutbacksett.players.dto.PlayerRequest;
import cl.duocuc.blackoutbacksett.players.dto.PlayerResponse;
import cl.duocuc.blackoutbacksett.players.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

// Rutas alineadas con lib/data.ts del frontend:
//   getPlayersByGame(game)      -> GET /api/games/{game}/players
//   getPlayer(id)                -> GET /api/players/{id}
//   searchPlayers(game, query)   -> GET /api/games/{game}/players/search?query=
//   getFeaturedPlayers(count)    -> GET /api/games/{game}/players/featured?count=
@RestController
@RequiredArgsConstructor
@Tag(name = "Players", description = "CRUD y consultas de jugadores de Valorant")
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping("/api/players")
    @Operation(summary = "Lista todos los jugadores de todos los juegos")
    public ResponseEntity<List<PlayerResponse>> findAll() {
        return ResponseEntity.ok(playerService.findAll());
    }

    @GetMapping("/api/players/{id}")
    @Operation(summary = "Obtiene un jugador por su id (slug)")
    public ResponseEntity<PlayerResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(playerService.findById(id));
    }

    @GetMapping("/api/games/{game}/players")
    @Operation(summary = "Lista los jugadores de un juego (ej: valorant)")
    public ResponseEntity<List<PlayerResponse>> findByGame(@PathVariable String game) {
        return ResponseEntity.ok(playerService.findByGame(game));
    }

    @GetMapping("/api/games/{game}/players/search")
    @Operation(summary = "Busca jugadores de un juego por handle, equipo o rol")
    public ResponseEntity<List<PlayerResponse>> search(
            @PathVariable String game,
            @RequestParam(required = false) String query
    ) {
        return ResponseEntity.ok(playerService.search(game, query));
    }

    @GetMapping("/api/games/{game}/players/featured")
    @Operation(summary = "Jugadores actualizados recientemente (para la home)")
    public ResponseEntity<List<PlayerResponse>> featured(
            @PathVariable String game,
            @RequestParam(defaultValue = "4") int count
    ) {
        return ResponseEntity.ok(playerService.findFeatured(game, count));
    }

    @PostMapping("/api/players")
    @Operation(summary = "Crea un nuevo jugador")
    public ResponseEntity<PlayerResponse> create(@Valid @RequestBody PlayerRequest request) {
        PlayerResponse created = playerService.create(request);
        return ResponseEntity
                .created(URI.create("/api/players/" + created.getId()))
                .body(created);
    }

    @PutMapping("/api/players/{id}")
    @Operation(summary = "Actualiza un jugador existente")
    public ResponseEntity<PlayerResponse> update(
            @PathVariable String id,
            @Valid @RequestBody PlayerRequest request
    ) {
        return ResponseEntity.ok(playerService.update(id, request));
    }

    @DeleteMapping("/api/players/{id}")
    @Operation(summary = "Elimina un jugador")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        playerService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
