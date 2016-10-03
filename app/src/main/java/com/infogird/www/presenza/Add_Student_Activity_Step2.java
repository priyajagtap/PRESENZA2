package com.infogird.www.presenza;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import Data_Model.server_conf;

import static com.infogird.www.presenza.Add_Students_Step1_Activity.KEY_Academic_year;
import static com.infogird.www.presenza.Add_Students_Step1_Activity.KEY_Branch;
import static com.infogird.www.presenza.Add_Students_Step1_Activity.KEY_class;
import static com.infogird.www.presenza.Add_Students_Step1_Activity.KEY_division;


public class Add_Student_Activity_Step2 extends AppCompatActivity {

    Cursor c;
    EditText tx_name,tx_rollno,tx_uid;
    Spinner sp_batch;
    Button btn_next,btn_save,btn_finish;

    String Aca_year,branch,classs,division;

    SqlHandler sql_handler_obj;
    int s_name_status =0, s_uid_status = 0, s_rollno_status =0;

    String NAMESPACE;
    String URL;
    String METHOD_NAME;
    String SOAP_ACTION;
    server_conf sc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_add__student__activity__step2);

        tx_name = (EditText)findViewById(R.id.tx_name);
        tx_rollno = (EditText)findViewById(R.id.tx_rollno);
        tx_uid = (EditText)findViewById(R.id.tx_uid);
        sp_batch =(Spinner)findViewById(R.id.sp_batch);
        btn_save = (Button)findViewById(R.id.bt_save);
        btn_next = (Button)findViewById(R.id.bt_next);
        btn_finish = (Button)findViewById(R.id.bt_submit);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        sc = new server_conf();
        NAMESPACE = sc.getNAMESPACE();
        URL = sc.getURL();
        sc.setMETHOD_NAME("Add_Student_Operation");
        METHOD_NAME = sc.getMETHOD_NAME();
        SOAP_ACTION = sc.getSOAP_ACTION();

        sql_handler_obj=new SqlHandler(getApplicationContext());

        Intent intent = getIntent();
        if (null != intent) {
            Aca_year = intent.getStringExtra(KEY_Academic_year).trim();
            branch = intent.getStringExtra(KEY_Branch).trim();
            classs = intent.getStringExtra(KEY_class).trim();
            division = intent.getStringExtra(KEY_division).trim();
        }

        Log.i("Values on step2",Aca_year+branch+classs+division);

        List<String> list_batch = new ArrayList<String>();
        list_batch.add("B1");
        list_batch.add("B2");
        list_batch.add("B3");
        list_batch.add("B4");


        //batch spinner adapter

        ArrayAdapter<String> dataAdapter_batch = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list_batch);

        dataAdapter_batch.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        sp_batch.setAdapter(dataAdapter_batch);
        // Spinner item selection Listener
        addListenerOnSpinnerItemSelection();

        btn_save.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        btn_save.requestFocus();
        validation();
        // Button click Listener
        if(s_rollno_status ==1 && s_uid_status ==1 && s_name_status ==1) {
            add_to_sql_db();
            addListenerOnButton();
        }
        else{
            Log.i("Status","status is not true");
        }
    }
    });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_next.requestFocus();
                tx_name.setText("");
                tx_uid.setText("");
                tx_rollno.setText("");
            }
        });

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_finish.requestFocus();
                Intent it = new Intent(v.getContext(),Drawer_Activity.class);
                startActivity(it);
                // write code to finish this activity
            }
        });

    }
    // Add spinner data

    public void addListenerOnSpinnerItemSelection(){
        sp_batch.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }


    public void validation(){

        if (tx_name.getText().toString().trim().equals("")){
            tx_name.setError("Please Enter Student Name");
            tx_name.requestFocus();
        }
        else{
            s_name_status =1;
        }
        if (tx_rollno.getText().toString().trim().equals("")){
            tx_rollno.setError("Please Enter Student Roll No");
            tx_rollno.requestFocus();
        }
        else{
            s_rollno_status =1;
        }
        if (tx_uid.getText().toString().trim().equals("")){
            tx_uid.setError("Please Enter Student UID");
            tx_uid.requestFocus();
        }
        else{
            s_uid_status =1;
        }
    }

    //get the selected dropdown list value

    public void addListenerOnButton() {
        try {
            String Query = "insert into student_details" +
                    "(unique_id,stud_name,roll_no,branch,class_year,academic_year,division,pract_batch)"
                    + " values('" + tx_uid.getText().toString().trim() + "','"
                    + tx_name.getText().toString().trim() + "','"
                    + tx_rollno.getText()+ "','"
                    + branch + "','"
                    + classs + "','"
                    + Aca_year + "','"
                    + division + "','"
                    + sp_batch.getSelectedItem().toString().trim()
                    + "')";
            Log.i("Log", Query);
            sql_handler_obj.executeQuery(Query);
            Log.i("Log", "Insert Successful");
            String Query_select = "select * from student_details";
            c = sql_handler_obj.selectQuery(Query_select);
            c.moveToFirst();
            showRecords();
            c.close();
        }
        catch(Exception e){
            Log.i("Exception",e.toString());
        }

        }
    public void add_to_sql_db(){

        SoapObject sp = new SoapObject(NAMESPACE, METHOD_NAME);
        sp.addProperty("st_uid",tx_uid.getText().toString().trim());
        sp.addProperty("st_name", tx_name.getText().toString().trim());
        sp.addProperty("st_rollno",tx_rollno.getText().toString().trim());
        sp.addProperty("st_branch",branch);
        sp.addProperty("st_study_year", classs);
        sp.addProperty("st_aca_year",Aca_year);
        sp.addProperty("st_div", division);
        sp.addProperty("st_pract_batch",sp_batch.getSelectedItem().toString().trim());

        SoapSerializationEnvelope ev = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        ev.setOutputSoapObject(sp);
        HttpTransportSE http = new HttpTransportSE(URL);

        try {

            http.call(SOAP_ACTION, ev);
            SoapPrimitive s = (SoapPrimitive) ev.getResponse();
            String response = s.toString();
            Log.i("response",response);

            if(response.equals("1")){
                Log.i("Student_Add","Student Add successful");
                Intent int_home = new Intent(this,Drawer_Activity.class);
                this.finish();
                startActivity(int_home);
            }
            else if(response.equals("0")){
                Log.i("Student_Add","Student Add failed");
            }
        }
        catch (Exception e){
            Log.i("exception",e.toString());
            new AlertDialog.Builder(this)
                    .setTitle("Student Add Failed to Server")
                    .setMessage("Please Try Again")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                               /* .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })*/
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }
//    public static ArrayList<Student_Details_Data_Model> stud_ar_list = new ArrayList<Student_Details_Data_Model>();;
    protected void showRecords() {

        String unique_id = c.getString(0);
        String stud_name = c.getString(1);
        int roll_no = c.getInt(2);
        String branch = c.getString(3);
        String class_year = c.getString(4);
        String academic_year = c.getString(5);
        String division = c.getString(6);
        String pract_batch = c.getString(7);

//        stud_ar_list.add(new Student_Details_Data_Model(unique_id,stud_name,roll_no,
//                branch,class_year,academic_year,division,pract_batch));


        Log.i("Log","Show Data"+unique_id+"\n"+stud_name+"\n"+roll_no+"\n"+branch+"\n"
                +class_year+"\n"+academic_year+"\n"+division+"\n"+pract_batch);

        //Printing string array list values on screen.


//        for (Student_Details_Data_Model ar : stud_ar_list) {
//           Log.i("Log","Data frim arraylist \n"+ ar.getUnique_id());
//        }

    }

    @Override
    public void onBackPressed() {
        this.finish();
        Intent it = new Intent(this,Drawer_Activity.class);
        startActivity(it);
        super.onBackPressed();
    }
}
