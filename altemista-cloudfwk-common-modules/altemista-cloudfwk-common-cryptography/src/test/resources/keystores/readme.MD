# Test Java key stores reference 

## core-certificate.jks

OpenSSL-generated

* keystore password: `pubks-key`
* public cert alias: `core`

## core-key.jks

OpenSSL-generated

* keystore password: `privks-key`
* key alias: `core-key`
* PKCS12 password: `corep12-key`

## rsa-keypair.jks

Keytool-generated key store with a RSA-encrypter key pair with the alias `myalias`.

`keytool -keystore rsa-keypair.jks -genkey -alias myalias -keyalg RSA -keysize 2048`

* keystore password: `kspassword`
* key pair alias: `myalias`
* key pair password: (leave empty; will use the same as the keystore password)
