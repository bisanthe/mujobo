package com.mujobo.util;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: eguller
 * Date: 2/21/13
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class Util {

    public static String uuidPure(){
       return UUID.randomUUID().toString().replace("-","");
    }



    public static void main(String[] args){
        String i1 = uuidPure();
        String i2 = uuidPure();
        System.err.println("");
    }
}
