package com.example.dell.attendancesystem.myapplication.AdminRole.EmployeeRecord.Services;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CallSoap {
    public String EmpRegistration(String name,
                                  String phone, String cd, String email, String cnic, String empPID, String gender, String des,
                                  String fname,
                                  String dob,
                                  String scale,
                                  String address, String joiningdate, String province, String district, String tehsil, String unioncouncil, String village1) {
        String SOAP_ACTION =  "http://tempuri.org/EmpRegistration";
        String OPERATION_NAME = "EmpRegistration";
        String WSDL_TARGET_NAMESPACE ="http://tempuri.org/";
        String SOAP_ADDRESS = "http://192.168.10.4/API.asmx?op=EmpRegistration";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);
        PropertyInfo PI = new PropertyInfo();
        PI.setName("Name");
        PI.setValue(name.trim());
        PI.setType(String.class);
        request.addProperty(PI);

        PropertyInfo PI2 = new PropertyInfo();

        PI2.setName("F_Name");
        PI2.setValue(fname.trim());
        PI2.setType(String.class);
        request.addProperty(PI2);

        PropertyInfo P3 = new PropertyInfo();

        P3.setName("DoB");
        P3.setValue(dob);
        P3.setType(String.class);
        request.addProperty(P3);

        PropertyInfo P4 = new PropertyInfo();

        P4.setName("DesignationID");
        P4.setValue(des);
        P4.setType(String.class);
        request.addProperty(P4);

        PropertyInfo P5 = new PropertyInfo();

        P5.setName("Gender");
        P5.setValue(gender);
        P5.setType(String.class);
        request.addProperty(P5);

        PropertyInfo P6 = new PropertyInfo();

        P6.setName("Phone");
        P6.setValue(phone);
        P6.setType(String.class);
        request.addProperty(P6);

        PropertyInfo P7 = new PropertyInfo();

        P7.setName("Email");
        P7.setValue(email);
        P7.setType(String.class);
        request.addProperty(P7);


        PropertyInfo P8 = new PropertyInfo();

        P8.setName("CurrentInstID");
        P8.setValue(cd);
        P8.setType(String.class);
        request.addProperty(P8);

        PropertyInfo P9 = new PropertyInfo();

        P9.setName("CNIC");
        P9.setValue(cnic);
        P9.setType(String.class);
        request.addProperty(P9);

        PropertyInfo P10 = new PropertyInfo();


        P10.setName("EmpPersonalID");
        P10.setValue(empPID);
        P10.setType(String.class);
        request.addProperty(P10);

        PropertyInfo P11 = new PropertyInfo();


        P11.setName("JoiningDate");
        P11.setValue(joiningdate);
        P11.setType(String.class);
        request.addProperty(P11);

        PropertyInfo P12 = new PropertyInfo();

        P12.setName("Address");
        P12.setValue(address);
        P12.setType(String.class);
        request.addProperty(P12);

        PropertyInfo P13 = new PropertyInfo();

        P13.setName("ScaleID");
        P13.setValue(scale);
        P13.setType(String.class);
        request.addProperty(P13);

        PropertyInfo P14 = new PropertyInfo();

        P14.setName("ProvinceName");
        P14.setValue(province);
        P14.setType(String.class);
        request.addProperty(P14);

        PropertyInfo P15 = new PropertyInfo();

        P15.setName("DistrictName");
        P15.setValue(district);
        P15.setType(String.class);
        request.addProperty(P15);

        PropertyInfo P16 = new PropertyInfo();

        P16.setName("TehsilName");
        P16.setValue(tehsil);
        P16.setType(String.class);
        request.addProperty(P16);

        PropertyInfo P17 = new PropertyInfo();

        P17.setName("UnionCouncil");
        P17.setValue(unioncouncil);
        P17.setType(String.class);
        request.addProperty(P17);

        PropertyInfo P18 = new PropertyInfo();

        P18.setName("VillageName");
        P18.setValue(village1);
        P18.setType(String.class);
        request.addProperty(P18);

        PropertyInfo P19 = new PropertyInfo();

        P19.setName("InstituteName");
        P19.setValue(cd);
        P19.setType(String.class);
        request.addProperty(P19);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        String response=null;
        try{
            HttpTransportSE httpTransport=new HttpTransportSE(SOAP_ADDRESS);
            httpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            httpTransport.debug=true;
            httpTransport.call(SOAP_ACTION,envelope);
            response=httpTransport.responseDump;

        } catch (XmlPullParserException e) {
           e.printStackTrace();
            response=e.getMessage().trim();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            response=e.getMessage().trim();
        } catch (IOException e) {
            e.printStackTrace();
            response=e.getMessage().trim();
        }

        return response;
    }
    public String TodayDate() {
        final SimpleDateFormat dateformat = new SimpleDateFormat("yyyy.MM.dd");
        final String currentDate = dateformat.format(new Date());
        final String todaydate = currentDate;
        return todaydate;
    }
}
