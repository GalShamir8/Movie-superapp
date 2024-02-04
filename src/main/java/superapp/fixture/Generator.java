package superapp.fixture;

import superapp.data.UserRole;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.Random;


public class Generator {
    private static Random rand = new Random();
    public static String generateString(){
        byte[] array = new byte[7]; // length is bounded by 7
        rand.nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        return generatedString;
    }
    public static String generateEmail(){
        String emailAddress = "";
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        while (emailAddress.length() < 7) {
            int character = (int) (Math.random() * 26);
            emailAddress += alphabet.substring(character, character + 1);
            emailAddress += Integer.valueOf((int) (Math.random() * 99)).toString();
        }
        emailAddress += "@" + "gmail.com";
        return emailAddress;
    }
    public static Date generateDateTime(){
        return new Date();
    }
    public static Boolean generateBool(){
        return  rand.nextBoolean();
    }

    public static UserRole generateUserRole() {
        int index = (int) Math.round(Math.random() * (UserRole.values().length - 1));
        return UserRole.values()[index];
    }
}
