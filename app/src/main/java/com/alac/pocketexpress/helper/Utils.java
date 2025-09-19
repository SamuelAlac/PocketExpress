package com.alac.pocketexpress.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils {

    public static Bitmap getImage(byte[] image){

        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException{
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int buffersize = 1024;
        byte[] buffer = new byte[buffersize];
        int length = 0;
        while((length = inputStream.read(buffer)) != -1){
            byteBuffer.write(buffer, 0, length);
        }
        return byteBuffer.toByteArray();
    }
}
