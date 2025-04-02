package sem4.ea.ss25.battleship.assignment2.board_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sem4.ea.ss25.battleship.assignment2.board_service.dto.BoardDTO;
import sem4.ea.ss25.battleship.assignment2.board_service.domain.BoardMessage;
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
		BoardMessage message = new BoardMessage();
		message.setType("CREATE_BOARD");

		boardMessageSender.sendCreateBoardCommand(message);
		return ResponseEntity.accepted().body("Board creation request sent");
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

		BoardMessage message = new BoardMessage();
		message.setType("MAKE_GUESS");
		message.setGameId(gameId);
		message.setBoardId(boardId);
		message.setPlayerId(playerId);
		message.setOpponentId(opponentId);
		message.setX(x);
		message.setY(y);

		boardMessageSender.sendMakeGuessCommand(message);
		return ResponseEntity.accepted().body("Guess request sent");
	}

	@PostMapping("/endGame")
	public ResponseEntity<String> endGame(@RequestParam Long boardId) {
		String result = boardService.endGame(boardId);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/{boardId}")
	public ResponseEntity<BoardDTO> getBoard(@PathVariable Long boardId) {
		BoardDTO board = boardService.getBoard(boardId);
		return ResponseEntity.ok(board);
	}
}