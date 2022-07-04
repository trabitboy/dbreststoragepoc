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
-- use @version for optimistic locking, on document, or similar approach by selecting before transaction end and throwing runtime business exception
-- optimistic locking using minor version (update of the latest version flag on previous latest version)
-- test by putting 10 sec wait sate in method and doing and update of version number in sql client
>optimistic locking implemented, very rapid operations on same document now error on h2

WIP
-- how to optimize result of query when searching latest version
-- flag?
-- highest number?
simply keep a foreign key to the latest minor version

-- how to make sure that query will not suffer on several documents with 12000 versions
       seems to work ok on oracle
-- > exploded dev database after 5 12000 version packages (12 go), need to request bigger
  


TODO 




-- more operations to load the save > leos does a couple extra fetches

- test with jdbc template, including transaction
-- put number on seq definitions (50) so no individual selects
--alternative to jdbc template
--hibernate dto projection
--https://vladmihalcea.com/the-best-way-to-map-a-projection-query-to-a-dto-with-jpa-and-hibernate/

suggestion of architect (from tuned oracle prod system, lots of users)
spring.datasource.hikari.minimumIdle=10
spring.datasource.hikari.maximumPoolSize=100
spring.datasource.hikari.maxLifetime=2400000

-- remove instantiation of objectmapper on each request as it is singleton
- test application context aware to log all available beans from context

- configure tomcat thread pool

--does more recent java increase performance ?

how to parameterize datasource when running as a jar - externalize dbprops
how to maximize availability (config of tomcat threadpool, 



26H00 h work so far
