package uk.co.metweather.metweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class SplashScreenActivity extends Activity {

   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);

       setContentView(R.layout.splash_screen);

       Handler handler = new Handler();

       // run a thread after 2 seconds to start the home screen
       handler.postDelayed(new Runnable() {

           @Override
           public void run() {

               // make sure we close the splash screen so the user won't come back when it presses back key
               finish();
               
               // start the home screen
               Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
               SplashScreenActivity.this.startActivity(intent);

           }

       }, 1500); // time in milliseconds until the run() method will be called

   }

}
