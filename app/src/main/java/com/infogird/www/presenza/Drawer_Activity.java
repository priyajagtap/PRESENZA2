package com.infogird.www.presenza;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


public class Drawer_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Cursor c1;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;

    SqlHandler sql_handler_obj_new;
    public static ArrayList<Subject_Details_Data_Model> sub_ar_list = new ArrayList<Subject_Details_Data_Model>();
    static View.OnClickListener myOnClickListener;
    FloatingActionButton fab_add_sub;
    Button take_att;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

     /*   take_att = (Button)findViewById(R.id.bt_take_attendence);
        take_att.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it_take_att = new Intent(Drawer_Activity.this,Take_Attendence_Activity.class);
                startActivity(it_take_att);
            }
        });
*/
        fab_add_sub = (FloatingActionButton)findViewById(R.id.fab);

        fab_add_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_Add_sub = new Intent(Drawer_Activity.this,Add_Subject_Activity.class);
                startActivity(intent_Add_sub);

            }
        });



        recyclerView = (RecyclerView) findViewById(R.id.sub_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        myOnClickListener = new MyOnClickListener(this);
        sql_handler_obj_new=new SqlHandler(this);
        attach_data_to_rv();
    }

    @Override
    protected void onRestart() {
        //clearData();

        attach_data_to_rv();
        super.onRestart();
    }
    @Override
    protected void onResume() {
       // attach_data_to_rv();
        super.onResume();
    }
/*    public void clearData() {
        int size = this.sub_ar_list.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.sub_ar_list.remove(0);
            }

          //  this.notifyItemRangeRemoved(0, size);
            adapter.notifyItemRangeChanged(0,size);
        }
    }*/
    public void attach_data_to_rv(){
        sub_ar_list.clear();
        String Query_select = "select * from Subject_Details";
        c1 = sql_handler_obj_new.selectQuery(Query_select);

        if(c1 != null) {
            c1.moveToFirst();
            showRecords();
        }
        adapter = new subject_cv_adapter(sub_ar_list);
        recyclerView.setAdapter(adapter);
    }

    // card view on click code

   /* private static class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
           // removeItem(v);
            Log.i("Log","clicked on card");

            v.getContext().startActivity(new Intent(v.getContext(),Take_Attendence_Activity.class));
        }
    }*/
    //CREATE TABLE "subject" ( `sub_id` , `sub_name` `teacher_id` `theory_or_practical` `Batch`  `Class` INTEGER NOT NULL )

    protected void showRecords() {

        if (c1.moveToFirst()) {
            do {
                int sub_id = c1.getInt(0);
                String sub_name = c1.getString(1);
                int teacher_id = c1.getInt(2);
                String theory_or_practical = c1.getString(3);
                String Batch = c1.getString(4);
                int Class = c1.getInt(5);

                sub_ar_list.add(new Subject_Details_Data_Model(sub_name,teacher_id,theory_or_practical,
                        Batch,Class));
                Log.i("Log","Show Data"+sub_id+"\n"+sub_name+"\n"+teacher_id+"\n"+theory_or_practical+"\n"
                        +Batch+"\n"+Class);

                // get the data into array, or class variable
            } while (c1.moveToNext());
        }
        c1.close();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent int_home = new Intent(this,Drawer_Activity.class);
            this.finish();
            startActivity(int_home);

        } else if (id == R.id.nav_teacher_profile) {
            Intent int_teacher_profile = new Intent(this,Teacher_Profile.class);
            this.finish();
            startActivity(int_teacher_profile);

        } else if (id == R.id.nav_add_student) {
            Intent int_add_student = new Intent(this,Add_Students_Step1_Activity.class);
            this.finish();
            startActivity(int_add_student);

        } else if (id == R.id.nav_report) {
            Intent int_report = new Intent(this,GenerateReport.class);
            this.finish();
            startActivity(int_report);

        } else if (id == R.id.nav_subject) {
            Intent int_subject = new Intent(this,Drawer_Activity.class);
            this.finish();
            startActivity(int_subject);

        }else if (id == R.id.nav_help) {
            Intent int_help = new Intent(this,Drawer_Activity.class);
            this.finish();
            startActivity(int_help);

        }else if (id == R.id.nav_logout) {
            Intent int_logout = new Intent(this,Drawer_Activity.class);
            this.finish();
            startActivity(int_logout);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
