package es.remara.notacommonsnake.object;

import java.util.LinkedList;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import es.remara.notacommonsnake.manager.ResourcesManager;
import es.remara.notacommonsnake.other.Direccion;


public class Snake extends Entity{
	
	private VertexBufferObjectManager vbom;
	
	/*
	 * Snake body
	 */
	private Rectangle head;
	private LinkedList<Rectangle> body = new LinkedList<Rectangle>();
	
	/*
	 * Snake atributes
	 */
	private Direccion direc;
	private float speed;
	
	private float initial_speed;
	
	/*
	 * Snake states
	 */
	private boolean ghost_mode;
	private boolean drunk;
	
	/*
	 * Snake dimensions
	 */
	private float ancho;
	private float largo;
		

	public Snake(float pX, float pY, float pWidth, float pHeight, float speed,
			VertexBufferObjectManager vbom) {
		super();
		
		this.direc = Direccion.DERECHA;
		this.ancho = pWidth;
		this.largo = pHeight;
		this.speed = speed;
		this.initial_speed = speed;
		this.ghost_mode = false;
		this.drunk = false;
		
		this.head = new Rectangle(pX, pY, pWidth, pHeight, vbom);
		head.setColor(Color.RED);
		attachChild(head);
		
		Rectangle cola = new Rectangle(pX - pWidth, pY, pWidth, pHeight, vbom);
		cola.setColor(Color.BLUE);
		this.body.addFirst(cola);
		attachChild(cola);
	}
	
	/*
	 * Constructor con texturas
	 */
	public Snake(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTexturaCuerpo, ITextureRegion pTexturaCabeza, ITextureRegion pTexturaCola,
			VertexBufferObjectManager vbom) {
		super();
		
		this.direc = Direccion.DERECHA;
		this.ancho = pWidth;
		this.largo = pHeight;
		
		this.head = new Rectangle(pX, pY, pWidth, pHeight, vbom);
		head.setColor(Color.RED);
		attachChild(head);
		
		Rectangle cola = new Rectangle(pX - pWidth, pY, pWidth, pHeight, vbom);
		cola.setColor(Color.BLUE);
		body.addFirst(cola);
		attachChild(cola);
	}

	public float getSpeed()
	{
		return this.speed;
	}
	
	public Direccion getDirec() {
		return direc;
	}

	public void setDirec(Direccion direc) {
		if(direc != Direccion.opuesta(direc))
			this.direc = direc;
	}
	
	public Rectangle getHead() {
		return head;
	}
	
	public boolean is_ghost_mode(){
		return this.ghost_mode;
	}
	
	//Añade secciones de cuerpo a la serpiente
	public void crece() {
		Rectangle colanuevaRectangle = new Rectangle(body.getFirst().getX(), body.getFirst().getY(), 
				ancho, largo, vbom);
		colanuevaRectangle.setColor(Color.BLUE);
		attachChild(colanuevaRectangle);
		body.addFirst(colanuevaRectangle);
	}

	public void come(Food food)
	{
		set_default_states();
		crece();
		switch(food.getType()){
			case AUG_SPEED:
				this.speed = this.speed * 2;
				break;
			case CHG_GAME_MODE:
				break;
			case GHOST_MODE:
				this.ghost_mode = true;
				break;
			case INV_CONTROLS:
				this.drunk = true;
				break;
			case REDUC_SPEED:
				this.speed = this.speed/2;
				break;
			case SUPER_GROW:
				crece();
				break;
			case X2:
				break;
			default:
				break;
		}
	}
	
	private void set_default_states()
	{
		this.speed = this.initial_speed;
		this.ghost_mode = false;
		this.drunk = false;
	}
	
	public void muevete()
	{
		Rectangle cola = body.removeLast();
		cola.setPosition(head);
		body.addFirst(cola);
		Direccion direccion;
		if(drunk)  direccion = Direccion.opuesta(this.direc);
		else direccion = this.direc;
		
		switch(direccion){
			case ARRIBA:
				if(head.getY() < ResourcesManager.getInstance().camera.getHeight() - largo/2)
					head.setPosition(head.getX(), head.getY() + largo);
				else
					head.setPosition(head.getX(),  largo/2);
				break;
			case ABAJO:
				if(head.getY() > largo/2)
					head.setPosition(head.getX(), head.getY() -  largo);
				else
					head.setPosition(head.getX(), ResourcesManager.getInstance().camera.getHeight() -  largo/2);
				break;
			case DERECHA:
				if(head.getX() < ResourcesManager.getInstance().camera.getWidth() - ancho/2)
					head.setPosition(head.getX() + ancho, head.getY());
				else
					head.setPosition(ancho/2, head.getY());
				break;
			case IZQUIERDA:
				if(head.getX() > ancho/2)
					head.setPosition(head.getX() - ancho, head.getY() );
				else
					head.setPosition(ResourcesManager.getInstance().camera.getWidth() - ancho/2, head.getY() );
				break;
		}
	}
	
	public boolean suicidado()
	{
		for (Rectangle parte : body) {
			if(parte.getX() == head.getX() && parte.getY() == head.getY())
				return true;
		}
		return false;
	}
	
	
}
