package com.airbnb.service;

import com.airbnb.exception.EmptyFileException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class BucketService {

    @Autowired
    private AmazonS3 amazonS3;

    public String uploadFile(MultipartFile file, String bucketName) throws IOException {
        if (file.isEmpty()) {
            throw new EmptyFileException("File is empty. Failed to upload.");
        }

        // Create a temporary file
        File tempFile = createTempFile(file);

        try {
            // Upload to S3
            amazonS3.putObject(bucketName, tempFile.getName(), tempFile);

            // Return the public URL of the uploaded file
            return amazonS3.getUrl(bucketName, tempFile.getName()).toString();
        } catch (AmazonS3Exception s3Exception) {
            throw new IllegalStateException("Failed to upload file to S3: " + s3Exception.getMessage(), s3Exception);
        } finally {
            // Clean up the temporary file
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    private File createTempFile(MultipartFile file) throws IOException {

        //fetches the temporary directory path
        File tempFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());

        //creates the file in the temporary directory
        file.transferTo(tempFile);
        return tempFile;
    }
}
