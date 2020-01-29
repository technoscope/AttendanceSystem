package com.example.dell.attendancesystem.myapplication.BiometricStuff;

public class FingerprintTemplate {
    public int[] getmMaxTemplateSize() {
        return mMaxTemplateSize;
    }

    public void setmMaxTemplateSize(int[] mMaxTemplateSize) {
        this.mMaxTemplateSize = mMaxTemplateSize;
    }

    public byte[] getmRegisterTemplate() {
        return mRegisterTemplate;
    }

    public void setmRegisterTemplate(byte[] mRegisterTemplate) {
        this.mRegisterTemplate = mRegisterTemplate;
    }

    public FingerprintTemplate(int[] mMaxTemplateSize, byte[] mRegisterTemplate) {
        this.mMaxTemplateSize = mMaxTemplateSize;
        this.mRegisterTemplate = mRegisterTemplate;
    }

    private int[] mMaxTemplateSize;
    private byte[] mRegisterTemplate;

}
