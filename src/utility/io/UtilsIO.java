package utility.io;

import java.util.Scanner;

/**
 * Created by nagash on 27/09/16.
 */
public class UtilsIO {

    public static boolean answerNy(String message) {
        System.out.print(message + "\n[y|N] > ");
        Scanner keyboard = new Scanner(System.in);
        String chose = keyboard.nextLine();
        if(chose.equals("y") || chose.equals("Y"))
            return true;
        else return false;
    }
    public static boolean answerYn(String message) {
        System.out.print(message + "\n[Y|n] > ");
        Scanner keyboard = new Scanner(System.in);
        String chose = keyboard.nextLine();
        if(chose.equals("n") || chose.equals("N"))
            return false;
        else return true;
    }
}
