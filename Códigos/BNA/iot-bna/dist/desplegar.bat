composer archive create -t dir -n ../

composer network install -a iot-bna@0.0.1.bna -c PeerAdmin@hlfv1

composer network start -c PeerAdmin@hlfv1 -n iot-bna -V 0.0.1 -A admin -S adminpw


composer card delete -c admin@iot-bna
composer card import -f admin@iot-bna.card


composer-rest-server -c admin@iot-bna -n always -u true


SET  COMPOSER_CARD=admin@iot-bna
SET  COMPOSER_NAMESPACES=never
SET  COMPOSER_AUTHENTICATION=true
#SET  COMPOSER_PROVIDERS={"local": {"provider": "local", "module": "passport-local", "usernameField": "username", "passwordField": "password", "authPath": "/auth/local", "callbackURL": "/auth/local/callback", "successRedirect": "/", "failureRedirect": "/", "setAccessToken": true, "callbackHTTPMethod": "post"}}
#SET  COMPOSER_PROVIDERS={"jwt": {"provider": "jwt", "module": "L:\\Datos\\HLF-Vagrant-Dev-Setup\\yeoman\\iot-bna\\dist\\custom-jwt.js", "secretOrKey": "gSi4WmttWuvy2ewoTGooigPwSDoxwZOy", "authScheme": "saml", "successRedirect": "/", "failureRedirect":"/"}}
#SET  COMPOSER_PROVIDERS={"jwt": {"provider": "jwt", "module": "passport-jwt", "secretOrKey": "gSi4WmttWuvy2ewoTGooigPwSDoxwZOy", "authScheme": "saml", "successRedirect": "/", "failureRedirect":"/"}}
SET  COMPOSER_PROVIDERS={"jwt": {"provider": "jwt", "module": "custom-jwt.js", "secretOrKey": "mBikxDAzkmh7eC3yZUOq5ZIFHz9rhF9h", "authScheme": "saml", "successRedirect": "/", "failureRedirect":"/"}}
#eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0aW1lc3RhbXAiOjE1NTU1ODczNTgsInVzZXJuYW1lIjoiYWxiZXJ0byJ9.3SKZabM6pu5Ig6-zx_vA4Oy_QG6r2mi3dgoT8uLoYl0

SET  COMPOSER_DATASOURCES={"db": {"name": "db", "host": "localhost", "connector": "mongodb" }}

composer-rest-server