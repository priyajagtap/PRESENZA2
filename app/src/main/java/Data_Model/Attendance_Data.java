package Data_Model;

/**
 * Created by infogird31 on 01/10/2016.
 */
public class Attendance_Data {
    public String getStud_ID() {
        return Stud_ID;
    }

    public void setStud_ID(String Stud_ID) {
        this.Stud_ID = Stud_ID;
    }

    public String getSub_Name() {
        return Sub_Name;
    }

    public void setSub_Name(String Sub_Name) {
        this.Sub_Name = Sub_Name;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String Time) {
        this.Time = Time;
    }

    public String getAttendence() {
        return Attendence;
    }

    public void setAttendence(String Attendence) {
        this.Attendence = Attendence;
    }

    public String getStud_Name() {
        return Stud_Name;
    }

    public void setStud_Name(String Stud_Name) {
        this.Stud_Name = Stud_Name;
    }

    public int getStud_Roll_No() {
        return Stud_Roll_No;
    }

    public void setStud_Roll_No(int Stud_Roll_No) {
        this.Stud_Roll_No = Stud_Roll_No;
    }

    public int getSub_Class() {
        return Sub_Class;
    }

    public void setSub_Class(int Sub_Class) {
        this.Sub_Class = Sub_Class;
    }

    public Attendance_Data(String Stud_ID, String Sub_Name, String Date, String Time, String Attendence, String Stud_Name, int Stud_Roll_No, int Sub_Class) {
        this.Stud_ID = Stud_ID;
        this.Sub_Name = Sub_Name;
        this.Date = Date;
        this.Time = Time;
        this.Attendence = Attendence;
        this.Stud_Name = Stud_Name;
        this.Stud_Roll_No = Stud_Roll_No;
        this.Sub_Class = Sub_Class;
    }

    String Stud_ID,Sub_Name,Date,Time,Attendence,Stud_Name;
    int Sub_Class,Stud_Roll_No;

    public Attendance_Data() {
    }
}
