package cl.duocuc.blackoutbacksett.players.service;

import cl.duocuc.blackoutbacksett.players.dto.PlayerMapper;
import cl.duocuc.blackoutbacksett.players.dto.PlayerRequest;
import cl.duocuc.blackoutbacksett.players.dto.PlayerResponse;
import cl.duocuc.blackoutbacksett.players.exception.DuplicatePlayerException;
import cl.duocuc.blackoutbacksett.players.exception.PlayerNotFoundException;
import cl.duocuc.blackoutbacksett.players.model.Player;
import cl.duocuc.blackoutbacksett.players.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlayerService {

    private final PlayerRepository playerRepository;

    public List<PlayerResponse> findAll() {
        return playerRepository.findAll()
                .stream()
                .map(PlayerMapper::toResponse)
                .toList();
    }

    public List<PlayerResponse> findByGame(String game) {
        return playerRepository.findByGameIgnoreCase(game)
                .stream()
                .map(PlayerMapper::toResponse)
                .toList();
    }

    public PlayerResponse findById(String id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id));
        return PlayerMapper.toResponse(player);
    }

    public List<PlayerResponse> search(String game, String query) {
        if (query == null || query.isBlank()) {
            return findByGame(game);
        }
        return playerRepository.search(game, query)
                .stream()
                .map(PlayerMapper::toResponse)
                .toList();
    }

    public List<PlayerResponse> findFeatured(String game, int count) {
        return playerRepository
                .findByGameIgnoreCaseOrderByUpdatedAtDesc(game, PageRequest.of(0, count))
                .stream()
                .map(PlayerMapper::toResponse)
                .toList();
    }

    @Transactional
    public PlayerResponse create(PlayerRequest request) {
        String id = (request.getId() != null && !request.getId().isBlank())
                ? request.getId()
                : PlayerMapper.slugify(request.getHandle());

        if (playerRepository.existsByIdIgnoreCase(id)) {
            throw new DuplicatePlayerException(id);
        }

        Player player = PlayerMapper.toEntity(request);
        Player saved = playerRepository.save(player);
        return PlayerMapper.toResponse(saved);
    }

    @Transactional
    public PlayerResponse update(String id, PlayerRequest request) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id));

        PlayerMapper.updateEntity(player, request);
        Player saved = playerRepository.save(player);
        return PlayerMapper.toResponse(saved);
    }

    @Transactional
    public void delete(String id) {
        if (!playerRepository.existsById(id)) {
            throw new PlayerNotFoundException(id);
        }
        playerRepository.deleteById(id);
    }
}
