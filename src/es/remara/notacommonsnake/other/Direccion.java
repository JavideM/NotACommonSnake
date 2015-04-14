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
}