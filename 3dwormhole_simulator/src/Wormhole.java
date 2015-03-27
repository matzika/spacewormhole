import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;


/**
 * @class Wormhole
 * @author Aikaterini (Katerina) Iliakopoulou
 * @email ai2315@columbia.edu
 * 
 * Simulates a wormhole and contains the main class of the program
 */

public class Wormhole {

	String windowTitle = "Simulation of a wormhole!";
    
    public boolean closeRequested = false;
    
    float angle = 1.0f;
    
    List<ParticleSource> sources = new ArrayList<ParticleSource>();
    
    Camera camera;
    
    int snapshot_count = 0;

    public Wormhole(){
    	
    }

    /**
     * Method that runs the simulation of the wormhole.
     * This is where the screen is created, openGL initialized 
     * and sources are created. While the screen is still open 
     * by the user, the program keeps running. 
     */
    public void run() {

        createWindow();
        initGL();
        
        //create the camera
        camera = new Camera();
        
        //start with two sources of particles on standard positions
        sources.add(createParticleSource());
        sources.get(0).setGLPosition(new Vector3f(-0.9f, -0.5f, -0.5f));
        sources.add(createParticleSource());
        sources.get(1).setGLPosition(new Vector3f(1.0f, 0.5f, -0.5f));
        
        //create a type of black hole source that sucks the particles
        ParticleSource blackHole = createParticleSource();
        blackHole.setStatus(true);
        sources.add(blackHole);
        
        while (!closeRequested) {
            pollInput();
            updateLogic();
            renderGL();
            Display.sync(60);
            Display.update();
        }
        
        cleanup();
    }
    
    
    /**
     * Sets openGL matrixes & states
     */
    private void initGL() {

        /* OpenGL */
        int width = Display.getDisplayMode().getWidth();
        int height = Display.getDisplayMode().getHeight();

        GL11.glViewport(0, 0, width, height); // Reset The Current Viewport
        GL11.glMatrixMode(GL11.GL_PROJECTION); // Select The Projection Matrix
        GL11.glLoadIdentity(); // Reset The Projection Matrix
      
        GLU.gluPerspective(135, ((float) width / (float) height), 0.1f, 100); //set perpective projection 
        GL11.glMatrixMode(GL11.GL_MODELVIEW); // Select The Modelview Matrix
        GL11.glLoadIdentity(); // Reset The Modelview Matrix

        GL11.glShadeModel(GL11.GL_SMOOTH); // Enables Smooth Shading
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Black Background
        GL11.glClearDepth(1.0f); // Depth Buffer Setup
        //GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
        GL11.glDepthFunc(GL11.GL_LEQUAL); // The Type Of Depth Test To Do
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST); // Really Nice Perspective Calculations
        
