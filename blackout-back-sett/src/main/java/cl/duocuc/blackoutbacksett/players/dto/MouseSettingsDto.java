package cl.duocuc.blackoutbacksett.players.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MouseSettingsDto {

    @NotNull(message = "El DPI es obligatorio")
    @Min(value = 100, message = "El DPI mínimo válido es 100")
    @Max(value = 32000, message = "El DPI máximo válido es 32000")
    private Integer dpi;

    @NotNull(message = "La sensibilidad in-game es obligatoria")
    @DecimalMin(value = "0.001", message = "La sensibilidad debe ser mayor a 0")
    private Double inGameSens;

    // No se recibe del cliente: el backend lo calcula (dpi * inGameSens)

    private Double windowsSens;

    @Min(value = 30, message = "La frecuencia mínima es 30 Hz")
    @Max(value = 1000, message = "La frecuencia máxima es 1000 Hz")
    private Integer hz;

    private String resolution;

    private String aspectRatio;
}
