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
-- test overlay in same folder as jar 
-- test oracle connection from server
--optimized way of having first and latest version
--method to create test pkg/doc/mav/miv with n versions  
--cuid field
-- jmeter create scenario 
--test create 1 document 12000 minor versions h2
--test create 1 document 12000 minor versions oracle
--create 12000 versions document
	h2 embedded blows up
	no pb on oracle
-- pass the payload via a multi part 
-- use document cuid as reference for version create and retrieve
--    >rest creation methods that returns id without hydrating all miv
-- creating 12000 versions documents with test method > couple of seconds
-- save one miv on 12000 versions documents around 130 ms
-- load test with 50 users 
-- 50 user concurrent on same doc> system holds up but optimistic locking to implement (latest version flag wrongly set)

WIP
- jmeter retrieve and save scenario
	- get latest (by flag(done) and by higher number(todo for comparison)  ) min version from current parent (major version)
	- save min version
-- how to optimize result of query when searching latest version
-- flag?
-- highest number?
simply keep a foreign key to the latest minor version
-- how to make sure that query will not suffer on several documents with 12000 versions
-- (query on 120000 min versions join mav join doc ?) 
-- create a lot of documents (see variables in jmeter)
-- > exploded dev database after 5, need to request bigger
  
TODO 


-- use @version for optimistic locking, on document, or similar approach by selecting before transaction end and throwing runtime business exception

-- more operations to load the save > leos does a couple extra fetches

- test with jdbc template, including transaction

--alternative to jdbc template
--hibernate dto projection
--https://vladmihalcea.com/the-best-way-to-map-a-projection-query-to-a-dto-with-jpa-and-hibernate/

suggestion of architect (from tuned oracle prod system, lots of users)
spring.datasource.hikari.minimumIdle=10
spring.datasource.hikari.maximumPoolSize=100
spring.datasource.hikari.maxLifetime=2400000

--recommended vm heap 2go
-- 

-- remove instantiation of objectmapper on each request as it is singleton
- test application context aware to log all available beans from context
- try to  increase load on h2 

- configure tomcat thread pool

QUESTIONS FOR COLLEAGUES
ask devs : correct version of ojdbc for our oracle


--does more recent java increase performance ?

arch>why not multipart? (for superthis heavy)
https://www.baeldung.com/sprint-boot-multipart-requests
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

24H30 h work so far