import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DBConfig {
    private static Properties props = new Properties();

    static {
        try {
            FileInputStream fis = new FileInputStream("config.properties");
            props.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getURL() {
        return props.getProperty("DB_URL");
    }

    public static String getUser() {
        return props.getProperty("DB_USER");
    }

    public static String getPass() {
        return props.getProperty("DB_PASS");
    }
}
