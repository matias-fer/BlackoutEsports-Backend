package cl.duocuc.blackoutbacksett.players.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerRequest {

    // Opcional: si no se envía, se genera automáticamente a partir del handle
    @Pattern(regexp = "^[a-z0-9-]*$", message = "El id solo puede tener minúsculas, números y guiones")
    private String id;

    @NotBlank(message = "El handle es obligatorio")
    @Size(max = 60)
    private String handle;

    @Size(max = 120)
    private String realName;

    @NotBlank(message = "El equipo es obligatorio")
    @Size(max = 60)
    private String team;

    @NotBlank(message = "El código de país es obligatorio (ej: CL, BR, US)")
    @Size(min = 2, max = 2)
    private String countryCode;

    @NotBlank(message = "El rol es obligatorio")
    @Size(max = 40)
    private String role;

    @NotBlank(message = "El juego es obligatorio")
    @Size(max = 30)
    private String game;

    @NotNull(message = "Las settings de mouse son obligatorias")
    @Valid
    private MouseSettingsDto settings;

    @Valid
    private CrosshairDto crosshair;

    @NotNull(message = "El gear es obligatorio")
    @Valid
    private GearDto gear;
}
