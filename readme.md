# RDF Datatype Usage Scanner

This is a tool to survey the usage of datatypes in RDF dataset files (as the [Web Data Commons](http://webdatacommons.org/))  on the web.

# Usage
How to start the experiment:
1. Preparation: Clone the repository and run `mvn clean package` to build the tool.
2. Run:
  * `java -jar target/CreateDatabase.jar` to create the database
  * `./measure.sh` to start the measure
  * `java -jar target/ExportDatabaseToCSV.jar` to export each table of the database as a .csv.gz file

For automatic restart on Linux edit crontab using `crontab -e` and add the line: `@reboot bash <path to script>/measure.sh` (keep an empty line at the end of the document)
