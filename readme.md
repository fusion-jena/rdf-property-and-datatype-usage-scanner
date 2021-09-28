How to start the experiment:
A) Preparation:
  1) mvn clean
  2) mvn compile
  3) mvn package
B) Run:
  1) java -jar target/CreateDatabase.jar
     to create the database
  2) java -jar target/Measure.jar
     to start the measure

For automatic restart on Linux:
  1) Edit crontab:
    crontab -e
    add the line:
    @reboot bash <path to script>restartMeasure.sh
    keep an empty line at the end of the document

  2) Update the path in restartMeasure.sh to your working directory
