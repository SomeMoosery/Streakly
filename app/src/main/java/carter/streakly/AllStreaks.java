package carter.streakly;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class AllStreaks extends AppCompatActivity {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private TableLayout mTableLayout;
    private TableRow mTableRow;
    private int i;
    private LinearLayout ll;
    private ArrayList<Streak> streakArrayList;
    private DatabaseHelper db;
    private Cursor res;
    private Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_streaks);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefs = getSharedPreferences("carter.streakly", MODE_PRIVATE);
        editor = prefs.edit();
        createAllStreaks();
    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(AllStreaks.this, MainActivity.class);
        startActivity(intent);
        /*
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Use other Buttons");
        builder.setMessage("Please use the on-screen back buttons");
        builder.show();
        */
    }

    public void onResume(){
        super.onResume();
        //createAllStreaks();
    }

    /*
    So here I'm basically just eliminating the pause functionality from this activity. It's probably not the best way to go about it,
        but fuck it lol.
     */
    public void onPause(){
        super.onPause();
    }

    private void createAllStreaks(){
        db = new DatabaseHelper(this);
        mTableLayout = (TableLayout) findViewById(R.id.all_streak_table);
        res = db.getAllData();
        if (res.getCount() == 0) {
            AlertDialog.Builder returnDiag = new AlertDialog.Builder(this);
            returnDiag.setTitle("Empty");
            returnDiag.setMessage("Go add some streaks, then come here!");
            returnDiag.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(AllStreaks.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            returnDiag.show();
        }

        streakArrayList = new ArrayList<>();

        int counter = 0;
        while (res.moveToNext()) {
            streakArrayList.add(new Streak(Integer.parseInt(res.getString(0)), res.getString(1), res.getString(2), res.getString(3), Integer.parseInt(res.getString(4)), Long.parseLong(res.getString(5)), Integer.parseInt(res.getString(6)), Long.parseLong(res.getString(7))));
            counter++;
        }

        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(200, 200);
        btnParams.setMargins(200, 30, 80, 30);

        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        tvParams.setMargins(100, 0, 0, 0);

        try{
            int startingPoint = prefs.getInt("idCount", 0);
        }catch(NullPointerException e){
            editor.putInt("idCount", 0);
        }
        i = 0;
        while (i < res.getCount()) {
            if (i % 2 == 0) {
                mTableRow = new TableRow(this);
                mTableLayout.addView(mTableRow);
            }

            ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.VERTICAL);
            mTableRow.addView(ll);

            final Button btn = new Button(this);
            //btn.setText("" + streakArrayList.get(i).getDaysKept());
            btn.setId(i);

            if(streakArrayList.get(i).getActivityCategory() == null){
                btn.setBackground(getResources().getDrawable(R.drawable.round_button));
            }
            else if (streakArrayList.get(i).getActivityCategory().equals("Health")) {
                btn.setBackground(getResources().getDrawable(R.drawable.category_health_bubble));
            }
            else if (streakArrayList.get(i).getActivityCategory().equals("Mental")) {
                btn.setBackground(getResources().getDrawable(R.drawable.category_mental_button));
            }
            else if (streakArrayList.get(i).getActivityCategory().equals("Personal")) {
                btn.setBackground(getResources().getDrawable(R.drawable.category_personal_bubble));
            }
            else if (streakArrayList.get(i).getActivityCategory().equals("Professional")) {
                btn.setBackground(getResources().getDrawable(R.drawable.category_professional_bubble));
            }
            else if (streakArrayList.get(i).getActivityCategory().equals("Social")) {
                btn.setBackground(getResources().getDrawable(R.drawable.category_social_bubble));
            }
            else{
                btn.setBackground(getResources().getDrawable(R.drawable.round_button));
            }

            btn.setLayoutParams(btnParams);
            btn.setText(""+0);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final Dialog dialog = new Dialog(AllStreaks.this);
                    dialog.setContentView(R.layout.dialog_complete_or_view);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

                    final Button yesButton = (Button) dialog.findViewById(R.id.yes_completed);
                    final Button viewButton = (Button) dialog.findViewById(R.id.view_streak);
                    final Timer timer = new Timer();

                    yesButton.setText("Did it!");

                    if (streakArrayList.get(btn.getId()).getCheckedTime() != 0){
                        yesButton.setClickable(false);
                        long checkedStartTime = streakArrayList.get(btn.getId()).getCheckedTime();
                        long checkedCurrTime = cal.getTimeInMillis();
                        long checkedTimeLeft = checkedCurrTime - checkedStartTime;
                        long passTimeLeft = 28800000 - checkedTimeLeft;
                        new CountDownTimer(passTimeLeft, 1000){
                            public void onTick(long millisUntilFinished){
                                yesButton.setText(String.format("%02d:%02d:%02d",
                                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                            }
                            public void onFinish(){
                                //db.updateDataCheckedTime(streakArrayList.get(btn.getId()).getActivityName(), 0);
                                changeDidItButton(yesButton);
                            }
                        }.start();
                    }
                    else{
                        yesButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String passNameString = Integer.toString(btn.getId());
                                String activityName = streakArrayList.get(btn.getId()).getActivityName();
                                String activityCategory = streakArrayList.get(btn.getId()).getActivityCategory();
                                int newNum = (streakArrayList.get(btn.getId()).getDaysKept()) + 1;
                                long startTime = streakArrayList.get(btn.getId()).getStartTime();
                                int isGoing = streakArrayList.get(btn.getId()).getIsGoing();

                                cal = Calendar.getInstance();
                                boolean isUpdated = db.updateData(activityName, activityName, activityCategory, newNum, cal.getTimeInMillis(), 1, cal.getTimeInMillis());
                                if (isUpdated == true){
                                    finish();
                                    startActivity(getIntent());
                                    dialog.dismiss();
                                }
                                db.updateDataCheckedTime(streakArrayList.get(btn.getId()).getActivityName(), cal.getTimeInMillis());
                            }
                        });
                    }

                    viewButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            editor.putInt("currButtonID", btn.getId()+1).commit();
                            editor.putString("currButtonActivityName", streakArrayList.get(btn.getId()).getActivityName()).commit();
                            editor.putString("currButtonActivityCategory", streakArrayList.get(btn.getId()).getActivityCategory()).commit();
                            editor.putInt("currButtonDaysKept", streakArrayList.get(btn.getId()).getDaysKept()).commit();
                            editor.putLong("currButtonStartTime", streakArrayList.get(btn.getId()).getStartTime()).commit();
                            editor.putInt("currButtonIsGoing", streakArrayList.get(btn.getId()).getIsGoing()).commit();
                            Log.d("carter.streakly", btn.getId() + "" + prefs.getInt("currButtonID", 999) + prefs.getString("currButtonActivityName", "") + prefs.getString("currButtonActivityCategory", "") +
                                    prefs.getInt("currButtonDaysKept", 999));

                            Intent intent = new Intent(AllStreaks.this, EnlargedActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            });

            ll.addView(btn);

            TextView tv = new TextView(this);
            tv.setText(streakArrayList.get(btn.getId()).getActivityName());
            tv.setId(i);
            tv.setGravity(Gravity.CENTER | Gravity.BOTTOM);
            tv.setTextSize(20);
            tv.setLayoutParams(tvParams);
            ll.addView(tv);

            int isGoing = streakArrayList.get(btn.getId()).getIsGoing();
            cal = Calendar.getInstance();
            long currTime = cal.getTimeInMillis();
            long starttime = streakArrayList.get(btn.getId()).getStartTime();
            if (isGoing == 1){
                if (currTime-starttime < 86400000){
                    btn.setText("" + streakArrayList.get(i).getDaysKept());
                }
                else{
                    btn.setText(""+0);
                    db.updateData(tv.getText().toString(), tv.getText().toString(), streakArrayList.get(btn.getId()).getActivityCategory(), 0, 0, 0, 0);
                }
            }
            i++;
        }
    }

    private void changeDidItButton(Button button){
        button.setClickable(true);
        button.setText("Did it!");
    }
}
