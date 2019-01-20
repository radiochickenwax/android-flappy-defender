package a.b.project;

import android.app.Activity;
import android.os.Bundle;


public class GameActivity extends Activity  {

    // Our object to handle the View
    private TDView gameView;

    
    // This is where the "Play" button from HomeActivity sends us
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
    }

    // If the Activity is paused make sure to pause our thread
    @Override
    protected void onPause() {
	super.onPause();
	gameView.pause();
    }

    // If the Activity is resumed make sure to resume our thread
    @Override
    protected void onResume() {
	super.onResume();
	gameView.resume();
    }


    
}
