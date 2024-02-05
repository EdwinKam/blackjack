# blackjack
deployment: https://dashboard.render.com/web/srv-cmav2uta73kc73bnncp0/events
# Java
this project uses java 17
# how to set up environent
1. make sure you using java17
2. create .dev.properties in root folder, copy from here https://docs.google.com/document/d/1CulwAb-T_3PKN_JoUklx5a7kCdYEZgnKHcgHz0h-UQ8/edit
3. go to mongo db website, login and add you ip to cluster. https://cloud.mongodb.com/v2/6599c7ccd12c135b062bf2a7#/security/network/accessList
# common start up error
1. `com.mongodb.MongoSocketWriteException: Exception sending message` -> make sure you added your ip in mongo website
2. `Exception authenticating MongoCredential{mechanism=SCRAM-SHA-1, userName=...` -> user password in .dev.properties could be wrong. Account password and user password of database are different
# How to run it locally
`./gradlew build`
`./gradlew bootRun`