#Census records indexer using Java 

A useful application where user can download and index images of census record from a Java server. Front end was coded using Swing, 

## Installation

Install SQLite Manager via Firefox add-ons 

## Import

Import existing users into SQLite:

```
java -jar recordDataImporter.jar Records/Records.xml 
```

## Access the Database


1) Go to firefox -> Tools -> SQLite Manager
2) SQLite Manager -> Connect Database and choose database/record.sqlite
3) Click on User table to find a list of pre-define users e.g. user:test1 pwd:test1

This will start a server on localhost with the desired port or a default port of 8088 if [port] is left blank. 

## Start Server

```
java -jar recordServer.jar [port] 

```

This will start a server on localhost with the desired port or a default port of 8088 if [port] is left blank. 


## Start Client

```
java -jar recordClient.jar [server] [port]

```

This will connect the client to the desired server. e.g java -jar recordClient.jar localhost 8088 


## Other

/doc/index.html- contains the API
/src - contain both client and server source code


