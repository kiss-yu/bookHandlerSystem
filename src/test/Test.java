import javax.xml.crypto.Data;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Kiss
 * @date 2018/05/05 0:08
 */
public class Test {
    @org.junit.Test
    public void test() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse("2018-05-24");
            Date now = format.parse(format.format(new Date()));
            System.out.println(format1.format(date));
            System.out.println(format1.format(now));
            System.out.println((date.getTime() - now.getTime())/(24*60*60*1000));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
