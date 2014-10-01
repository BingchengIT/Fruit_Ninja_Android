/**
 * CS349 Winter 2014
 * Bingcheng Zhu
 * University of Waterloo
 */
package com.example.a4;
import java.util.Random;

import android.R;
import android.graphics.*;
import android.util.Log;

/**
 * Class that represents a Fruit. Can be split into two separate fruits.
 */
public class Fruit {
    private Path path = new Path();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Matrix transform = new Matrix();
    
    // add private member
    private int move_directory = 1;
    private boolean piece_fruit = false;
    private boolean shouldbeRemove = false;
    
    /**
     * A fruit is represented as Path, typically populated 
     * by a series of points 
     */
    Fruit(float[] points) {
        init();
        this.path.reset();
        this.path.moveTo(points[0], points[1]);
        for (int i = 2; i < points.length; i += 2) {
            this.path.lineTo(points[i], points[i + 1]);
        }
        this.path.moveTo(points[0], points[1]);
    }

    Fruit(Region region) {
        init();
        this.path = region.getBoundaryPath();
    }

    Fruit(Path path) {
        init();
        this.path = path;
    }

    private void init() {
    	Random rnd = new Random(); 
    	int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        this.paint.setColor(color);
        this.paint.setStrokeWidth(5);
    }

    /**
     * The color used to paint the interior of the Fruit.
     */
    public int getFillColor() { return paint.getColor(); }
    public void setFillColor(int color) { paint.setColor(color); }

    /**
     * The width of the outline stroke used when painting.
     */
    public double getOutlineWidth() { return paint.getStrokeWidth(); }
    public void setOutlineWidth(float newWidth) { paint.setStrokeWidth(newWidth); }

    /**
     * Concatenates transforms to the Fruit's affine transform
     */
    public void rotate(float theta) { transform.postRotate(theta); }
    public void scale(float x, float y) { transform.postScale(x, y); }
    public void translate(float tx, float ty) { transform.postTranslate(tx, ty); }

    /**
     * Returns the Fruit's affine transform that is used when painting
     */
    public Matrix getTransform() { return transform; }

    /**
     * The path used to describe the fruit shape.
     */
    public Path getTransformedPath() {
        Path originalPath = new Path(path);
        Path transformedPath = new Path();
        originalPath.transform(transform, transformedPath);
        return transformedPath;
    }

    /**
     * Paints the Fruit to the screen using its current affine
     * transform and paint settings (fill, outline)
     */
    public void draw(Canvas canvas) {
        // TODO BEGIN CS349
    	canvas.drawPath(this.getTransformedPath(), paint);
        // tell the shape to draw itself using the matrix and paint parameters
        // TODO END CS349
    }

    /**
     * Tests whether the line represented by the two points intersects
     * this Fruit.
     */
    public boolean intersects(PointF p1, PointF p2) {
        // TODO BEGIN CS349
    	
    	float angle = Graphics2D.findAngle(p1, p2);
    	//System.out.println(angle);
   
    	RectF realFruit_bound = new RectF();
    	Fruit realFruit = new Fruit(getTransformedPath()); 
    	if (realFruit.contains(p1) || realFruit.contains(p2)) {
    		//System.out.println("Fk my life");
    		return false;
    	}
    	
    	realFruit.translate(-p1.x, -p1.y);
    	realFruit.rotate(angle);
    	realFruit = new Fruit(realFruit.getTransformedPath());
    	realFruit.path.computeBounds(realFruit_bound, true);
    	
    	Path line = new Path();
    	line.reset();
    	line.moveTo(p1.x, p1.y);
    	line.lineTo(p2.x, p2.y);
    	line.moveTo(p2.x, p2.y);
    	
    	RectF lineFruit_bound = new RectF();
    	Fruit lineFruit = new Fruit(line);
    	lineFruit.translate(-p1.x, -p1.y);
    	lineFruit.rotate(angle);
    	lineFruit = new Fruit(lineFruit.getTransformedPath());
    	lineFruit.path.computeBounds(lineFruit_bound, true);
    	//System.out.println(realFruit_bound.intersect(lineFruit_bound));
    	return realFruit_bound.intersect(lineFruit_bound);
	
        // calculate angle between points
        // rotate and flatten points passed in 
        // rotate path and create region for comparison
        // TODO END CS349
        //return false;
    }

