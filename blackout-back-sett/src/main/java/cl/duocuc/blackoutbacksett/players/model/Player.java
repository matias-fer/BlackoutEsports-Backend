package cl.duocuc.blackoutbacksett.players.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

// El "id" es un slug de texto (ej: "prinzcl"), igual a como lo consume
// el frontend en las rutas /valorant/[playerId] - no es un Long autoincremental.
@Entity
@Table(name = "players")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Player {

    @Id
    @Column(length = 60)
    private String id;

    @Column(nullable = false, unique = true, length = 60)
    private String handle;

    @Column(name = "real_name", length = 120)
    private String realName;

    @Column(nullable = false, length = 60)
    private String team;

    @Column(name = "country_code", nullable = false, length = 2)
    private String countryCode;

    @Column(nullable = false, length = 40)
    private String role;

    // Por ahora solo "valorant"; queda como String para agregar juegos después
    // sin tener que migrar un enum.
    @Column(nullable = false, length = 30)
    private String game;

    @Embedded
    private MouseSettings settings;

    @Embedded
    private Crosshair crosshair;

    @Embedded
    private Gear gear;

    @Column(name = "updated_at", nullable = false)
    private LocalDate updatedAt;

    @PrePersist
    protected void onCreate() {
        this.updatedAt = LocalDate.now();
        recalculateEdpi();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDate.now();
        recalculateEdpi();
    }

    private void recalculateEdpi() {
        if (settings != null && settings.getDpi() != null && settings.getInGameSens() != null) {
            double edpi = Math.round(settings.getDpi() * settings.getInGameSens() * 100) / 100.0;
            settings.setEdpi(edpi);
        }
    }
}
