package com.geektrust.backend.loggers;

public class testhash {

        public static void main(String[] args){
            String a = "KID";
            String b = "SENIOR_CITIZEN";
            // case sensitive
            System.out.println(a.compareTo(b)); //-32
            // ignoring case
            System.out.println(b.compareTo(a)); //-32

        }

}
