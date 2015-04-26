package es.remara.notacommonsnake.other;


public enum Direction{
	TOP, DOWN, RIGHT, LEFT;

	public static Direction opposite(Direction direc) {
		switch(direc)
		{
		case TOP:
			return DOWN;
		case DOWN:
			return TOP;
		case RIGHT:
			return LEFT;
		case LEFT:
			return RIGHT;
		default:
			return null;
		}
	}
	
	public static Direction relative_left(Direction direc) {
		switch(direc)
		{
		case TOP:
			return LEFT;
		case DOWN:
			return RIGHT;
		case RIGHT:
			return TOP;
		case LEFT:
			return DOWN;
		default:
			return null;
		}
	}
	
	public static Direction relative_right(Direction direc) {
		switch(direc)
		{
		case TOP:
			return RIGHT;
		case DOWN:
			return LEFT;
		case RIGHT:
			return DOWN;
		case LEFT:
			return TOP;
		default:
			return null;
		}
	}
}