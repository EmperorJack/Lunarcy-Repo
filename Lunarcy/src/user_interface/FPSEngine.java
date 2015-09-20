package user_interface;
/**
 * So bored, enjoy xoxox
 * @author Kelly
 *
 */

import java.awt.Graphics;

import processing.core.*;
public class FPSEngine extends PApplet{
	public PGraphics canvas3D;
	public Canvas canvas;
	
	//camera fields
	public PVector camEye;
	public PVector camCenter;
	public float rotationAngle = 0;
	public float elevationAngle = 0;
	
	public FPSEngine(PGraphics canvas3D, Canvas canvas){
		this.canvas3D = canvas3D;
		this.canvas = canvas;
		//Camera setup
		camEye = new PVector(0,0,0);
		camCenter = new PVector(0,0, 0.0f);
	}
	
	public void draw(){
		canvas3D.beginDraw();
		canvas3D.clear();
		canvas3D.stroke(0);
		canvas3D.fill(0,0,255);
		canvas3D.camera(camEye.x, camEye.y, camEye.z, camCenter.x, camCenter.y, camCenter.z, 
			       0.0f, 1,0);
		canvas3D.translate(0, 0, -80);
		canvas3D.sphere(10);
		canvas3D.endDraw();
	}
	

	void updateCamera(float rotationAngle, float elevationAngle, PVector move) {
		camEye.x += move.x;
		camEye.z += move.y;
		  camCenter.x = cos(rotationAngle) + camEye.x;
		  camCenter.z = sin(rotationAngle) + camEye.z;
		  camCenter.y =  -cos(elevationAngle) + camEye.y;
		  
		}
}
