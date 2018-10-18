package group29.cse535.fall17.asu.edu.thoughtid;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by sgollana on 11/25/2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ProcessedData";
    public Context context;
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
    private static final String CREATE_TABLE_COLUMNS =" ( id integer primary key,delta_mean text,delta_std text,delta_entropy text,theta_mean text,theta_std text,theta_entropy text,alpha_mean text,alpha_std text,alpha_entropy text,beta_mean text,beta_std text,beta_entropy text,gamma_mean text,gamma_std text,gamma_entropy text,userId text);";

    public DBHelper(Context mycontext) {
        super(mycontext, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        this.context = mycontext;
        //if(null!=context)
        //onCreate(sqLiteDatabase);
    }


    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        ArrayList<String> listOfUsers = new ArrayList<String>();
        listOfUsers.add("user1");
        listOfUsers.add("user2");
        listOfUsers.add("user3");
        listOfUsers.add("user4");
        listOfUsers.add("user5");
        listOfUsers.add("user6");
        listOfUsers.add("user7");
        listOfUsers.add("user8");
        listOfUsers.add("user9");
        listOfUsers.add("user10");
        for (String currentUser : listOfUsers) {
            Log.i("BrainDB", CREATE_TABLE + currentUser + CREATE_TABLE_COLUMNS);
            database.execSQL(CREATE_TABLE + currentUser + CREATE_TABLE_COLUMNS);
            //Log.i("BRain",""+getProfilesCount(database));
        }

        //Log.i("Database count",String.valueOf(getProfilesCount()));
    }
    public void importData(SQLiteDatabase database){
        Log.i("The count",""+getProfilesCount(database));
        if(getProfilesCount(database)<1000) {
            HashMap<String, String> userToFileMap = new HashMap<String, String>();
            userToFileMap.put("user1", "processedDataUser1.csv");
            userToFileMap.put("user2", "processedDataUser2.csv");
            userToFileMap.put("user3", "processedDataUser3.csv");
            userToFileMap.put("user4", "processedDataUser4.csv");
            userToFileMap.put("user5", "processedDataUser5.csv");
            userToFileMap.put("user6", "processedDataUser6.csv");
            userToFileMap.put("user7", "processedDataUser7.csv");
            userToFileMap.put("user8", "processedDataUser8.csv");
            userToFileMap.put("user9", "processedDataUser9.csv");
            userToFileMap.put("user10", "processedDataUser10.csv");
            Iterator it = userToFileMap.entrySet().iterator();
            AssetManager assetManager = null;
            if (context != null) {
                assetManager = context.getAssets();
            }
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();

                InputStreamReader inputStream = null;
                try {
                    if (context != null)
                        inputStream = new InputStreamReader(assetManager.open(pair.getValue().toString()));

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BufferedReader reader = new BufferedReader(inputStream);
                try {
                    reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String line = "";
                String tableName = pair.getKey().toString();
                String columns = "delta_mean,delta_std,delta_entropy,theta_mean,theta_std,theta_entropy,alpha_mean,alpha_std,alpha_entropy,beta_mean,beta_std,beta_entropy,gamma_mean,gamma_std,gamma_entropy,userId";
                String str1 = "INSERT INTO " + tableName + " (" + columns + ") values(";
                String str2 = ");";

                database.beginTransaction();
                try {
                    while ((line = reader.readLine()) != null) {
                        StringBuilder sb = new StringBuilder(str1);
                        String[] str = line.split(",");
                        for (int i = 0; i < 15; i++) {
                            sb.append("'" + str[i] + "',");
                        }
                        sb.append("'" + str[15] + "')");
                        database.execSQL(sb.toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                database.setTransactionSuccessful();
                database.endTransaction();
                it.remove(); // avoids a ConcurrentModificationException
            }
        }
        else{
            Log.i("Brain id","Not initialized, as data is present");
        }
    }
    public long getProfilesCount(SQLiteDatabase db) {
        //SQLiteDatabase db = this.getReadableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(db, "user2");
        //db.close();
        return cnt;
    }

    // Method is called during an upgrade of the database,
    @Override
    public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion){
        Log.w(DBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS MyEmployees");
        onCreate(database);
    }

    public String getUserData(String userTableName) {
        String selectQuery = "SELECT  * FROM " + userTableName+" where id>10 LIMIT 800";
        Log.i("BrainID","Query: "+selectQuery);
        SQLiteDatabase db  = this.getReadableDatabase();
        Cursor cursor      = db.rawQuery(selectQuery, null);
        String[] data      = null;
        int i = 0;
        StringBuilder deltaMean = new StringBuilder();
        StringBuilder deltaStd = new StringBuilder();
        StringBuilder deltaEntropy = new StringBuilder();
        StringBuilder thetamean= new StringBuilder();
        StringBuilder thetastd= new StringBuilder();
        StringBuilder thetaentropy= new StringBuilder();
        StringBuilder alphamean= new StringBuilder();
        StringBuilder alphastd= new StringBuilder();
        StringBuilder alphaentropy= new StringBuilder();
        StringBuilder betamean= new StringBuilder();
        StringBuilder betastd= new StringBuilder();
        StringBuilder betaentropy= new StringBuilder();
        StringBuilder gammamean= new StringBuilder();
        StringBuilder gammastd= new StringBuilder();
        StringBuilder gammaentropy= new StringBuilder();
        if (cursor.moveToFirst()) {
            do {
                deltaMean=prepareString(String.valueOf(i),1,deltaMean,cursor);
                deltaStd=prepareString(String.valueOf(i),2,deltaStd,cursor);
                deltaEntropy=prepareString(String.valueOf(i),3,deltaEntropy,cursor);
                thetamean=prepareString(String.valueOf(i),4,thetamean,cursor);
                thetastd=prepareString(String.valueOf(i),5,thetastd,cursor);
                thetaentropy=prepareString(String.valueOf(i),6,thetaentropy,cursor);
                alphamean=prepareString(String.valueOf(i),7,alphamean,cursor);
                alphastd=prepareString(String.valueOf(i),8,alphastd,cursor);
                alphaentropy=prepareString(String.valueOf(i),9,alphaentropy,cursor);
                betamean=prepareString(String.valueOf(i),10,betamean,cursor);
                betastd=prepareString(String.valueOf(i),11,betastd,cursor);
                betaentropy=prepareString(String.valueOf(i),12,betaentropy,cursor);
                gammamean=prepareString(String.valueOf(i),13,gammamean,cursor);
                gammastd=prepareString(String.valueOf(i),14,gammastd,cursor);
                gammaentropy=prepareString(String.valueOf(i),15,gammaentropy,cursor);
                i++;
                //Log.i("DBHelper ",cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        deltaMean=prepareJsonString(deltaMean,"delta_mean");
        deltaStd=prepareJsonString(deltaStd,"delta_std");
        deltaEntropy=prepareJsonString(deltaEntropy,"delta_entropy");
        thetamean=prepareJsonString(thetamean,"theta_mean");
        thetastd=prepareJsonString(thetastd,"theta_std");
        thetaentropy=prepareJsonString(thetaentropy,"theta_entropy");
        alphamean=prepareJsonString(alphamean,"alpha_mean");
        alphastd=prepareJsonString(alphastd,"alpha_std");
        alphaentropy=prepareJsonString(alphaentropy,"alpha_entropy");
        betamean=prepareJsonString(betamean,"beta_mean");
        betastd=prepareJsonString(betastd,"beta_std");
        betaentropy=prepareJsonString(betaentropy,"beta_entropy");
        gammamean=prepareJsonString(gammamean,"gamma_mean");
        gammastd=prepareJsonString(gammastd,"gamma_std");
        gammaentropy=prepareJsonString(gammaentropy,"gamma_entropy");
        Log.i("TotalJsonRequest ",gammaentropy.toString());
        //wrtieFileOnInternalStorage(context,"requestjson.json",deltaMean.toString()+deltaStd.toString()+deltaEntropy.toString()+thetamean.toString()+thetastd.toString()+thetaentropy.toString()+alphamean.toString()+alphastd.toString()+alphaentropy.toString()+betamean.toString()+betastd.toString()+betaentropy.toString()+gammamean.toString()+gammastd.toString()+gammaentropy.toString());
        return deltaMean.toString()+deltaStd.toString()+deltaEntropy.toString()+thetamean.toString()+thetastd.toString()+thetaentropy.toString()+alphamean.toString()+alphastd.toString()+alphaentropy.toString()+betamean.toString()+betastd.toString()+betaentropy.toString()+gammamean.toString()+gammastd.toString()+gammaentropy.toString();
    }
    public StringBuilder prepareString(String i,int position,StringBuilder sb,Cursor cursor){
        sb.append("\""+String.valueOf(i)+"\"");
        sb.append(":");
        sb.append(String.valueOf(cursor.getString(position)));
        sb.append(",");
        return sb;
    }
    public StringBuilder prepareJsonString(StringBuilder sb,String tag){
        sb.setLength(sb.length() - 1);
        sb.insert(0,"\""+tag+"\""+":{");
        if(tag=="gamma_entropy"){
            sb.append("}");
        }
        else{
            sb.append("},");
        }
        return sb;
    }
    public void wrtieFileOnInternalStorage(Context mcoContext,String sFileName, String sBody){
        File file = new File(mcoContext.getFilesDir(),"mydir");
        if(!file.exists()){
            file.mkdir();
        }

        try{
            File gpxfile = new File(file, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();

        }catch (Exception e){

        }
    }
}
