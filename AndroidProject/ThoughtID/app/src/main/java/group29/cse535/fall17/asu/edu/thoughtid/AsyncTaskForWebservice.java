package group29.cse535.fall17.asu.edu.thoughtid;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URL;

import group29.cse535.fall17.asu.edu.thoughtid.beans.AuthResponse;

import static android.content.Context.BATTERY_SERVICE;

class AsyncTaskForWebService extends AsyncTask<String, String, String> {
    private Context context;
    ProgressDialog dialog;
    AuthResponse authResponse=null;
    int batteryLevel=0;
    String serverAddress;
    String isAdaptive;
    Long cloundLatency=null;
    Long fogLatency=null;
    public AsyncTaskForWebService(){
        //set context variables if required
    }



    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage("Posting the data to the server");
        dialog.setCanceledOnTouchOutside(Boolean.FALSE);
        dialog.setCancelable(Boolean.FALSE);
        dialog.show();
    }
    @Override
    protected void onPostExecute(String result) {
        // execution of result of Long time consuming operation
        if(dialog.isShowing())
            dialog.dismiss();
        if(null!=authResponse) {
            Toast.makeText(context, "LSVM " + authResponse.getLinearSVMResult() + " LR " + authResponse.getLogisticRegressionResult() + " NB " + authResponse.getNaiveBayesResult(), Toast.LENGTH_LONG).show();
            Intent loginResult = null;
            if (authResponse.getNaiveBayesResult()) {
                loginResult = new Intent(context, LoginSuccessActivity.class);
            } else {
                loginResult = new Intent(context, LoginFailureActivity.class);
            }

                loginResult.putExtra("adaptionResult",serverAddress+"");
                loginResult.putExtra("cloudLatency",cloundLatency+"");
                loginResult.putExtra("fogLatency",fogLatency+"");
                loginResult.putExtra("fogComputation",810+"");
                loginResult.putExtra("cloudComputation",750+"");
                loginResult.putExtra("battery",batteryLevel+"");

            context.startActivity(loginResult);
        }
    }

    //mainActivity.putExtra("IS_INITIALIZED", "true");



    public AsyncTaskForWebService(Context myContext) {
        this.context = myContext;
    }

    @Override
    protected String doInBackground(String... params) {
        BatteryManager bm = (BatteryManager)context.getSystemService(BATTERY_SERVICE);
        batteryLevel = bm .getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        Log.i("Battery","batteryleve "+batteryLevel);
        String userid = params[0];
        String userid2 = params[1];
        String fogServerAddress = params[2];
        isAdaptive = params[3];
        String userIdFordb = "";
        serverAddress = "http://ec2-52-34-148-140.us-west-2.compute.amazonaws.com:5000/prediction/";
        InputStream inputStream = null;
        String result = "";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost=null;
        Log.i("BRAIN ID","fogserver"+fogServerAddress);
        Log.i("ASync TASK","USERID2 "+userid2);
        if(null!=isAdaptive){
            Log.i("isAdaptive",isAdaptive);}
        if(null!= isAdaptive && isAdaptive.length()>0){
            serverAddress = adaptToNetwork(serverAddress,fogServerAddress);
            Log.i("Brain id","The adaptation has selected "+serverAddress+" as server");
        }
        else {
            if (null != fogServerAddress && fogServerAddress != "" && fogServerAddress.length() != 0) {
                serverAddress = fogServerAddress;
                //fogLatency = findLatency(serverAddress);
                Log.i("BRAINId", "Selecting fogserver");
            } else {
                serverAddress = "http://ec2-52-34-148-140.us-west-2.compute.amazonaws.com:5000/prediction/";
                //cloundLatency=findLatency(serverAddress);
                Log.i("BRAINId", "Selecting cloud server");
            }
        }
        Log.i("Brainid","userid2 "+userid2);
        if(null!=userid2 && userid2!="" && userid2.length()!=0) {
            userIdFordb = userid2;
            Log.i("Brainid","Selecting userid2 for db");
        }
        else{
            userIdFordb = userid;
            Log.i("Brainid","Selecting userid for db");
        }
        httpPost = new HttpPost(serverAddress+userid);
        Log.i("Brainid","URL "+serverAddress+userid);
        StringEntity se = null;
        try {
            se = new StringEntity(getJsonDataFromDB(userIdFordb));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        httpPost.setEntity(se);

        // 7. Set some headers to inform server about the type of the content
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        // 8. Execute POST request to the given URL
        HttpResponse httpResponse = null;
        try {
            long beforeTime = System.currentTimeMillis();
            httpResponse = httpclient.execute(httpPost);
            long afterTime = System.currentTimeMillis();
            if(null==isAdaptive){
            fogLatency=cloundLatency=afterTime-beforeTime;}
        } catch (IOException e) {
            Toast.makeText(context,"Exception "+e.getMessage(),Toast.LENGTH_SHORT);
            e.printStackTrace();
        }

        // 9. receive response as inputStream
        try {
            inputStream = httpResponse.getEntity().getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 10. convert inputstream to string
        if(inputStream != null)
            try {
                result = convertInputStreamToString(inputStream);
                if(null!=result)
                    Log.i("BrainNet",result.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        else{
            result = "Did not work!";}
        authResponse = stringToJson(result);

       /* URL url = null;
        HttpPost httpPost = null;
        String response = "";
        try {
            url = new URL("http://ec2-52-34-148-140.us-west-2.compute.amazonaws.com:5000/prediction/1");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn = null;
        try {
            //conn.setRequestProperty("User-Agent","");
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);

        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("User-Agent","");
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        conn.setDoInput(true);
        conn.setDoOutput(true);

        OutputStream os = null;
        try {
            conn.connect();
            os = conn.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(
                    new OutputStreamWriter(os));
            writer.write(loadJSONFromAsset());
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="SomethingElse";

            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


*/
        //urlConn.setRequestProperty("Content-Type","application/json");
        return result;
    }

    private AuthResponse stringToJson(String result) {
        AuthResponse authResponse=null;
        try {
            JSONObject json = new JSONObject(result);
            JSONObject auth = new JSONObject(json.get("auth").toString());
            String userId = json.get("userID").toString();
            Log.i("Brain id ",userId);
            Log.i("BrainNet Linear SVM",auth.get("Linear SVM").toString());
            Log.i("BrainlogisticRegression",auth.get("logisticRegression").toString());
            Log.i("BrainNet-naiveBayes",auth.get("naiveBayes").toString());
            Log.i("BrainNet-json-parsing ",auth.toString());
            authResponse = new AuthResponse(Boolean.valueOf(auth.get("Linear SVM").toString()),Boolean.valueOf(auth.get("logisticRegression").toString()),Boolean.valueOf(auth.get("naiveBayes").toString()),userId);
            Log.i("BrainNet-auth",authResponse.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  authResponse;
    }

    public String getJsonDataFromDB(String useridfromMethod){
        DBHelper dbHelper = new DBHelper(context);
        String jsonRequest ="{ "+dbHelper.getUserData("user"+useridfromMethod)+" }";
        Log.i("user recived  ","user"+useridfromMethod);
        wrtieFileOnExternalStorage(context,"jsonRequest_"+System.currentTimeMillis()+".json",jsonRequest);
        return jsonRequest;
    }
    public void wrtieFileOnExternalStorage(Context mcoContext,String sFileName, String sBody) {
        final int REQUEST_WRITE_STORAGE = 112;
        boolean hasPermission = (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions((Activity) mcoContext,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WRITE_STORAGE);
        } else {
            // You are allowed to write external storage:
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ThoughtID_Files/";
            File storageDir = new File(path);
            if (!storageDir.exists() && !storageDir.mkdirs()) {
                // This should never happen - log handled exception!
            }
            //if(!file.exists()){file.mkdirs();}
        }
        if(checkExternalMedia()) {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/ThoughtID_Files/"+"/"+sFileName);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                //File gpxfile = new File(file, sFileName);
                FileWriter writer = new FileWriter(file);
                writer.append(sBody);
                writer.flush();
                writer.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = context.getAssets().open("sample.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer);
            Log.i("Information for",json);

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
    private Boolean checkExternalMedia(){
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // Can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Can't read or write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
        return mExternalStorageWriteable;
    }
    private long findLatency(String serverAddress){
        int timeOut = 3000;
        long BeforeTime = System.currentTimeMillis();
        //InetAddress in=null;
        Boolean isReachable;
        try {
            isReachable =  InetAddress.getByName(new URL(serverAddress).getHost()).isReachable(timeOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long AfterTime = System.currentTimeMillis();
        Long TimeDifference = AfterTime - BeforeTime;
        Log.i("Brain id"," Latency for "+serverAddress+" is "+TimeDifference.toString());
        //if(serverAddress.equals(""))
        return TimeDifference;
    }
    private String adaptToNetwork(String cloudServer,String fogServer){
        if(batteryLevel<25){
            return fogServer;
        }
        cloundLatency = findLatency(cloudServer);
        fogLatency = findLatency(fogServer);
        return  fogLatency>cloundLatency+760?cloudServer+810:fogServer;
    }
}