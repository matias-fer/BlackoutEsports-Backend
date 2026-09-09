package cl.duocuc.blackoutbacksett.players.exception;

public class PlayerNotFoundException extends RuntimeException {

    public PlayerNotFoundException(String id) {
        super("No se encontró un jugador con id '" + id + "'");
    }
}
