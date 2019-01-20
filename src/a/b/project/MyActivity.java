package a.b.project;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.app.Activity;
import android.content.Intent;

public class MyActivity extends Activity implements View.OnClickListener {

    // This is the entry point to our game
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	//Here we set our UI layout as the view
	setContentView(R.layout.main);

	// Get a reference to the button in our layout
	final Button buttonPlay = (Button)findViewById(R.id.buttonPlay);
	// Listen for clicks
	buttonPlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
	// must be the Play button.
	// Create a new Intent object
	Intent i = new Intent(this, GameActivity.class);
	// Start our GameActivity class via the Intent
	startActivity(i);
	// Now shut this activity down
	finish();
	
    }
    
}
