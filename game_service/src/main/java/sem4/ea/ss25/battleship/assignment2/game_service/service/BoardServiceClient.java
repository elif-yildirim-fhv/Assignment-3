package sem4.ea.ss25.battleship.assignment2.game_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import sem4.ea.ss25.battleship.assignment2.game_service.dto.BoardDTO;
import sem4.ea.ss25.battleship.assignment2.game_service.dto.ShipDTO;

@FeignClient(name = "board-service")
public interface BoardServiceClient {

	@PostMapping("/api/board/create")
	BoardDTO createBoard();

	@PostMapping("/api/board/placeShip")
	ShipDTO placeShip(
			@RequestParam Long boardId,
			@RequestParam Long playerId,
			@RequestParam int length,
			@RequestParam int x,
			@RequestParam int y,
			@RequestParam boolean isHorizontal);

	@PostMapping("/api/board/guess")
	String makeGuess(
			@RequestParam Long gameId,
			@RequestParam Long boardId,
			@RequestParam Long playerId,
			@RequestParam Long opponentId,
			@RequestParam int x,
			@RequestParam int y);

	@PostMapping("/api/board/endGame")
	String endGame(@RequestParam Long boardId);

	@GetMapping("/api/board/{boardId}")
	BoardDTO getBoard(@PathVariable Long boardId);
}