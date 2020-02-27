package com.mycompany.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.application.module.amazon.SimpleS3Service;


@RestController
public class HomeController {
	
	@Autowired
	SimpleS3Service simpleS3Service;

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String send(@RequestParam MultipartFile file) {
		simpleS3Service.uploadFileToS3Bucket(file, true);
		
		return "file uploaded !";
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String send(@RequestParam String file) {
		simpleS3Service.deleteFileFromS3Bucket(file);
		
		return "file deleted !";
	}
}
