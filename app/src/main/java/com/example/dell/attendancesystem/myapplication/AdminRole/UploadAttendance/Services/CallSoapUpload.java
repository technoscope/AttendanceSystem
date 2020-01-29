package com.example.dell.attendancesystem.myapplication.AdminRole.UploadAttendance.Services;

import com.example.dell.attendancesystem.myapplication.AdminRole.UploadAttendance.DataModel;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class CallSoapUpload {
    public String AttendanceRecord(String EmpPID,
                                   String TimeIn, String TimeOut, String Date, String Status) {


        ArrayList<DataModel> uploadAttendanceRecords = new ArrayList<>();

        uploadAttendanceRecords.add(new DataModel(EmpPID, TimeIn, TimeOut, Date, Status));
        uploadAttendanceRecords.add(new DataModel(EmpPID, TimeIn, TimeOut, Date, Status));
        uploadAttendanceRecords.add(new DataModel(EmpPID, TimeIn, TimeOut, Date, Status));
        uploadAttendanceRecords.add(new DataModel(EmpPID, TimeIn, TimeOut, Date, Status));
        uploadAttendanceRecords.add(new DataModel(EmpPID, TimeIn, TimeOut, Date, Status));
        uploadAttendanceRecords.add(new DataModel(EmpPID, TimeIn, TimeOut, Date, Status));

        SoapObject employeeArray = new SoapObject("http://tempuri.org/", "shoppingCartProductEntityArray");


        for (int i = 0; i < uploadAttendanceRecords.size(); i++) {
            DataModel dataModel = uploadAttendanceRecords.get(i);

            SoapObject object = new SoapObject("http://tempuri.org/", "shoppingCartProductEntity");
            object.addProperty("employ_id", dataModel.getEmpid());
            object.addProperty("employ_status", dataModel.getStatus());
            //add more dields and change the name convention
            employeeArray.addProperty("employee_" + i, object);
        }


        String SOAP_ACTION = "http://tempuri.org/AddEmpAttendance";
        String OPERATION_NAME = "AddEmpAttendance";
        String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
        String SOAP_ADDRESS = "http://192.168.10.4/API.asmx?op=AddEmpAttendance";
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);
        request.addProperty("employ_array", employeeArray);

        PropertyInfo PI = new PropertyInfo();
        PI.setName("EmployeeID");
        PI.setValue(EmpPID);
        PI.setType(String.class);
        request.addProperty(PI);


        PropertyInfo P4 = new PropertyInfo();
        P4.setName("date");
        P4.setValue(Date);
        P4.setType(String.class);
        request.addProperty(P4);


        PropertyInfo PI6 = new PropertyInfo();
        PI6.setName("CurrentInstituteID");
        PI6.setValue("DCS");
        PI6.setType(String.class);
        request.addProperty(PI6);


        PropertyInfo P2 = new PropertyInfo();
        P2.setName("TimeIn");
        P2.setValue(TimeIn);
        P2.setType(String.class);
        request.addProperty(P2);


        PropertyInfo P3 = new PropertyInfo();
        P3.setName("TimeOut");
        P3.setValue(TimeOut);
        P3.setType(String.class);
        request.addProperty(P3);


        PropertyInfo P5 = new PropertyInfo();
        P5.setName("Status");
        P5.setValue(Status);
        PI.setType(String.class);
        request.addProperty(P5);


        System.out.println(request.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        String response = null;
        try {
            HttpTransportSE httpsTransportSE = new HttpTransportSE(SOAP_ADDRESS);
            httpsTransportSE.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            httpsTransportSE.debug = true;
            httpsTransportSE.call(SOAP_ACTION, envelope);
            response = httpsTransportSE.responseDump;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }
}
/*
dependencies {
     compile 'com.google.code.gson:gson:2.2.4'
}
Then use below Code

List<List> encodedlist = Arrays.asList(Arrays.asList("one", "two","three"),
                                       Arrays.asList("four", "five","six"));

String levelPatternGson = new Gson().toJson(encodedlist);
invokeUploadMultipleDocuments(levelPatternGson);
Now code for sending it to server side

public SoapObject invokeUploadMultipleDocuments(String levelPatternGson){
         SoapObject request = new SoapObject(NAMESPACE, webMethodName);
         SoapObject WebRq = new SoapObject("", "PropertyInfo");
         WebRq.addProperty("FileNames",FileNames);
         request.addSoapObject(WebRq);
         return request;
      }
      */