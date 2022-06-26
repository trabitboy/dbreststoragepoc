package org.trab.test.dbreststorage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.trab.test.dbreststorage.service.DocService;

@RestController
public class MinorVersionController {

	@Autowired
	DocService docService;
	
	@PostMapping(value = {"/minorversion/createFromXml"}, produces = "application/json")
	public ResponseEntity<String> userEdits(
			@RequestBody String json
	) {
		
		//TODO make service to save miv from mav id and xml
//		docSer vice.
		String ret="TODO not finished";
		return new ResponseEntity<>(ret, HttpStatus.OK);
	}
}
