package sem4.ea.ss25.battleship.assignment2.board_service.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Ship {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private int length;
	private int startX;
	private int startY;
	private boolean isHorizontal;

	public Ship() {
	}

	public Ship(int length, int startX, int startY, boolean isHorizontal) {
		this.length = length;
		this.startX = startX;
		this.startY = startY;
		this.isHorizontal = isHorizontal;
	}

	// Getter und Setter
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

	public int getStartX() {
		return startX;
	}

	public void setStartX(int startX) {
		this.startX = startX;
	}

	public int getStartY() {
		return startY;
	}

	public void setStartY(int startY) {
		this.startY = startY;
	}

	public boolean isHorizontal() {
		return isHorizontal;
	}

	public void setHorizontal(boolean horizontal) {
		isHorizontal = horizontal;
	}
}