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
import android.media.SoundPool;
import android.view.MotionEvent;
import java.util.ArrayList;
import android.graphics.Rect;

/*TDView.java:6: error: TDView is not abstract and does not override abstract method run() in Runnable*/
public class TDView extends SurfaceView implements Runnable {

    private   SoundPool soundPool;
    int   start = -1;
    int   bump = -1;
    int   destroyed = -1;
    int   win = -1;

    private boolean gameEnded;
    private Context context;
    private   float distanceRemaining;
    private   long timeTaken;
    private   long timeStarted;
    private   long fastestTime;

    private int screenX;
    private int screenY;



    volatile boolean playing; /*using the volatile keyword as it will be accessed from outside the thread and from within.*/

    Thread gameThread = null;

    //Game objects
    private PlayerShip player;
    public EnemyShip enemy1;
    public EnemyShip enemy2;
    public EnemyShip enemy3;

    // Make some random space dust
    public ArrayList<SpaceDust> dustList = new ArrayList<SpaceDust>();

    
    // For drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;

    
    public TDView(Context context, int x, int y) {
	super(context);
	this.context = context;

	screenX = x;
	screenY = y;


	// Initialize our drawing objects
	ourHolder = getHolder();
	paint = new Paint();
	// // Initialize our player ship
	// player = new PlayerShip(context, x, y);
	// enemy1 = new EnemyShip(context, x, y);
	// enemy2 = new EnemyShip(context, x, y);
	// enemy3 = new EnemyShip(context, x, y);

	// /*
	//   initialize a whole bunch of the SpaceDust objects
	//   using a for loop and then stash them into the ArrayList object:
	//  */
	// int numSpecs = 40;
	// for (int i = 0; i < numSpecs; i++) {
	//     // Where will the dust spawn?
	//     SpaceDust spec = new SpaceDust(x, y);
	//     dustList.add(spec);
	// }

	startGame();
    }


    private void startGame() {
        //Initialize game objects
	gameEnded = false;
            player = new PlayerShip(context, screenX, screenY);
            enemy1 = new EnemyShip(context, screenX, screenY);
            enemy2 = new EnemyShip(context, screenX, screenY);
            enemy3 = new EnemyShip(context, screenX, screenY);

              int numSpecs = 40;
              for (int i = 0; i < numSpecs; i++) {
                  // Where will the dust spawn?
                  SpaceDust spec = new SpaceDust(screenX, screenY);
                  dustList.add(spec);
              }

              // Reset time and distance
              distanceRemaining = 10000;// 10 km
              timeTaken = 0;

              // Get start time
              timeStarted = System.currentTimeMillis();
	      gameEnded = false;
    }



    @Override
    public void run() {
	while (playing) {
	    update();
	    draw();
	    control();	    
	}
    }


    // SurfaceView allows us to handle the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

	// There are many different events in MotionEvent
	// We care about just 2 - for now.
	switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

	    // Has the player lifted their finger up?
	case MotionEvent.ACTION_UP:
	    // Do something here
	    player.stopBoosting();
	    break;

