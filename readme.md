To build a package:
"./gradlew build"

To run the package:
"java -jar build/libs/credit-suisse-all-1.0-SNAPSHOT.jar src/test/resources/original-example.log"
where the last argument can be replaced with other log file.

To do:
* create unit tests to most classes
* use more database connections (ideally one per thread)