        GL11.glEnable(GL11.GL_BLEND);//enables blening so that we see the particles fading smoothly
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
      
    }
    
    /**
     * Create a source of particles. Initiliazes it and colors it.
     * Sets number of particles generated from the source, particles' lifetime
     * and the gravitational force that moves them in or out of the source. 
     * @return
     */
    private ParticleSource createParticleSource(){
    
    	//create particle source initial position vector & velocity vector
        Vector3f inpos = new Vector3f(0f,0f,0f);
        Vector3f invel = new Vector3f(0f,0f,0.1f);
        
        ParticleSource s = new ParticleSource(inpos,invel);
        
        //choose random color for the particles of the source
        Random randomGenerator = new Random();
        
        float colorx = (float) (randomGenerator.nextDouble() - 1);
        float colory = (float) (randomGenerator.nextDouble() - 1);
        float colorz = (float) (randomGenerator.nextDouble() - 1);
        
        s.setNumberOfParticles(150);
        s.setLifetime(300);
        s.setGravity(new Vector3f(0,-0.000001f,0));
        s.setColor(new Vector3f((1-colorx),(1-colory),(1-colorz)));
        
        return s;
    }
    
    /*
     * Updates the program with new values based on system's current state
     * and user's interaction
     */
    private void updateLogic() {
    	
    	//update first particle source
    	sources.get(0).updateSource();
    	//Track particles that were lost due to gravity from source 1
    	List<Particle> particlesToBeMoved = sources.get(0).trackLostParticles();
    	//Add those lost particles to the blackhole
    	sources.get(2).addParticles(particlesToBeMoved);
    	
    	//update second particle source
    	sources.get(1).updateSource();
    	//Track particles that were lost due to gravity from source 1
    	particlesToBeMoved = sources.get(1).trackLostParticles();
    	//Add those lost particles to the blackhole
    	sources.get(2).addParticles(particlesToBeMoved);
    	
    	//update black hole itself
    	sources.get(2).updateSource();
    	
    	angle +=2f;
       
    }


    /**
     * Draws all the sources on screen based on their current state of being.
     */
    private void renderGL() {

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // Clear The Screen And The Depth Buffer
        GL11.glLoadIdentity(); // Reset The View

        camera.apply();
        
        //place the first particle source
        GL11.glPushMatrix();
        {
        	ParticleSource s1 = sources.get(0);
        	GL11.glTranslatef(-0.9f, -0.5f, -0.5f);
        	s1.drawParticleSource();
        }
        GL11.glPopMatrix();
        
        //place the second particle source
        GL11.glPushMatrix();
        {
        	ParticleSource s2 = sources.get(1);
        	GL11.glTranslatef(1.0f, 0.5f, -0.5f);
        	s2.drawParticleSource();
        }
        GL11.glPopMatrix();
        
        //place the black hole
        GL11.glPushMatrix();
        {
        	ParticleSource bh = sources.get(2);
        	GL11.glTranslatef(0.0f, 0.0f, -0.5f);
        	bh.drawParticleSource();
        }
        GL11.glPopMatrix();

    }

    
    /**
     * Polls Input from keyboard to create an interactive program
     */
    public void pollInput() {

    	//control first source --> Z:makes it bigger//X:makes it smaller 
        if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
        	sources.get(0).setSpeed(sources.get(0).getSpeed() * 1.01f);
        } else if (Keyboard.isKeyDown(Keyboard.KEY_X)) {
        	sources.get(0).setSpeed(sources.get(0).getSpeed() / 1.01f);
        }
        //control second source--> N:makes it bigger//M:makes it smaller 
        if (Keyboard.isKeyDown(Keyboard.KEY_N)) {
        	sources.get(1).setSpeed(sources.get(1).getSpeed() * 1.01f);
        } else if (Keyboard.isKeyDown(Keyboard.KEY_M)) {
        	sources.get(1).setSpeed(sources.get(1).getSpeed() / 1.01f);
        }
        //control both sources at once --> U:makes it bigger//D:makes it smaller 
        if (Keyboard.isKeyDown(Keyboard.KEY_U)) {
        	for(ParticleSource source : sources)
        		source.setSpeed(source.getSpeed() * 1.01f);
        } else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
        	for(ParticleSource source : sources)
        		source.setSpeed(source.getSpeed() / 1.01f);
        }
        
        //basic movement in the universe on the y axis (Forward, Backward, Left, Right)
        if(Keyboard.isKeyDown(Keyboard.KEY_UP))
        	camera.move(-1,1);
        if(Keyboard.isKeyDown(Keyboard.KEY_DOWN))
        	camera.move(1,1);
        if(Keyboard.isKeyDown(Keyboard.KEY_LEFT))
        	camera.move(-1,0);
        if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
        	camera.move(1,0);
        
        // scroll through key events
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE)
                    closeRequested = true;
                else if (Keyboard.getEventKey() == Keyboard.KEY_P)
                    snapshot();
            }
        }

        if (Display.isCloseRequested()) {
            closeRequested = true;
        }
    }

    /**
     * Takes a snapshot from the screen
     */
    public void snapshot() {
        System.out.println("Taking a snapshot ... snapshot.png");

        GL11.glReadBuffer(GL11.GL_FRONT);

        int width = Display.getDisplayMode().getWidth();
        int height= Display.getDisplayMode().getHeight();
        int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
        GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer );

        File file = new File("snapshot"+(snapshot_count)+".png"); // The file to save to.
        snapshot_count++;
        String format = "PNG"; // Example: "PNG" or "JPG"
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
   
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                int i = (x + (width * y)) * bpp;
                int r = buffer.get(i) & 0xFF;
                int g = buffer.get(i + 1) & 0xFF;
                int b = buffer.get(i + 2) & 0xFF;
                image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
            }
        }
           
        try {
            ImageIO.write(image, format, file);
        } catch (IOException e) { e.printStackTrace(); }
    }
    
    /**
     * Creates the screen for the simulation to be displayed 
     */
    private void createWindow() {
        try {
            Display.setDisplayMode(new DisplayMode(800, 600));
            //Display.setVSyncEnabled(true);
            Display.setTitle(windowTitle);
            Display.create();
        } catch (LWJGLException e) {
            Sys.alert("Error", "Initialization failed!\n\n" + e.getMessage());
            System.exit(0);
        }
    }
    
    /**
     * Destroy and clean up resources
     */
    private void cleanup() {
        Display.destroy();
    }
    
    public static void main(String[] args) {
         new Wormhole().run();
    }
    
}
