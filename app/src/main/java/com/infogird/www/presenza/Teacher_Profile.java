package com.infogird.www.presenza;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import org.ksoap2.SoapFault;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;

import Data_Model.SignUp_Data;
import Data_Model.server_conf;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Teacher_Profile extends AppCompatActivity {

    TextView tv_tid,tv_tname,tv_t_dept,tv_t_post;
    Button btn_edit_profile;

    String NAMESPACE;
    String URL;
    String METHOD_NAME;
    String SOAP_ACTION;
    server_conf sc;
    ArrayList<SignUp_Data> reg = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher__profile);

        tv_tid = (TextView)findViewById(R.id.tv_t_uid);
        tv_tname = (TextView)findViewById(R.id.tv_t_name);
        tv_t_dept = (TextView)findViewById(R.id.tv_t_dept);
        tv_t_post =(TextView)findViewById(R.id.tv_t_post);
        btn_edit_profile = (Button)findViewById(R.id.btn_edit_profile);

        sc = new server_conf();
        NAMESPACE = sc.getNAMESPACE();
        URL = sc.getURL();
        sc.setMETHOD_NAME("Show_Teacher_Profile_Operation");
        METHOD_NAME = sc.getMETHOD_NAME();
        SOAP_ACTION = sc.getSOAP_ACTION();

        //network policy
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //web service call
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("s","67");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        new MarshalBase64().register(envelope);
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            String result = response.toString();

            Gson ob = new Gson();
            Type t1 = new TypeToken<ArrayList<SignUp_Data>>()
            {

            }.getType();

            Log.i("token",t1+"");
            Log.i("result",result);
            reg = ob.fromJson(result, t1);

            for( SignUp_Data rd1:reg) {

                tv_tid.setText(rd1.getTeacher_id());
                tv_tname.setText(rd1.getTeacher_name());
                tv_t_dept.setText(rd1.getDept());
                tv_t_post.setText(rd1.getTeacher_post());
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
