package com.infogird.www.presenza;

/**
 * Created by infogird31 on 26/08/2016.
 */
public class Student_Details_Data_Model {
    String unique_id;
    String stud_name;

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    public String getStud_name() {
        return stud_name;
    }

    public void setStud_name(String stud_name) {
        this.stud_name = stud_name;
    }

    public String getRoll_no() {
        return roll_no;
    }

    public void setRoll_no(String roll_no) {
        this.roll_no = roll_no;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getClass_year() {
        return class_year;
    }

    public void setClass_year(String class_year) {
        this.class_year = class_year;
    }

    public String getAcademic_year() {
        return academic_year;
    }

    public void setAcademic_year(String academic_year) {
        this.academic_year = academic_year;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getPract_batch() {
        return pract_batch;
    }

    public void setPract_batch(String pract_batch) {
        this.pract_batch = pract_batch;
    }

    String roll_no;
    String branch;
    String class_year;
    String academic_year;
    String division;
    String pract_batch ;

    public Student_Details_Data_Model(String unique_id, String stud_name, String roll_no, String branch, String class_year, String academic_year, String division, String pract_batch) {
        this.unique_id = unique_id;
        this.stud_name = stud_name;
        this.roll_no = roll_no;
        this.branch = branch;
        this.class_year = class_year;
        this.academic_year = academic_year;
        this.division = division;
        this.pract_batch = pract_batch;
    }
}
