package com.cofco;

import android.os.Bundle;
import android.app.Activity;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {
	private RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_bar);
        relativeLayout = (RelativeLayout) this.findViewById(R.id.layout_bottom_main);
    }
    
    
}
