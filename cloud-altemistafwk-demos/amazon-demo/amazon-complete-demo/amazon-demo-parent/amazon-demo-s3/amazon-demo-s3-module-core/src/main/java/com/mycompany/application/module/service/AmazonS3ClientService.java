package com.mycompany.application.module.service;

import org.springframework.web.multipart.MultipartFile;


public interface AmazonS3ClientService {
	
    public void uploadFileToS3Bucket(MultipartFile multipartFile, boolean enablePublicReadAccess);

    public void deleteFileFromS3Bucket(String fileName) ;

}
