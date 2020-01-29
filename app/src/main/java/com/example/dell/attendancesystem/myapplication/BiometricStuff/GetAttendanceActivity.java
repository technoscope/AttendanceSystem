package com.example.dell.attendancesystem.myapplication.BiometricStuff;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.dell.attendancesystem.R;
import com.example.dell.attendancesystem.myapplication.AdminRole.EmployeeRecord.model;
import com.example.dell.attendancesystem.myapplication.SQLiteDatabaseHelper.DatabaseHelper;
import com.example.dell.attendancesystem.myapplication.UI.MainActivity;

import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import SecuGen.FDxSDKPro.JSGFPLib;
import SecuGen.FDxSDKPro.SGFDxDeviceName;
import SecuGen.FDxSDKPro.SGFDxErrorCode;
import SecuGen.FDxSDKPro.SGFDxSecurityLevel;
import SecuGen.FDxSDKPro.SGFDxTemplateFormat;
import SecuGen.FDxSDKPro.SGFingerInfo;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class GetAttendanceActivity extends AppCompatActivity {
    AlertDialog.Builder alertDialogl;
    Button verifybtn;
    boolean mAutoOnEnabled;
    ImageView mImageViewVerify;
    DatabaseHelper db;
    TextView mTextViewResult;
    private IntentFilter filter;
    private PendingIntent mPermissionIntent;
    private boolean bSecuGenDeviceOpened;
    private byte[] mVerifyImage;
    private byte[] mRegisterTemplate;
    private byte[] mVerifyTemplate;
    private int[] mMaxTemplateSize;
    private static int mImageWidth;
    private static int mImageHeight;
    private int mImageDPI;
    private int[] grayBuffer;
    private Bitmap grayBitmap;
    private boolean usbPermissionRequested;
    private JSGFPLib sgfplib;
    ArrayList<byte[]> temp;
    model model;
    private ImageView backpress;
    boolean check = false;
    TextToSpeech t1;

    private void debugMessage(String message) {
        //this.mEditLog.append(message);
        // this.mEditLog.invalidate(); //TODO trying to get Edit log to update after each line written
    }

    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_employee);
        temp = new ArrayList<>();

        ;
        db = new DatabaseHelper(this);
        verifybtn = findViewById(R.id.verify_Btn);
        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.getDefault());
                }
            }
        });
        mImageViewVerify = findViewById(R.id.verify_image);
        mImageViewVerify.setImageResource(R.drawable.fingericontest);
        mTextViewResult = findViewById(R.id.check_text);
        backpress = findViewById(R.id.verify_bck_btn_id);
        backpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GetAttendanceActivity.this, MainActivity.class));
            }
        });
        final TextView txtview = findViewById(R.id.datentime);

        final Date currentTime = Calendar.getInstance().getTime();
        TimePicker timePicker = new TimePicker(this);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                //txtview.setText("" + timePicker);
            }
        });
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        final String currentTimee = sdf.format(new Date());
        final SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd");
        final String currentDate = date.format(new Date());
        final String time = currentTimee;
        final String todaydate = currentDate;
        txtview.setText(todaydate.toString() + "\n" + time);
        model = new model();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Todo if(time==8to10&&time==4to6)
        verifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 10/7/2019        db.addEntryForAttendance("", "","","");
                // String empname="khan";

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                long dwTimeStart = 0, dwTimeEnd = 0, dwTimeElapsed = 0;
                //DEBUG Log.d(TAG, "Clicked MATCH");
                debugMessage("Clicked MATCH\n");
                if (mVerifyImage != null)
                    mVerifyImage = null;
                mVerifyImage = new byte[mImageWidth * mImageHeight];
                dwTimeStart = System.currentTimeMillis();
                long result = sgfplib.GetImage(mVerifyImage);
                //DumpFile("verify.raw", mVerifyImage);
                dwTimeEnd = System.currentTimeMillis();
                dwTimeElapsed = dwTimeEnd - dwTimeStart;
                debugMessage("GetImage() ret:" + result + " [" + dwTimeElapsed + "ms]\n");
                // mImageViewFingerprint.setImageBitmap(BiometricRegistration.toGrayscale(mVerifyImage));
                mImageViewVerify.setImageBitmap(GetAttendanceActivity.toGrayscale(mVerifyImage));
                dwTimeStart = System.currentTimeMillis();
                result = sgfplib.SetTemplateFormat(SGFDxTemplateFormat.TEMPLATE_FORMAT_SG400);
                dwTimeEnd = System.currentTimeMillis();
                dwTimeElapsed = dwTimeEnd - dwTimeStart;
                debugMessage("SetTemplateFormat(SG400) ret:" + result + " [" + dwTimeElapsed + "ms]\n");
                SGFingerInfo fpInfo = new SGFingerInfo();
                for (int i = 0; i < mVerifyTemplate.length; ++i)
                    mVerifyTemplate[i] = 0;
                dwTimeStart = System.currentTimeMillis();
                //select * frmVerifyTemplate
                result = sgfplib.CreateTemplate(fpInfo, mVerifyImage, mVerifyTemplate);

                //DumpFile("verify.min", mVerifyTemplate);
                dwTimeEnd = System.currentTimeMillis();
                dwTimeElapsed = dwTimeEnd - dwTimeStart;
                debugMessage("CreateTemplate() ret:" + result + " [" + dwTimeElapsed + "ms]\n");
                boolean[] matched = new boolean[1];
                dwTimeStart = System.currentTimeMillis();
                Cursor cr = db.GetEmplooyeeRecord();
                if (cr.moveToFirst()) {

                    do {
                        mRegisterTemplate = cr.getBlob(20);
                        String PIDd = cr.getString(1);
                        result = sgfplib.MatchTemplate(mRegisterTemplate, mVerifyTemplate, SGFDxSecurityLevel.SL_NORMAL, matched);

                        model.setEmpPID(PIDd);
                        dwTimeEnd = System.currentTimeMillis();
                        dwTimeElapsed = dwTimeEnd - dwTimeStart;
                        debugMessage("MatchTemplate() ret:" + result + " [" + dwTimeElapsed + "ms]\n");
                        if (matched[0]) {
                            // mTextViewResult.setText("MATCHED!!\n");
                            //    this.mCheckBoxMatched.setChecked(true);
                            String PID = model.getEmpPID();
                            try {
                                if (db.CheckTodayTimIn(currentDate, PID) == false && db.CheckTodayTimOut(currentDate, PID) == false) {
//working
                                    db.addEntryForAttendance(PID, time, todaydate, "present");
                                    mTextViewResult.setText("Matched!!\n");
                                    Toast.makeText(GetAttendanceActivity.this, "Check in at :" + currentTimee, Toast.LENGTH_SHORT).show();
                                    String toSpeak = "Welcome Mr." + cr.getString(3);
                                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                    Toast.makeText(GetAttendanceActivity.this, "Welcome", Toast.LENGTH_LONG).show();
                                    check = true;
                                    break;
                                } else if (db.CheckTodayTimIn(currentDate, PID) == true && db.CheckTodayTimOut(currentDate, PID) == false
                                        && timelaibetween(db.CheckTodayTimInTime(currentDate, PID), Add10minutes(db.CheckTodayTimInTime(currentDate, PID))) == true) {
                                    String toSpeak = "Access Denied You are Already CheckedIn";
                                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                    Toast.makeText(GetAttendanceActivity.this, "Access Denied \n You are Already Checked in", Toast.LENGTH_LONG).show();
                                    mTextViewResult.setText("Matched!!\n");
                                    check = true;
                                    break;


                                } else if (db.CheckTodayTimOut(currentDate, PID) == false && db.CheckTodayTimIn(currentDate, PID) == true) {
//working
                                    db.UpdateAttendance(PID, time, currentDate);
                                    String toSpeak = "Good by Mr." + cr.getString(3);
                                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                    Toast.makeText(GetAttendanceActivity.this, "Check out at:" + currentTimee, Toast.LENGTH_SHORT).show();
                                    // Toast.makeText(GetAttendanceActivity.this, "Good By ...", Toast.LENGTH_LONG).show();
                                    mTextViewResult.setText("MATCHED!!!!!!\n");
                                    check = true;
                                    break;
                                    //working
                                } else if (db.CheckTodayTimOut(currentDate, PID) == true && db.CheckTodayTimIn(currentDate, PID) == true) {
                                    String toSpeak = "you are already checked out";
                                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                    mTextViewResult.setText("you are already checked out\n");
                                    check = true;
                                    break;

                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            debugMessage("MATCHED!!\n");
                        }
                    }
                    while (cr.moveToNext());
                    if (check == false) {
                        mTextViewResult.setText("Not Matched");
                    }
                    //result = sgfplib.MatchTemplate(mRegisterTemplate, mVerifyTemplate, SGFDxSecurityLevel.SL_NORMAL, matched);


                    mVerifyImage = null;
                    fpInfo = null;
                    matched = null;

                } else {
                    mTextViewResult.setText("notmatch");
                }
                cr.close();
            }

        });


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        grayBuffer = new int[JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES * JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES];
        for (int i = 0; i < grayBuffer.length; ++i)
            grayBuffer[i] = android.graphics.Color.GRAY;
        grayBitmap = Bitmap.createBitmap(JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES, Bitmap.Config.ARGB_8888);
        grayBitmap.setPixels(grayBuffer, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES, 0, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES);
        //mImageViewFingerprint.setImageBitmap(grayBitmap);

        int[] sintbuffer = new int[(JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES / 2) * (JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES / 2)];
        for (int i = 0; i < sintbuffer.length; ++i)
            sintbuffer[i] = android.graphics.Color.GRAY;
        Bitmap sb = Bitmap.createBitmap(JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES / 2, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES / 2, Bitmap.Config.ARGB_8888);
        sb.setPixels(sintbuffer, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES / 2, 0, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES / 2, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES / 2);
        // mImageViewRegister.setImageBitmap(grayBitmap);
        mImageViewVerify.setImageBitmap(grayBitmap);
        mMaxTemplateSize = new int[1];
        //USB Permissions
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(mUsbReceiver, filter);
        sgfplib = new JSGFPLib((UsbManager) getSystemService(Context.USB_SERVICE));
        //   this.mToggleButtonSmartCapture.toggle();
        bSecuGenDeviceOpened = false;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    }

    public void Add10minutes(Timestamp timestap) {
        Calendar calendar = Calendar.getInstance();
        long calendar1 = timestap.getTime();
        Timestamp timestamp = timestap;
        System.out.println("Current Date = " + calendar.getTime());
        // Add 10 minutes to current date
        calendar.add(Calendar.MINUTE, 10);
    }

    public static Timestamp convertStringToTimestamp(String str_date) {
        try {

            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            Date date = formatter.parse(str_date);
            java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
            return timeStampDate;

        } catch (ParseException e) {
            System.out.println("Exception :" + e);
            return null;
        }
    }

    public String Add10minutes(String str_date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date dNow = formatter.parse(str_date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dNow);
        calendar.add(Calendar.MINUTE, 10);
        dNow = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        final String currentTimee = sdf.format(dNow);
        String someRandomTime = currentTimee;
        return someRandomTime;
    }


    public boolean timelaibetween(String starttime, String Endtime) throws ParseException {
        String string1 = starttime;//"20:11:13";
        Date time1 = new SimpleDateFormat("HH:mm:ss").parse(string1);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(time1);
        calendar1.add(Calendar.DATE, 1);
        String string2 = Endtime;//"14:49:00";
        Date time2 = new SimpleDateFormat("HH:mm:ss").parse(string2);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(time2);
        calendar2.add(Calendar.DATE, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        final String currentTimee = sdf.format(new Date());
        String someRandomTime = currentTimee;
        Date d = new SimpleDateFormat("HH:mm:ss").parse(someRandomTime);
        Calendar calendar3 = Calendar.getInstance();
        calendar3.setTime(d);
        calendar3.add(Calendar.DATE, 1);

        Date x = calendar3.getTime();
        if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
            //checkes whether the current time is between 14:49:00 and 20:11:13.
            return true;
        } else {
            return false;
        }
    }


    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //Log.d(TAG,"Enter mUsbReceiver.onReceive()");
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            //DEBUG Log.d(TAG, "Vendor ID : " + device.getVendorId() + "\n");
                            //DEBUG Log.d(TAG, "Product ID: " + device.getProductId() + "\n");
                            debugMessage("USB BroadcastReceiver VID : " + device.getVendorId() + "\n");
                            debugMessage("USB BroadcastReceiver PID: " + device.getProductId() + "\n");
                        } else
                            Log.e("ds", "mUsbReceiver.onReceive() Device is null");
                    } else
                        Log.e("sd", "mUsbReceiver.onReceive() permission denied for device " + device);
                }
            }
        }
    };


    @Override
    public void onDestroy() {
        //Log.d(TAG, "onDestroy()");
        sgfplib.CloseDevice();
        //   mRegisterImage = null;
        mVerifyImage = null;
        mRegisterTemplate = null;
        mVerifyTemplate = null;
        sgfplib.Close();
//    	unregisterReceiver(mUsbReceiver);
        super.onDestroy();
    }

    public void showMessage(String title, String Message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();


    }

    public static Bitmap toGrayscale(byte[] mImageBuffer) {
        byte[] Bits = new byte[mImageBuffer.length * 4];
        for (int i = 0; i < mImageBuffer.length; i++) {
            Bits[i * 4] = Bits[i * 4 + 1] = Bits[i * 4 + 2] = mImageBuffer[i]; // Invert the source bits
            Bits[i * 4 + 3] = -1;// 0xff, that's the alpha.
        }

        Bitmap bmpGrayscale = Bitmap.createBitmap(mImageWidth, mImageHeight, Bitmap.Config.ARGB_8888);
        //Bitmap bm contains the fingerprint img
        bmpGrayscale.copyPixelsFromBuffer(ByteBuffer.wrap(Bits));
        return bmpGrayscale;

    }

    public static Bitmap toGrayscale(byte[] mImageBuffer, int width, int height) {
        byte[] Bits = new byte[mImageBuffer.length * 4];
        for (int i = 0; i < mImageBuffer.length; i++) {
            Bits[i * 4] = Bits[i * 4 + 1] = Bits[i * 4 + 2] = mImageBuffer[i]; // Invert the source bits
            Bits[i * 4 + 3] = -1;// 0xff, that's the alpha.
        }

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //Bitmap bm contains the fingerprint img
        bmpGrayscale.copyPixelsFromBuffer(ByteBuffer.wrap(Bits));
        return bmpGrayscale;
    }

    @Override
    protected void onResume() {
        super.onResume();


        DisableControls();

        registerReceiver(mUsbReceiver, filter);
        long error = sgfplib.Init(SGFDxDeviceName.SG_DEV_AUTO);
        if (error != SGFDxErrorCode.SGFDX_ERROR_NONE) {
            android.app.AlertDialog.Builder dlgAlert = new android.app.AlertDialog.Builder(this);
            if (error == SGFDxErrorCode.SGFDX_ERROR_DEVICE_NOT_FOUND)
                dlgAlert.setMessage("The attached fingerprint device is not supported on Android");
            else ;
            dlgAlert.setMessage("Fingerprint device initialization failed!");
            dlgAlert.setTitle("SecuGen Fingerprint SDK");
            dlgAlert.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            finish();
                            return;
                        }
                    }
            );
            dlgAlert.setCancelable(false);
            dlgAlert.create().show();
        } else {
            UsbDevice usbDevice = sgfplib.GetUsbDevice();
            if (usbDevice == null) {
                android.app.AlertDialog.Builder dlgAlert = new android.app.AlertDialog.Builder(this);
                dlgAlert.setMessage("SecuGen fingerprint sensor not found!");
                dlgAlert.setTitle("SecuGen Fingerprint SDK");
                dlgAlert.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                finish();
                                return;
                            }
                        }
                );
                dlgAlert.setCancelable(false);
                dlgAlert.create().show();
            } else {
                boolean hasPermission = sgfplib.GetUsbManager().hasPermission(usbDevice);
                if (!hasPermission) {
                    if (!usbPermissionRequested) {
                        debugMessage("Requesting USB Permission\n");
                        //Log.d(TAG, "Call GetUsbManager().requestPermission()");
                        usbPermissionRequested = true;
                        sgfplib.GetUsbManager().requestPermission(usbDevice, mPermissionIntent);
                    } else {
                        //wait up to 20 seconds for the system to grant USB permission
                        hasPermission = sgfplib.GetUsbManager().hasPermission(usbDevice);
                        debugMessage("Waiting for USB Permission\n");
                        int i = 0;
                        while ((hasPermission == false) && (i <= 40)) {
                            ++i;
                            hasPermission = sgfplib.GetUsbManager().hasPermission(usbDevice);
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //Log.d(TAG, "Waited " + i*50 + " milliseconds for USB permission");
                        }
                    }
                }
                if (hasPermission) {
                    debugMessage("Opening SecuGen Device\n");
                    error = sgfplib.OpenDevice(0);
                    debugMessage("OpenDevice() ret: " + error + "\n");
                    if (error == SGFDxErrorCode.SGFDX_ERROR_NONE) {
                        bSecuGenDeviceOpened = true;
                        SecuGen.FDxSDKPro.SGDeviceInfoParam deviceInfo = new SecuGen.FDxSDKPro.SGDeviceInfoParam();
                        error = sgfplib.GetDeviceInfo(deviceInfo);
                        debugMessage("GetDeviceInfo() ret: " + error + "\n");
                        mImageWidth = deviceInfo.imageWidth;
                        mImageHeight = deviceInfo.imageHeight;
                        mImageDPI = deviceInfo.imageDPI;
                        debugMessage("Image width: " + mImageWidth + "\n");
                        debugMessage("Image height: " + mImageHeight + "\n");
                        debugMessage("Image resolution: " + mImageDPI + "\n");
                        debugMessage("Serial Number: " + new String(deviceInfo.deviceSN()) + "\n");
                        sgfplib.SetTemplateFormat(SGFDxTemplateFormat.TEMPLATE_FORMAT_SG400);
                        sgfplib.GetMaxTemplateSize(mMaxTemplateSize);
                        debugMessage("TEMPLATE_FORMAT_SG400 SIZE: " + mMaxTemplateSize[0] + "\n");
                        mRegisterTemplate = new byte[mMaxTemplateSize[0]];
                        mVerifyTemplate = new byte[mMaxTemplateSize[0]];
                        EnableControls();
                        //              boolean smartCaptureEnabled = this.mToggleButtonSmartCapture.isChecked();


                    }
                } else {
                    debugMessage("Waiting for USB Permission\n");
                }
            }
            //Thread thread = new Thread(this);
            //thread.start();
        }

    }

    public void DisableControls() {
        this.verifybtn.setClickable(false);
        this.verifybtn.setTextColor(getResources().getColor(android.R.color.black));

    }

    public Handler fingerDetectedHandler = new Handler() {
        // @Override
        public void handleMessage(Message msg) {
            //Handle the message
            CaptureFingerPrint();

            if (mAutoOnEnabled) {
                //  mToggleButtonAutoOn.toggle();
                EnableControls();
            }
        }
    };

    public void CaptureFingerPrint() {
        long dwTimeStart = 0, dwTimeEnd = 0, dwTimeElapsed = 0;
        //this.mCheckBoxMatched.setChecked(false);
        byte[] buffer = new byte[mImageWidth * mImageHeight];
        dwTimeStart = System.currentTimeMillis();
        //long result = sgfplib.GetImage(buffer);
        long result = sgfplib.GetImage(buffer);
        String NFIQString = null;
        dwTimeEnd = System.currentTimeMillis();
        dwTimeElapsed = dwTimeEnd - dwTimeStart;
        debugMessage("getImageEx(10000,50) ret:" + result + " [" + dwTimeElapsed + "ms]" + NFIQString + "\n");
        //DumpFile("capture2016.raw", buffer);

        mTextViewResult.setText("getImageEx(10000,50) ret: " + result + " [" + dwTimeElapsed + "ms] " + NFIQString + "\n");
        //mImageViewFingerprint.setImageBitmap(this.toGrayscale(buffer));

        // buffer = null;
    }

    @Override
    protected void onPause() {
        //mImageViewRegister.setImageBitmap(grayBitmap);
        if (bSecuGenDeviceOpened) {
            sgfplib.CloseDevice();
            bSecuGenDeviceOpened = false;
        }
        unregisterReceiver(mUsbReceiver);

        mVerifyImage = null;
        mRegisterTemplate = null;
        mVerifyTemplate = null;
        mImageViewVerify.setImageBitmap(grayBitmap);
        super.onPause();


    }

    public void EnableControls() {
        this.verifybtn.setClickable(true);
        this.verifybtn.setTextColor(getResources().getColor(android.R.color.white));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(GetAttendanceActivity.this, MainActivity.class));
        super.onBackPressed();
    }
}


