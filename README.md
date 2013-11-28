RawJavaServer
=============

Just a probe of concept to handle HTTP requests without any help of Java Frameworks

If you want to run this application:
* `mvn package` 
* `mvn exec:java`
* Server will be launched in port 8081. There are only three available resources:
  *  [GET]  localhost:8081/\<userId\>/login
  *  [POST] localhost:8081/\<levelId\>/score?sessionkey=\<sessionKey\>
  *  [GET]  localhost:8081/\<levelId\>/highscorelist
*  `mvn test` to pass all unit tests included. But a unit test is disabled due its long-running behaviour. Just set a lower value to SessionKey.MAX_VALID_TIME and enable `testExpiration` test case.