	    // Has the player touched the screen?
	case MotionEvent.ACTION_DOWN:
	    // Do something here
	    player.setBoosting();
	    // If we are currently on the pause screen, start a new game
	    if(gameEnded){
		startGame();
	    }
	    break;
	}
	return true;
    }

    
    private void update() {
	// Collision detection on new positions
	// Before move because we are testing last frames
	// position which has just been drawn
	boolean hitDetected = false;
	// If you are using images in excess of 100 pixels
	// wide then increase the -100 value accordingly
	if(Rect.intersects
	   (player.getHitbox(), enemy1.getHitbox())){
	    hitDetected = true;
	    enemy1.setX(-200);
	}

	if(Rect.intersects
	   (player.getHitbox(), enemy2.getHitbox())){
	    hitDetected = true;
	    enemy2.setX(-200);
	}

	if(Rect.intersects
	   (player.getHitbox(), enemy3.getHitbox())){
	    hitDetected = true;
	    enemy3.setX(-200);
	}

	
	if(hitDetected) {
	    player.reduceShieldStrength();
	    if (player.getShieldStrength() < 0) {
                //game over so do something
		gameEnded = true;
	    }
	}


	// Update the player
	player.update();
	// Update the enemies
	enemy1.update(player.getSpeed());
	enemy2.update(player.getSpeed());
	enemy3.update(player.getSpeed());
	for (SpaceDust sd : dustList) {
	    sd.update(player.getSpeed());
	}

	if(!gameEnded) {
	    //subtract distance to home planet based on current speed
	    distanceRemaining -= player.getSpeed();

	    //How long has the player been flying
	    timeTaken = System.currentTimeMillis() - timeStarted;
	}

	//Completed the game!
	if(distanceRemaining < 0){
	    //check for new fastest time
	    if(timeTaken < fastestTime) {
		fastestTime = timeTaken;
	    }

	    // avoid ugly negative numbers
	    // in the HUD
	    distanceRemaining = 0;

	    // Now end the game
	    gameEnded = true;
	}


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

	    // For debugging
	    // Switch to white pixels
	    //paint.setColor(Color.argb(255, 255, 255, 255));

	    // Draw Hit boxes
	    // canvas.drawRect(player.getHitbox().left,
	    // 		    player.getHitbox().top,
	    // 		    player.getHitbox().right,
	    // 		    player.getHitbox().bottom,
	    // 		    paint);

	    // canvas.drawRect(enemy1.getHitbox().left,
	    // 		    enemy1.getHitbox().top,
	    // 		    enemy1.getHitbox().right,
	    // 		    enemy1.getHitbox().bottom,
	    // 		    paint);

	    // canvas.drawRect(enemy2.getHitbox().left,
	    // 		    enemy2.getHitbox().top,
	    // 		    enemy2.getHitbox().right,
	    // 		    enemy2.getHitbox().bottom,
	    // 		    paint);

	    // canvas.drawRect(enemy3.getHitbox().left,
	    // 		    enemy3.getHitbox().top,
	    // 		    enemy3.getHitbox().right,
	    // 		    enemy3.getHitbox().bottom,
	    // 		    paint);

	    // Rub out the last frame
	    //canvas.drawColor(Color.argb(255, 0, 0, 0));

	    // White specs of dust
	    paint.setColor(Color.argb(255, 255, 255, 255));

	    //Draw the dust from our arrayList
	    for (SpaceDust sd : dustList) {
		canvas.drawPoint(sd.getX(), sd.getY(), paint);
	    }
	    
	    // Draw the player
	    canvas.drawBitmap( player.getBitmap(),  player.getX(),  player.getY(), paint );

	    canvas.drawBitmap
		(enemy1.getBitmap(),
		 enemy1.getX(),
		 enemy1.getY(), paint);

	    canvas.drawBitmap
		(enemy2.getBitmap(),
		 enemy2.getX(),
		 enemy2.getY(), paint);

	    canvas.drawBitmap
		(enemy3.getBitmap(),
		 enemy3.getX(),
		 enemy3.getY(), paint);

	    if (!gameEnded) {
		// Draw the hud
		paint.setTextAlign(Paint.Align.LEFT);
		paint.setColor(Color.argb(255, 255, 255, 255));
		paint.setTextSize(10);
		canvas.drawText("Fastest:"+ fastestTime + "s", 10, 20, paint);
		canvas.drawText("Time:" + timeTaken + "s", screenX / 2, 20,
				paint);
		canvas.drawText("Distance:" +
				distanceRemaining / 1000 +
				" KM", screenX / 3, screenY - 20, paint);

		canvas.drawText("Shield:" +
				player.getShieldStrength(), 10, screenY - 20, paint);

		canvas.drawText("Speed:" +
				player.getSpeed() * 60 +
				" MPS", (screenX /3 ) * 2, screenY - 20, paint);

	    } else {
		// Show pause screen
		paint.setTextSize(80);
		paint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText("Game Over", screenX/2, 100, paint);
		paint.setTextSize(25);
		canvas.drawText("Fastest:"+ 
				fastestTime + "s", screenX/2, 160, paint);
		canvas.drawText("Time:" + timeTaken + 
				"s", screenX / 2, 200, paint);
		canvas.drawText("Distance remaining:" + 
				distanceRemaining/1000 + " KM",screenX/2, 240, paint);
		paint.setTextSize(80);
		canvas.drawText("Tap to replay!", screenX/2, 350, paint);
	    }
	    
	    // Unlock and draw the scene
	    ourHolder.unlockCanvasAndPost(canvas);

	}

    }

    private void control() {
	
	try {
	    /*17 milliseconds = (1000(milliseconds)/60(FPS)) */
	    //gameThread.sleep(17);
	    gameThread.sleep(30);
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
