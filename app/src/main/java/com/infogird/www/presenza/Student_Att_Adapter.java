package com.infogird.www.presenza;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.BoolRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import Data_Model.Subject_Details_Att;

/**
 * Created by infogird31 on 27/08/2016.
 */
public class Student_Att_Adapter extends RecyclerView.Adapter<Student_Att_Adapter.MyViewHolder> {
    Cursor crsr;
    Context context;
    public ArrayList<Student_Details_Data_Model> dataSet;
    SqlHandler sql_handler_obj_new, sql_handler_obj_att;
    Date new_date = new Date();
    public static class MyViewHolder extends RecyclerView.ViewHolder {

       /* TextView textViewName;
        TextView textViewVersion;
        ImageView imageViewIcon;
        */
        TextView Stud_Name;
        TextView Stud_Roll_No;
        TextView Stud_class;
        TextView Stud_batch;
        Button btn_presnet,btn_absent;

        private CardView cardView;
        public MyViewHolder(View itemView) {
            super(itemView);
            /*this.textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            this.textViewVersion = (TextView) itemView.findViewById(R.id.textViewVersion);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.imageView);
            */
            this.Stud_Name = (TextView)itemView.findViewById(R.id.tv_st_name);
            this.Stud_class = (TextView) itemView.findViewById(R.id.tv_st_class);
            this.Stud_Roll_No = (TextView)itemView.findViewById(R.id.tv_St_rollno);
            this.Stud_batch = (TextView) itemView.findViewById(R.id.tv_st_batch);
            this.btn_presnet = (Button)itemView.findViewById(R.id.btn_present);
            this.btn_absent = (Button)itemView.findViewById(R.id.btn_absent);
            cardView = (CardView) itemView.findViewById(R.id.card_view_student);
        }
    }

    public Student_Att_Adapter(ArrayList<Student_Details_Data_Model> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);

