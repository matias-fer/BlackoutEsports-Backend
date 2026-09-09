package cl.duocuc.blackoutbacksett.players.dto;

import cl.duocuc.blackoutbacksett.players.model.Crosshair;
import cl.duocuc.blackoutbacksett.players.model.Gear;
import cl.duocuc.blackoutbacksett.players.model.MouseSettings;
import cl.duocuc.blackoutbacksett.players.model.Player;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class PlayerMapper {

    private static final Pattern NON_ALPHANUMERIC = Pattern.compile("[^a-z0-9]+");

    private PlayerMapper() {
    }

    // Convierte "Prinzcl" -> "prinzcl", "TenZ 2.0" -> "tenz-2-0"
    public static String slugify(String handle) {
        String normalized = Normalizer.normalize(handle, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase();
        String slug = NON_ALPHANUMERIC.matcher(normalized).replaceAll("-");
        return slug.replaceAll("^-+|-+$", "");
    }

    public static Player toEntity(PlayerRequest request) {
        MouseSettings settings = MouseSettings.builder()
                .dpi(request.getSettings().getDpi())
                .inGameSens(request.getSettings().getInGameSens())
                .windowsSens(request.getSettings().getWindowsSens())
                .hz(request.getSettings().getHz())
                .resolution(request.getSettings().getResolution())
                .aspectRatio(request.getSettings().getAspectRatio())
                .build();

        Crosshair crosshair = null;
        if (request.getCrosshair() != null) {
            CrosshairDto c = request.getCrosshair();
            crosshair = Crosshair.builder()
                    .code(c.getCode())
                    .color(c.getColor())
                    .size(c.getSize())
                    .thickness(c.getThickness())
                    .gap(c.getGap())
                    .outline(c.getOutline())
                    .opacity(c.getOpacity())
                    .centerDot(c.getCenterDot())
                    .outerLines(c.getOuterLines())
                    .build();
        }

        Gear gear = Gear.builder()
                .mouse(request.getGear().getMouse())
                .mousepad(request.getGear().getMousepad())
                .keyboard(request.getGear().getKeyboard())
                .monitor(request.getGear().getMonitor())
                .headset(request.getGear().getHeadset())
                .build();

        String id = (request.getId() != null && !request.getId().isBlank())
                ? request.getId()
                : slugify(request.getHandle());

        return Player.builder()
                .id(id)
                .handle(request.getHandle())
                .realName(request.getRealName())
                .team(request.getTeam())
                .countryCode(request.getCountryCode().toUpperCase())
                .role(request.getRole())
                .game(request.getGame())
                .settings(settings)
                .crosshair(crosshair)
                .gear(gear)
                .build();
    }

    public static void updateEntity(Player player, PlayerRequest request) {
        player.setHandle(request.getHandle());
        player.setRealName(request.getRealName());
        player.setTeam(request.getTeam());
        player.setCountryCode(request.getCountryCode().toUpperCase());
        player.setRole(request.getRole());
        player.setGame(request.getGame());

        player.getSettings().setDpi(request.getSettings().getDpi());
        player.getSettings().setInGameSens(request.getSettings().getInGameSens());
        player.getSettings().setWindowsSens(request.getSettings().getWindowsSens());
        player.getSettings().setHz(request.getSettings().getHz());
        player.getSettings().setResolution(request.getSettings().getResolution());
        player.getSettings().setAspectRatio(request.getSettings().getAspectRatio());

        if (request.getCrosshair() != null) {
            CrosshairDto c = request.getCrosshair();
            player.setCrosshair(Crosshair.builder()
                    .code(c.getCode())
                    .color(c.getColor())
                    .size(c.getSize())
                    .thickness(c.getThickness())
                    .gap(c.getGap())
                    .outline(c.getOutline())
                    .opacity(c.getOpacity())
                    .centerDot(c.getCenterDot())
                    .outerLines(c.getOuterLines())
                    .build());
        } else {
            player.setCrosshair(null);
        }

        player.getGear().setMouse(request.getGear().getMouse());
        player.getGear().setMousepad(request.getGear().getMousepad());
        player.getGear().setKeyboard(request.getGear().getKeyboard());
        player.getGear().setMonitor(request.getGear().getMonitor());
        player.getGear().setHeadset(request.getGear().getHeadset());
    }

    public static PlayerResponse toResponse(Player player) {
        MouseSettingsResponse settings = MouseSettingsResponse.builder()
                .dpi(player.getSettings().getDpi())
                .inGameSens(player.getSettings().getInGameSens())
                .edpi(player.getSettings().getEdpi())
                .windowsSens(player.getSettings().getWindowsSens())
                .hz(player.getSettings().getHz())
                .resolution(player.getSettings().getResolution())
                .aspectRatio(player.getSettings().getAspectRatio())
                .build();

        CrosshairDto crosshair = null;
        if (player.getCrosshair() != null && player.getCrosshair().isConfigured()) {
            Crosshair c = player.getCrosshair();
            crosshair = new CrosshairDto();
            crosshair.setCode(c.getCode());
            crosshair.setColor(c.getColor());
            crosshair.setSize(c.getSize());
            crosshair.setThickness(c.getThickness());
            crosshair.setGap(c.getGap());
            crosshair.setOutline(c.getOutline());
            crosshair.setOpacity(c.getOpacity());
            crosshair.setCenterDot(c.getCenterDot());
            crosshair.setOuterLines(c.getOuterLines());
        }

        GearDto gear = new GearDto();
        gear.setMouse(player.getGear().getMouse());
        gear.setMousepad(player.getGear().getMousepad());
        gear.setKeyboard(player.getGear().getKeyboard());
        gear.setMonitor(player.getGear().getMonitor());
        gear.setHeadset(player.getGear().getHeadset());

        return PlayerResponse.builder()
                .id(player.getId())
                .handle(player.getHandle())
                .realName(player.getRealName())
                .team(player.getTeam())
                .countryCode(player.getCountryCode())
                .role(player.getRole())
                .game(player.getGame())
                .settings(settings)
                .crosshair(crosshair)
                .gear(gear)
                .updatedAt(player.getUpdatedAt())
                .build();
    }
}
