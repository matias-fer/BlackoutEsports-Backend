package cl.duocuc.blackoutbacksett.players.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Crosshair {

    @Column(name = "crosshair_code", length = 255)
    private String code;

    @Column(name = "crosshair_color", length = 20)
    private String color;

    @Column(name = "crosshair_size")
    private Integer size;

    @Column(name = "crosshair_thickness")
    private Integer thickness;

    @Column(name = "crosshair_gap")
    private Integer gap;

    @Column(name = "crosshair_outline")
    private Boolean outline;

    @Column(name = "crosshair_opacity")
    private Double opacity;

    @Column(name = "crosshair_center_dot")
    private Boolean centerDot;

    @Column(name = "crosshair_outer_lines")
    private Boolean outerLines;

    // true si el jugador tiene una mira configurada (code no vacío).
    // Se usa para decidir si el frontend recibe "crosshair": null o el objeto completo.
    public boolean isConfigured() {
        return code != null && !code.isBlank();
    }
}
