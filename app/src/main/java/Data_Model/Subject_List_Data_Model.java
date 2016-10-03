package Data_Model;

/**
 * Created by infogird31 on 03/09/2016.
 */
public class Subject_List_Data_Model {
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

    int sub_id;

    public Subject_List_Data_Model(int sub_id, String sub_name) {
        this.sub_id = sub_id;
        this.sub_name = sub_name;
    }

    String sub_name;
}
