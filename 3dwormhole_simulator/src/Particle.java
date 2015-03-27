import org.lwjgl.util.vector.Vector3f;

/**
 * @class Particle
 * @author Aikaterini (Katerina) Iliakopoulou
 * @email ai2315@columbia.edu
 * 
 * Simulates a particle
 */
public class Particle {
	private int lifetime;
	
	private Vector3f position;
	private Vector3f velocity;
	

	public Particle(float x, float y, float z, Vector3f velocity, int lifetime){
		this.position = new Vector3f(x,y,z);
		this.velocity = velocity;
		this.lifetime = lifetime;
	}
	
	public Particle(Vector3f position,Vector3f velocity, int lifetime){
		this.position = position;
		this.velocity = velocity;
		this.lifetime = lifetime;
	}
	
	public void setLifetime(int lifetime){
		this.lifetime = lifetime;
	}
	
	public int getLifetime(){
		return this.lifetime;
	}
	
	public void setPosition(Vector3f position){
		this.position = position;
	}
	
	public Vector3f getPosition(){
		return this.position;
	}
	
	public void setVelocity(Vector3f velocity){
		this.velocity = velocity;
	}
	
	public Vector3f getVelocity(){
		return this.velocity;
	}
	/**
	 * Translates the particle's position after it's been added to the
	 * source (in case it transfered from one source to the other)
	 * @param pos
	 */
	public void translatePosition(Vector3f pos){
		//this.velocity.x = position.x + pos.x;
		//this.velocity.y = position.y + pos.y;
		this.velocity.z = pos.z;
	}
	/**
	 * Checks if particle's expired; 
	 * @return
	 */
	public boolean isDead(){
		
		if(lifetime <= 0)
			return true;
		
		return false;
	}
	
	/**
	 * Updates particle's position by moving it away from the source
	 * @param vRate
	 */
	public void expandState(Vector3f vRate){
		position.translate(velocity.x, velocity.y, velocity.z);
		velocity.translate(vRate.x, vRate.y, vRate.z);
		lifetime -=1;
	}
	/**
	 * Updates particle's position by moving it to the source
	 * @param vRate
	 */
	public void shrinkState(Vector3f vRate){
		position.translate(-velocity.x, -velocity.y, -velocity.z);
		velocity.translate(vRate.x, vRate.y, vRate.z);
		lifetime -=1;
	}
	
	/**
	 * Prints particle's coordinates
	 */
	public void print(){
		System.out.println("Particle's position: "+this.position.x+" x, "+this.position.y+" y, "+this.position.z+" z");
		System.out.println("Particle's lifetime: "+lifetime);
	}
}
