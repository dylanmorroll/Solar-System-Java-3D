import java.awt.BorderLayout;
import java.awt.Container;
import java.util.Random;

import javax.media.j3d.Alpha;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JApplet;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;

@SuppressWarnings("serial")
public class Example3D extends JApplet {
	
	// Random object to generate random numbers for comet belt
	Random rand = new Random();
    
    public BranchGroup createSceneGraph() {
	
    	// --- Setting up the universe
    	
		// Creating the bounds of the universe
		BoundingSphere bounds =
		       new BoundingSphere(new Point3d(0.0,0.0,0.0), 1000.0);
		    
		// Create the main branch group 
		BranchGroup objRoot = new BranchGroup();
	
		// Creating a main transform group (inherits mouse behaviour) 
		TransformGroup mainTG = new TransformGroup();		
		// Set the capability to TRANSFORM the main TG (eg mouse MOVEMENT) 
		mainTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		mainTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		// Set the background of the universe
		TextureLoader backgroundTextureLoader = new TextureLoader("background.jpg", new Container());
		Texture backgroundTexture = backgroundTextureLoader.getTexture();
		Appearance backgroundAppearence = new Appearance();
		backgroundAppearence.setTexture(backgroundTexture);
		Sphere backgroundSphere = new Sphere(500,
				Primitive.GENERATE_TEXTURE_COORDS | Primitive.GENERATE_NORMALS_INWARD,
				1000, backgroundAppearence);
		mainTG.addChild(backgroundSphere);
		
		// Add the main transform group to the root branch
		objRoot.addChild(mainTG);
	
		// Create the rotate behavior node
		MouseRotate behavior = new MouseRotate();
		behavior.setTransformGroup(mainTG);
		objRoot.addChild(behavior);
		behavior.setSchedulingBounds(bounds);
		
		// Create the zoom behavior node
		MouseZoom behavior2 = new MouseZoom();
		behavior2.setTransformGroup(mainTG);
		objRoot.addChild(behavior2);	
	    behavior2.setSchedulingBounds(bounds);
		
		// Create the translate behavior node
		MouseTranslate behavior3 = new MouseTranslate();
		behavior3.setTransformGroup(mainTG);
		objRoot.addChild(behavior3);
		behavior3.setSchedulingBounds(bounds);
	 
		// --- End setup TODO
		
		createPlanets(mainTG, bounds);
	
		objRoot.compile();
		return objRoot;
    }
    
