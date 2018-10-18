package group29.cse535.fall17.asu.edu.thoughtid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Button continueButton = (Button)findViewById(R.id.continue_button);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startVideoActivity = new Intent(Main3Activity.this,Main2Activity.class);
                startVideoActivity.putExtra("userid", getIntent().getStringExtra("userid"));
                startVideoActivity.putExtra("userid2", getIntent().getStringExtra("userid2"));
                startVideoActivity.putExtra("isAdaptive", getIntent().getStringExtra("isAdaptive"));
                Log.i("From Main3Activity","userid2 "+getIntent().getStringExtra("userid2"));
                startVideoActivity.putExtra("fogServerAddress", getIntent().getStringExtra("fogServerAddress"));
                startActivity(startVideoActivity);
            }
        });
    }

}
