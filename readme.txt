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
  -generate creation script ok
- intercept/retrieve oracle creation script
  
  
TODO 
- jmeter create scenario 
- jmeter retrieve and save scenario
	- getmin version
	- save min version
- test with jmeter > with metrics
- http client save scenario
- run on oracle


QUESTIONS FOR DEVS
how to parameterize datasource when running as a jar - externalize dbprops
how to maximize availability (config of tomcat threadpool, 
	for tuning of hibernate singleton persistence layer vs tons of requests
	numbr of beans prototype vs singleton ; does spring prototype = false help)
mission control>do I use it -- follow up cpu and memory
how to have automatic json translation in controller

what to use for fastest jdbc approach ? spring data instead of template?
what advantage for spring data ?
how to configure/check default jdbc connection pool/parameterize it
which oracle driver, is there a native one to bundle?
how to run jdbc template and hibernate in the same spring boot project
- hibernate for schema definition and prototyping,
- some jdbc template for speed

12 h work so far