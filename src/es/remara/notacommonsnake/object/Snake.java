package es.remara.notacommonsnake.object;

import java.util.LinkedList;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import es.remara.notacommonsnake.manager.ResourcesManager;
import es.remara.notacommonsnake.other.Direction;


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
	private Direction direc;
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
	private float width;
	private float height;

	/*
	 * Snake Textures
	 */
	private ITextureRegion text_head;
	private ITextureRegion text_body;
	private ITextureRegion text_tail;
	private ITextureRegion text_corner;

	public Snake(float pX, float pY, float pWidth, float pHeight, float speed,
			VertexBufferObjectManager vbom) {
		super();
		
		this.direc = Direction.RIGHT;
		this.width = pWidth;
		this.height = pHeight;
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
			ITextureRegion pTextureBody, ITextureRegion pTextureHead, ITextureRegion pTextureTail, ITextureRegion pTextureCorner, float speed,
			VertexBufferObjectManager vbom) {
		super();
		
		this.direc = Direction.RIGHT;
		this.width = pWidth;
		this.height = pHeight;
		this.speed = speed;
		this.initial_speed = speed;
		this.ghost_mode = false;
		this.drunk = false;
		
		this.text_head = pTextureHead;
		this.text_body = pTextureBody;
		this.text_tail = pTextureTail;
		this.text_corner = pTextureCorner;
		
		
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
	
	public Direction getDirec() {
		return direc;
	}

	public void setDirec(Direction direc) {
		Direction new_direc;
		if(direc != this.direc)
		{
			new_direc = drunk? Direction.opposite(direc): direc;
			if(Direction.relative_left(new_direc) == this.direc){
				head.setRotation(head.getRotation() + 90.0f);
			}
			if(Direction.relative_right(new_direc) == this.direc){
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
	public void grow() {
		//New Sprite for the new body part
		Sprite newtail = new Sprite(body.getFirst().getX(), body.getFirst().getY(), 
				this.text_body, vbom);
		//Set the rotation equal to the first part of the tail rotation
		newtail.setRotation(this.body.getFirst().getRotation());
		//attach the new body part in the Snake entity and add it to the body of the snake
		attachChild(newtail);
		body.addFirst(newtail);
	}

	public void eat(Food food)
	{
		set_default_states();
		grow();
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
				grow();
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
	
	public void move()
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
			case TOP:
				if(head.getY() < ResourcesManager.getInstance().camera.getHeight() - height/2)
					head.setPosition(head.getX(), head.getY() + height);
				else
					head.setPosition(head.getX(),  height/2);
				break;
			case DOWN:
				if(head.getY() > height/2)
					head.setPosition(head.getX(), head.getY() -  height);
				else
					head.setPosition(head.getX(), ResourcesManager.getInstance().camera.getHeight() -  height/2);
				break;
			case RIGHT:
				if(head.getX() < ResourcesManager.getInstance().camera.getWidth() - width/2)
					head.setPosition(head.getX() + width, head.getY());
				else
					head.setPosition(width/2, head.getY());
				break;
			case LEFT:
				if(head.getX() > width/2)
					head.setPosition(head.getX() - width, head.getY() );
				else
					head.setPosition(ResourcesManager.getInstance().camera.getWidth() - width/2, head.getY() );
				break; 
		}
	}
	
	public boolean suicide()
	{
		for (Entity part : body) {
			if(part.getX() == head.getX() && part.getY() == head.getY())
				return true;
		}
		return false;
	}

	public boolean hit_a_wall(Walls walls) {
		return walls.is_there_a_wall(head);
	}
	
	
}
