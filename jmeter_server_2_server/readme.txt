in this folder is a server to server test for 100 users working on 20 documents
- upload it to your server 
- set the absolute path of "multipart" folder on your server at start of .jmx file
- unzip jmeter on server
- in apache-jmeter/bin , chmod +x jmeter.sh
- run 
./jmeter -n -t  ../../multipart/saveminorversion/saveminorversiontestmanualedit20pack100users.jmx -l test100.csv

- you can now download test100.csv
- replace , by ; in notepad++
- open with excel, suppress first row, do average of time