package group29.cse535.fall17.asu.edu.thoughtid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class AsyncForDB extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_for_db);
        new AsyncTaskRunner(AsyncForDB.this).execute();
    }
}
class AsyncTaskRunner extends AsyncTask<String, String, String> {
    private Context mycontext;
    private String resp;
    ProgressDialog dialog;
    public AsyncTaskRunner(Context context){
        this.mycontext = context;
    }
    @Override
    protected String doInBackground(String... params) {
        DBHelper dbHelper = new DBHelper(mycontext);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        dbHelper.importData(sqLiteDatabase);
        return resp;
    }


    @Override
    protected void onPostExecute(String result) {
        // execution of result of Long time consuming operation
        if(dialog.isShowing())
        dialog.dismiss();
        Intent mainActivity = new Intent(mycontext,MainActivity.class);
        mainActivity.putExtra("IS_INITIALIZED", "true");
        mycontext.startActivity(mainActivity);

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(mycontext);
        dialog.setMessage("Initializing the database, please wait");
        dialog.setCanceledOnTouchOutside(Boolean.FALSE);
        dialog.show();
    }



}

