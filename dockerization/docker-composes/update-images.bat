echo -------------------------------
echo Account Balance Management Function
echo -------------------------------
mvn clean compile jib:build -pl account-balance-management-function -Dimage=beko2001/account-balance-management-function

echo -------------------------------
echo Account Order Management
echo -------------------------------
mvn clean compile jib:build -pl account-order-management -Dimage=beko2001/account-order-management

echo -------------------------------
echo Charging Gateway Function
echo -------------------------------
mvn clean compile jib:build -pl charging-gateway-function -Dimage=beko2001/charging-gateway-function

echo -------------------------------
echo Notification Function
echo -------------------------------
mvn clean compile jib:build -pl notification-function -Dimage=beko2001/notification-function

echo -------------------------------
echo Online Charging System
echo -------------------------------
mvn clean compile jib:build -pl online-charging-system -Dimage=beko2001/online-charging-system

echo -------------------------------
echo Diameter Gateway
echo -------------------------------
mvn clean compile jib:build -pl diameter-gateway -Dimage=beko2001/diameter-gateway

echo -------------------------------
echo TUM IMAGELAR BUILD EDILDI.
echo -------------------------------
