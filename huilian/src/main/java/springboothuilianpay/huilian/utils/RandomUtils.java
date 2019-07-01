package springboothuilianpay.huilian.utils;

import java.util.Random;

public class RandomUtils {
    public static String generateString(int len){
        Random random = new Random();
        String result="";
        for (int i=0;i<len;i++)
        {
            result+=random.nextInt(10);
        }
        return result;
    }
}
