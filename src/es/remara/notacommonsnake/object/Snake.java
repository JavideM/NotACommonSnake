package es.remara.notacommonsnake.object;

import java.util.LinkedList;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import es.remara.notacommonsnake.manager.ResourcesManager;
import es.remara.notacommonsnake.other.Direction;

public class Snake extends Entity {

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
	private Direction actual_direc;
	private float speed;

	private float initial_speed;

	/*
	 * Snake states
	 */
	private boolean ghost_mode;
	private boolean drunk;
	private boolean moving_through_worlds;
	
	private boolean has_rotate_left;
	private boolean has_rotate_right;

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

	/*
	 * Constructor con texturas
	 */
	public Snake(float pX, float pY, float pWidth, float pHeight,
			float speed, VertexBufferObjectManager vbom) {
		super();

		this.actual_direc = Direction.RIGHT;
		this.direc = this.actual_direc;
		this.width = pWidth;
		this.height = pHeight;
		this.speed = speed;
		this.initial_speed = speed;
		this.ghost_mode = false;
		this.drunk = false;
		this.moving_through_worlds = false;

		this.text_head = ResourcesManager.getInstance().snake_head_region;
		this.text_body = ResourcesManager.getInstance().snake_body_region;
		this.text_tail = ResourcesManager.getInstance().snake_tail_region;
		this.text_corner = ResourcesManager.getInstance().snake_corner_region;

		this.head = new Sprite(pX, pY, this.text_head, vbom);
		attachChild(head);

		Entity cola = new Sprite(pX - pWidth, pY, 
				this.text_tail, vbom);
		body.addFirst(cola);
		attachChild(cola);

		Entity body_part = new Snake_Body_Part(pX - pWidth, pY, 
				this.text_body, vbom);
		body.addFirst(body_part);
		attachChild(body_part);
	}

	public float getSpeed() {
		return this.speed;
	}

	public Direction getDirec() {
		return actual_direc;
	}

	public void setDirec(Direction direc) {
		if (direc != this.direc) {
			this.direc = drunk ? Direction.opposite(direc) : direc;
		}

	}

	public Entity getHead() {
		return head;
	}

	public boolean is_ghost_mode() {
		return this.ghost_mode;
	}
	
	public boolean is_moving_through_worlds() {
		return this.moving_through_worlds;
	}

	// Añade secciones de cuerpo a la serpiente
	public void grow() {
		// New Sprite for the new body part
		Entity newtail = ((Snake_Body_Part)body.getFirst()).clone();
		// Set the rotation equal to the first part of the tail rotation
		newtail.setRotation(((Snake_Body_Part) this.body.getFirst()).getRotation());
		// attach the new body part in the Snake entity and add it to the body
		// of the snake
		attachChild(newtail);
		body.addFirst(newtail);
	}

	public void eat(Food food) {
		set_default_states();
		
		switch (food.getType()) {
		case AUG_SPEED:
			this.speed = this.speed / 1.5f;
			break;
		case CHG_GAME_MODE:
			this.moving_through_worlds = true;
			break;
		case GHOST_MODE:
			this.ghost_mode = true;
			break;
		case INV_CONTROLS:
			this.drunk = true;
			break;
		case REDUC_SPEED:
			this.speed = this.speed * 1.8f;
			break;
		case SUPER_GROW:
			grow();
			break;
		case X2:
			break;
		default:
			break;
		}
		if(!moving_through_worlds)
			grow();
	}

	private void set_default_states() {
		this.speed = this.initial_speed;
		this.ghost_mode = false;
		this.drunk = false;
		this.moving_through_worlds = false;
	}

