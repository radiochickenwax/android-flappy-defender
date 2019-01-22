package a.b.project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/*What do we need our PlayerShip class to be able to know about itself? As a bare
minimum it needs to:

    •	 Know where it is on the screen
    •	 What it looks like
    •	 How fast it is flying

    •	 Prepare itself
    •	 Update itself
    •	 Share it's state with our view

*/
public class PlayerShip  {
    private Bitmap bitmap;
    private int x, y;
    private int speed = 0;
    private boolean boosting;


    // Constructor
    public PlayerShip(Context context) {
	boosting = false;
	x = 50;
	y = 50;
	speed = 1;
	bitmap = BitmapFactory.decodeResource
            (context.getResources(), R.drawable.ship);

    }

    public void setBoosting() {
	boosting = true;
    }

    public void stopBoosting() {
	boosting = false;
    }

    public void update() {
      x++;
    }


    //Getters
    public Bitmap getBitmap() {
      return bitmap;
    }

    public int getSpeed() {
	return speed;
    }

    public int getX() {
	return x;
    }

    public int getY() {
	return y;
    }

}
