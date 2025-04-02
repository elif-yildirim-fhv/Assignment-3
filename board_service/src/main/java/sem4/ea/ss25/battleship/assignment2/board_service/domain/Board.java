package sem4.ea.ss25.battleship.assignment2.board_service.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Board {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<Cell> cells = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<Ship> ships = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Cell> getCells() {
		return cells;
	}

	public void setCells(List<Cell> cells) {
		this.cells = cells;
	}

	public List<Ship> getShips() {
		return ships;
	}

	public void setShips(List<Ship> ships) {
		this.ships = ships;
	}

	public void initializeBoard(int size) {
		cells.clear();
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				Cell cell = new Cell();
				cell.setX(x);
				cell.setY(y);
				cell.setHasShip(false);
				cell.setHit(false);
				cells.add(cell);
			}
		}
	}

	public Ship placeShip(int length, int x, int y, boolean isHorizontal) {
		// Validate ship placement
		if (!isValidPlacement(length, x, y, isHorizontal)) {
			throw new IllegalArgumentException("Invalid ship placement");
		}

		// Create ship
		Ship ship = new Ship();
		ship.setLength(length);

		// Mark cells as having a ship
		List<Cell> shipCells = new ArrayList<>();
		if (isHorizontal) {
			for (int i = 0; i < length; i++) {
				Cell cell = getCellAt(x + i, y);
				cell.setHasShip(true);
				shipCells.add(cell);
			}
		} else {
			for (int i = 0; i < length; i++) {
				Cell cell = getCellAt(x, y + i);
				cell.setHasShip(true);
				shipCells.add(cell);
			}
		}

		ship.setCells(shipCells);
		ships.add(ship);

		return ship;
	}

	private boolean isValidPlacement(int length, int x, int y, boolean isHorizontal) {
		int boardSize = (int) Math.sqrt(cells.size());

		// Check if ship is within board boundaries
		if (isHorizontal) {
			if (x < 0 || x + length > boardSize || y < 0 || y >= boardSize) {
				return false;
			}
		} else {
			if (x < 0 || x >= boardSize || y < 0 || y + length > boardSize) {
				return false;
			}
		}

		// Check if cells are already occupied
		if (isHorizontal) {
			for (int i = 0; i < length; i++) {
				if (getCellAt(x + i, y).isHasShip()) {
					return false;
				}
			}
		} else {
			for (int i = 0; i < length; i++) {
				if (getCellAt(x, y + i).isHasShip()) {
					return false;
				}
			}
		}

		return true;
	}

	private Cell getCellAt(int x, int y) {
		return cells.stream()
				.filter(cell -> cell.getX() == x && cell.getY() == y)
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Cell not found at position (" + x + "," + y + ")"));
	}

	public boolean isAllShipsHit() {
		// If there are no ships, return false
		if (ships.isEmpty()) {
			return false;
		}

		// Check if all ships are destroyed
		for (Ship ship : ships) {
			for (Cell cell : ship.getCells()) {
				if (!cell.isHit()) {
					return false;
				}
			}
		}

		return true;
	}
}