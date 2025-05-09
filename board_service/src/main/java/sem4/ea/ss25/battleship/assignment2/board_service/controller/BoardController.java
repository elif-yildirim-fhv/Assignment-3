package sem4.ea.ss25.battleship.assignment2.board_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sem4.ea.ss25.battleship.assignment2.board_service.dto.ShipDTO;
import sem4.ea.ss25.battleship.assignment2.board_service.service.BoardService;
import sem4.ea.ss25.battleship.assignment2.board_service.service.BoardMessageSender;

@RestController
@RequestMapping("/api/board")
public class BoardController {

	@Autowired
	private BoardService boardService;

	@Autowired
	private BoardMessageSender boardMessageSender;

	@PostMapping("/create")
	public ResponseEntity<String> createBoard() {
		boardMessageSender.sendCreateBoardMessage();
		return ResponseEntity.ok("Board creation request sent");
	}

	@PostMapping("/placeShip")
	public ResponseEntity<ShipDTO> placeShip(
			@RequestParam Long boardId,
			@RequestParam Long playerId,
			@RequestParam int length,
			@RequestParam int x,
			@RequestParam int y,
			@RequestParam boolean isHorizontal) {
		ShipDTO ship = boardService.placeShip(boardId, playerId, length, x, y, isHorizontal);
		return ResponseEntity.ok(ship);
	}

	@PostMapping("/guess")
	public ResponseEntity<String> makeGuess(
			@RequestParam Long gameId,
			@RequestParam Long boardId,
			@RequestParam Long playerId,
			@RequestParam Long opponentId,
			@RequestParam int x,
			@RequestParam int y) {
		String result = boardService.makeGuess(gameId, boardId, playerId, opponentId, x, y);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/endGame")
	public ResponseEntity<String> endGame(@RequestParam Long boardId) {
		String result = boardService.endGame(boardId);
		return ResponseEntity.ok(result);
	}
}