package org.trab.test.dbreststorage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.trab.test.dbreststorage.entity.DocPackage;
import org.trab.test.dbreststorage.service.DocService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
public class PackageController {
	@Autowired
	DocService docService;
	
	@PostMapping(value = {"/package/test/createFromXml"}, produces = "application/json")
	public ResponseEntity<String> exportNotification(
			@RequestBody String json
	) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode actualObj = mapper.readTree(json);
//			String name = actualObj.get("name").asText();
			String xml=actualObj.get("xml").asText();

		
			Long id = docService.createPackage(xml);
			DocPackage pkg=docService.testGetAllPackageInitialized(id);

		    // create a JSON object
		    ObjectNode jpkg = mapper.createObjectNode();
		    jpkg.put("pkgid", pkg.getId());
		    jpkg.put("docid", pkg.getDocuments().get(0).getId());
		    jpkg.put("mavid", pkg.getDocuments().get(0).getMajorVersions().get(0).getId());
		    jpkg.put("mivid", pkg.getDocuments().get(0).getMajorVersions().get(0).getMinorVersions().get(0).getId());
		    jpkg.put("xcid", pkg.getDocuments().get(0).getMajorVersions().get(0).getMinorVersions().get(0).getXmls().get(0).getId());

		    // convert `ObjectNode` to pretty-print JSON
		    // without pretty-print, use `user.toString()` method
		    String ret = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jpkg);
			
			
			
//			String ret="{ \"pkgid\"=\""+id+"\"   }";
		return new ResponseEntity<>(ret, HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>("FAIL", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
