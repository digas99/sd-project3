package client.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConnectionData {

    private String filePath;
    private File configFile;
    private Properties properties;

    public ConnectionData(String filePath) throws FileNotFoundException, IOException {
        this.filePath = filePath;
        this.configFile = new File(filePath);
        this.properties = new Properties();

        if (!configFile.exists())
            throw new FileNotFoundException("File does not exist at: " + filePath);

        try (FileInputStream in = new FileInputStream(configFile)) {
            properties.load(in);
        }
    }

    public String getFilePath() {
        return filePath;
    }

    public File getConfigFile() {
        return configFile;
    }

    public String getMachine(String key) {
        return properties.getProperty(key + "_MACHINE");
    }

    public int getPort(String key) {
        return Integer.parseInt(properties.getProperty(key + "_PORT"));
    }
}
