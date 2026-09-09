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
public class MouseSettings {

    @Column(name = "dpi", nullable = false)
    private Integer dpi;

    @Column(name = "in_game_sens", nullable = false)
    private Double inGameSens;

    // Se recalcula siempre a partir de dpi * inGameSens, no se recibe del cliente
    @Column(name = "edpi")
    private Double edpi;

    @Column(name = "windows_sens")
    private Double windowsSens;

    @Column(name = "hz")
    private Integer hz;

    @Column(name = "resolution", length = 20)
    private String resolution;

    @Column(name = "aspect_ratio", length = 20)
    private String aspectRatio;
}
