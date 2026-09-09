package cl.duocuc.blackoutbacksett.players.repository;

import cl.duocuc.blackoutbacksett.players.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, String> {

    boolean existsByIdIgnoreCase(String id);

    List<Player> findByGameIgnoreCase(String game);

    @Query("""
            SELECT p FROM Player p
            WHERE LOWER(p.game) = LOWER(:game)
            AND (
                LOWER(p.handle) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(p.team) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(p.role) LIKE LOWER(CONCAT('%', :query, '%'))
            )
            """)
    List<Player> search(@Param("game") String game, @Param("query") String query);

    List<Player> findByGameIgnoreCaseOrderByUpdatedAtDesc(String game, org.springframework.data.domain.Pageable pageable);
}
