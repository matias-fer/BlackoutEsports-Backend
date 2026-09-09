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
public class Gear {

    @Column(name = "gear_mouse", length = 120)
    private String mouse;

    @Column(name = "gear_mousepad", length = 120)
    private String mousepad;

    @Column(name = "gear_keyboard", length = 120)
    private String keyboard;

    @Column(name = "gear_monitor", length = 120)
    private String monitor;

    @Column(name = "gear_headset", length = 120)
    private String headset;
}