    /**
     * Returns whether the given point is within the Fruit's shape.
     */
    public boolean contains(PointF p1) {
        Region region = new Region();
        boolean valid = region.setPath(getTransformedPath(), new Region());
        return valid && region.contains((int) p1.x, (int) p1.y);
    }

    /**
     * This method assumes that the line represented by the two points
     * intersects the fruit. If not, unpredictable results will occur.
     * Returns two new Fruits, split by the line represented by the
     * two points given.
     */
    public Fruit[] split(PointF p1, PointF p2) {
    	//Region clip = new Region(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    	float angle = Graphics2D.findAngle(p1, p2);
    	Fruit realFruit = new Fruit(getTransformedPath());
    	realFruit.translate(-p1.x, -p1.y);
    	realFruit.rotate(angle);
    	realFruit = new Fruit(realFruit.getTransformedPath());
    	RectF realFruit_bound = new RectF();
    	realFruit.path.computeBounds(realFruit_bound, true);
    	
    	Region TOP_region = new Region((int)realFruit_bound.left, (int)realFruit_bound.top, (int)realFruit_bound.right, 0);
    	Region BOT_region = new Region((int)realFruit_bound.left, 0, (int)realFruit_bound.right, (int)realFruit_bound.bottom);
    		
    	TOP_region.setPath(realFruit.path, TOP_region);
    	BOT_region.setPath(realFruit.path, BOT_region);
    	
    	Fruit TOP_Fruit = new Fruit(TOP_region);
    	TOP_Fruit.translate(0, -10);
    	TOP_Fruit.rotate(-angle);
    	TOP_Fruit.translate(p1.x,p1.y);
    	TOP_Fruit = new Fruit(TOP_Fruit.getTransformedPath());
    	
    	Fruit BOT_Fruit = new Fruit(BOT_region);
    	BOT_Fruit.translate(0, 10);
    	BOT_Fruit.rotate(-angle);
    	BOT_Fruit.translate(p1.x,p1.y);
    	BOT_Fruit = new Fruit(BOT_Fruit.getTransformedPath());
    	
        if (TOP_region != null && BOT_region != null) {
        	TOP_Fruit.move_directory = 0;
        	BOT_Fruit.move_directory = 0;
        	TOP_Fruit.piece_fruit = true;
        	BOT_Fruit.piece_fruit = true;
        	return new Fruit[] {TOP_Fruit, BOT_Fruit};
        }
        return new Fruit[0];
    	// TODO BEGIN CS349
        // calculate angle between points
        // rotate and flatten points passed in
        // rotate region
        // define region masks and use to split region into top and bottom
        // TODO END CS349
    }
    
    public boolean Fruit_shouldbeRemoved(){
    	return this.shouldbeRemove;
    }
    
    public boolean IsSliced_IsDirectory_down() {
    	return (this.move_directory == 0 && this.piece_fruit);
    }
    
    public float Getdistance_between_topbound() {
    	float Bounding1 = 100;
    	RectF curFruit_bound = new RectF();
    	this.getTransformedPath().computeBounds(curFruit_bound, true);
    	return curFruit_bound.centerY() - Bounding1;
    }
    
    public float Velocity() {
    	if(this.Getdistance_between_topbound() <= 0.05 || this.move_directory == 0) {
    		this.move_directory = 0;
    		if (this.Getdistance_between_topbound() > 800 && this.Getdistance_between_topbound() < 805) {
    			this.shouldbeRemove = true;
    		}
    		if (this.Getdistance_between_topbound() >= 900) {
    			this.move_directory = 1;
    		}
    		return (float) Math.sqrt(Math.abs(this.Getdistance_between_topbound()) * this.Get_gravity());
    	}
    	if(this.Getdistance_between_topbound() >=900 || this.move_directory == 1) {
    		this.move_directory = 1;
    		return (float) (0 - Math.sqrt(this.Getdistance_between_topbound() * this.Get_gravity()));
    	}
    	else {
    		this.move_directory = 0;
    		return (float) Math.sqrt(this.Getdistance_between_topbound() * this.Get_gravity());
    	}
    }
    
    public float Get_gravity() {
    	return (float) 0.2;
    }
    
    
    public float H_Velocity() {
    	RectF curFruit_bound = new RectF();
    	this.getTransformedPath().computeBounds(curFruit_bound, true);
    	if (curFruit_bound.centerX() <= 200 && curFruit_bound.centerY() <= 700) {
    		return (float) 0.5;
    	}
    	else {
    		return (float) -0.5;
    	}
    }
}
