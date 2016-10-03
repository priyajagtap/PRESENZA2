package Data_Model;

/**
 * Created by infogird31 on 03/10/2016.
 */
public class server_conf {
    String NAMESPACE = "http://web_services/";
    String URL = "http://192.168.1.47:8080/Presenza_web_app/Signup_webservice?WSDL";
    String METHOD_NAME;
    String SOAP_ACTION;

    public String getNAMESPACE() {
        return NAMESPACE;
    }

    public String getURL() {
        return URL;
    }

    public String getMETHOD_NAME() {
        return METHOD_NAME;
    }

    public void setMETHOD_NAME(String METHOD_NAME) {
        this.METHOD_NAME = METHOD_NAME;
        this.SOAP_ACTION = this.NAMESPACE+this.METHOD_NAME;
    }

    public String getSOAP_ACTION() {
        return SOAP_ACTION;
    }
}
