package a.b.project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.util.Random;

public class EnemyShip{
    private Bitmap bitmap;
    private int x, y;
    private int speed = 1;

    // Detect enemies leaving the screen
    private int maxX;
    private int minX;

    // Spawn enemies within screen bounds
    private int maxY;
    private int minY;

    //Getters and Setters
    public Bitmap getBitmap(){
	return bitmap;
    }

    public int getX() {
	return x;
    }

    public int getY() {
	return y;
    }
      


    // Constructor
    public EnemyShip(Context context, int screenX, int screenY){
	bitmap = BitmapFactory.decodeResource
	    (context.getResources(), R.drawable.enemy);



	maxX   =   screenX;
	maxY   =   screenY;
	minX   =   0;
	minY   =   0;

	Random generator = new Random();
	speed = generator.nextInt(6)+10;

	x = screenX;
	y = generator.nextInt(maxY) - bitmap.getHeight();
    }


}

