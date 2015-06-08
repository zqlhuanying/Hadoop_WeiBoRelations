package WUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by root on 15-5-17.
 */
public class DateUtil {

    public static final String date2String(Date date){
        return date2String(date, "yyyy:MM:dd HH:mm:ss");
    }

    public static final String date2String(Date date, String format){
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }
}
