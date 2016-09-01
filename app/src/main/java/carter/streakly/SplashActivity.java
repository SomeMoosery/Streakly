package carter.streakly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences("carter.streakly", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        //editor.putBoolean("firstTimeWelcome", true).commit();

        if (prefs.getBoolean("firstTimeWelcome", true) == true){
            Intent intent = new Intent(this, WelcomeInfo.class);
            editor.putBoolean("firstTimeWelcome", false).commit(); //UN-COMMENT AFTER DONE TESTING
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
