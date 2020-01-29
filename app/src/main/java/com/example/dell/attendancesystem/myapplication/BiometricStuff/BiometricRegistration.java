/*
 * Copyright (C) 2016 SecuGen Corporation
 *
 */

package com.example.dell.attendancesystem.myapplication.BiometricStuff;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.attendancesystem.R;
import com.example.dell.attendancesystem.myapplication.AdminRole.MainAdminActivity.AdminRole;
import com.example.dell.attendancesystem.myapplication.SQLiteDatabaseHelper.DatabaseHelper;
import com.example.dell.attendancesystem.myapplication.UI.EmployeeRegistration1;
import com.example.dell.attendancesystem.myapplication.UI.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

import SecuGen.FDxSDKPro.JSGFPLib;
import SecuGen.FDxSDKPro.SGFDxDeviceName;
import SecuGen.FDxSDKPro.SGFDxErrorCode;
import SecuGen.FDxSDKPro.SGFDxSecurityLevel;
import SecuGen.FDxSDKPro.SGFDxTemplateFormat;
import SecuGen.FDxSDKPro.SGFingerInfo;


public class BiometricRegistration extends Activity {

    private static final String TAG = "SecuGen USB";
    private Button mButtonCapture;
    private Button mButtonRegister;
    private Button mButtonMatch;
    private android.widget.TextView mTextViewResult;
    private static android.widget.CheckBox mCheckBoxMatched;
    private PendingIntent mPermissionIntent;
    // private ImageView mImageViewFingerprint;
    private ImageView mImageViewRegister;
    private ImageView mImageViewVerify;
    private byte[] mRegisterImage;
    private byte[] mVerifyImage;
    private byte[] mRegisterTemplate;
    private byte[] mVerifyTemplate;
    private int[] mMaxTemplateSize;
    private static int mImageWidth;
    private static int mImageHeight;
    private int mImageDPI;
    private int[] grayBuffer;
    private Bitmap grayBitmap;
    private IntentFilter filter; //2014-04-11
    private boolean mLed;
    private boolean mAutoOnEnabled;
    private int nCaptureModeN;
    private Button mButtonSetBrightness0;
    private Button mButtonSetBrightness100;
    private Button mButtonReadSN;
    private boolean bSecuGenDeviceOpened;
    private JSGFPLib sgfplib;
    private boolean usbPermissionRequested;
    DatabaseHelper databaseHelper;

    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
    private void debugMessage(String message) {
        //this.mEditLog.append(message);
        // this.mEditLog.invalidate(); //TODO trying to get Edit log to update after each line written
    }

