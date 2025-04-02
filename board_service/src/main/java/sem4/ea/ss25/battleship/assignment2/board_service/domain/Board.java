package sem4.ea.ss25.battleship.assignment2.board_service.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long gameId;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "board_id")
	private List<Cell> cells = new ArrayList<>();

	// Getter und Setter
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	public List<Cell> getCells() {
		return cells;
	}

	public void setCells(List<Cell> cells) {
		this.cells = cells;
	}


	public void initializeBoard(int size) {
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				Cell cell = new Cell(x, y, false, false);
				cells.add(cell);
			}
		}
	}

	public Ship placeShip(int length, int x, int y, boolean isHorizontal) {
		for (int i = 0; i < length; i++) {
			int newX = isHorizontal ? x + i : x;
			int newY = isHorizontal ? y : y + i;
			Cell cell = getCell(newX, newY);
			if (cell != null) {
				cell.setHasShip(true);
			}
		}

		return new Ship(length, x, y, isHorizontal);
	}
	private Cell getCell(int x, int y) {
		return cells.stream()
				.filter(c -> c.getX() == x && c.getY() == y)
				.findFirst()
				.orElse(null);
	}

	public boolean canPlaceShip(int length, int x, int y, boolean isHorizontal) {
		int boardSize = 10;

		if (isHorizontal) {
			if (x + length > boardSize) return false;
		} else {
			if (y + length > boardSize) return false;
		}

		for (int i = 0; i < length; i++) {
			int checkX = isHorizontal ? x + i : x;
			int checkY = isHorizontal ? y : y + i;

			Cell cell = getCell(checkX, checkY);
			if (cell == null || cell.isHasShip()) {
				return false;
			}
		}

		return true;
	}


}