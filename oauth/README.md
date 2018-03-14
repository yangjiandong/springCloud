spring oauth2
====

- [参考](https://my.oschina.net/LinYuanBaoBao/blog/1613463)
- [spring-security-oauth-jwt](http://www.baeldung.com/spring-security-oauth-jwt)

modules
---

- oauth-config
- oauth-service

keytools
---

> 注意，证书有时间期限

生产证书，Generate JKS Java KeyStore File

Let’s first generate the keys – and more specifically a .jks file – using the command line tool keytool:

```
keytool -genkeypair -alias mytest 
                    -keyalg RSA 
                    -keypass mypass 
                    -keystore mytest.jks 
                    -storepass mypass

```

show mytest.jks info

```
keytool -list  -v -keystore mytest.jks
```

Export Public Key

```
keytool -list -rfc --keystore mytest.jks | openssl x509 -inform pem -pubkey
```

take only our Public key and copy it to our resource server src/main/resources/public.txt