    //This broadcast receiver is necessary to get user permissions to access the attached USB device
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @SuppressLint("NewApi")
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
                            Log.e(TAG, "mUsbReceiver.onReceive() Device is null");
                    } else
                        Log.e(TAG, "mUsbReceiver.onReceive() permission denied for device " + device);
                }
            }
        }
    };

    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
    //This message handler is used to access local resources not
    //accessible by SGFingerPresentCallback() because it is called by
    //a separate thread.
    @SuppressLint("HandlerLeak")
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

    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
    public void EnableControls() {
        this.mButtonRegister.setClickable(true);
        this.mButtonRegister.setTextColor(getResources().getColor(android.R.color.white));
        this.mButtonMatch.setClickable(true);
        this.mButtonMatch.setTextColor(getResources().getColor(android.R.color.white));
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
    public void DisableControls() {
        this.mButtonRegister.setClickable(false);
        this.mButtonRegister.setTextColor(getResources().getColor(android.R.color.black));
        this.mButtonMatch.setClickable(false);
        this.mButtonMatch.setTextColor(getResources().getColor(android.R.color.black));

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);

        Bundle bn = getIntent().getExtras();
        final String Employe_Name = bn.getString("name");
        final String Employe_FNAME = bn.getString("fname");
        final String Employe_Cnic = bn.getString("Cnic");
        final String Employe_Occupation = bn.getString("Occupation");
        final String Employe_Current_Dept = bn.getString("CurrentDept");
        final String Gender = bn.getString("Gender");
        final String PID = bn.getString("personalid");
        final String Emailaddress = bn.getString("emailaddress");
        final String phoneno = bn.getString("phoneno");
        final String Dateofbirth = bn.getString("dateofbirth");
        final String Address = bn.getString("Address");
        final String Province = bn.getString("provice");
        final String sDistrict = bn.getString("district");
        final String sVillage = bn.getString("village");
        final String stehsil = bn.getString("tehsil");
        final String sUnioncouncil = bn.getString("uc");
        final String sScale = bn.getString("scale");
        final String sJoiningdate = bn.getString("joiningdate");


        databaseHelper = new DatabaseHelper(this);

        setContentView(R.layout.biometric_registration_activity);
        mButtonRegister =findViewById(R.id.buttonRegister);

        mButtonMatch =findViewById(R.id.buttonMatch);

        mTextViewResult =findViewById(R.id.textviewresult);
        mImageViewRegister = findViewById(R.id.imageViewRegister);
        mImageViewVerify =findViewById(R.id.imageViewRegister);
        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long dwTimeStart = 0, dwTimeEnd = 0, dwTimeElapsed = 0;
                //DEBUG Log.d(TAG, "Clicked REGISTER");
                debugMessage("Clicked REGISTER\n");
                if (mRegisterImage != null)
                    mRegisterImage = null;
                mRegisterImage = new byte[mImageWidth * mImageHeight];
                // BiometricRegistration.mCheckBoxMatched.setChecked(false);
                dwTimeStart = System.currentTimeMillis();
                long result = sgfplib.GetImage(mRegisterImage);
                DumpFile("register.raw", mRegisterImage);
                dwTimeEnd = System.currentTimeMillis();
                dwTimeElapsed = dwTimeEnd - dwTimeStart;
                debugMessage("GetImage() ret:" + result + " [" + dwTimeElapsed + "ms]\n");
                  //     mImageViewFingerprint.setImageBitmap(BiometricRegistration.toGrayscale(mRegisterImage));
                dwTimeStart = System.currentTimeMillis();
                result = sgfplib.SetTemplateFormat(SGFDxTemplateFormat.TEMPLATE_FORMAT_SG400);
                dwTimeEnd = System.currentTimeMillis();
                dwTimeElapsed = dwTimeEnd - dwTimeStart;
                debugMessage("SetTemplateFormat(SG400) ret:" + result + " [" + dwTimeElapsed + "ms]\n");
                SGFingerInfo fpInfo = new SGFingerInfo();
                for (int i = 0; i < mRegisterTemplate.length; ++i)
                    mRegisterTemplate[i] = 0;
                dwTimeStart = System.currentTimeMillis();
                result = sgfplib.CreateTemplate(fpInfo, mRegisterImage, mRegisterTemplate);
                DumpFile("register.min", mRegisterTemplate);
                dwTimeEnd = System.currentTimeMillis();
                dwTimeElapsed = dwTimeEnd - dwTimeStart;
                debugMessage("CreateTemplate() ret:" + result + " [" + dwTimeElapsed + "ms]\n");
                mImageViewRegister.setImageBitmap(BiometricRegistration.toGrayscale(mRegisterImage));
                mTextViewResult.setText("Click Verify");
                mRegisterImage = null;
                fpInfo = null;
                //databaseHelper.addEntryofEmpInfo(Employe_Name,Employe_Cnic,Employe_Occupation,Gender,Employe_Current_Dept,"register.min",mRegisterImage);


            }
        });

        mButtonMatch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //TwoStatePreference mCheckBoxMatched;

                long dwTimeStart = 0, dwTimeEnd = 0, dwTimeElapsed = 0;
                //DEBUG Log.d(TAG, "Clicked MATCH");
                debugMessage("Clicked MATCH\n");
                if (mVerifyImage != null)
                    mVerifyImage = null;
                mVerifyImage = new byte[mImageWidth * mImageHeight];
                dwTimeStart = System.currentTimeMillis();
                long result = sgfplib.GetImage(mVerifyImage);
                DumpFile("verify.raw", mVerifyImage);
                dwTimeEnd = System.currentTimeMillis();
                dwTimeElapsed = dwTimeEnd - dwTimeStart;
                debugMessage("GetImage() ret:" + result + " [" + dwTimeElapsed + "ms]\n");
                // mImageViewFingerprint.setImageBitmap(BiometricRegistration.toGrayscale(mVerifyImage));
                mImageViewVerify.setImageBitmap(BiometricRegistration.toGrayscale(mVerifyImage));
                dwTimeStart = System.currentTimeMillis();
                result = sgfplib.SetTemplateFormat(SGFDxTemplateFormat.TEMPLATE_FORMAT_SG400);
                dwTimeEnd = System.currentTimeMillis();
                dwTimeElapsed = dwTimeEnd - dwTimeStart;
                debugMessage("SetTemplateFormat(SG400) ret:" + result + " [" + dwTimeElapsed + "ms]\n");
                SGFingerInfo fpInfo = new SGFingerInfo();
                for (int i = 0; i < mVerifyTemplate.length; ++i)
                    mVerifyTemplate[i] = 0;
                dwTimeStart = System.currentTimeMillis();
                result = sgfplib.CreateTemplate(fpInfo, mVerifyImage, mVerifyTemplate);
                DumpFile("verify.min", mVerifyTemplate);
                dwTimeEnd = System.currentTimeMillis();
                dwTimeElapsed = dwTimeEnd - dwTimeStart;
                debugMessage("CreateTemplate() ret:" + result + " [" + dwTimeElapsed + "ms]\n");
                boolean[] matched = new boolean[1];
                dwTimeStart = System.currentTimeMillis();
                result = sgfplib.MatchTemplate(mRegisterTemplate, mVerifyTemplate, SGFDxSecurityLevel.SL_NORMAL, matched);
                dwTimeEnd = System.currentTimeMillis();
                dwTimeElapsed = dwTimeEnd - dwTimeStart;
                debugMessage("MatchTemplate() ret:" + result + " [" + dwTimeElapsed + "ms]\n");
                if (matched[0]) {
                    setProgressBarVisibility(true);
                    mTextViewResult.setText("MATCHED!!\n");
                    //char char1=Dateofbirth.charAt(6);
                   // char char2=Dateofbirth.charAt(7);
                   // char char3=Dateofbirth.charAt(8);
                   // char char4=Dateofbirth.charAt(9);
                   // char charname1=Employe_Name.charAt(0);
                    //char charname2=Employe_Name.charAt(1);

                    String ppiidd=PID;
                    String SScale=sScale;
                    String FingerPrintID="dsadas";//char1+char2+char3+char4+charname1+charname2+ppiidd+SScale;

                    if (databaseHelper.addEntryofEmpInfo(Employe_Name,Employe_FNAME,Employe_Cnic,Dateofbirth,Gender,
                            Address,Province,sDistrict,stehsil,sVillage,sUnioncouncil, phoneno,Emailaddress,PID
                            ,Employe_Occupation, sScale,Employe_Current_Dept,sJoiningdate,FingerPrintID, mVerifyTemplate) == true) {
                        setProgressBarVisibility(false);
                        Toast.makeText(BiometricRegistration.this, "Submitted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(BiometricRegistration.this, AdminRole.class));
                    } else {
                        Toast.makeText(BiometricRegistration.this, "not Submitted", Toast.LENGTH_SHORT).show();
                        setProgressBarVisibility(false);
                    }
                    debugMessage("MATCHED!!\n");
                } else {
                    mTextViewResult.setText("NOT MATCHED!!");
                    //  this.mCheckBoxMatched.setChecked(false);
                    debugMessage("NOT MATCHED!!\n");
                }
                mVerifyImage = null;
                fpInfo = null;
                matched = null;

            }
        });


        grayBuffer = new int[JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES * JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES];
        for (int i = 0; i < grayBuffer.length; ++i)
            grayBuffer[i] = Color.GRAY;
        grayBitmap = Bitmap.createBitmap(JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES, Bitmap.Config.ARGB_8888);
        grayBitmap.setPixels(grayBuffer, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES, 0, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES);
        // mImageViewFingerprint.setImageBitmap(grayBitmap);

        int[] sintbuffer = new int[(JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES / 2) * (JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES / 2)];
        for (int i = 0; i < sintbuffer.length; ++i)
            sintbuffer[i] = Color.GRAY;
        Bitmap sb = Bitmap.createBitmap(JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES / 2, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES / 2, Bitmap.Config.ARGB_8888);
        sb.setPixels(sintbuffer, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES / 2, 0, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES / 2, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES / 2);
        mImageViewRegister.setImageBitmap(grayBitmap);
        mImageViewVerify.setImageBitmap(grayBitmap);
        mMaxTemplateSize = new int[1];
        //USB Permissions
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(mUsbReceiver, filter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            sgfplib = new JSGFPLib((UsbManager) getSystemService(Context.USB_SERVICE));
        }
        //this.mToggleButtonSmartCapture.toggle();
        bSecuGenDeviceOpened = false;
        usbPermissionRequested = false;
        //debugMessage("Starting Activity\n");
        //debugMessage("jnisgfplib version: " + Integer.toHexString((int) sgfplib.Version()) + "\n");
        mLed = true;
        mAutoOnEnabled = true;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onPause() {
        //Log.d(TAG, "onPause()");
        if (bSecuGenDeviceOpened) {

            EnableControls();
            sgfplib.CloseDevice();
            bSecuGenDeviceOpened = false;
        }
        unregisterReceiver(mUsbReceiver);
        mRegisterImage = null;
        mVerifyImage = null;
        mRegisterTemplate = null;
        mVerifyTemplate = null;
        // mImageViewFingerprint.setImageBitmap(grayBitmap);
        mImageViewRegister.setImageBitmap(grayBitmap);
        mImageViewVerify.setImageBitmap(grayBitmap);
        super.onPause();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onResume() {
        //Log.d(TAG, "onResume()");
        super.onResume();
        DisableControls();
        registerReceiver(mUsbReceiver, filter);
        long error = sgfplib.Init(SGFDxDeviceName.SG_DEV_AUTO);
        if (error != SGFDxErrorCode.SGFDX_ERROR_NONE) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
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
            dlgAlert.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();

                }
            });
            dlgAlert.setCancelable(false);
            dlgAlert.create().show();
        } else {
            UsbDevice usbDevice = sgfplib.GetUsbDevice();
            if (usbDevice == null) {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
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

    private final BroadcastReceiver mUsbReceiverr = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //DEBUG Log.d(TAG,"Enter mUsbReceiver.onReceive()");
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            //DEBUG Log.d(TAG, "Vendor ID : " + device.getVendorId() + "\n");
                            //DEBUG Log.d(TAG, "Product ID: " + device.getProductId() + "\n");
                            debugMessage("Vendor ID : " + device.getVendorId() + "\n");
                            debugMessage("Product ID: " + device.getProductId() + "\n");
                        } else
                            Log.e(TAG, "mUsbReceiver.onReceive() Device is null");
                    } else
                        Log.e(TAG, "mUsbReceiver.onReceive() permission denied for device " + device);
                }
            }
        }
    };

    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onDestroy() {
        //Log.d(TAG, "onDestroy()");
        sgfplib.CloseDevice();
        mRegisterImage = null;
        mVerifyImage = null;
        mRegisterTemplate = null;
        mVerifyTemplate = null;
        sgfplib.Close();
//    	unregisterReceiver(mUsbReceiver);
        super.onDestroy();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
    //Converts image to grayscale (NEW)
    public Bitmap toGrayscale(byte[] mImageBuffer, int width, int height) {
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

    public void DumpFile(final String fileName, final byte[] buffer) {// Uncomment section below to dump images and templates to SD card

        try {
            File myFile = new File("/sdcard/Download/" + fileName);
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            fOut.write(buffer, 0, buffer.length);
            fOut.close();
        } catch (Exception e) {
            debugMessage("Exception when writing file" + fileName);
        }


        //DatabaseHelper databaseHelper=new DatabaseHelper(this);
        //databaseHelper.addEntry(fileName,buffer);
    }


    public void CaptureFingerPrint() {
        long dwTimeStart = 0, dwTimeEnd = 0, dwTimeElapsed = 0;
        this.mCheckBoxMatched.setChecked(false);
        byte[] buffer = new byte[mImageWidth * mImageHeight];
        dwTimeStart = System.currentTimeMillis();
        //long result = sgfplib.GetImage(buffer);
        long result = sgfplib.GetImage(buffer);
        String NFIQString = null;
        dwTimeEnd = System.currentTimeMillis();
        dwTimeElapsed = dwTimeEnd - dwTimeStart;
        debugMessage("getImageEx(10000,50) ret:" + result + " [" + dwTimeElapsed + "ms]" + NFIQString + "\n");
        DumpFile("capture2016.raw", buffer);

        mTextViewResult.setText("getImageEx(10000,50) ret: " + result + " [" + dwTimeElapsed + "ms] " + NFIQString + "\n");
        //mImageViewFingerprint.setImageBitmap(this.toGrayscale(buffer));

        // buffer = null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(BiometricRegistration.this, EmployeeRegistration1.class));
    }


}