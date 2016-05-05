/**
 * Describe class (scene graph example 2) Scene2 here.
 *
 * Time-stamp: <2015-02-27 10:27:52 rlc3> edited by rlc 
 *
 * Created: 2014-02-21 by rlc 
 *
 */

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.awt.*;
import com.sun.j3d.utils.behaviors.mouse.*; 
import javax.swing.JApplet;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Box;

public class Scene2 extends JApplet {
    
    public BranchGroup createSceneGraph() {

		// creating the bounds of the universe
		// see mouse behaviour below 
		BoundingSphere bounds =
		    new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
	 
		// creating a branch group
		BranchGroup objRoot = new BranchGroup();
		BranchGroup bgA = new BranchGroup();
		BranchGroup bgB = new BranchGroup();
	
		// creating a main transform group 
		TransformGroup mainTG = new TransformGroup();		
		mainTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		mainTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		TransformGroup TGA = new TransformGroup();		
		TransformGroup TGB = new TransformGroup();		
		objRoot.addChild(mainTG);
        mainTG.addChild(bgA);
        mainTG.addChild(bgB);
        bgA.addChild(TGA);
        bgB.addChild(TGB);
	
		// creating another transform group (cubeTG1, from t)
        // first creating a transformation t 
		Transform3D t = new Transform3D();
        // scale by 2, then translate -5 along z-axis 
        t.setScale(new Vector3d(2.0, 2.0 ,2.0));
        t.setTranslation(new Vector3d(0.0, 0.0 ,-5));
		TransformGroup cubeTG1 = new TransformGroup(t);
	
		// creating a rotation interpolator for a new cubeTG2
		TransformGroup cubeTG2 = new TransformGroup();
		cubeTG2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE); 
		cubeTG2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ); 
        Transform3D yAxiscubeTG2 = new Transform3D();
		// What does this do? yAxis.rotZ(-Math.PI/2); 
        Alpha rotationAlphacubeTG2 = new Alpha(-1, 4000);
        RotationInterpolator rotatorcubeTG2 = new RotationInterpolator(
        		rotationAlphacubeTG2,
		        cubeTG2, yAxiscubeTG2, 0.0f, (float) Math.PI * (2.0f));
	    rotatorcubeTG2.setSchedulingBounds(bounds);
	
		// creating another transform group (boxTG1, from tt)
        // first creating a transformation tt 
		Transform3D tt = new Transform3D();
        tt.setTranslation(new Vector3d(3.0, 0.0 ,0.0));
		TransformGroup boxTG1 = new TransformGroup(tt);
	
		// creating a rotation interpolator for a new boxTG2
		TransformGroup boxTG2 = new TransformGroup();
		boxTG2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		boxTG2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        Transform3D yAxisboxTG2 = new Transform3D();
        Alpha rotationAlphaboxTG2 = new Alpha(-1, 4000);
        RotationInterpolator rotatorboxTG2 = new RotationInterpolator(rotationAlphaboxTG2,
        boxTG2, yAxisboxTG2, 0.0f, (float) Math.PI * (2.0f));
        rotatorboxTG2.setSchedulingBounds(bounds);
	
		// creating an appearance (for a box)
		Appearance groundApp = new Appearance();
		Color3f groundColor = new Color3f(0.0f, 1.0f, 0.0f);
		ColoringAttributes groundCA = new ColoringAttributes();
		groundCA.setColor(groundColor);
		groundApp.setColoringAttributes(groundCA);
	
		// creating 3D shapes: colorcubes
        // try altering the ".5" sizing parameter
		ColorCube c = new ColorCube(.5);
		ColorCube co = new ColorCube(.5);
        ColorCube cr = new ColorCube(.2); 
	
        // create a box
		Box b = new Box(0.8f,0.8f,.1f,groundApp);
	
        // make edge relations between the scene graph nodes
		// cube c is transformed by cubeTG2 then cubeTG1
        // cube co and box b at the origin 
	
		TGA.addChild(cubeTG1);
		cubeTG1.addChild(cubeTG2);
        cubeTG2.addChild(c);
        cubeTG2.addChild(rotatorcubeTG2);
		TGA.addChild(co);
        TGB.addChild(boxTG1); 
        boxTG1.addChild(boxTG2);
        boxTG2.addChild(b);
        boxTG2.addChild(rotatorboxTG2);
	
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
	
		objRoot.compile();
		return objRoot;
    }
 
    // create a "standard" universe using SimpleUniverse 
    public Scene2() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		Canvas3D c = new Canvas3D(SimpleUniverse.getPreferredConfiguration() );
		cp.add("Center", c);
		BranchGroup scene = createSceneGraph();
		SimpleUniverse u = new SimpleUniverse(c);
		u.getViewingPlatform().setNominalViewingTransform();
		u.addBranchGraph(scene);
    }

    public static void main(String[] args) {        

    	new MainFrame(new Scene2(), 512, 512);
    }
    
}
