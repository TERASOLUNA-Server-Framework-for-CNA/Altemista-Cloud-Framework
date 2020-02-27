package com.mycompany.application.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.application.module.service.AmazonRDSService;
import com.mycompany.application.module.service.AmazonS3ClientService;
import com.mycompany.application.module.service.MailSenderService;
import com.mycompany.application.module.service.SqsQueueSender;

@RestController
public class S3Controller {
	
	@Autowired
	private AmazonS3ClientService s3Service;
	@Autowired
	private MailSenderService sesService;
	@Autowired
	private AmazonRDSService rdsService;
	@Autowired
	private SqsQueueSender sqsService;
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public void upload (@RequestPart(value = "file") MultipartFile file) throws IOException {
		
		sqsService.send("Start to upload the file");
		s3Service.uploadFileToS3Bucket( file, true);
		sesService.sendMessage("The file has been uploaded");
		rdsService.insertRow("The file has been uploaded");
		
	}
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public void delete (@RequestParam(value = "file") String fileName) throws IOException {
		
		sqsService.send("Start to delete the file");
		s3Service.deleteFileFromS3Bucket(fileName);
		sesService.sendMessage("The file has been deleted");
		rdsService.insertRow("The file has been deleted");

		
	}

}
