package cl.duocuc.blackoutbacksett.players.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrosshairDto {

    @Size(max = 255)
    private String code;

    private String color;
    private Integer size;
    private Integer thickness;
    private Integer gap;
    private Boolean outline;
    private Double opacity;
    private Boolean centerDot;
    private Boolean outerLines;
}
