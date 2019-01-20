package a.b.project;

/*
The Context parameter that is passed into our constructor is a reference to the
current state of our application within the Android system that is held by our
GameActivity class. 
*/
import android.content.Context;
import android.view.SurfaceView;

/*TDView.java:6: error: TDView is not abstract and does not override abstract method run() in Runnable*/
public class TDView extends SurfaceView implements Runnable {
    volatile boolean playing; /*using the volatile keyword as it will be accessed from outside the thread and from within.*/
    public TDView(Context context) {
	super(context);
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

    }

    private void draw(){

    }

    private void control(){

    }
    
}
