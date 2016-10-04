package com.infogird.www.presenza;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.poi.hssf.record.formula.functions.Column;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;

import Data_Model.Attendance_Data;
import Data_Model.server_conf;

public class GenerateReport extends AppCompatActivity {
    TableLayout table_layout;
    ProgressDialog PD;
    Button addmem_btn,upload_data_btn;
    SqlHandler sqlcon;
    TextView Output, Start_date_tv;
    Cursor cursor_sub,crsr;
    Spinner sp_rclass,sp_rsub,sp_rbranch,sp_rbatch,sp_rdiv;
    int year;
    int month;
    int day;
    Date start_convertedDate = new Date();
    Date end_convertedDate = new Date();
    static final int DATE_PICKER_ID = 1111;
    static final int DATE_PICKER_ID_start = 2222;
    static String TAG = "ExelLog";
    ImageView send_mail_iv;
    //New Workbook
    Workbook wb = new HSSFWorkbook();
    Cell c = null;
    Date last_sync_date = new Date();
    static String last_sync_time;

    //Cell style for header row
    CellStyle cs = wb.createCellStyle();


    //New Sheet
    Sheet sheet1 = null;

    String NAMESPACE;
    String URL;
    String METHOD_NAME;
    String SOAP_ACTION;
    server_conf sc;

    ArrayList<Attendance_Data> ar = new ArrayList<Attendance_Data>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_report);
        addmem_btn = (Button) findViewById(R.id.addmem_btn_id);
        table_layout = (TableLayout) findViewById(R.id.tableLayout1);
        Output = (TextView) findViewById(R.id.Output);
        Start_date_tv = (TextView)findViewById(R.id.start_dt_tv);
        sp_rclass = (Spinner)findViewById(R.id.report_class);
        sp_rsub = (Spinner)findViewById(R.id.report_subject);
        sp_rbatch = (Spinner)findViewById(R.id.report_batch);
        sp_rbranch = (Spinner)findViewById(R.id.report_branch);
        sp_rdiv = (Spinner)findViewById(R.id.report_div);
        send_mail_iv = (ImageView)findViewById(R.id.send_mail_iv);
        upload_data_btn = (Button)findViewById(R.id.upload_data);

        cs.setFillForegroundColor(HSSFColor.LIME.index);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        sheet1 = wb.createSheet("myOrder");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        sc = new server_conf();
        NAMESPACE = sc.getNAMESPACE();
        URL = sc.getURL();
        sc.setMETHOD_NAME("Insert_Attendance");
        METHOD_NAME = sc.getMETHOD_NAME();
        SOAP_ACTION = sc.getSOAP_ACTION();

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


        List<String> list_batch = new ArrayList<String>();
        list_batch.add("B1");
        list_batch.add("B2");
        list_batch.add("B3");
        list_batch.add("B4");
        list_batch.add("ALL");

        List<String> list_subject = new ArrayList<String>();
        list_subject.add("ALL");

        try {
            String Query_select_sub = "select Subject_Name from All_Subjects";

            cursor_sub = sqlcon.selectQuery(Query_select_sub);
            cursor_sub.moveToFirst();
            if (cursor_sub.moveToFirst()) {
                do {
                    String Sub_Name = cursor_sub.getString(0);
                    list_subject.add(Sub_Name);
                    Log.i("Log","Show Data"+Sub_Name);

                } while (cursor_sub.moveToNext());
            }
            cursor_sub.close();
        }
        catch(Exception e){
            Log.i("Exception",e.toString());
        }
        //batch spinner adapter

        ArrayAdapter<String> dataAdapter_batch = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list_batch);

        dataAdapter_batch.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        sp_rbatch.setAdapter(dataAdapter_batch);


        //class spinner adapter

        ArrayAdapter<String> dataAdapter_class = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list_Class);

        dataAdapter_class.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        sp_rclass.setAdapter(dataAdapter_class);

        //branch spinner adapter

        ArrayAdapter<String> dataAdapter_branch = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list_Branch);

        dataAdapter_branch.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        sp_rbranch.setAdapter(dataAdapter_branch);

        //div spinner adapter

        ArrayAdapter<String> dataAdapter_div = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list_Division);

        dataAdapter_div.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        sp_rdiv.setAdapter(dataAdapter_div);

          //subject spinner adapter

        ArrayAdapter<String> dataAdapter_sub = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list_subject);

        dataAdapter_sub.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        sp_rsub.setAdapter(dataAdapter_sub);
        // Spinner item selection Listener
        addListenerOnSpinnerItemSelection();


        // Get current date by calender

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

        // Show current date

        Output.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(day).append("-").append(month + 1).append("-")
                .append(year).append(" "));



        Start_date_tv.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(day).append("-").append(month + 1).append("-")
                .append(year).append(" "));




        // Button listener to show date picker dialog

        Output.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // On button click show datepicker dialog
                showDialog(DATE_PICKER_ID);

            }

        });

        // Button listener to show date picker dialog

        Start_date_tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // On button click show datepicker dialog
                showDialog(DATE_PICKER_ID_start);

            }

        });

        sqlcon=new SqlHandler(getApplicationContext());
        BuildTable();


        File folder = new File(Environment.getExternalStorageDirectory() + "/Presenza");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
       /* if (success) {
            // Do something on success
        } else {
            // Do something else on failure
        }*/

        addmem_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new MyAsync().execute();

               // readExcelFile(getApplicationContext(),"myexcel.xls");
            }
        });

        send_mail_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_mail();
            }
        });


        upload_data_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_data_btn.requestFocus();
                upload_att_data_to_server();
            }
        });


    }
    public void addListenerOnSpinnerItemSelection(){

        sp_rbranch.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        sp_rclass.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        sp_rdiv.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        sp_rsub.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        sp_rbatch.setOnItemSelectedListener(new CustomOnItemSelectedListener());

    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
                // open datepicker dialog.
                // set date picker for current date
                // add pickerListener listner to date picker
                return new DatePickerDialog(this, pickerListener, year, month,day);
            case DATE_PICKER_ID_start:
                return new DatePickerDialog(this, pickerListener_start, year, month,day);


        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;

                Output.setText(new StringBuilder().append(day).append("-").append(month + 1)
                        .append("-").append(year)
                        .append(" "));

            // Show selected date

        }
    };

    private DatePickerDialog.OnDateSetListener pickerListener_start = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;



                Start_date_tv.setText(new StringBuilder().append(day).append("-")
                        .append(month + 1)
                        .append("-").append(year)
                        .append(" "));

            // Show selected date

        }
    };


