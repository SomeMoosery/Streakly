package carter.streakly;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class FindOutMore extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_out_more);

        //Button instagram = (Button) findViewById(R.id.instagram);
        //instagram.setVisibility(View.GONE);

        /*
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://instagram.com/streakly.app");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                intent.setPackage("com.instagram.android");

                try{
                    startActivity(intent);
                }catch(ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/streakly.app")));
                }
            }
        });
        */
    }
}