    // Function for generating all the planets, in a loop, from given lists
    public void createPlanets(TransformGroup mainTG, BoundingSphere bounds) {
    	
    	// List of all sets of int values required for generating the orbital rotation
    	// The orbital rotation, is the rotation of the planet around the y-axis centred
    	// at the origin
    	// Each set is {x axis offset, z axis offset, y axis rotation speed}
    	// The axis offset creates the plane in which the planet rotates 
    	int[][] orbitals = {
    			{0, 0, 0},	// This is the centre planet, which is the sun
    			{2, 0, 16000},
    			{4, 0, 14000},
    			{6, 0, 18000},
    			{8, 0, 15000},
    			{10, 0, 20000},	// 5
    			{12, 0, 16000},		// 6
    			{14, 0, 17000},
    			{16, 0, 15000},
    			{18, 0, 16000},
    			{20, 0, 13000}
    	};
    	
    	// List of all sets of int values required for generating the translations
    	// Each set is {x displacement, y displacement, z displacement}
    	int[][] translations = {
    			{0, 0, 0},		// This is the centre planet, which is the sun
    			{150, 0, 0},	// 1
    			{-80, 0, 20},		// 2
    			{20, 0, -90},		// 3
    			{-90, 0, -40},		// 4
    			{10, 0, 100},	// 5
    			{-50, 0, 40},		// 6
    			{60, 0, -30},		// 7
    			{-20, 0, -70},		// 8
    			{40, 0, 80},	// 9
    			{-30, 0, 120}		// 10
    	};
    	
    	// List of all int values required for generating the axis rotation
    	// Each value is the rotational speed of the planet around it's own x axis
    	int[] axis = {
    			3300,	// This is the centre planet, which is the sun
    			12000,
    			4250,
    			4100,
    			52000,
    			11800,
    			7900,
    			9750,
    			6200,
    			8600,
    			11700
    	};
    	
    	// List of all int values required for generating planets
    	// Each set is {radius, smoothness of surface)
    	int[][] planets = {
    			{20, 75},	// This is the centre planet, which is the sun
    			{7, 85},
    			{4, 89},
    			{5, 83},
    			{4, 84},
    			{5, 80},
    			{3, 81},	// 6
    			{12, 40},
    			{2, 86},
    			{4, 82},
    			{6, 87}
    	};
    	
    	// Loop through given lists to generate all planets
    	for (int i = 0; i < orbitals.length; i++) {
    		
    		// Create branch group for planet
        	BranchGroup background = new BranchGroup();
        	
        	// Add the branch group to the main transform group
        	mainTG.addChild(background);
    		
        	// Get the orbital rotation values for this planet
        	int[] thisOrbital = orbitals[i];
        	// Create the orbital rotation
        	TransformGroup orbitalRotation = createOrbitalRotation(bounds,
        			thisOrbital[0], 
        			thisOrbital[1],
        			thisOrbital[2]);
        	// Add the orbital rotiation to the background
        	background.addChild(orbitalRotation);
        	
        	// Get the translation values for this planet
        	int[] thisTranslation = translations[i];
        	// Create a translation
    		TransformGroup translation = createTranslation(
    				thisTranslation[0],
    				thisTranslation[1],
    				thisTranslation[2]);
    		// Add the translation to the previous transform group (orbital rotation)
    		orbitalRotation.addChild(translation);
    		
    		// Get the axis speed value for this planet
    		int thisAxis = axis[i];
    		// Create the axis rotation
    		TransformGroup axisRotation = createAxisRotation(bounds, thisAxis);
    		// Add the axis rotation to the previous transform group (the translation)
    		translation.addChild(axisRotation);
    	
    		// Get the planet values for this planet
    		int[] planetAttributes = planets[i];
    		// Create a sphere object for the planet
    		Sphere planet = createPlanet(
    				"plan" + i + ".jpg", //the file names are named incrementally
    				planetAttributes[0],
    				planetAttributes[1]);
    		// Add the planet to the previous transform group (the axis rotation)
    		axisRotation.addChild(planet);
    		
    		if (i == 7) {
    			
    			generateParticleRing(axisRotation);
    		}
    	}
    	
    }
    
    public TransformGroup createOrbitalRotation(BoundingSphere bounds,
    		double xAngle,
    		double zAngle,
    		int rotationSpeed) {
    	
    	// Create the top level transform group for the planet
	    // This controls planetary rotation around the y-axis
		TransformGroup transformGroup = new TransformGroup();	
		// Set the capability to TRANSFORM the TGA (by ROTATION) 
		transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	    
		// creating a rotation interpolator for TGA 
	    Transform3D yAxis = new Transform3D();
	    // rotate the y axis pi/2 around x axis 
	    // so the y axis "becomes" the z axis
	    // hence bgA rotates around the z axis 
	    Transform3D newAxis = new Transform3D();
	    yAxis.rotX((Math.PI / 180.0) * xAngle);
	    newAxis = yAxis; // use SENSIBLE VARIABLE NAMES !!
	    
	    // Aplha (number of rotations, time to go from 0 to 1)
	    Alpha rotation = new Alpha(-1, rotationSpeed);
	    // this rotator is for TGA - cubes c and co
	    RotationInterpolator rotatorTGA = new RotationInterpolator(
	    		rotation,					// Alpha alpha
	    		transformGroup,				// TransformGroup target
	            yAxis,						// Transform3D axisOfTransform
	            0,							// float minimumAngle
	            (float) Math.PI * (2.0f));	// float maximumAngle
	    rotatorTGA.setSchedulingBounds(bounds);
		
		// Add the transform group to the background
	    transformGroup.addChild(rotatorTGA);
	    
	    return transformGroup;
    }
    
    public TransformGroup createTranslation(double x, double y, double z) {

		Transform3D temp = new Transform3D();
		
    	Vector3d translationVector = new Vector3d(x, y, z);
		temp.setTranslation(translationVector);
		
		TransformGroup translation = new TransformGroup();
		translation.setTransform(temp);
		
		return translation;
    }
    
