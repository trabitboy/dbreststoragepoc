package org.trab.test.dbreststorage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.trab.test.dbreststorage.service.DocService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class MajorVersionController {

	@Autowired
	DocService docService;
	
	@GetMapping(value = {"/majorversion/ping"}, produces = "application/json")
	public ResponseEntity<String> exportNotification(
			
	) {
		return new ResponseEntity<>("ping ok", HttpStatus.OK);
	}

	
//	@GetMapping(value = {"/majorversion/ping"}, produces = "application/json")
//	public ResponseEntity<String> exportNotification(
//			
//	) {
//		return new ResponseEntity<>("ping ok", HttpStatus.OK);
//	}
	

	@PostMapping(value = {"/majorversion/createFromName"}, produces = "application/json")
	public ResponseEntity<String> createMajorVersion(
			@RequestBody String json
	) {
//		System.out.println(json
//				);
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode actualObj = mapper.readTree(json);
			String name = actualObj.get("name").asText();
			String xml=actualObj.get("xml").asText();
//			System.out.println(xml);
			long id=docService.saveMajorVersionWithXml(name, xml);
			String ret=Long.toString(id);
			return new ResponseEntity<>(ret, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("post NOT ok", HttpStatus.OK);
		}
	}

	

}
