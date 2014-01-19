_This is just an incomplete prototype at the moment._

Schematica is a simple Java library to store, update, merge and query JSON documents in a relational database. Schematica is ideal 
for services that send JSON to clients that then modify the document and send it back to the service, which must then 
update/merge the changes made by the client into the stored JSON document -- which may have changed since the client first saw
the document.

The primary features of Schematica include:

* use the standard [JSON-P API (JSR-353)][jsr-353] to create, change, (de)serialize, and read JSON documents
* compare documents
* merge changes from one document into another
* find documents with filters that use the underlying relational database's query capabilities
* process documents with map-reduce
* observable events when stored documents are changed
* support for BSON datatypes and its binary serialization format
* integrates with existing Java transactions via JTA support
* access and query stored documents using JDBC and SQL



[jsr353]: https://jcp.org/en/jsr/detail?id=353
