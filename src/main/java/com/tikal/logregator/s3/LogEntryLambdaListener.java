package com.tikal.logregator.s3;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tikal.logregator.LogEntry;
import com.tikal.logregator.LogSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

/**
 * Created by haimcohen on 07/12/2016.
 */
public class LogEntryLambdaListener implements RequestHandler<S3Event, String> {

    Logger logger = LoggerFactory.getLogger(LogEntryLambdaListener.class);

    @Override
    public String handleRequest(S3Event input, Context context) {
        AmazonS3 s3Client = new AmazonS3Client();
        List<S3EventNotification.S3EventNotificationRecord> records = input.getRecords();
        ObjectMapper mapper = new ObjectMapper();
        LogSender kafkaSender = new LogSender();
        records.forEach(record -> {
            String srcBucket = record.getS3().getBucket().getName();
            // Object key may have spaces or unicode non-ASCII characters.
            String srcKey = record.getS3().getObject().getKey();

            try {
                logger.info("Param from env:" + System.getenv("TEST_KEY"));
                S3Object s3Object = s3Client.getObject(new GetObjectRequest(
                        srcBucket, srcKey));

                InputStream is = s3Object.getObjectContent();
                InputStreamReader r = new InputStreamReader(is);
                BufferedReader reader = new BufferedReader(r);
                String line = null;
                while((line = reader.readLine()) != null) {
                    LogEntry entry = LogEntry.parse(line);
                    String json = mapper.writeValueAsString(entry);
                    //TODO Send file to kafka
                    logger.info("sending to kafka the following json: " + json);
                    kafkaSender.sendMessage(json);
                }
                logger.info("Handling bucket " + srcBucket + " and key " + srcKey);
            } catch (IOException e) {
                logger.error("Unable handling s3 object " + srcBucket+"/"+srcKey+": " + e.getMessage(), e);
            }

        });



        return null;
    }
}
