# RDF Datatype Usage Scanner

This is a tool to survey the usage of properties and datatypes in RDF dataset files (as the [Web Data Commons](http://webdatacommons.org/)) on the web.

# Usage
How to start the experiment:
1. Preparation: Clone the repository and run `mvn clean package` to build the tool.
2. Run:
  * `java -jar target/Scanner.jar --category <category_name> --list <list_file_url> <database_folder>` to schedule files to scan
  * `./measure.sh <database_folder>` to start the measure in background
  * `java -jar target/Scanner.jar --results <result_file.csv.gz> <database_folder>` to export measurements as a gzipped CSV
  * `java -jar target/Scanner.jar --errors <error_file.csv.gz> <database_folder>` to export errors as a gzipped CSV

For automatic restart on Linux edit crontab using `crontab -e` and add the line: `@reboot bash <path to script>/measure.sh  <database_folder>` (keep an empty line at the end of the document)
