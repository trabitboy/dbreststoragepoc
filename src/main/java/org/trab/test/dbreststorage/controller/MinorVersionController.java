package org.trab.test.dbreststorage.controller;

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
	
	
	
	

	
	//alternate to json embed the xml
	// just because jmeter and eclise text editors dont like 110k on one line
	@PostMapping(path="/minorversion/multipart/createFromXml/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> uploadFile(
			@RequestParam("metadata") String json, 
			@RequestParam("file") MultipartFile file) 
	{
		try {
			JsonNode actualObj = mapper.readTree(json);
			String cuid= actualObj.get("cuid").asText();
			String xml=new String(file.getBytes());
//			System.out.println(xml);
			long id=docService.saveMinorVersionFromCuid(cuid, xml);
			String ret=Long.toString(id);
			return new ResponseEntity<>(ret, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("post NOT ok", HttpStatus.OK);
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
			return new ResponseEntity<>("post NOT ok", HttpStatus.OK);
		}
		
		//TODO make service to save miv from mav id and xml
//		docSer vice.
//		String ret="TODO not finished";
//		return new ResponseEntity<>(ret, HttpStatus.OK);
	}
	
//	@PostMapping(value = {"/minorversion/createFromMavId"}, produces = "application/json")
//	public ResponseEntity<String> createMajorVersion(
//			@RequestBody String json
//	) {
////		System.out.println(json
////				);
//		try {
//			JsonNode actualObj = mapper.readTree(json);
//			String name = actualObj.get("mavid").asText();
//			String xml=actualObj.get("xml").asText();
////			System.out.println(xml);
//			long id=docService.saveMajorVersionWithXml(name, xml);
//			String ret=Long.toString(id);
//			return new ResponseEntity<>(ret, HttpStatus.OK);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return new ResponseEntity<>("post NOT ok", HttpStatus.OK);
//		}
//	}

}
