package com.infogird.www.presenza;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import com.infogird.www.presenza.Add_Student_Activity_Step2;

import Data_Model.Subject_Details_Att;

public class Take_Attendence_Activity extends AppCompatActivity {

    Cursor c1, cursor_data,cr;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;

    public ArrayList<Student_Details_Data_Model> dataSet;
    static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;
    SqlHandler sql_handler_obj_new, sql_handler_obj_att;
    public static ArrayList<Student_Details_Data_Model> stud_ar_list = new ArrayList<Student_Details_Data_Model>();;

    String Sub_Name;
    int Sub_class;

    Subject_Details_Att adt = new Subject_Details_Att();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take__attendence_);
/*
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

             Sub_Name = extras.getString("Sub_Name");
             Sub_class = extras.getInt("Sub_class");

            Log.i("Log","values received by sub adapter"+extras.getString("Sub_Name")+extras.getInt("Sub_class"));
            //The key argument here must match that used in the other activity
        }*/

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            Sub_Name = extras.getString("Sub_Name");
            Sub_class = extras.getInt("Sub_class");
            adt.setSub_Name(extras.getString("Sub_Name"));
            adt.setSub_class(extras.getInt("Sub_class"));


            Log.i("Log","values received by sub adapter"+extras.getString("Sub_Name")+extras.getInt("Sub_class"));
            //The key argument here must match that used in the other activity
        }


        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        sql_handler_obj_new=new SqlHandler(this);

        String Query_select = "select * from student_details";
        c1 = sql_handler_obj_new.selectQuery(Query_select);
        c1.moveToFirst();
        showRecords();




//        data = new ArrayList<Student_Details_Data_Model>();

//        for (int i = 0; i < MyData.nameArray.length; i++) {
//            data.add(new DataModel(
//                    MyData.nameArray[i],
//                    MyData.versionArray[i],
//                    MyData.id_[i],
//                    MyData.drawableArray[i]
//            ));
//        }

//        removedItems = new ArrayList<Integer>();

        adapter = new Student_Att_Adapter(stud_ar_list);
        recyclerView.setAdapter(adapter);

 /*       OpenDbHelperClass odhc_obj=new OpenDbHelperClass(this);
        try
        {
            odhc_obj.createDB();
        }
        catch (Exception e) {

        }*/
        sql_handler_obj_att=new SqlHandler(this);

/*
//new code to swipe

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }


            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDirection) {

                if (swipeDirection == ItemTouchHelper.LEFT){
                    viewHolder.getAdapterPosition();


                  //  int position = viewHolder.getAdapterPosition();

                //    data_set.get(position).getSub_name();
                   // Log.i("Log","Subject: " + data_set.get(position).getSub_name());
                    Toast.makeText(Take_Attendence_Activity.this, "swipe left", Toast.LENGTH_SHORT).show();
                    stud_ar_list.remove(viewHolder.getAdapterPosition());
                    adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                } else if(swipeDirection == ItemTouchHelper.RIGHT) {
                    Toast.makeText(Take_Attendence_Activity.this, "swipe right", Toast.LENGTH_SHORT).show();
                    stud_ar_list.remove(viewHolder.getAdapterPosition());
                    adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                }
            }

            @Override
            public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

                return super.getMovementFlags(recyclerView, viewHolder);
            }


        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);*/


         }

    public void removeitem(int pos){
        //stud_ar_list.remove(pos);
        //adapter.notifyItemRemoved(pos);

       // layoutManager.smoothScrollToPosition(recyclerView,RecyclerView.State,pos+1);


    /*    if( layoutManager.getItemCount() == pos){
            Toast.makeText(Take_Attendence_Activity.this, "Attendance Completed...", Toast.LENGTH_SHORT).show();
            Intent it = new Intent(this,Drawer_Activity.class);
            startActivity(it);
        }
        else {*/
            recyclerView.scrollToPosition(pos+1);
       // }
    }

    public boolean insert_attendence_new(String Stud_ID,String Date,String Time,int Attendence,
                                         String Stud_Name,int Stud_Roll_No){

        Sub_Name = adt.getSub_Name();
        Sub_class = adt.getSub_class();

        try {
            String Query = "insert into Attendence_Details" +
                    "(Stud_ID,Sub_Class,Sub_Name,Date,Time,Attendence,Stud_Name,Stud_Roll_No)"
                    + " values('" +Stud_ID + "','"
                    + Sub_class + "','"
                    + Sub_Name + "','"
                    + Date + "','"
                    + Time + "','"
                    + Attendence + "','"
                    + Stud_Name + "','"
                    + Stud_Roll_No
                    + "')";
            Log.i("Log", Query);
            //sql_handler_obj_att=new SqlHandler(this);
            sql_handler_obj_att.executeQuery(Query);
            Log.i("Log", "Insert Successful");

            return true;

        }
        catch(Exception e){
            Log.i("Exception",e+"");
            return false;
        }
    }

    public void get_data_ready(){
        String Query_select = "select * from student_details";
        cursor_data = sql_handler_obj_new.selectQuery(Query_select);
        cursor_data.moveToFirst();
        showRecords();

    }

    public boolean insert_attendence(String Stud_ID, int sub_class, String Sub_name,String Date,String Time,int Attendence,
                                     String Stud_Name,int Stud_Roll_No){


    //    sql_handler_obj_att=new SqlHandler(this);
        try {
            String Query = "insert into Attendence_Details" +
                    "(Stud_ID,Sub_Class,Sub_Name,Date,Time,Attendence,Stud_Name,Stud_Roll_No)"
                    + " values('" +Stud_ID + "','"
                    + sub_class + "','"
                    + Sub_name + "','"
                    + Date + "','"
                    + Time + "','"
                    + Attendence + "','"
                    + Stud_Name + "','"
                    + Stud_Roll_No
                    + "')";
            Log.i("Log", Query);

            sql_handler_obj_att.executeQuery(Query);
            Log.i("Log", "Insert Successful");

            return true;

        }
        catch(Exception e){
            Log.i("Exception",e.toString());
            return false;
        }
    }

    protected void showRecords() {

        if (c1.moveToFirst()) {
            do {
                String unique_id = c1.getString(0);
                String stud_name = c1.getString(1);
                String roll_no = c1.getString(2);
                String branch = c1.getString(3);
                String class_year = c1.getString(4);
                String academic_year = c1.getString(5);
                String division = c1.getString(6);
                String pract_batch = c1.getString(7);

                stud_ar_list.add(new Student_Details_Data_Model(unique_id,stud_name,roll_no,
                        branch,class_year,academic_year,division,pract_batch));


                Log.i("Log","Show Data"+unique_id+"\n"+stud_name+"\n"+roll_no+"\n"+branch+"\n"
                        +class_year+"\n"+academic_year+"\n"+division+"\n"+pract_batch);

            } while (c1.moveToNext());
        }
        c1.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_take_attendence, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.add_item) {
            //check if any items to add
            if (removedItems.size() != 0) {
                addRemovedItemToList();
            } else {
                Toast.makeText(this, "Nothing to add", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    private void addRemovedItemToList() {

    }
   }


