package cl.duocuc.blackoutbacksett.players.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

// Los nombres de los campos son EXACTAMENTE los mismos que en
// blackout-esports/lib/types.ts (interface Player) para que el frontend
// pueda usar la respuesta del backend sin transformar nada.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerResponse {

    private String id;
    private String handle;
    private String realName;
    private String team;
    private String countryCode;
    private String role;
    private String game;
    private MouseSettingsResponse settings;
    private CrosshairDto crosshair; // null si el jugador no tiene mira configurada
    private GearDto gear;
    private LocalDate updatedAt;
}
