package sem4.ea.ss25.battleship.assignment2.board_service.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import sem4.ea.ss25.battleship.assignment2.board_service.config.RabbitMQConfig;
import sem4.ea.ss25.battleship.assignment2.board_service.dto.BoardDTO;


@Service
public class BoardMessageReceiver {

	private final BoardService boardService;

	public BoardMessageReceiver(BoardService boardService) {
		this.boardService = boardService;
	}

	@RabbitListener(queues = RabbitMQConfig.QUEUE)
	public BoardDTO handleCreateBoardMessage(String message) {
		return boardService.createBoard();
	}
}