package cl.duocuc.blackoutbacksett.players.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GearDto {

    @NotBlank(message = "El mouse es obligatorio")
    private String mouse;

    private String mousepad;

    @NotBlank(message = "El teclado es obligatorio")
    private String keyboard;

    private String monitor;

    @NotBlank(message = "El headset es obligatorio")
    private String headset;
}
