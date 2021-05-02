Technologies used
**************************************
Spring boot
REST
Apache Kafka
CQRS
Event Driven Architecture
logback
lombook
Junit


Docker compose
*************************************
docker-compose -f docker-compose.yml up -d

swagger UI
**************************************
http://localhost:8888/price-monitor/swagger-ui.html

Resources endpoints
****************************************
Create Tick:
---------------------------
POST http://localhost:8888/price-monitor/api/v1/ticks

Fetch statistics
------------------------------
GET http://localhost:8888/price-monitor/api/v1/statistics

Fetch statistics by instrument
--------------------------------
GET http://localhost:8888/price-monitor/api/v1/statistics/IBM



run test cases
**********************************************
execute the below command from the root project directory (price-monitor)

mvn clean test


Run the application
**********************************************
jar path : tick-price-monitor\target\tick-price-monitor-0.0.1-SNAPSHOT.jar

Command : java -jar tick-price-monitor-0.0.1-SNAPSHOT.jar
