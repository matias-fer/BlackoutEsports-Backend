package cl.duocuc.blackoutbacksett.players.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MouseSettingsResponse {
    private Integer dpi;
    private Double inGameSens;
    private Double edpi; // calculado por el backend: dpi * inGameSens
    private Double windowsSens;
    private Integer hz;
    private String resolution;
    private String aspectRatio;
}
