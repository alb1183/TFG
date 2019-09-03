SET  COMPOSER_CARD=admin@iot-bna
SET  COMPOSER_NAMESPACES=never
SET  COMPOSER_AUTHENTICATION=true
SET  COMPOSER_PROVIDERS={"github": {"provider": "github", "module": "passport-github", "clientID": "492c315d47d515c8ab0c", "clientSecret": "c83eb56bf7b21c990dcd3437f9a8a33a5a9bfb24", "authPath": "/auth/github", "callbackURL": "/auth/github/callback", "successRedirect": "/", "failureRedirect": "/"}}
SET  COMPOSER_DATASOURCES={"db": {"name": "db", "host": "cluster0-qwdnd.mongodb.net", "port": 27017, "database": "test", "user": "alberto", "password": "2019", "connector": "mongodb" }}

composer-rest-server
