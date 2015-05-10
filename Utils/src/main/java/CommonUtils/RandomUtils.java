package CommonUtils;

import java.util.Random;

/**
 * Created by root on 15-4-28.
 */
public class RandomUtils {

    private final static Character[] name = {'a','b','c','d','e','f','g','h','i','j','k','A','B','C','D','E','F','G','H','I','J','K'};

    // 随机生成6个英文字母的名字
    public static String random(){
        StringBuffer str = new StringBuffer();
        for(int i = 0; i < 6; i++){
            str.append(name[new Random().nextInt(name.length)]);
        }
        return str.toString();
    }
}
