# ILO3 Standalone Remote Console
ILO3 Remote Console as Standalone application

By default connects to host via 443 port and requires modern TLS protocols (needs openssl config file or running nginx+socat)

### How to use
If you decided to use iLO for remote server control, most likely you won't need manual how to use this and fix issues.

First headache is that even latest iLO firmware doesn't bring support for modern TLS protocols, so you'll need either force Java to ignore insecure protocols, or make quick reverse proxy with self-signed certs.

As I don't use Java for anything other than running Minecraft servers, here's minimal nginx config:

```conf
# nginx -c $PWD/nginx.conf
daemon off;
events {}
http {
	include /etc/nginx/mime.types;
	default_type application/octet-stream;
	access_log off;
	error_log ./error.log;
	server {
		listen 127.0.0.1:443 ssl http2;
        ssl_certificate     ./server.crt;
        ssl_certificate_key ./server.key;
		location / {
			proxy_ssl_conf_command Options UnsafeLegacyRenegotiation;
            proxy_ssl_conf_command CipherString DEFAULT@SECLEVEL=0;
			proxy_pass https://192.168.0.100$uri$is_args$args;
		}
	}
}
```

If you decided to run nginx, then you should connect to `127.0.0.1`, just also make sure to forward these two ports:

```sh
socat TCP4-LISTEN:17988,fork,reuseaddr,bind=127.0.0.1 TCP4:192.168.0.100:17988 & \
socat TCP4-LISTEN:17990,fork,reuseaddr,bind=127.0.0.1 TCP4:192.168.0.100:17990
```

And here's this project comes in, download from [Releases](https://github.com/Saiv46/ILO3-Standalone-Remote-Console/releases/latest) or `gradlew build` this repository. Then you can run it like that:

```sh
./bin/ILO3RemCon 127.0.0.1 {cookie.sessionKey}
```

Upstream repo requires a login and password to acquire session for you ... and ultimately fails at this. In this fork, you should log in with your modern browser and take take value of `sessionKey` cookie to paste into terminal (make sure to log out after if you're paranoid enough).
