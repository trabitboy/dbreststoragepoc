hibernate + rest to save and retrieve xml document versions

DONE
  spring boot project with jpa h2 oracle dependencies
  minimal rest service
  minimal jmeter test
  jmeter test with several actions (synchronous)
  hibernate object entities > direct db schema generation
  dao
  call db from rest controller
- add service for transactionality
- port unit test on basic xml save and retrieve
- methods to create a full test package-doc-mav-miv-xml +junit test
- controller method to create test pkg from xml + return json with ids
  
TODO 
- jmeter create scenario 
	- getmin version
	- save min version
- http client save scenario
- jmeter retrieve and save scenario
- test with jmeter
- run on oracle
- intercept/retrieve oracle creation script


QUESTIONS FOR DEVS
how to parameterize datasource when running as a jar - externalize dbprops
how to maximize availability (config of tomcat threadpool, does spring prototype = false help)
how to have automatic json translation in controller
what to use for fastest jdbc approach ? spring data instead of template?
what advantage for spring data ?




12 h work so far