package cl.duocuc.blackoutbacksett.players.exception;

public class DuplicatePlayerException extends RuntimeException {

    public DuplicatePlayerException(String id) {
        super("Ya existe un jugador con el id '" + id + "'");
    }
}
