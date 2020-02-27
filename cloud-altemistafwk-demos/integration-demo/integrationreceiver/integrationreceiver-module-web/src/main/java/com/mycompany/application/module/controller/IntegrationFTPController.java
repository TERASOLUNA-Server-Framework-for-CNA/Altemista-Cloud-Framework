package com.mycompany.application.module.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.application.module.service.IntegrationFTPService;

@RestController
@RequestMapping("ftp")
public class IntegrationFTPController {
	
	@Autowired(required = false)
	private IntegrationFTPService integrationFTPService;

	@RequestMapping("send")
	public ResponseEntity<Map<String,String>> sendFileToFtp(@RequestParam String filePath) {
		Map<String,String> result = new HashMap<String,String>();
		if (integrationFTPService!=null) {
			integrationFTPService.sendFile(filePath);
			result.put("result", "File sended!!!");
		} else {
			result.put("result", "Ftp Service Null");
		}
		return ResponseEntity.ok(result);
	}
	
//	@RequestMapping("get")
//	public ResponseEntity<Map<String,String>> getFileFromFtp(@RequestParam String filePath) {
//		Map<String,String> result = new HashMap<String,String>();
//		if (integrationFTPService!=null) {
//			integrationFTPService.getFile(filePath);
//			result.put("result", "File received!!!");
//		} else {
//			result.put("result", "Ftp Service Null");
//		}
//		return ResponseEntity.ok(result);
//	}
}
