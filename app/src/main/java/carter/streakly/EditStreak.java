package carter.streakly;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditStreak extends AppCompatActivity {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private DatabaseHelper db;
    private Cursor res;
    private EditText streakIcon;
    private Button doneButton, deleteStreakButton, healthButton, mentalButton, personalButton, professionalButton, socialButton;
    private TextView categoryIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_streak);

        prefs = getSharedPreferences("carter.streakly", Context.MODE_PRIVATE);
        editor = prefs.edit();

        db = new DatabaseHelper(this);
        res = db.getAllData();
        if (res.getCount() == 0) {
            //show message
            showMessage("Error", "Nothing found");
            return;
        }

        streakIcon = new EditText(this);
        doneButton = (Button) findViewById(R.id.edit_done_button);
        deleteStreakButton = (Button) findViewById(R.id.delete_streak_buton);

        healthButton = (Button) findViewById(R.id.dialog_health);
        mentalButton = (Button) findViewById(R.id.dialog_mental);
        personalButton = (Button) findViewById(R.id.dialog_personal);
        professionalButton = (Button) findViewById(R.id.dialog_professional);
        socialButton = (Button) findViewById(R.id.dialog_social);

        streakIcon = (EditText) findViewById(R.id.edit_streak_name);
        categoryIcon = (TextView) findViewById(R.id.category_selection);
        //streakDaysKept = (EditText) findViewById(R.id.edit_days_kept);
        streakIcon.setGravity(Gravity.CENTER);
        streakIcon.setTextSize(30);

        streakIcon.setText(prefs.getString("currButtonActivityName", ""));
        //streakDaysKept.setText(Integer.toString(prefs.getInt("currButtonDaysKept", 0)));
        categoryIcon.setText(prefs.getString("currButtonActivityCategory", ""));

        healthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryIcon.setText("Health");
            }
        });

        mentalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryIcon.setText("Mental");
            }
        });

        personalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryIcon.setText("Personal");
            }
        });

        professionalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryIcon.setText("Professional");
            }
        });

        socialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryIcon.setText("Social");
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String whichName = prefs.getString("currButtonActivityName", "");
                editor.putString("currButtonActivityName", streakIcon.getText().toString().trim()).commit();
                editor.putString("currButtonActivityCategory", categoryIcon.getText().toString().trim()).commit();
                //editor.putInt("currButtonDaysKept", Integer.parseInt(streakDaysKept.getText().toString().trim())).commit();
                String updateName = prefs.getString("currButtonActivityName", "").trim();
                String updateCategory = prefs.getString("currButtonActivityCategory", "").trim();
                int updateDaysKept = prefs.getInt("currButtonDaysKept", 0);
                boolean isUpdated = db.updateDataSimple(whichName, updateName, updateCategory);
                if (isUpdated == true) {
                    Intent intent = new Intent(EditStreak.this, EnlargedActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(EditStreak.this, "Data not Updated", Toast.LENGTH_LONG);
                }
            }
        });

        deleteStreakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(EditStreak.this);
                dialog.setContentView(R.layout.dialog_delete_streak);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                Button finalDeleteButton = (Button) dialog.findViewById(R.id.yes_delete);
                Button finalBackButton = (Button) dialog.findViewById(R.id.go_back);

                finalDeleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Integer deletedRows = db.deleteData(prefs.getString("currButtonActivityName", ""));
                        if (deletedRows > 0) {
                            Intent intent = new Intent(EditStreak.this, AllStreaks.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(EditStreak.this, "Data not Deleted", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                finalBackButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}