        view.setOnClickListener(Take_Attendence_Activity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        /*OpenDbHelperClass odhc_obj=new OpenDbHelperClass(parent.getContext());
        try
        {
            odhc_obj.createDB();
        }
        catch (Exception e) {

        }*/
        sql_handler_obj_att=new SqlHandler(parent.getContext());
        context = parent.getContext();
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView tv_st_name= holder.Stud_Name;
        TextView tv_st_rollno = holder.Stud_Roll_No;
        TextView tv_st_class= holder.Stud_class;
        TextView tv_st_batch = holder.Stud_batch;

        tv_st_name.setText(dataSet.get(listPosition).getStud_name());
        tv_st_rollno.setText(dataSet.get(listPosition).getRoll_no());
        tv_st_class.setText(dataSet.get(listPosition).getClass_year());
        tv_st_batch.setText(dataSet.get(listPosition).getPract_batch());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.cardView.requestFocus();
                Log.i("Log","clicked on"+dataSet.get(listPosition).getUnique_id());
                 String Stud_ID = dataSet.get(listPosition).getUnique_id();
            }
        });

        holder.btn_presnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Log","Clicked on Present");
                holder.btn_presnet.requestFocus();
                String Stud_name_data =  dataSet.get(listPosition).getStud_name();
                String stud_rollno_data = dataSet.get(listPosition).getRoll_no();

               String sub_batch = dataSet.get(listPosition).getPract_batch();
               String uid = dataSet.get(listPosition).getUnique_id();

               Subject_Details_Att adt = new Subject_Details_Att();
                String subname = adt.getSub_Name();
                int subclass = adt.getSub_class();

                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                Date formattedDate = new Date();


                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                try {
                    new_date = dateFormat.parse(df.format(formattedDate));
                  //  end_convertedDate = dateFormat.parse(Output.getText().toString());
                    Log.i("new date",new_date+"");
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
                Date currentLocalTime = cal.getTime();
                DateFormat date = new SimpleDateFormat("HH:mm a");
                date.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
                String localTime = date.format(currentLocalTime);


                try {
                    String check_duplicate_query = "SELECT * FROM Attendence_Details" +
                            " WHERE Stud_ID ='" + uid +
                            "' AND Sub_Class ='" + subclass +
                            "' AND Sub_Name ='" + subname +
                            "' AND Date ='" + new_date +
                            "' AND Time ='" + localTime +
                            "' AND Attendence = 'A'" +
                            " AND Stud_Name ='" + Stud_name_data +
                            "' AND Stud_Roll_No ='" + Integer.parseInt(stud_rollno_data)+"'";

                    Log.i("Query", check_duplicate_query);
                    crsr = sql_handler_obj_att.selectQuery(check_duplicate_query);

                    if (crsr != null &&  crsr.moveToFirst()){

                        Log.i("Log","Cursor data"+crsr+"\n"+crsr.getString(6));
                        showRecords();
                        crsr.close();
                        // Context context;
                        new AlertDialog.Builder(context)
                                .setTitle("Duplicate Entry")
                                .setMessage("Attendence for student "+stud_name+" is already recorded")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                    }
                    else{
                        try {
                            String Query = "insert into Attendence_Details" +
                                    "(Stud_ID,Sub_Class,Sub_Name,Date,Time,Attendence,Stud_Name,Stud_Roll_No)"
                                    + " values('" +uid + "','"
                                    + subclass + "','"
                                    + subname + "','"
                                    + new_date + "','"
                                    + localTime + "','"
                                    + "P" + "','"
                                    + Stud_name_data + "','"
                                    +  Integer.parseInt(stud_rollno_data)
                                    + "')";
                            Log.i("Log", Query);

                            sql_handler_obj_att.executeQuery(Query);
                            Log.i("Log", "Insert Successful");
                        }
                        catch(Exception e){
                            Log.i("Exception: Insert Fail",e.toString());
                        }
                        // code to sroll list :-
                        taa.removeitem(listPosition);
                    }
                }
                catch (Exception e){
                    Log.i("exception",e.toString());
                }
            }
        });

        holder.btn_absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Log","Clicked on absent");
                holder.btn_absent.requestFocus();

                String Stud_name_data =  dataSet.get(listPosition).getStud_name();
                String stud_rollno_data = dataSet.get(listPosition).getRoll_no();

                String sub_batch = dataSet.get(listPosition).getPract_batch();
                String uid = dataSet.get(listPosition).getUnique_id();

                Subject_Details_Att adt = new Subject_Details_Att();
                String subname = adt.getSub_Name();
                int subclass = adt.getSub_class();

                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                Date formattedDate = new Date();

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                try {
                    new_date = dateFormat.parse(df.format(formattedDate));
                    //  end_convertedDate = dateFormat.parse(Output.getText().toString());
                    Log.i("new date",new_date+"");
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
                Date currentLocalTime = cal.getTime();
                DateFormat date = new SimpleDateFormat("HH:mm a");
                date.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
                String localTime = date.format(currentLocalTime);

                try {
                    String check_duplicate_query = "SELECT * FROM Attendence_Details" +
                    " WHERE Stud_ID ='" + uid +
                    "' AND Sub_Class ='" + subclass +
                    "' AND Sub_Name ='" + subname +
                    "' AND Date ='" + new_date +
                    "' AND Time ='" + localTime +
                    "' AND Attendence = 'A'" +
                    " AND Stud_Name ='" + Stud_name_data +
                            "' AND Stud_Roll_No ='" + Integer.parseInt(stud_rollno_data)+"'";

                    Log.i("Query", check_duplicate_query);
                    crsr = sql_handler_obj_att.selectQuery(check_duplicate_query);

                    if (crsr != null &&  crsr.moveToFirst()){

                        Log.i("Log","Cursor data"+crsr+"\n"+crsr.getString(6));
                        showRecords();
                        crsr.close();
                       // Context context;
                        new AlertDialog.Builder(context)
                                .setTitle("Duplicate Entry")
                                .setMessage("Attendence for student "+stud_name+" is already recorded")
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
                    else{
                        try {
                            String Query = "insert into Attendence_Details" +
                                    "(Stud_ID,Sub_Class,Sub_Name,Date,Time,Attendence,Stud_Name,Stud_Roll_No)"
                                    + " values('" +uid + "','"
                                    + subclass + "','"
                                    + subname + "','"
                                    + new_date + "','"
                                    + localTime + "','"
                                    + "A" + "','"
                                    + Stud_name_data + "','"
                                    + Integer.parseInt(stud_rollno_data)
                                    + "')";
                            Log.i("Log", Query);

                            sql_handler_obj_att.executeQuery(Query);
                            Log.i("Log", "Insert Successful");
                        }
                        catch(Exception e){
                            Log.i("Exception: Insert Fail",e.toString());
                        }
                        taa.removeitem(listPosition);
                    }
                }
                catch (Exception e){
                    Log.i("exception",e.toString());
                }


            }
        });
    }
    Take_Attendence_Activity taa = new Take_Attendence_Activity();
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
    String stud_name;
    protected void showRecords() {

        String unique_id = crsr.getString(0);
        int roll_no = crsr.getInt(1);
        stud_name = crsr.getString(6);
        int branch =  crsr.getInt(3);
        String class_year = crsr.getString(4);
        String academic_year = crsr.getString(5);
        String division = crsr.getString(2);
        int pract_batch = crsr.getInt(7);

        Log.i("Log","Show Data"+unique_id+"\n"+stud_name+"\n"+roll_no+"\n"+branch+"\n"
                +class_year+"\n"+academic_year+"\n"+division+"\n"+pract_batch);

    }
}