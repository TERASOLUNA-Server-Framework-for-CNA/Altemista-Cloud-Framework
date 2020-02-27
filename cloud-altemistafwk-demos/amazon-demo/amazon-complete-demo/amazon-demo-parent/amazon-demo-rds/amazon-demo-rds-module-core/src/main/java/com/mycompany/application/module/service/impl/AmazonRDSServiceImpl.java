package com.mycompany.application.module.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.application.module.amazon.SimpleDatabaseService;
import com.mycompany.application.module.service.AmazonRDSService;

@Service
public class AmazonRDSServiceImpl implements AmazonRDSService{
	
	@Autowired
	SimpleDatabaseService simpleDateBase;
	
	public void AmazonRDSServiceImpl(){
		
	}

	public void insertRow(String description){
		simpleDateBase.insert(description);
	}
}
