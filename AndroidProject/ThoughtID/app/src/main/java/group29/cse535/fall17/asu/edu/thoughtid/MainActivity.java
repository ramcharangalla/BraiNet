package group29.cse535.fall17.asu.edu.thoughtid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Switch expertSwitch = null;
    String isAdaptive = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        final TextView userNameTextView = (TextView) findViewById(R.id.username);
        final TextView userId2 = (TextView) findViewById(R.id.userid2);
        final TextView fogServerAddress = (TextView) findViewById(R.id.fogServerAddress);
        Button proceedButton = (Button)findViewById(R.id.proceed_button);
        final RadioGroup serverRadioGroup = (RadioGroup) findViewById(R.id.serverRadioGroup);
        final RadioButton cloudRadioButton = (RadioButton) findViewById(R.id.cloud);
        expertSwitch =(Switch) findViewById(R.id.expertModeSwitch);
        expertSwitch.setText("Expert Mode Off");
        super.onCreate(savedInstanceState);
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cloudRadioButton.setChecked(Boolean.TRUE);
                Toast.makeText(getApplicationContext(),userNameTextView.getText()+" trying to login ",Toast.LENGTH_SHORT).show();
                Intent startVideoActivity = new Intent(MainActivity.this,Main3Activity.class);
                startVideoActivity.putExtra("userid", userNameTextView.getText().toString());
                if(null!=userId2 && null!=userId2.getText() && null!=userId2.getText().toString()){
                    startVideoActivity.putExtra("userid2", userId2.getText().toString());
                    Log.i("BrainId","userid2 "+userId2.getText().toString());
                }
                if(null!=isAdaptive && isAdaptive=="true"){
                    startVideoActivity.putExtra("isAdaptive", isAdaptive);
                }
                if(null!=fogServerAddress && null!=fogServerAddress.getText() && null!=fogServerAddress.getText().toString()){
                    startVideoActivity.putExtra("fogServerAddress", fogServerAddress.getText().toString());
                    Log.i("BrainId","fogserverAddress "+fogServerAddress.getText().toString());
                }

                startActivity(startVideoActivity);
            }
        });
        if(getIntent().getStringExtra("IS_INITIALIZED")==null){
            Intent dbInitialization = new Intent(getApplicationContext(),AsyncForDB.class);
            startActivity(dbInitialization);
        }

        expertSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if(isChecked){
                    //Toast.makeText(getApplicationContext(),userNameTextView.getText()+" Switching to expert mode ",Toast.LENGTH_SHORT).show();
                    expertSwitch.setText("Expert Mode On");
                    userId2.setVisibility(View.VISIBLE);
                    serverRadioGroup.setVisibility(View.VISIBLE);
                    serverRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            Log.d("chk", "id" + checkedId);
                            if (checkedId == R.id.fog){
                                fogServerAddress.setVisibility(View.VISIBLE);
                                isAdaptive=null;
                            }else if(checkedId == R.id.adaptive) {
                                fogServerAddress.setVisibility(View.VISIBLE);
                                isAdaptive="true";
                            }

                            else{
                                fogServerAddress.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
                else{
                    //Toast.makeText(getApplicationContext(),userNameTextView.getText()+" Switching to normal mode ",Toast.LENGTH_SHORT).show();
                    expertSwitch.setText("Expert Mode Off");
                    userId2.setVisibility(View.INVISIBLE);
                    serverRadioGroup.setVisibility(View.INVISIBLE);
                    fogServerAddress.setVisibility(View.INVISIBLE);
                }
            }
        });
        //initialDBImportActivity.execute();
    }
}