    public TransformGroup createAxisRotation(BoundingSphere bounds,
    		int rotationSpeed) {
    	
    	// Create the top level transform group for the planet
	    // This controls planetary rotation around the y-axis
		TransformGroup transformGroup = new TransformGroup();	
		// Set the capability to TRANSFORM the TGA (by ROTATION) 
		transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	    
		// creating a rotation interpolator for TGA 
	    Transform3D yAxis = new Transform3D();
	    // Aplha (number of rotations, time to go from 0 to 1)
	    Alpha rotation = new Alpha(-1, rotationSpeed);
	    // this rotator is for TGA - cubes c and co
	    RotationInterpolator rotatorTGA = new RotationInterpolator(
	    		rotation,					// Alpha alpha
	    		transformGroup,				// TransformGroup target
	            yAxis,						// Transform3D axisOfTransform
	            0,							// float minimumAngle
	            (float) Math.PI * (2.0f));	// float maximumAngle
	    rotatorTGA.setSchedulingBounds(bounds);
		
		// Add the transform group to the background
	    transformGroup.addChild(rotatorTGA);
	    
	    return transformGroup;
    }
    
    public Sphere createPlanet(String fileName, int radius, int roughness) {
    	
    	// Planet texture
		TextureLoader textureLoader = new TextureLoader(fileName, new Container());
		Texture texture = textureLoader.getTexture();
		TextureAttributes textureAttributes = new TextureAttributes();
		textureAttributes.setTextureMode(TextureAttributes.MODULATE);
		Appearance appearence = new Appearance();
		appearence.setTexture(texture);
		appearence.setTextureAttributes(textureAttributes);
		int primFlags = Primitive.GENERATE_NORMALS + Primitive.GENERATE_TEXTURE_COORDS;
		Sphere planet1 = new Sphere(radius, primFlags, roughness, appearence);
		
		return planet1;
    }
    
    public void generateParticleRing(TransformGroup toAddTo) {
    	
    	int numOfParticles = 700;
    	for (int j = 0; j < numOfParticles; j++) {
			
			// Generates a random number between 20 and 25 inclusive
			int radius = rand.nextInt(6)+20;
			int xDisplacement;
			int zDisplacement;

			if (j < (numOfParticles / 2)) {

				// Generates a random number between 0 and (20-25) inclusive
				xDisplacement = rand.nextInt(26);
				
				if (xDisplacement > radius)
					xDisplacement = radius;
				
				double z = Math.sqrt(Math.pow(radius, 2) - Math.pow(xDisplacement, 2));
				zDisplacement = (int) Math.round(z);
				
			} else {
				
				// Generates a random number between 0 and (20-25) inclusive
				zDisplacement = rand.nextInt(26);
				
				if (zDisplacement > radius)
					zDisplacement = radius;
				
				double z = Math.sqrt(Math.pow(radius, 2) - Math.pow(zDisplacement, 2));
				xDisplacement = (int) Math.round(z);
			}
			
			// Half the numbers have negative displacement
			if (j % 2 == 0)
				xDisplacement = -xDisplacement;
			if ((j/2)%2 == 0)
				zDisplacement = -zDisplacement;
			
			TransformGroup particleTranslation = createTranslation(xDisplacement, 0, zDisplacement);
			toAddTo.addChild(particleTranslation);
			
			// Create an ice belt for the frozen planet
    		Sphere particle = createPlanet(
    				"comet.jpg", //the file names are named incrementally
    				1,
    				5);
    		// Add the planet to the previous transform group (the axis rotation)
    		particleTranslation.addChild(particle);
		}
    }
 
    // create a "standard" universe using SimpleUniverse 
    public Example3D() {
    	
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		Canvas3D c = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
		cp.add("Center", c);
		BranchGroup scene = createSceneGraph();
		SimpleUniverse u = new SimpleUniverse(c);
		u.getViewingPlatform().setNominalViewingTransform();
		TransformGroup camera = u.getViewingPlatform().getViewPlatformTransform();
		Transform3D trans = new Transform3D();
		trans.setTranslation(new Vector3d(0, 0, 250));
		camera.setTransform(new Transform3D(trans));
		u.addBranchGraph(scene);
		u.getViewer().getView().setBackClipDistance(500);
    }

    public static void main(String[] args) {        

    	new MainFrame(new Example3D(), 512, 512);
    }
    
}
