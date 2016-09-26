package reflectionPattern.dataGeneration;

import org.terracotta.statistics.jsr166e.ThreadLocalRandom;
import reflectionPattern.model.knowledge.*;
import reflectionPattern.model.knowledge.quantity.Unit;
import reflectionPattern.model.operational.*;

import java.security.SecureRandom;
import java.util.Set;

/**
 * Created by nagash on 07/09/16.
 */
public class RandomUtils {




    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    public static String randomString( int len ){
        return randomStringRandomLength(len, len);
    }
    public static String randomStringRandomLength( Range lengthRange ) {
        return randomStringRandomLength(lengthRange.inf(), lengthRange.sup());
    }
    public static String randomStringRandomLength( int minLen, int maxLen )
    {
        int len = randInt(minLen, maxLen);
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

    public static int randInt() {
        return ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
    public static int randInt(int min, int max) {
        if(min==max) return min;
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
    public static int randInt(Range range) {
        return randInt(range.inf(), range.sup());
    }


    public static long timeMillis(){
        return (System.currentTimeMillis()); // % 1000);
    }



}


