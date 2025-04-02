package sem4.ea.ss25.battleship.assignment2.board_service.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ship {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private int length;

	@ManyToMany(fetch = FetchType.EAGER)
	private List<Cell> cells = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public List<Cell> getCells() {
		return cells;
	}

	public void setCells(List<Cell> cells) {
		this.cells = cells;
	}

	public boolean isDestroyed() {
		for (Cell cell : cells) {
			if (!cell.isHit()) {
				return false;
			}
		}
		return true;
	}
}