# RDF Datatype Usage Analyser

This is a tool to survey the usage of datatypes in RDF dataset files (as the [Web Data Commons](http://webdatacommons.org/))  on the web.

# Usage
How to start the experiment:
1. Preparation: Clone the repository and run `mvn clean package` to build teh tool.
2. Run:
  * `java -jar target/CreateDatabase.jar` to create the database
  * `java -jar target/Measure.jar` to start the measure
  * `java -jar target/ExportDatabaseToCSV.jar` to export each table of the database as a .csv file

For automatic restart on Linux:
1. Edit crontab using `crontab -e` and add the line: `@reboot bash <path to script>restartMeasure.sh` (keep an empty line at the end of the document)
2. Update the path in restartMeasure.sh to your working directory
