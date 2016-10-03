package com.infogird.www.presenza;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import Data_Model.Subject_List_Data_Model;
import Data_Model.server_conf;
//CREATE TABLE `All_Subjects` ( `Sub_ID` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `Subject_Name` TEXT NOT NULL )

public class Add_Subject_Activity extends AppCompatActivity {

    Spinner sp_sub_type, sp_sub_class, sp_sub_batch,sp_Sub_list;
    Button btn_add_sub;

    SqlHandler sql_handler_obj;
    Cursor c,cursor_sub;
    public static ArrayList<Subject_List_Data_Model> sub_list_ar_list = new ArrayList<Subject_List_Data_Model>();

    String NAMESPACE;
    String URL;
    String METHOD_NAME;
    String SOAP_ACTION;
    server_conf sc;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__subject_);
        sp_sub_type = (Spinner)findViewById(R.id.sp_sub_type);
        sp_sub_class=(Spinner)findViewById(R.id.sp_class);
        sp_sub_batch = (Spinner)findViewById(R.id.sp_batch);
        btn_add_sub =(Button)findViewById(R.id.btn_done);
        sp_Sub_list = (Spinner) findViewById(R.id.sp_sub_name);

        sql_handler_obj=new SqlHandler(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        sc = new server_conf();
        NAMESPACE = sc.getNAMESPACE();
        URL = sc.getURL();
        sc.setMETHOD_NAME("Add_Subject_Operation");
        METHOD_NAME = sc.getMETHOD_NAME();
        SOAP_ACTION = sc.getSOAP_ACTION();

        List<String> list_Sub_type = new ArrayList<String>();
        list_Sub_type.add("Thoery");
        list_Sub_type.add("Practical");

        List<String> list_sub_batch = new ArrayList<String>();
        list_sub_batch.add("B1");
        list_sub_batch.add("B2");
        list_sub_batch.add("B3");
        list_sub_batch.add("B4");
        list_sub_batch.add("ALL");

        List<String> list_Class = new ArrayList<String>();
        list_Class.add("1");
        list_Class.add("2");
        list_Class.add("3");
        list_Class.add("4");

        List<String> list_Subjects = new ArrayList<String>();


        try {
            String Query_select_sub = "select Subject_Name from All_Subjects";

            cursor_sub = sql_handler_obj.selectQuery(Query_select_sub);
            cursor_sub.moveToFirst();
            if (cursor_sub.moveToFirst()) {
                do {
                    String Sub_Name = cursor_sub.getString(0);


                    list_Subjects.add(Sub_Name);


                    Log.i("Log","Show Data"+Sub_Name);

                } while (cursor_sub.moveToNext());
            }
            cursor_sub.close();
        }
        catch(Exception e){
            Log.i("Exception",e.toString());
        }

        // subject type spinner adapter
        ArrayAdapter<String> dataAdapter_sub_type = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list_Sub_type);

        dataAdapter_sub_type.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        sp_sub_type.setAdapter(dataAdapter_sub_type);

        // batch spinner adapter
        ArrayAdapter<String> dataAdapter_sub_batch = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list_sub_batch);

        dataAdapter_sub_batch.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        sp_sub_batch.setAdapter(dataAdapter_sub_batch);

        //class spinner adapter

        ArrayAdapter<String> dataAdapter_class = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list_Class);

        dataAdapter_class.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        sp_sub_class.setAdapter(dataAdapter_class);

        //subject spinner adapter

        ArrayAdapter<String> dataAdapter_subject = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list_Subjects);

        dataAdapter_subject.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        sp_Sub_list.setAdapter(dataAdapter_subject);
        //CREATE TABLE `All_Subjects` ( `Sub_ID` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `Subject_Name` TEXT NOT NULL )

        btn_add_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    btn_add_sub.requestFocus();
                    try {
                        String Query = "insert into Subject_Details" +
                                "(Sub_Name,Teacher_ID,Sub_Type,Sub_Batch,Sub_Class)"
                                + " values('" +sp_Sub_list.getSelectedItem().toString().trim()+ "','"
                                + 1 + "','"
                                + sp_sub_type.getSelectedItem().toString().trim()+ "','"
                                + sp_sub_batch.getSelectedItem().toString().trim() + "','"
                                + sp_sub_class.getSelectedItem().toString().trim()
                                + "')";
                        Log.i("Log", Query);
                        sql_handler_obj.executeQuery(Query);
                        Log.i("Log", "Insert Successful");
                        String Query_select = "select * from Subject_Details";
                        c = sql_handler_obj.selectQuery(Query_select);
                        c.moveToFirst();
                        showRecords();
                        c.close();
                    }
                    catch(Exception e){
                        Log.i("Exception",e.toString());
                    }

                add_to_server();

                Intent intent_home = new Intent(Add_Subject_Activity.this,Drawer_Activity.class);
               intent_home.putExtra("Detect_intent",true);
                startActivity(intent_home);

            }
        });
    }

    public void add_to_server(){

        SoapObject sp = new SoapObject(NAMESPACE, METHOD_NAME);
        sp.addProperty("Sub_Name",sp_Sub_list.getSelectedItem().toString().trim());
        sp.addProperty("Teacher_Uid",1);
        sp.addProperty("Sub_Type",sp_sub_type.getSelectedItem().toString().trim());
        sp.addProperty("Sub_Batch",sp_sub_batch.getSelectedItem().toString().trim());
        sp.addProperty("Sub_Class",sp_sub_class.getSelectedItem().toString().trim());

        SoapSerializationEnvelope ev = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        ev.setOutputSoapObject(sp);
        HttpTransportSE http = new HttpTransportSE(URL);

        try {

            http.call(SOAP_ACTION, ev);
            SoapPrimitive s = (SoapPrimitive) ev.getResponse();
            String response = s.toString();
            Log.i("response",response);

            if(response.equals("1")){
                Log.i("Subject_Add","Subject Add successful");
                Intent int_home = new Intent(this,Drawer_Activity.class);
                this.finish();
                startActivity(int_home);
            }
            else if(response.equals("0")){
                Log.i("Subject_Add","Subject Add failed");
            }
        }
        catch (Exception e){
            Log.i("exception",e.toString());
            new AlertDialog.Builder(this)
                    .setTitle("Subject Add to Server Failed")
                    .setMessage("Please Try Again")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }
    protected void showRecords() {

        int sub_id = c.getInt(0);
        String sub_name = c.getString(1);
        int teacher_id = c.getInt(2);
        String theory_or_practical = c.getString(3);
        String Batch = c.getString(4);
        int Class = c.getInt(5);

        Log.i("Log","Show Data"+sub_id+"\n"+sub_name+"\n"+teacher_id+"\n"+theory_or_practical+"\n"
                +Batch+"\n"+Class);
    }
}
