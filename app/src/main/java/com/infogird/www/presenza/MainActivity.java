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

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import Data_Model.SignUp_Data;
import Data_Model.server_conf;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_login,btn_signup;
    EditText edit_username,edit_password;
    int un_status = 0,pass_status = 0;

    String NAMESPACE;
    String URL;
    String METHOD_NAME;
    String SOAP_ACTION;
    server_conf sc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        sc = new server_conf();
        NAMESPACE = sc.getNAMESPACE();
        URL = sc.getURL();
        sc.setMETHOD_NAME("LoginOperation");
        METHOD_NAME = sc.getMETHOD_NAME();
        SOAP_ACTION = sc.getSOAP_ACTION();

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
}
