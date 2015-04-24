package es.remara.notacommonsnake.other;


public enum Direccion{
	ARRIBA, ABAJO, DERECHA, IZQUIERDA;

	public static Direccion opuesta(Direccion direc) {
		switch(direc)
		{
		case ARRIBA:
			return ABAJO;
		case ABAJO:
			return ARRIBA;
		case DERECHA:
			return IZQUIERDA;
		case IZQUIERDA:
			return DERECHA;
		default:
			return null;
		}
	}
	
	public static Direccion relative_left(Direccion direc) {
		switch(direc)
		{
		case ARRIBA:
			return IZQUIERDA;
		case ABAJO:
			return DERECHA;
		case DERECHA:
			return ARRIBA;
		case IZQUIERDA:
			return ABAJO;
		default:
			return null;
		}
	}
	
	public static Direccion relative_right(Direccion direc) {
		switch(direc)
		{
		case ARRIBA:
			return DERECHA;
		case ABAJO:
			return IZQUIERDA;
		case DERECHA:
			return ABAJO;
		case IZQUIERDA:
			return ARRIBA;
		default:
			return null;
		}
	}
}