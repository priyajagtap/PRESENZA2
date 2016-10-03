package com.infogird.www.presenza;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import Data_Model.SignUp_Data;
import Data_Model.server_conf;

public class Teacher_SignUp_Activity extends AppCompatActivity {

    Spinner t_dept,t_post;
    EditText t_uid,t_name,t_pass,t_c_pass;
    Button signup_btn;

    String NAMESPACE;
    String URL;
    String METHOD_NAME;
    String SOAP_ACTION;
    server_conf sc;

    int pass_status=0,t_uid_status=0,t_name_status=0,t_pass_status =0,t_c_pass_status=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher__sign_up_);
        t_dept = (Spinner)findViewById(R.id.sp_dept);
        t_post = (Spinner)findViewById(R.id.sp_post);
        t_uid = (EditText)findViewById(R.id.tx_teacher_id);
        t_name = (EditText)findViewById(R.id.tx_teacher_name);
        t_pass = (EditText)findViewById(R.id.tx_passwd);
        t_c_pass = (EditText)findViewById(R.id.tx_confirm_passwd);
        signup_btn = (Button)findViewById(R.id.bt_sign_up);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        sc = new server_conf();
        NAMESPACE = sc.getNAMESPACE();
        URL = sc.getURL();
        sc.setMETHOD_NAME("Signup");
        METHOD_NAME = sc.getMETHOD_NAME();
        SOAP_ACTION = sc.getSOAP_ACTION();

        List<String> list_dept = new ArrayList<String>();
        list_dept.add("Computer Science Engineering");
        list_dept.add("Electronics and Telecommunication");
        list_dept.add("Civil Engineering");
        list_dept.add("Electrical Engineering");
        list_dept.add("Plastic and Polymer Engineering");
        list_dept.add("Other");

        // dept spinner adapter
        ArrayAdapter<String> dataAdapter_dept = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list_dept);

        dataAdapter_dept.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        t_dept.setAdapter(dataAdapter_dept);

        List<String> list_post = new ArrayList<String>();
        list_post.add("Principal");
        list_post.add("Vice Principal");
        list_post.add("Head Of Department");
        list_post.add("Lecturer");
        list_post.add("Assistant Lecturer");
        list_post.add("Other");

        // post spinner adapter
        ArrayAdapter<String> dataAdapter_post = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list_post);

        dataAdapter_post.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        t_post.setAdapter(dataAdapter_post);

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup_btn.requestFocus();
                    validation();
                if (pass_status ==1 && t_uid_status ==1 && t_name_status ==1 && t_pass_status ==1 && t_c_pass_status ==1){

                    SoapObject sp = new SoapObject(NAMESPACE, METHOD_NAME);
                    sp.addProperty("teacher_uid", t_uid.getText().toString());
                    sp.addProperty("teacher_name", t_name.getText().toString());
                    sp.addProperty("teacher_dept", t_dept.getSelectedItem().toString());
                    sp.addProperty("teacher_post",t_post.getSelectedItem().toString());
                    sp.addProperty("teacher_pwd", t_pass.getText().toString());

                    SoapSerializationEnvelope ev = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    ev.setOutputSoapObject(sp);
                    HttpTransportSE http = new HttpTransportSE(URL);

                    try {
                        http.call(SOAP_ACTION, ev);
                        SoapPrimitive s = (SoapPrimitive) ev.getResponse();
                        String response = s.toString();
                        Log.i("response",response);

                        if (response.equals("1")){
                            Log.i("Register","Registration successful");

                            Intent nex_act = new Intent(v.getContext(),Drawer_Activity.class);
                            startActivity(nex_act);
                        }
                        else {
                            Log.i("Register","Registration failed");

                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("Registration Failed")
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
                    catch (Exception e){
                        Log.i("exception",e.toString());
                    }
                }
                else{
                    Log.i("Log","status is not true:1");
                }

            }
        });


    }

    public void validation(){

        if (t_pass.getText().toString().trim().equals("")){
            t_pass.setError("Please Enter Password");
            t_pass.requestFocus();
        }
        else{
            t_pass_status =1;
        }
        if(t_c_pass.getText().toString().trim().equals("")){
            t_c_pass.setError("Please Enter Same Password Again");
            t_c_pass.requestFocus();
        }
        else{
            t_c_pass_status =1;
        }

        if(t_pass.getText().toString().trim().equals(t_c_pass.getText().toString().trim())){
            pass_status =1;
        }
        else{
            t_pass.setError("Please Enter Same Password");
            t_pass.requestFocus();
            t_c_pass.setError("Please Enter Same Password");
            t_c_pass.requestFocus();
        }
        if(t_uid.getText().toString().trim().equals("")){
            t_uid.setError("Please Enter User ID");
            t_uid.requestFocus();
        }
        else{
            t_uid_status =1;
        }
        if(t_name.getText().toString().trim().equals("")){
            t_name.setError("Please Enter User Name");
            t_name.requestFocus();
        }
        else {
            t_name_status = 1;
        }
    }
}