//    CREATE TABLE "Attendence_Details" `Stud_ID` TEXT, `Sub_Class` INTEGER, `Sub_Name` TEXT, `Date` TEXT, `Time` TEXT, `Attendence` INTEGER, `Stud_Name` TEXT, `Stud_Roll_No` INTEGER )
    private void BuildTable() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            start_convertedDate = dateFormat.parse(Start_date_tv.getText().toString());
            end_convertedDate = dateFormat.parse(Output.getText().toString());

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //CREATE TABLE "Attendence_Details" ( `Stud_ID` TEXT, `Sub_Class` INTEGER, `Sub_Name` TEXT, `Date` NUMERIC, `Time` TEXT, `Attendence` TEXT, `Stud_Name` TEXT, `Stud_Roll_No` INTEGER )
        Log.i("Log","start date :"+start_convertedDate+"end date:"+end_convertedDate);
        String Query_select = "SELECT Stud_Roll_No,Stud_Name,Attendence FROM Attendence_Details WHERE Date BETWEEN '"+start_convertedDate+"' AND '"+end_convertedDate+"'";

        Cursor c = sqlcon.selectQuery(Query_select);
        if (c != null) {
            c.moveToFirst();
        }
        int rows = c.getCount();
        int cols = c.getColumnCount();

        c.moveToFirst();

        // outer for loop
        for (int i = 0; i < rows; i++) {

            TableRow row = new TableRow(this);
            row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));

            // inner for loop
            for (int j = 0; j < cols; j++) {

                TextView tv = new TextView(this);
                tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT));
             /*   tv.setBackgroundResource(R.drawable.cell_shape);*/
                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(18);
                tv.setPadding(0, 5, 0, 5);

                tv.setText(c.getString(j));

                Log.i("Log_data",c.getString(j)+"at row :"+i+"at col: "+j);

                row.addView(tv);

                insert_data(j,i,c.getString(j));
            }

            c.moveToNext();

            table_layout.addView(row);

        }
        c.close();
    }

    private class MyAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            table_layout.removeAllViews();

            PD = new ProgressDialog(GenerateReport.this);
            PD.setTitle("Please Wait..");
            PD.setMessage("Loading...");
            PD.setCancelable(false);
            PD.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

          /*  String firstname = firstname_et.getText().toString();
            String lastname = lastname_et.getText().toString();

            // inserting data
            sqlcon.open();
            sqlcon.insertData(firstname, lastname);*/
            // BuildTable();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            BuildTable();
            saveExcelFile(getApplicationContext(),"myexcel.xls");
            PD.dismiss();
        }
    }

    private boolean saveExcelFile(Context context, String fileName){

        // check if available and not read only
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e(TAG, "Storage not available or read only");
            return false;
        }

        boolean success = false;

        File file_obj = new File(fileName);
        try{
        FileInputStream fIP = new FileInputStream(file_obj);
        }
        catch (Exception e){
            Log.i("exception",e.toString());
        }


        Row row = sheet1.createRow(0);

        if(file_obj.isFile() && file_obj.exists())
        {
            System.out.println(
                    "openworkbook.xlsx file open successfully.");


        }
        else
        {
            System.out.println(
                    "Error to open openworkbook.xlsx file.");


           // Generate column headings


            c = row.createCell(0);
            c.setCellValue("Roll No");
            c.setCellStyle(cs);

            c = row.createCell(1);
            c.setCellValue("Name");
            c.setCellStyle(cs);

            c = row.createCell(2);
            c.setCellValue("Attendence");
            c.setCellStyle(cs);


            Log.i("Log","start date :"+start_convertedDate+"end date:"+end_convertedDate);
            String Query_select = "SELECT Stud_Roll_No,Stud_Name,Attendence FROM Attendence_Details WHERE Date BETWEEN '"+start_convertedDate+"' AND '"+end_convertedDate+"'";

            Cursor c = sqlcon.selectQuery(Query_select);
            if (c != null) {
                c.moveToFirst();
            }
            int rows = c.getCount();
            int cols = c.getColumnCount();

            c.moveToFirst();

            // outer for loop
            for (int i = 0; i < rows; i++) {

                TableRow row_table = new TableRow(this);
                row_table.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));

                // inner for loop
                for (int j = 0; j < cols; j++) {
                    Log.i("Log_data", c.getString(j) + "at row :" + i + "at col: " + j);
                    insert_data(j, i+1, c.getString(j));
                }

                c.moveToNext();
            }
            c.close();

            sheet1.setColumnWidth(0, (15 * 500));
            sheet1.setColumnWidth(1, (15 * 500));
            sheet1.setColumnWidth(2, (15 * 500));

            // Create a path where we will place our List of objects on external storage
            File file = new File(Environment.getExternalStorageDirectory()+"/Presenza/", fileName);
            FileOutputStream os = null;

            try {
                os = new FileOutputStream(file);
                wb.write(os);
                Log.w("FileUtils", "Writing file" + file);
                success = true;
            } catch (IOException e) {
                Log.w("FileUtils", "Error writing " + file, e);
            } catch (Exception e) {
                Log.w("FileUtils", "Failed to save file", e);
            } finally {
                try {
                    if (null != os)
                        os.close();
                } catch (Exception ex) {
                }
            }
        }
        if(success){
              File file = new File(Environment.getExternalStorageDirectory()+ "/Presenza/" +"myexcel.xls");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file),"application/vnd.ms-excel");
                startActivity(intent);
        }
        return success;
    }

    public void insert_data(int cl,int rw,String data){

        Row row_new;
            if(cl ==0){
                 row_new = sheet1.createRow(rw);
            }
            else{
                 row_new = sheet1.getRow(rw);
            }
                c = row_new.createCell(cl);
                c.setCellValue(data);
                c.setCellStyle(cs);

       // }
    }

    private static void readExcelFile(Context context, String filename) {

        if (!isExternalStorageAvailable() || isExternalStorageReadOnly())
        {
            Log.e(TAG, "Storage not available or read only");
            return;
        }

        try{
            // Creating Input Stream
            File file = new File(Environment.getExternalStorageDirectory()+"/Presenza/", filename);
            FileInputStream myInput = new FileInputStream(file);

            // Create a POIFSFileSystem object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);

            /** We now need something to iterate through the cells.**/
            Iterator rowIter = mySheet.rowIterator();

            while(rowIter.hasNext()){
                HSSFRow myRow = (HSSFRow) rowIter.next();
                Iterator cellIter = myRow.cellIterator();
                while(cellIter.hasNext()){
                    HSSFCell myCell = (HSSFCell) cellIter.next();
                    Log.d(TAG, "Cell Value: " +  myCell.toString());
                    Toast.makeText(context, "cell Value: " + myCell.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){e.printStackTrace(); }

        return;
    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public void send_mail(){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"p.k.jagtap100@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Attendence report");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Report Generated - Latest");
        File root = Environment.getExternalStorageDirectory();
        String pathToMyAttachedFile = "Presenza/myexcel.xls";
        File file = new File(root, pathToMyAttachedFile);
        if (!file.exists() || !file.canRead()) {
            return;
        }
        Uri uri = Uri.fromFile(file);
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
    }

    @Override
    public void onBackPressed() {
        this.finish();
        Intent it = new Intent(this,Drawer_Activity.class);
        startActivity(it);
        super.onBackPressed();
    }

    public void upload_att_data_to_server(){

        Attendance_Data st = new Attendance_Data();

        String Query_select = "select * from Attendence_Details";
        Log.i("Log",Query_select);
        crsr = sqlcon.selectQuery(Query_select);

        if(crsr != null) {
            crsr.moveToFirst();
            if (crsr.moveToFirst()) {
                do {

                    String Stud_ID = crsr.getString(0);
                    int Sub_Class = crsr.getInt(1);
                    String Sub_Name = crsr.getString(2);
                    String Date = crsr.getString(3);
                    String Time = crsr.getString(4);
                    String Attendence = crsr.getString(5);
                    String Stud_Name = crsr.getString(6);
                    int Stud_Roll_No = crsr.getInt(7);

                    st.setStud_ID(Stud_ID);
                    st.setSub_Class(Sub_Class);
                    st.setSub_Name(Sub_Name);
                    st.setDate(Date);
                    st.setTime(Time);
                    st.setAttendence(Attendence);
                    st.setStud_Name(Stud_Name);
                    st.setStud_Roll_No(Stud_Roll_No);

                    ar.add(st);
 // get the data into array, or class variable
                } while (crsr.moveToNext());
            }
            crsr.close();

        }

        Gson ob = new Gson();
        String str = ob.toJson(ar);
        Log.i("Res_ponse :str",str);
        SoapObject sp = new SoapObject(NAMESPACE, METHOD_NAME);
        sp.addProperty("s", str);

        SoapSerializationEnvelope ev = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        ev.setOutputSoapObject(sp);
        HttpTransportSE http = new HttpTransportSE(URL);

        try {

            http.call(SOAP_ACTION, ev);
            SoapPrimitive s = (SoapPrimitive) ev.getResponse();
            Log.i("Res_ponse :s",s.toString());

            String temp = s.toString();

            if(temp == "1"){
                Log.i("Res_ponse Result",temp);
                //getDate_Time();
            }
            else{
                Log.i("Res_ponse Result",temp);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.i("Res_ponse exception",e.toString());

        }

    }

    public void getDate_Time(){

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date formattedDate = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            last_sync_date = dateFormat.parse(df.format(formattedDate));
            //  end_convertedDate = dateFormat.parse(Output.getText().toString());
            Log.i("last_sync_date",last_sync_date+"");
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm a");
        date.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
        last_sync_time = date.format(currentLocalTime);
        Log.i("last_sync_time",last_sync_time+"");
    }
}
