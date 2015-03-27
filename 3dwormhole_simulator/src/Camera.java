import org.lwjgl.opengl.GL11;

/**
 * @class Camera
 * @author Aikaterini (Katerina) Iliakopoulou
 * @email ai2315@columbia.edu
 * 
 * Simulates the camera 
 */
public class Camera {
        public static float moveSpeed = 0.01f;

        private float x;
        private float y;
        private float z;
        
        private float rx;
        private float ry;
        private float rz;
        
        public Camera(){
        	this.x = 0;
        	this.y = 0;
        	this.z = 0;
        	this.rx = 0;
        	this.ry = 0;
        	this.rz = 0;
        }

        /**
         * apply correct movement to the camera
         */
        public void apply() {
            if (ry / 360 > 1) {
                ry -= 360;
            } else if (ry / 360 < -1) {
                ry += 360;
            }

            GL11.glRotatef(rx, 1, 0, 0);
            GL11.glRotatef(ry, 0, 1, 0);
            GL11.glRotatef(rz, 0, 0, 1);
            GL11.glTranslatef(-x, -y, -z);
        }

        public float getX()
        {
        	return x;
        }

        public float getY()
        {
        	return y;
        }

        public float getZ()
        {
        	return z;
        }

        public void setX(float x)
        {
        	this.x = x;
        }

        public void setY(float y)
        {
        	this.y = y;
        }

        public void setZ(float z)
        {
        	this.z = z;
        }

        public float getRX()
        {
        	return rx;
        }

        public float getRY()
        {
        	return ry;
        }

        public float getRZ()
        {
        	return rz;
        }

        public void setRX(float rx)
        {
        	this.rx = rx;
        }

        public void setRY(float ry)
        {
        	this.ry = ry;
        }

        public void setRZ(float rz)
        {
        	this.rz = rz;
        }
        
        /**
         * Move on the y-axis based on directions you were given
         * @param dir1
         * @param dir2
         */
        public void move(float dir1, float dir2){
        	z += dir1* moveSpeed * Math.sin(Math.toRadians(ry + 90 * dir2));
        	x += dir1* moveSpeed * Math.cos(Math.toRadians(ry + 90 * dir2));
        }
        
        public void moveForward(){
        	z -= moveSpeed;
        }
        
        public void moveBackward(){
        	z += moveSpeed;
        }
        
        public void moveLeft(){
        	x -= moveSpeed;
        }
        
        public void moveRight(){
        	x += moveSpeed;
        }
        
        public void rotateY(){
        	ry += moveSpeed;
        }
    }