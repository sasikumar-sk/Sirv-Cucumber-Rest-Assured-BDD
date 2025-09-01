package support;

import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Properties p = new Properties();

    static {
        try (InputStream is = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            p.load(is);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String get(String key) {
        return p.getProperty(key);
    }
}
