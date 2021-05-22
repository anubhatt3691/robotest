package com.testrobo.demo.controller;



import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.transform.stream.StreamResult;

import org.springframework.core.env.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.testrobo.demo.model.DemoApplicationModel;
import com.testrobo.demo.service.DemoApplicationService;

@RestController
public class DemoApplicationController {
	
	@Autowired
	Environment env;
	
	@Autowired
	DemoApplicationService service;
	
	@PostMapping(value ="/getOutput", produces="application/xml")
	public DemoApplicationModel getOutput(@RequestParam("file") MultipartFile file) {
		
		String path = env.getProperty("file.upload");
		Path location = Paths
                .get(env.getProperty("file.upload"));
		Path checkLoc = Paths
                .get(path + StringUtils.cleanPath(file.getOriginalFilename()));
		try {
			
			Files.deleteIfExists(checkLoc);
			Files.copy(file.getInputStream(), location.resolve(file.getOriginalFilename()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Path copyLocation = Paths.get(path+ StringUtils.cleanPath(file.getOriginalFilename()));
		System.out.println(copyLocation);
		return service.getOutput(copyLocation.toString());
	}
	
	@PostMapping(value="/getOutputFile",produces="application/xml")
	public void getOutputFile(@RequestParam("file") MultipartFile file){
		
		String path = env.getProperty("file.upload");
		Path location = Paths
                .get(env.getProperty("file.upload"));
		Path checkLoc = Paths
                .get(path + StringUtils.cleanPath(file.getOriginalFilename()));
		try {
			
			Files.deleteIfExists(checkLoc);
			Files.copy(file.getInputStream(), location.resolve(file.getOriginalFilename()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DemoApplicationModel model  = service.getOutput(
				Paths.get(path+ StringUtils.cleanPath(file.getOriginalFilename())).toString());
		service.generateOutputXmlFile(model);
	
		
	}
}
