# Space Wormhole
A  3D space wormhole implemented in OpenGL 


The program simulates three sources. Two are sources that generate particles (also quoted as ‘generator’ sources) and one is a source that pulls the particles that come close to it due to strong gravitational force, like it happens in a black hole (also quoted as ‘black hole’ source). The outcome is the creation of a channel between the two particle generator sources, where their particles are being pulled into the black hole.
The submitted code contains the following .java files:

1. Camera.java

2. Particle.java

3. ParticleSource.java

4. Wormhole.java

All of the sources are represented by the ParticleSource class, which has a local position, a number of particles that are generated in each update, a gravitational force and a direction in space, which the source sends the generated particles to. Each particle in the program is represented by the Particle class and has its own local position and velocity.
The two sources that generate particles create a standard number of particles in each update and refresh existed particles position based on gravitational force and direction. The source that represents the black hole doesn’t generate new particles, but “steals” those particles that come close to it, from the other two sources. So, whenever a particle finds itself in a position near the black hole source, it is removed by the list of particles of the ‘generator’ source it belongs to and it’s added to the list of particles of the ‘black hole’ source, while its lifetime is reset to its initial value.
Particles are being created at random positions in the proximity of the source they belong to. While they are still alive, their position is constantly being updated by translating their position using their velocity. The velocity is also being translated during the update step using the gravitational force of the source.
When a particle is detached from its original source and added to the black hole, its coordinates are translated to the global system and its direction is reversed, so that it is actually being pulled by the black hole.
￼￼￼￼
The Camera class represents the view in the program. It contains the methods that define what the user can see in the screen and how he/she can interact with the environment.
Lastly, the Wormhole class is the main class of the program. It creates the screen and initializes openGL matrices and states. Then, it creates the three sources, the particles ‘generator’ sources and the ‘black hole’ source, in standard positions. The class repetitively updates the three sources based on current information and input, and draws them on the screen. Finally, the class contains an input method to receive input from keyboard or mouse.

#How to run

We have created a wormhole_simulation.jar that contains all the native libraries that are necessary for the program to run. No additional libraries except for lwjgl were used. To run you should link the build path with the native library of your OS. All native libraries for each OS are included in the lwjgl folder.  
￼￼
#How to play with it

You as a user can interact with the program! The main way to do so is the keyboard, but you could also click with the mouse on a ‘generator’ source to see the particles disappear and then appear back again, as soon as you release the mouse button.
To Navigate inside the Universe:
Use the arrow buttons. The move at the moment is restricted on the y-axis so there is no complete freedom of movement, however, it possible to move next to the sources, zoom in or zoom out. This movement wasn’t developed further on the other two axes, because it wasn’t one of the primary goals of this project, but was added more for the user’s convenience.
To Increase the Speed of the Particles in the “generator” Sources:

 -> For source on the Left: Press ‘Z’ key to increase the speed and ‘N’ key to decrease it.
 
 -> For source on the Right: Press ‘N’ key to increase the speed and ‘M’ key to decrease it.
 
 -> For both sources at once: Press ‘U’ key to increase the speed and ‘D’ key to decrease it.
 
 Here is a short demo to check out the simulation: https://www.youtube.com/watch?v=ZwkQmab37AU
 
 ![Simulation of wormhole](/images/wormhole.png)
 
Enjoy!
