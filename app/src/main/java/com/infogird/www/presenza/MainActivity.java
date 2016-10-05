package com.infogird.www.presenza;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import Data_Model.SessionManagement;
import Data_Model.SignUp_Data;
import Data_Model.server_conf;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_login,btn_signup;
    EditText edit_username,edit_password;
    int un_status = 0,pass_status = 0;
    String uid_web;
    String uid_sp,name_sp,dept_sp,post_sp;

    String NAMESPACE;
    String URL;
    String METHOD_NAME;
    String SOAP_ACTION;
    server_conf sc;

    String METHOD_NAME_SP;
    String SOAP_ACTION_SP;
    server_conf sc_sp;
    ArrayList<SignUp_Data> reg = new ArrayList<>();

    // Session Manager Class
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Session Manager
        session = new SessionManagement(getApplicationContext());

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        sc = new server_conf();
        NAMESPACE = sc.getNAMESPACE();
        URL = sc.getURL();
        sc.setMETHOD_NAME("LoginOperation");
        METHOD_NAME = sc.getMETHOD_NAME();
        SOAP_ACTION = sc.getSOAP_ACTION();

        sc_sp.setMETHOD_NAME("Show_Teacher_Profile_Operation");
        METHOD_NAME_SP = sc_sp.getMETHOD_NAME();
        SOAP_ACTION_SP = sc_sp.getSOAP_ACTION();

        OpenDbHelperClass odbhc_obj = new OpenDbHelperClass(getApplicationContext());
        if(odbhc_obj.checkDB()){
            //presnet
        }
        else{
            try {
                odbhc_obj.createDB();
                //absent
            }
            catch (Exception e){

            }
        }

        btn_login = (Button)findViewById(R.id.bt_login);
        btn_signup = (Button)findViewById(R.id.btn_signup);
        edit_username = (EditText)findViewById(R.id.tx_username);
        edit_password = (EditText)findViewById(R.id.tx_passwd);
        btn_login.setOnClickListener(this);
        btn_signup.setOnClickListener(this);

    }

    public void validation(){
        if(edit_username.getText().toString().trim().equals("")){
            edit_username.setError("Please enter uid");
            edit_username.requestFocus();
        }else{
            un_status =1;
        }
        if(edit_password.getText().toString().trim().equals("")){
            edit_password.setError("Please enter password");
            edit_password.requestFocus();
        }else{
            pass_status = 1;
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_login:
                btn_login.requestFocus();
                validation();
                if(un_status == 1 && pass_status ==1){

                    SoapObject sp = new SoapObject(NAMESPACE, METHOD_NAME);
                    sp.addProperty("UID", edit_username.getText().toString());
                    sp.addProperty("PWD", edit_password.getText().toString());

                    SoapSerializationEnvelope ev = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    ev.setOutputSoapObject(sp);
                    HttpTransportSE http = new HttpTransportSE(URL);

                    try {

                        http.call(SOAP_ACTION, ev);
                        SoapPrimitive s = (SoapPrimitive) ev.getResponse();
                        String response = s.toString();
                        Log.i("response",response);

                        if(response.equals("1")){
                            Log.i("Login","Login successful");

                            uid_web = edit_username.getText().toString().trim();

                            // Creating user login session
                            // For testing i am stroing name, email as follow
                            // Use user real data
                            session.createLoginSession(uid_sp,name_sp,dept_sp,post_sp);

                            Intent int_home = new Intent(this,Drawer_Activity.class);
                            this.finish();
                            startActivity(int_home);
                        }
                        else if(response.equals("0")){
                            Log.i("Login","Login failed");
                        }
                    }
                    catch (Exception e){
                        Log.i("exception",e.toString());
                        new AlertDialog.Builder(this)
                                .setTitle("Login Failed")
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
                break;

            case R.id.btn_signup:
                btn_signup.requestFocus();
                Intent int_suhome = new Intent(this,Teacher_SignUp_Activity.class);
               // this.finish();
                startActivity(int_suhome);
                break;
        }

    }

    public void get_user_details(){
        //web service call
        SoapObject request_sp = new SoapObject(NAMESPACE, METHOD_NAME_SP);

        request_sp.addProperty("s",uid_web);
        SoapSerializationEnvelope envelope_sp = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        new MarshalBase64().register(envelope_sp);
        envelope_sp.setOutputSoapObject(request_sp);
        HttpTransportSE androidHttpTransport_sp = new HttpTransportSE(URL);
        try {
            androidHttpTransport_sp.call(SOAP_ACTION_SP, envelope_sp);
            SoapPrimitive response_sp = (SoapPrimitive) envelope_sp.getResponse();
            String result_sp = response_sp.toString();

            Gson ob_sp = new Gson();
            Type t1_sp = new TypeToken<ArrayList<SignUp_Data>>()
            {

            }.getType();

            Log.i("token",t1_sp+"");
            Log.i("result",result_sp);
            reg = ob_sp.fromJson(result_sp, t1_sp);

            for( SignUp_Data rd1:reg) {

                uid_sp = rd1.getTeacher_id();
                name_sp = rd1.getTeacher_name();
                dept_sp = rd1.getDept();
                post_sp = rd1.getTeacher_post();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
            Log.i("Log exception",e.toString());
        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
            Log.i("Log exception",soapFault.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("Log exception",e.toString());
        }
    }
    }

