package com.lgcns.dcxandroid;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StringUtil {

    public static String getCurrentDateTime() {
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 hh24:mm");
        return dateFormat.format(currentDate);

    }

    public static String getFileNameWithoutExt(String fileName) {
        int pos = fileName.lastIndexOf(".");
        if (pos > 0 && pos < (fileName.length() - 1)) { //If '.' is not the first or last character.
            fileName = fileName.substring(0, pos);
        }
        return fileName;

    }


}
