package a.b.project;

import android.app.Activity;
import android.os.Bundle;

public class MyActivity extends Activity{

    // This is the entry point to our game
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	//Here we set our UI layout as the view
	setContentView(R.layout.main);

    }
}