	public void move() {
		//Turns the snake head
		if (Direction.relative_left(this.direc) == this.actual_direc) {
			head.setRotation(head.getRotation() + 90.0f);
			has_rotate_left = true;
		}else{
			has_rotate_left = false;
		}
		if (Direction.relative_right(this.direc) == this.actual_direc) {
			head.setRotation(head.getRotation() - 90.0f);
			has_rotate_right = true;
		}
		else{
			has_rotate_right = false;
		}
		this.actual_direc = this.direc;
		
		//Moves the body
		Entity new_tail = body.removeLast();
		detachChild(new_tail);
		new_tail = new Sprite(0, 0, text_tail, vbom);
		attachChild(new_tail);
		Snake_Body_Part new_body_part = (Snake_Body_Part) body.removeLast();
		new_tail.setPosition(new_body_part);
		new_tail.setRotation(new_body_part.getTrue_rotation());
		if(has_rotate_left || has_rotate_right){
			detachChild(new_body_part);
			new_body_part = new Snake_Body_Part(head.getX(), head.getY(), text_corner, vbom);
			new_body_part.setRotation(has_rotate_left?  180+head.getRotation():head.getRotation() +90);
			new_body_part.setTrue_rotation(head.getRotation());
			attachChild(new_body_part);
		}else{
			detachChild(new_body_part);
			new_body_part = new Snake_Body_Part(head.getX(), head.getY(), text_body, vbom);	
			new_body_part.setPosition(head);
			new_body_part.setTrue_rotation(head.getRotation());
			new_body_part.setRotation(head.getRotation());
			attachChild(new_body_part);
		}

		body.addLast(new_tail);
		body.addFirst(new_body_part);
		
		//Moves the head
		switch (this.actual_direc) {
		case TOP:
			if (head.getY() < ResourcesManager.getInstance().camera.getHeight()
					- height / 2)
				head.setPosition(head.getX(), head.getY() + height);
			else
				head.setPosition(head.getX(), height / 2);
			break;
		case DOWN:
			if (head.getY() > height / 2)
				head.setPosition(head.getX(), head.getY() - height);
			else
				head.setPosition(head.getX(),
						ResourcesManager.getInstance().camera.getHeight()
								- height / 2);
			break;
		case RIGHT:
			if (head.getX() < ResourcesManager.getInstance().camera.getWidth()
					- width / 2)
				head.setPosition(head.getX() + width, head.getY());
			else
				head.setPosition(width / 2, head.getY());
			break;
		case LEFT:
			if (head.getX() > width / 2)
				head.setPosition(head.getX() - width, head.getY());
			else
				head.setPosition(
						ResourcesManager.getInstance().camera.getWidth()
								- width / 2, head.getY());
			break;
		}
	}

	public boolean suicide() {
		for (Entity part : body) {
			if (part.getX() == head.getX() && part.getY() == head.getY())
				return true;
		}
		return false;
	}

	public boolean hit_a_wall(Walls walls) {
		return walls.is_there_a_wall(head);
	}

	public boolean go_through_portal() {
		has_rotate_left = false;
		has_rotate_right = false;
		if (head != null) {
			// Moves the body to the heads position
			Entity new_tail = body.removeLast();
			detachChild(new_tail);
			new_tail = new Sprite(0, 0, text_tail, vbom);
			attachChild(new_tail);
			Snake_Body_Part new_body_part = (Snake_Body_Part) body.removeLast();
			new_tail.setPosition(new_body_part);
			new_tail.setRotation(new_body_part.getTrue_rotation());
			if (has_rotate_left || has_rotate_right) {
				detachChild(new_body_part);
				new_body_part = new Snake_Body_Part(head.getX(), head.getY(),
						text_corner, vbom);
				new_body_part.setRotation(has_rotate_left ? 180 + head
						.getRotation() : head.getRotation() + 90);
				new_body_part.setTrue_rotation(head.getRotation());
				attachChild(new_body_part);
			} else {
				detachChild(new_body_part);
				new_body_part = new Snake_Body_Part(head.getX(), head.getY(),
						text_body, vbom);
				new_body_part.setPosition(head);
				new_body_part.setTrue_rotation(head.getRotation());
				new_body_part.setRotation(head.getRotation());
				attachChild(new_body_part);
			}

			body.addLast(new_tail);
			body.addFirst(new_body_part);
			
			//Removes the head because now it's in "the other side of the portal"
			detachChild(head);
			head = null;
			return false;
		} else {
			if (body.size() > 1) {
				Entity new_tail = body.removeLast();
				Snake_Body_Part new_body_part = (Snake_Body_Part) body
						.removeLast();
				new_tail.setPosition(new_body_part);
				new_tail.setRotation(new_body_part.getTrue_rotation());
				detachChild(new_body_part);
				body.addLast(new_tail);
				return false;

			}else{
				return true;
			}
		}

	}

}
