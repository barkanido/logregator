package com.tikal.logregator.s3;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tikal.logregator.LogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by haimcohen on 07/12/2016.
 */
public class LogEntryFileTest {

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(LogEntryFileTest.class);
        try {
            File f = new File("sample-data/example-log.txt");
            InputStream is = new FileInputStream(f);
            InputStreamReader r = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(r);
            String line = null;
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            while((line = reader.readLine()) != null) {
                LogEntry entry = LogEntry.parse(line);
                System.out.println(mapper.writeValueAsString(entry));
            }
        } catch (IOException e) {
            logger.error("Unable to parse file: " + e.getMessage(), e);
        }
    }
}
