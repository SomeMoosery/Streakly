package carter.streakly;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class EnlargedActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private DatabaseHelper db;
    private Cursor res;
    private TextView streakIcon, countdown;
    private ArrayList<Streak> streakArrayList;
    private RelativeLayout editBackground;
    private Button editButton, backButton;
    private Calendar cal;

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateGUI(intent);
        }
    };

    public static EnlargedActivity enlargedActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enlarged);
        prefs = getSharedPreferences("carter.streakly", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void onResume(){
        super.onResume();
        createEnlargedActivity();
    }

    public void updateGUI(Intent intent){
        if(intent.getExtras() != null){
            long millisUntilFinished = intent.getLongExtra("countdown", 0);
        }
    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void createEnlargedActivity(){
        db = new DatabaseHelper(this);
        res = db.getAllData();
        if (res.getCount() == 0) {
            //show message
            showMessage("Error", "Nothing found");
            return;
        }

        cal = Calendar.getInstance();

        enlargedActivity = this;

        int passName = prefs.getInt("passName", 0);

        streakArrayList = new ArrayList<>();
        streakIcon = new EditText(this);
        editButton = (Button) findViewById(R.id.edit_enlarge_button);
        editBackground = (RelativeLayout) findViewById(R.id.edit_background);
        backButton = (Button) findViewById(R.id.back_to_all_streaks_button);

        streakIcon = (TextView) findViewById(R.id.activity_enlarge_icon);
        countdown = (TextView) findViewById(R.id.countdown);

        streakIcon.setGravity(Gravity.CENTER);
        streakIcon.setTextSize(30);

        streakIcon.setCursorVisible(false);

        while (res.moveToNext()){
            streakArrayList.add(new Streak(Integer.parseInt(res.getString(0)), res.getString(1), res.getString(2), res.getString(3), Integer.parseInt(res.getString(4)), Long.parseLong(res.getString(5)), Integer.parseInt(res.getString(6)), Long.parseLong(res.getString(7))));
        }

        final String name = prefs.getString("currButtonActivityName", "");
        final String category = prefs.getString("currButtonActivityCategory", "");
        final int daysKept = prefs.getInt("currButtonDaysKept", 0);
        long passStartTime = prefs.getLong("currButtonStartTime", 0);
        int passIsGoing = prefs.getInt("currButtonIsGoing", 0);

        if (passIsGoing == 1){
            long currTime = cal.getTimeInMillis();
            long startTime = passStartTime;
            final long timeLeft = currTime-startTime;
            if(timeLeft<86400000){
                long passTimeLeft = 86400000-timeLeft;
                new CountDownTimer(passTimeLeft, 1000){
                    public void onTick(long millisUntilFinished){
                        countdown.setText("You have " + String.format("%02d:%02d:%02d",
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))) + " to keep this streak!");
                    }
                    public void onFinish(){
                        countdown.setText("Not currently going");
                        db.updateData(name, name, category, 0,0,0,0);
                    }
                }.start();
            }
        }

        if(category == null){
            editBackground.setBackgroundColor(getResources().getColor(R.color.white));
        }
        else if (category.equals("Health")) {
            editBackground.setBackgroundColor(getResources().getColor(R.color.healthColor));
        }
        else if (category.equals("Mental")) {
            editBackground.setBackgroundColor(getResources().getColor(R.color.mentalColor));
        }
        else if (category.equals("Personal")) {
            editBackground.setBackgroundColor(getResources().getColor(R.color.personalColor));
        }
        else if (category.equals("Professional")) {
            editBackground.setBackgroundColor(getResources().getColor(R.color.professionalColor));
        }
        else if (category.equals("Social")) {
            editBackground.setBackgroundColor(getResources().getColor(R.color.socialColor));
        }
        else{
            editBackground.setBackgroundColor(getResources().getColor(R.color.white));
        }

        streakIcon.setText(prefs.getString("currButtonActivityName", "") + "\n" + "\n" + "Days kept:" + prefs.getInt("currButtonDaysKept", 0));

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EnlargedActivity.this, EditStreak.class);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.remove("currButtonID").commit();
                editor.remove("currButtonActivityName").commit();
                editor.remove("currButtonActivityCategory").commit();
                editor.remove("currButtonDaysKept").commit();
                Intent intent = new Intent(EnlargedActivity.this, AllStreaks.class);
                startActivity(intent);
            }
        });
    }

}