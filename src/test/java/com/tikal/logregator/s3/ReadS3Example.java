package com.tikal.logregator.s3;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by haimcohen on 07/12/2016.
 */
public class ReadS3Example {

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(ReadS3Example.class);
        try {
            AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
            String bucketName = args[0];
            String key = args[1];
            S3Object object = s3Client.getObject(
                    new GetObjectRequest(bucketName, key));

            InputStream is = object.getObjectContent();
            InputStreamReader r = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(r);
            String line = null;
            while((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            logger.error("Main Error", e);
        }
    }

}
