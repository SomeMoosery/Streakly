package carter.streakly;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Random;
import java.util.UUID;

public class MainActivity extends AppCompatActivity{

    private DatabaseHelper db;
    private String addStreakName, addCategory, userID;
    private int streakCounter;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private EditText chooseDate;
    private Button closeAddDialog, healthButton,
            mentalButton, personalButton, professionalButton, socialButton, submitStreakButton;
    private ImageButton profileButton, allStreaks, addStreak;
    private Dialog pickDialog;
    private TextView categorySelector, quoteView;

    private int backgroundCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DatabaseHelper(this);

        /*
            Creates shared preferences and editor
         */
        prefs = getSharedPreferences("carter.streakly", MODE_PRIVATE);
        editor = prefs.edit();

        // Checks if it's the first time the user opens the app. If so, generates a unique user ID and stores it in shared prefs
        if (prefs.getBoolean("firstTime", true)){
            userID = UUID.randomUUID().toString();
            editor.putString("user", userID);
            editor.putBoolean("firstTime", false);
            editor.commit();
        }

        allStreaks = (ImageButton) findViewById(R.id.all_streaks_button);
        addStreak = (ImageButton) findViewById(R.id.add_streak_button);
        profileButton = (ImageButton) findViewById(R.id.profile_button);

        quoteView = (TextView) findViewById(R.id.quote_text);
        String[] quoteList = getResources().getStringArray(R.array.quote_list);
        Random rand = new Random();
        quoteView.setText(quoteList[rand.nextInt(7)]);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //profileButton.startAnimation(shake);
                Intent intent = new Intent(MainActivity.this, Profile.class);
                startActivity(intent);
            }
        });

        //Brings user to the All Streaks activity, and passes the LinkedList<Streak> streakList
        allStreaks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AllStreaks.class);
                startActivity(intent);
            }
        });

        //Shows an Alert Dialog that allows users to enter in the type of streak they want
        addStreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCustomDialog();
            }
        });
    }

    private void createCustomDialog(){
        pickDialog = new Dialog(MainActivity.this);
        pickDialog.setContentView(R.layout.dialog_add_streak);
        pickDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final EditText chooseName = (EditText) pickDialog.findViewById(R.id.dialog_acitivty_name);
        healthButton = (Button) pickDialog.findViewById(R.id.dialog_health);
        mentalButton = (Button) pickDialog.findViewById(R.id.dialog_mental);
        personalButton = (Button) pickDialog.findViewById(R.id.dialog_personal);
        professionalButton = (Button) pickDialog.findViewById(R.id.dialog_professional);
        socialButton = (Button) pickDialog.findViewById(R.id.dialog_social);
        categorySelector = (TextView) pickDialog.findViewById(R.id.selected_streak);
        submitStreakButton = (Button) pickDialog.findViewById(R.id.dialog_submit_button);
        chooseDate = (EditText) pickDialog.findViewById(R.id.dialog_input_date);
        closeAddDialog = (Button) pickDialog.findViewById(R.id.close_add_dialog);

        pickDialog.show();

        categorySelector.setText("Health");

        healthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categorySelector.setText("Health");
                editor.putInt("AddCategory", 0).commit();
                editor.putString("Category", "Health");
                editor.commit();
                recolorCategory();
            }
        });

        mentalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categorySelector.setText("Mental");
                editor.putInt("AddCategory", 1).commit();
                editor.putString("Category", "Mental");
                editor.commit();
                recolorCategory();
            }
        });

        personalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categorySelector.setText("Personal");
                editor.putInt("AddCategory", 2).commit();
                editor.putString("Category", "Personal");
                editor.commit();
                recolorCategory();
            }
        });

        professionalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categorySelector.setText("Professional");
                editor.putInt("AddCategory", 3).commit();
                editor.putString("Category", "Professional");
                editor.commit();
                recolorCategory();
            }
        });

        socialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categorySelector.setText("Social");
                editor.putInt("AddCategory", 4).commit();
                editor.putString("Category", "Social");
                editor.commit();
                recolorCategory();
            }
        });

        chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseDate.setTextColor(Color.rgb(51,51,255));
            }
        });

        closeAddDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDialog.dismiss();
            }
        });

        submitStreakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                    If the user selected today's date, enter the days kept as 0. If the user selected how long they've kept the streak for, enter the days kept as chooseDate
                 */
                addStreakName = chooseName.getText().toString();
                addCategory = prefs.getString("Category", "");

                Calendar cal = Calendar.getInstance();
                long startTime = cal.getTimeInMillis();

                if (chooseDate.getText().toString().equals(null) || chooseDate.getText().toString().equals("")){
                    boolean isInserted = db.insertData(addStreakName, addCategory, 1, startTime, 1, startTime);
                    if(isInserted){
                        Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                        editor.putInt("FinalStreakCount", prefs.getInt("FinalStreakCount", 0) + 1).commit();
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                        editor.putInt("FinalStreakCount", prefs.getInt("FinalStreakCount", 0) + 1).commit();
                    }
                }
                else {
                    boolean isInserted = db.insertData(addStreakName, addCategory, Integer.parseInt(chooseDate.getText().toString()), cal.getTimeInMillis(), 1, startTime);
                    if(isInserted){
                        Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                    }
                }

                streakCounter++;
                editor.putInt("streakCounter", streakCounter).commit();

                pickDialog.dismiss();
            }
        });
    }

    /*
        Highlights which category is currently chosen
     */
    private void recolorCategory(){
        Button[] categoryList = {healthButton, mentalButton, personalButton, professionalButton, socialButton};
        int recolorIndex = prefs.getInt("AddCategory", 0);
        categoryList[recolorIndex].setTextColor(Color.rgb(51,51,255));
        for (int i = 0; i < 5; i++){
            if (i != recolorIndex) categoryList[i].setTextColor(Color.rgb(153, 255, 102));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
