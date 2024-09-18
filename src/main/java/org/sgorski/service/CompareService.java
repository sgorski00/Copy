package org.sgorski.service;

import org.sgorski.Compare;

public class CompareService {
    public static boolean compare(Compare compare, int length, String filename){
        int fileNameLength = filename.length();
        //min version - 1.14

//            return switch (compare){
//                case LESS -> fileNameLength < length;
//                case LESSEQUAL -> fileNameLength <= length;
//                case EQUAL -> fileNameLength == length;
//                case GREATEREQUAL -> fileNameLength >= length;
//                case GREATER -> fileNameLength > length;
//            };


        //older versions
        if(compare.equals(Compare.LESS))
            return fileNameLength < length;
        else if(compare.equals(Compare.LESSEQUAL))
            return fileNameLength <= length;
        else if(compare.equals(Compare.EQUAL))
            return fileNameLength == length;
        else if(compare.equals(Compare.GREATEREQUAL))
            return fileNameLength >= length;
        else if(compare.equals(Compare.GREATER))
            return fileNameLength > length;
        else
            return false;
    }
}
