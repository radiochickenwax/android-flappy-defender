package a.b.project;

/*
The Context parameter that is passed into our constructor is a reference to the
current state of our application within the Android system that is held by our
GameActivity class. 
*/
import android.content.Context;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/*TDView.java:6: error: TDView is not abstract and does not override abstract method run() in Runnable*/
public class TDView extends SurfaceView implements Runnable {

    volatile boolean playing; /*using the volatile keyword as it will be accessed from outside the thread and from within.*/

    Thread gameThread = null;

    //Game objects
    private PlayerShip player;

    // For drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;

    
    public TDView(Context context) {
	super(context);
	// Initialize our drawing objects
	ourHolder = getHolder();
	paint = new Paint();
	// Initialize our player ship
	player = new PlayerShip(context);

    }



    @Override
    public void run() {
	while (playing) {
	    update();
	    draw();
	    control();	    
	}
    }

    private void update(){
	// Update the player
	player.update();

    }

    private void draw(){
	/*
	  1.	 Check that our SurfaceHolder class is valid.
	  2.	 Lock the Canvas object.
	  3.	 Clear the screen with a call to drawColor().
	  4.	 Splash some virtual paint on it by calling drawBitmap() and passing in the
	  PlayerShip bitmap and an x, y coordinate.
	  5.	 Finally, unlock the Canvas object and draw the scene.
	*/
	if (ourHolder.getSurface().isValid()) {

	    //First we lock the area of memory we will be drawing to
	    canvas = ourHolder.lockCanvas();

	    // Rub out the last frame
	    canvas.drawColor(Color.argb(255, 0, 0, 0));

	    // Draw the player
	    canvas.drawBitmap(
			      player.getBitmap(),
			      player.getX(),
			      player.getY(),
			      paint);

	    // Unlock and draw the scene
	    ourHolder.unlockCanvasAndPost(canvas);
	}

    }

    private void control() {
	
	try {
	    gameThread.sleep(17);
	} catch (InterruptedException e) {

        }

    }


    public void pause() {
	playing = false;
	try {
	    gameThread.join();
	} catch (InterruptedException e) {

	}

    }


    // Make a new thread and start it
    // Execution moves to our R
    public void resume() {
	playing = true;
	gameThread = new Thread(this);
	gameThread.start();
    }
    
}
