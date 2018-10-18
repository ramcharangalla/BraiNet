package group29.cse535.fall17.asu.edu.thoughtid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginSuccessActivity extends AppCompatActivity {

    Button startover=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);
        startover= (Button) findViewById(R.id.startoverbtns);
        startover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startOver = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(startOver);
            }
        });
        TextView adaptation = (TextView) findViewById(R.id.adaptionSuccess);
        String adaption =getIntent().getStringExtra("adaptionResult");
        String fogLatency =getIntent().getStringExtra("fogLatency");
        String cloudLatency =getIntent().getStringExtra("cloudLatency");
        String batteryLevel =getIntent().getStringExtra("battery");
        String s = "";
        String l = "";
        String c = "";
        //c=c+getIntent().getStringExtra("fogComputation");
        if(adaption!=null && adaption.toString().contains("ec2")){
            s="Cloud";
            l=cloudLatency;
            c=getIntent().getStringExtra("cloudComputation");
        }
        if(adaption.toString().contains("10.")){
            s="Fog";
            l=fogLatency;
            c=getIntent().getStringExtra("fogComputation");
        }
        if(adaption!=null && fogLatency!=null){
            adaptation.setText("Server :"+s+"; The execution time is "+l+"ms. Battery level is "+batteryLevel+"%");
        }


    }
    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"Please press startover instead",Toast.LENGTH_SHORT).show();
    }
}
