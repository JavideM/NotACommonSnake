package es.remara.notacommonsnake.object;

import java.util.LinkedList;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
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
	private Entity head;
	private LinkedList<Entity> body = new LinkedList<Entity>();
	
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

	/*
	 * Snake Textures
	 */
	private ITextureRegion text_head;
	private ITextureRegion text_body;
	private ITextureRegion text_tail;
		

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
			ITextureRegion pTexturaCuerpo, ITextureRegion pTexturaCabeza, ITextureRegion pTexturaCola, float speed,
			VertexBufferObjectManager vbom) {
		super();
		
		this.direc = Direccion.DERECHA;
		this.ancho = pWidth;
		this.largo = pHeight;
		this.speed = speed;
		this.initial_speed = speed;
		this.ghost_mode = false;
		this.drunk = false;
		
		this.text_head = pTexturaCabeza;
		this.text_body = pTexturaCuerpo;
		this.text_tail = pTexturaCola;
		
		
		this.head = new Sprite(pX, pY, this.text_head, vbom);
		attachChild(head);
		
		Entity cola = new Sprite(pX - pWidth, pY, pWidth, pHeight, this.text_tail, vbom);
		body.addFirst(cola);
		attachChild(cola);
		
		Entity body_part = new Sprite(pX - pWidth, pY, pWidth, pHeight, this.text_body, vbom);
		body.addFirst(body_part);
		attachChild(body_part);
	}

	public float getSpeed()
	{
		return this.speed;
	}
	
	public Direccion getDirec() {
		return direc;
	}

	public void setDirec(Direccion direc) {
		Direccion new_direc;
		if(direc != this.direc)
		{
			new_direc = drunk? Direccion.opuesta(direc): direc;
			if(Direccion.relative_left(direc) == this.direc){
				head.setRotation(head.getRotation() + 90.0f);
			}
			if(Direccion.relative_right(direc) == this.direc){
				head.setRotation(head.getRotation() - 90.0f);
			}
			this.direc = new_direc;
		}
	
	}
	
	public Entity getHead() {
		return head;
	}
	
	public boolean is_ghost_mode(){
		return this.ghost_mode;
	}
	
	//Añade secciones de cuerpo a la serpiente
	public void crece() {
		Sprite colanueva = new Sprite(body.getFirst().getX(), body.getFirst().getY(), 
				this.text_body, vbom);
		attachChild(colanueva);
		body.addFirst(colanueva);
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
		Entity new_tail = body.removeLast();
		Entity new_body_part = body.removeLast();
		new_tail.setPosition(new_body_part);
		new_tail.setRotation(new_body_part.getRotation());
		new_body_part.setPosition(head);
		new_body_part.setRotation(head.getRotation());

		body.addLast(new_tail);
		body.addFirst(new_body_part);
		
		switch(this.direc){
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
		for (Entity parte : body) {
			if(parte.getX() == head.getX() && parte.getY() == head.getY())
				return true;
		}
		return false;
	}
	
	
}
