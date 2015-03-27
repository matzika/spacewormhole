import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;


/**
 * @class ParticleSource
 * @author Aikaterini (Katerina) Iliakopoulou
 * @email ai2315@columbia.edu
 * 
 * Simulates a source of particles 
 */
public class ParticleSource {
	
	private static Random randomGenerator = new Random();
	
	private final List<Particle> particles;
	
	private float velocity;
	private float speed = 1.5f; 
	private float velFactor = 400; // higher for slower movement - lower for faster movement
	
	private int lifetime = 300;
	private int numberOfParticles = 3;
	
	private Vector3f g = new Vector3f(0, -0.0003f, 0);
	private Vector3f position;
	private Vector3f glPosition;
	private Vector3f initialVelocity;
	private Vector3f color;
	
	boolean isBlackHole = false;
	
	public ParticleSource(Vector3f position, Vector3f initialVelocity){
		
		this.position = position;
		this.initialVelocity = initialVelocity;
		this.particles = new ArrayList<Particle>(numberOfParticles*lifetime);
	}
	
	public ParticleSource(List<Particle> particles, Vector3f position, Vector3f initialVelocity){
		//this.randomGenerator = new Random();
		this.position = position;
		this.initialVelocity = initialVelocity;
		this.particles = particles;
	}
	
	public List<Particle> getParticles(){
		return this.particles;
	}
	
	public void addParticles(List<Particle> particles){
		for(Particle particle : particles)
			addParticle(particle);
	}
	
	public void addParticle(Particle particle){
		this.particles.add(particle);
	}
	
	public void removeParticle(Particle particle){
		this.particles.remove(particle);
	}
	
	public void setVelocity(float velocity){
		this.velocity = velocity;
	}
	
	public float getVelocity(){
		return this.velocity;
	}
	
	public void setSpeed(float speed){
		this.speed = speed;
	}
	
	public float getSpeed(){
		return this.speed;
	}
	
	public void setNumberOfParticles(float numberOfParticles){
		this.numberOfParticles = (int) numberOfParticles;
	}
	
	public int getNumberOfParticles(){
		return this.numberOfParticles;
	}
	
	public void setLifetime(int lifetime){
		this.lifetime = lifetime;
	}
	
	public int getLifetime(){
		return this.lifetime;
	}
	
	public void setGravity(Vector3f gravity){
		this.g = gravity;
	}
	
	public Vector3f getGravity(){
		return this.g;
	}
	
	public void setInitialVelocity(Vector3f initialVelocity){
		this.initialVelocity = initialVelocity;
	}
	
	public Vector3f getInitialVelocity(){
		return this.initialVelocity;
	}
	
	public void setPosition(Vector3f position){
		this.position = position;
	}
	
	public Vector3f getPosition(){
		return this.position;
	}
	
	public void setGLPosition(Vector3f glPosition){
		this.glPosition = glPosition;
	}
	
	public Vector3f getGLPosition(){
		return this.glPosition;
	}
	
	
	public void setColor(Vector3f color){
		this.color = color;
	}
	
	public Vector3f getColor(){
		return this.color;
	}
	
	public void setStatus(boolean isBlackHole){
		this.isBlackHole = isBlackHole;
	}
	
	public boolean getStatus(){
		return this.isBlackHole;
	}
	
	/**
	 * Creates a particle on a random position
	 * around the source (the radius can vary)
	 * @return the created particle
	 */
	public Particle createParticle(){
		
		Vector3f ppos = new Vector3f(position);
				
		float x = (float) randomGenerator.nextDouble() - 0.6f;
		float y = (float) randomGenerator.nextDouble() - 0.6f;
		float z = (float) randomGenerator.nextDouble() - 0.6f;
		
		float vx = (x + initialVelocity.x) / velFactor;
		float vy = (y + initialVelocity.y) / velFactor;
		float vz = (z + initialVelocity.z) / velFactor;
		
		Vector3f pvel = new Vector3f(vx,vy,vz);
		pvel.scale(speed);
		
		Particle particle = new Particle(ppos,pvel,lifetime);
		
		return particle;
	}

	/**
	 * Tracks which particles are lost based on their current position
	 * Given that the blackhole is located on the global coordinate system
	 * on (0,0,z) then if the particle find itself close to the black hole, it 
	 * leaves its source and sinks into the hole. Newly added particles to the hole
	 * have their lifetime reset, so that they can keep living.
	 * @return the lost particles to the hole
	 */
	public List<Particle> trackLostParticles(){
		List<Particle> lostParticles = new ArrayList<Particle>();
		
		for(Particle particle : particles){
			double a = particle.getPosition().x + glPosition.x;
			double b = particle.getPosition().y + glPosition.y;
			
		    if(((Math.pow(a, 2) + Math.pow(b, 2)) <= 0.4)){
		    	
		    	particle.setLifetime(lifetime);
		    
		    	lostParticles.add(particle);
		    }
    	}
		
		for(Particle particle : lostParticles)
			particles.remove(particle);
		
		return lostParticles;
	}
	
	/**
	 * Update the source of particles based on particles' current movement
	 * and lifetime. Creates new particles to be added to the source. 
	 */
	public void updateSource(){
		//update velocity of the particles and remove the ones that are lost to the whole
		List<Integer> deadParticles = new ArrayList<Integer>();
		for(int i=0;i<particles.size();i++){
			 if(this.isBlackHole)
				 	particles.get(i).shrinkState(g);
	            else
	            	particles.get(i).expandState(g);
			 
			if(particles.get(i).isDead())
				deadParticles.add(i);
		}
		
		for(int i=0;i<deadParticles.size();i++)
			particles.remove(i);
		
        if (!Mouse.isButtonDown(0)) {
        	if(!this.isBlackHole)
	            for (int i = 0; i < numberOfParticles; i++) {
	                particles.add(createParticle());
	            }
        }
       
	}
	/**
	 * Draws the source of particle based on the particle's lifetime and position
	 * The older the particle is the more it fades on the screen. 
	 */
	public void drawParticleSource(){
		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
		GL11.glBegin(GL11.GL_POINTS);
		
		for(Particle particle : particles){
			float density = (float) particle.getLifetime() / lifetime;
			GL11.glColor4f(color.z * density, color.y * density, color.x * density, density);
			
			GL11.glVertex3f(particle.getPosition().x, particle.getPosition().y, particle.getPosition().z);
		}
		
		GL11.glEnd();
		GL11.glPopAttrib();
	}
	/*
	 * Prints the coordinates of the particles
	 */
	public void printParticles(){
		for(Particle particle : particles){
			particle.print();
		}
	}
}
