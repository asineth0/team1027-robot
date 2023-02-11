package frc.robot;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    public static void log(String val) {
        System.out.println(String.format("[asilog] %s: %s",
                new SimpleDateFormat("HH:mm:ss.SSS").format(new Date(System.currentTimeMillis())),
                val));
    }
}
