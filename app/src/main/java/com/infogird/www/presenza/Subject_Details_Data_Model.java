package com.infogird.www.presenza;

/**
 * Created by infogird31 on 02/09/2016.
 */
public class Subject_Details_Data_Model {

    int sub_id;
    String sub_name;
    int teacher_id;



    public int getSub_id() {
        return sub_id;
    }

    public void setSub_id(int sub_id) {
        this.sub_id = sub_id;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public int getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getTheory_or_practical() {
        return theory_or_practical;
    }

    public void setTheory_or_practical(String theory_or_practical) {
        this.theory_or_practical = theory_or_practical;
    }

    public String getBatch() {
        return Batch;
    }

    public void setBatch(String batch) {
        Batch = batch;
    }

    public Subject_Details_Data_Model(String sub_name, int teacher_id, String theory_or_practical, String batch, int sub_Class) {
        this.sub_name = sub_name;
        this.teacher_id = teacher_id;
        this.theory_or_practical = theory_or_practical;
        Batch = batch;
        this.sub_Class = sub_Class;
    }

    String theory_or_practical;
    String Batch;

    public int getSub_Class() {
        return sub_Class;
    }

    public void setSub_Class(int sub_Class) {
        this.sub_Class = sub_Class;
    }

    int sub_Class;
}
