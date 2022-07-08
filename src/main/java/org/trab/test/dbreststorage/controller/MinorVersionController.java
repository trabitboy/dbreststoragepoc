package org.trab.test.dbreststorage.controller;

import javax.persistence.OptimisticLockException;

import org.hibernate.StaleStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.trab.test.dbreststorage.service.DocService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
public class MinorVersionController {

	ObjectMapper mapper = new ObjectMapper();

	
	@Autowired
	DocService docService;
	
	
	Logger logger = LoggerFactory.getLogger(MinorVersionController.class);
	
	//alternate to json embed the xml
	// just because jmeter and eclise text editors dont like 110k on one line
	@PostMapping(path="/minorversion/multipart/createFromXml/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> uploadFile(
//			@RequestBody String jmeterJson, //we can inject variables in jmeter scripts (not working, cant get jmeter to send body +2 multipart files
			@RequestParam("metadata") String json, 
			@RequestParam("file") MultipartFile file) 
	{
		try {
			
			
			JsonNode actualObj = mapper.readTree(json);
			String cuid= actualObj.get("cuid").asText();
			String xml=new String(file.getBytes());
			boolean wait = actualObj.get("wait").asBoolean();
//			System.out.println(xml);
			long id=docService.saveMinorVersionFromCuid(cuid, xml,wait);
			String ret=Long.toString(id);
			return new ResponseEntity<>("save success "+ret, HttpStatus.OK);
		} catch (StaleStateException e) {
//			e.printStackTrace();
			// optimistick lock is a feature, should not cause request to fail (business exception)
			logger.error("optimistic lock");
			return new ResponseEntity<>("concurrency error", HttpStatus.OK);
		} catch (OptimisticLockException e) {
//			e.printStackTrace();
			// optimistick lock is a feature, should not cause request to fail (business exception)
			logger.error("optimistic lock");
			return new ResponseEntity<>("concurrency error", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("post NOT ok", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	
	
	@PostMapping(value = {"/minorversion/createFromXml"}, produces = "application/json")
	public ResponseEntity<String> userEdits(
			@RequestBody String json
	) {
		
		try {
			JsonNode actualObj = mapper.readTree(json);
			Long mavid= actualObj.get("mavid").asLong();
			String xml=actualObj.get("xml").asText();
//			System.out.println(xml);
			long id=docService.saveMinorVersionFromMavId(mavid, xml);
			String ret=Long.toString(id);
			return new ResponseEntity<>(ret, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("post NOT ok", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}		

}
