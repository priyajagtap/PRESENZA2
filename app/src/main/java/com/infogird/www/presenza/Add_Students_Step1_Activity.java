package com.infogird.www.presenza;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Add_Students_Step1_Activity extends AppCompatActivity implements View.OnClickListener {
    Spinner sp_class_s,sp_div,sp_branch;
    Button btn_next_step;
    EditText Aca_year;
    int Aca_year_status = 0;

    public static final String KEY_Academic_year="Academic_year";
    public static final String KEY_Branch="Branch";
    public static final String KEY_class = "Class";
    public static final String KEY_division = "Division";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__students__step1_);
        btn_next_step = (Button)findViewById(R.id.bt_next);
        btn_next_step.setOnClickListener(this);
        sp_branch = (Spinner)findViewById(R.id.sp_branch);
        sp_class_s = (Spinner)findViewById(R.id.sp_class);
        sp_div = (Spinner)findViewById(R.id.sp_division);
        Aca_year = (EditText)findViewById(R.id.tx_ac_year);

        List<String> list_Branch = new ArrayList<String>();
        list_Branch.add("Computer Science Engineering");
        list_Branch.add("Electronics and Telecommunication");
        list_Branch.add("Civil Engineering");
        list_Branch.add("Electrical Engineering");
        list_Branch.add("Plastic and Polymer Engineering");

        List<String> list_Class = new ArrayList<String>();
        list_Class.add("1");
        list_Class.add("2");
        list_Class.add("3");
        list_Class.add("4");

        List<String> list_Division = new ArrayList<String>();
        list_Division.add("A");
        list_Division.add("B");

        // branch spinner adapter
        ArrayAdapter<String> dataAdapter_branch = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list_Branch);

        dataAdapter_branch.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        sp_branch.setAdapter(dataAdapter_branch);

        //class spinner adapter

        ArrayAdapter<String> dataAdapter_class = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list_Class);

        dataAdapter_class.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        sp_class_s.setAdapter(dataAdapter_class);
        //division spinner adapter

        ArrayAdapter<String> dataAdapter_div = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list_Division);

        dataAdapter_div.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        sp_div.setAdapter(dataAdapter_div);
        // Spinner item selection Listener
        addListenerOnSpinnerItemSelection();


    }
    public void addListenerOnSpinnerItemSelection(){

        sp_branch.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        sp_class_s.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        sp_div.setOnItemSelectedListener(new CustomOnItemSelectedListener());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_next:
                btn_next_step.requestFocus();
                validation();
                if(Aca_year_status == 1) {
                    Log.i("Values :", Aca_year.getText()
                            + sp_branch.getSelectedItem().toString()
                            + sp_class_s.getSelectedItem().toString()
                            + sp_div.getSelectedItem().toString());

                    Intent int_step2 = new Intent(this, Add_Student_Activity_Step2.class);

                    int_step2.putExtra(KEY_Academic_year, Aca_year.getText().toString());
                    int_step2.putExtra(KEY_Branch, sp_branch.getSelectedItem().toString());
                    int_step2.putExtra(KEY_class, sp_class_s.getSelectedItem().toString());
                    int_step2.putExtra(KEY_division, sp_div.getSelectedItem().toString());
                    this.finish();
                    startActivity(int_step2);
                }
                else{
                    Log.i("Status","status is not true");
                }
                break;
        }

    }

    public void validation(){
        if(Aca_year.getText().toString().trim().equals("")){
            Aca_year.setError("Enter Acadamic Year");
            Aca_year.requestFocus();
        }
        else{
            Aca_year_status = 1;
        }
    }

}
