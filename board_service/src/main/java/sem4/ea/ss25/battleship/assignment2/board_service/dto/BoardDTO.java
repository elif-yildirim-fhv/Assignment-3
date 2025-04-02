package sem4.ea.ss25.battleship.assignment2.board_service.dto;

import java.util.List;

public record BoardDTO(Long id, List<CellDTO> cells) {
}