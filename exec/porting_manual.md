# 🌐 포팅 매뉴얼

# 버전 정보

## 백

- Java - `openjdk 17.0.9 correto`
- Spring - `3.2.3 Gradle, Jar`
- Tomcat - `10.1.19`

## 프론트

- npm - `10.2.4`
- NodeJS - `20.11.0`
- React - `18.2.66`
- Vite - `5.2.0`
- Typescript - `5.2.2`
- 사용 라이브러리
    - Redux
    - axios
    - sematic ui react
    - **monaco-editor/react**
    - **allotment**
    - **openvidu-browser**


## 기타

- EC2 ubuntu - `20.04.6 LTS`
- so - `25.0.4`
- Nginx - `1.25.3`
- MySQL - `8.0.36`
- MySQLWorkbench - `8.0.21`
- Redis - `7.2.4 LTS`
- Sonarqube - `4.2.0.3129`

# 포트 정보

```jsx
80 : 프록시 서버 → 443
443 : 프록시 서버(SSL)

3001 : 프론트 서버
3002 : 백 blue
3003 : 백 green
3100 : 젠킨스 서버
6379 : Redis

3000 : grafana
3101 : loki
9090 : prometheus
9100 : node exporter
9113 : nginx exporter

// Sonarqube는 Ssafy 제공

// openvidu
4442 : nginx
4443 : nginx
3478 : TURN server
5442 : openvidu server(시그널링 서버)
5443 : openvidu based application
8888 : KMS
```

# 변수 및 보안 정보

- 백
  ```yaml
  spring:
    datasource:
      url: jdbc:mysql://db:3306/urturn
      username: {USERNAME}
      password: {PASSWORD}
      driver-class-name: com.mysql.cj.jdbc.Driver
      
    jpa:
      hibernate:
        ddl-auto: update
      properties:
        hibernate:
          format_sql: true
          dialect: org.hibernate.dialect.MySQLDialect
          default_batch_fetch_size: 1000
      open-in-view : false
    
    data:
      redis:
        host: redis
        port: 6379
        password: {REDIS_PASSWORD}
    
    jwt:
      header: Authorization
      secret: {YOUR_SECRET_SSL_ENCRYPTION_CREATED_BY_openssl rand -base64 60}
      access-token-validity-in-seconds: 7200
      refresh-token-validity-in-seconds: 86400

    security:
      debug: true
      oauth2:
        client:
          registration:
            github:
              client-id: {YOUR_CLIENT_ID} 
              client-secret: {YOUR_CLIENT_SECRET} 
            password-salt: {YOUR_PASSWORD_SALT} 

    servlet:
      multipart:
        max-file-size: 20MB
        max-request-size: 25MB


  logging:
    level:
      org.hibernate.SQL: debug
    file:
      path: /logs/springboot
    logback:
      rollingpolicy:
        max-history: 10
        max-file-size: 500MB


  cloud:
    aws:
      s3:
        bucket: {YOUR_BUCKET_NAME}
        region:
          static: ap-northeast-2
      stack.auto: false
      credentials:
        access-key: {YOUR_ACCESS_KEY}
        secret-key: {YOUR_SECRET_KEY}

  aes:
    secret-key: {YOUR_AES128_ENCRYPTION_SECRET_KEY} 
  
  springdoc:
  show-login-endpoint: true

  server:
    forward-headers-strategy: framework

  OPENVIDU_URL: https://{YOUR_SERVICE_DOMAIN}:4443/
  OPENVIDU_SECRET: {OPENVIDU_SECRET}

  grading-server:
    url: http://{GRADING_SERVER_IP}:3000

  management:
    endpoints:
      web:
        exposure:
          include: prometheus, metrics, health

    endpoint:
      health:
        enabled: true
      metrics:
        enabled: true
      prometheus:
        enabled: true

  ```


- 프론트
  ```bash
  VITE_API_BASE_URL={YOUR_DOMAIN_NAME}/api
  VITE_JWT_ACCESS_EXPIRE_TIME=7200
  VITE_JWT_REFRESH_EXPIRE_TIME=86400
  VITE_GITHUB_CLIENT_ID={GITHUB_OAUTH_APPLICATION_KEY}
  VITE_GITHUB_REDIRECT_URI={YOUR_DOMAIN_NAME}/auth/github
  VITE_API_WEBSOCKET_URL=wss://{YOUR_DOMAIN_NAME}/wsapi
  ```

# 방화벽 정보

- `ufw status`를 통해 나오는 정보는 다음과 같다.
  ```jsx
  To                         Action      From
  --                         ------      ----
  22                         ALLOW       Anywhere
  8989                       ALLOW       Anywhere
  443                        ALLOW       Anywhere
  80                         ALLOW       Anywhere
  3306                       ALLOW       Anywhere
  6379                       ALLOW       Anywhere
  33060                      ALLOW       Anywhere
  8888                       ALLOW       Anywhere
  8443                       ALLOW       Anywhere
  8081                       ALLOW       Anywhere
  3478                       ALLOW       Anywhere
  57001:65535/udp            ALLOW       Anywhere
  57001:65535/tcp            ALLOW       Anywhere
  3478/udp                   ALLOW       Anywhere
  3478/tcp                   ALLOW       Anywhere
  40000:57000/tcp            ALLOW       Anywhere
  40000:57000/udp            ALLOW       Anywhere
  8081/tcp                   ALLOW       Anywhere
  8443/tcp                   ALLOW       Anywhere
  22/tcp                     ALLOW       Anywhere
  80/tcp                     ALLOW       Anywhere
  443/tcp                    ALLOW       Anywhere
  8442/tcp                   ALLOW       Anywhere
  4443                       ALLOW       Anywhere
  5443                       ALLOW       Anywhere
  5000                       ALLOW       Anywhere
  3000                       ALLOW       Anywhere
  4442                       ALLOW       Anywhere
  3003                       ALLOW       Anywhere
  9090                       ALLOW       Anywhere
  9100                       ALLOW       Anywhere
  9113                       ALLOW       Anywhere
  22 (v6)                    ALLOW       Anywhere (v6)
  8989 (v6)                  ALLOW       Anywhere (v6)
  443 (v6)                   ALLOW       Anywhere (v6)
  80 (v6)                    ALLOW       Anywhere (v6)
  3306 (v6)                  ALLOW       Anywhere (v6)
  6379 (v6)                  ALLOW       Anywhere (v6)
  33060 (v6)                 ALLOW       Anywhere (v6)
  8888 (v6)                  ALLOW       Anywhere (v6)
  8443 (v6)                  ALLOW       Anywhere (v6)
  8081 (v6)                  ALLOW       Anywhere (v6)
  3478 (v6)                  ALLOW       Anywhere (v6)
  57001:65535/udp (v6)       ALLOW       Anywhere (v6)
  57001:65535/tcp (v6)       ALLOW       Anywhere (v6)
  3478/udp (v6)              ALLOW       Anywhere (v6)
  3478/tcp (v6)              ALLOW       Anywhere (v6)
  40000:57000/tcp (v6)       ALLOW       Anywhere (v6)
  40000:57000/udp (v6)       ALLOW       Anywhere (v6)
  8081/tcp (v6)              ALLOW       Anywhere (v6)
  8443/tcp (v6)              ALLOW       Anywhere (v6)
  22/tcp (v6)                ALLOW       Anywhere (v6)
  80/tcp (v6)                ALLOW       Anywhere (v6)
  443/tcp (v6)               ALLOW       Anywhere (v6)
  8442/tcp (v6)              ALLOW       Anywhere (v6)
  4443 (v6)                  ALLOW       Anywhere (v6)
  5443 (v6)                  ALLOW       Anywhere (v6)
  5000 (v6)                  ALLOW       Anywhere (v6)
  3000 (v6)                  ALLOW       Anywhere (v6)
  4442 (v6)                  ALLOW       Anywhere (v6)
  3003 (v6)                  ALLOW       Anywhere (v6)
  9090 (v6)                  ALLOW       Anywhere (v6)
  9100 (v6)                  ALLOW       Anywhere (v6)
  9113 (v6)                  ALLOW       Anywhere (v6)
  ```

# 서버 환경 구축 방법

- 할당 받은 ec2에서 안전한 환경 구축을 위해 도커를 설치하여 프록시 서버인 Nginx를 제외한 모든 프로그램을 도커 컨테이너로 관리한다.

- 서버 구축에 관련한 docker-compose.yml, Dockerfile과 .env, application.yml등의 보안 정보는 개인 레포지토리`{YOUR PRIVATE GIT REPOSITORY}`에서 관리한다.

- 젠킨스를 통해 배포를 자동화하여 관리하였고 `/var/jenkins_home` 에 settings 디렉토리를 만들어 세팅 파일을 관리하였기에 이후의 스크립트에서 해당 경로가 등장할 수 있다.
- 젠킨스 디렉토리 구조
  ```
  /var/jenkins_home/settings
  ├── front/  : Dockerfile 
  │   ├── static/ : assets, index.html, vite.svg
  │   └── conf.d/ : default.conf
  ├── back/ : Dockerfile, application.yml, back-0.0.1-SNAPSHOT.jar
  ├── monitoring/ : loki-local-config.yaml, prometheus.yml, promtail-local-config.yaml
  │   └── logs/back/ : spring.log.YYYY-MM-DD.0.gz
  ├── jenkins/ : Dockerfile
  ├── docker-compose.yml
  ├── docker-compose.blue.yml
  ├── docker-compose.green.yml
  └── deploy.sh
  ```
- docker-compose.yml : WAS를 제외한 모든 도커 컨테이너에 대한 도커 컴포즈 yml 파일 
- docker-compose.blue.yml, docker-compose.green-yml : WAS 도커 컴포즈 yml 파일 
- deploy.sh : blue-green 배포를 위한 스크립트 파일

# 프로젝트 세팅


- 도커 설치
  ```bash
  apt install -y apt-transport-https ca-certificates curl gnupg-agent software-properties-common
  curl -fsSL https://download.docker.com/linux/ubuntu/gpg | apt-key add -
  add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"

  sudo add-apt-repository --remove ppa:certbot/certbot


  apt update
  apt install -y docker-ce docker-ce-cli containerd.io docker-compose docker-compose-plugin


  systemctl status docker
  ```

- Nginx 설치 및 세팅
  - Nginx를 프록시 서버로 활용하여 배포를 진행한다.
  - Nginx 설치
    ```bash
    sudo touch /etc/apt/sources.list.d/nginx.list
    echo "deb http://nginx.org/packages/ubuntu/ bionic nginx" | sudo tee -a /etc/apt/sources.list.d/nginx.list
    echo "deb-src http://nginx.org/packages/ubuntu/ bionic nginx"| sudo tee -a /etc/apt/sources.list.d/nginx.list

    # 인증 키 등록
    wget http://nginx.org/keys/nginx_signing.key
    sudo apt-key add nginx_signing.key

    # 저장소 업데이트
    sudo apt-get update

    # nginx 설치
    sudo apt-get install nginx
    ```

  - 80번 포트로 들어온 요청은 ssl인증을 받기 위해 443 포트로 리디렉션 된 이후 각 서버로 프록시 패스된다.

  - SSL 인증은 여러가지 방법이 있고, 본 프로젝트는 ZeroSSL로 진행되었다
    1. ZeroSSL
      - [공식 사이트](https://help.zerossl.com/hc/en-us/articles/360058295894-Installing-SSL-Certificate-on-NGINX)
      - 해당 프로젝트에서는 /etc/zerossl 디렉토리를 만들어서 넣어주었다.

    2. Letsencrypt
      - 이 링크에서 설정 방법을 보다 자세히 볼 수 있다. [https://docs.openvidu.io/en/2.29.0/deployment/ce/on-premises/](https://docs.openvidu.io/en/2.29.0/deployment/ce/on-premises/)


  - 백엔드는 개발용과 배포용 서버를 따로 띄워 관리하며 이에 따라 Nginx에서 업스트림으로 이름을 설정한다.
  - default.conf

    ```bash
    upstream api-blue{ 
    server urturn.site:3002;
    }

    upstream api-green{
    server urturn.site:3003;
    }


    server {
    listen 443 ssl;
    listen [::]:443 ssl;
    ssl_certificate /etc/zerossl/certificate.crt;
    ssl_certificate_key /etc/zerossl/private.key;

    include /etc/nginx/conf.d/service-url.inc;

    server_name urturn.site;

    access_log /var/log/nginx/nginx.vhost.access.log;
    error_log /var/log/nginx/nginx.vhost.error.log;

    location / {
            root   /usr/share/nginx/html;
            index  index.html index.htm;

            proxy_pass http://urturn.site:3001;
    }

      location /api { # /api 로 들어오는 요청은 개발용 서버로
            rewrite ^/api/(.*)$ /$1 break;
            proxy_pass $service_url;
            proxy_set_header X-Forwarded-Prefix /api;
            proxy_pass_request_headers on;


      }

      location /wsapi/ {
            rewrite ^/wsapi/(.*)$ /$1 break;
            proxy_pass $service_url;

            #Websocket support
            proxy_http_version 1.1;
            proxy_set_header Upgrade $http_upgrade;
            proxy_set_header Connection "upgrade";
            proxy_set_header Host $host;

            proxy_read_timeout 3600000; # 1 * 60 * 60 * 1000 : 웹소켓 유지를 위한 설정
            proxy_send_timeout 3600000; # 1 * 60 * 60 * 1000 : 웹소켓 유지를 위한 설정

      }

    }

    server {
              listen 80; listen [::]:80;
              server_name k10a206.p.ssafy.io urturn.site;
              return 301 https://urturn.site$request_uri;
    }

    server {
              listen 443; listen [::]:443;
              server_name k10a206.p.ssafy.io;
              return 301 https://urturn.site$request_uri;
    }

    ```

  - service-url.inc (for blue-green 배포)
  ```bash
  set $service_url http://api-blue; # api-blue & api-green 변경하며 라우팅
  ```

  - metrics.conf (for monitoring)
  ```bash
  server {
  listen 80 default_server;
  server_name default_server;

  location /metrics {
    stub_status on; # stub_status 활성화
    allow all; # allow 접근을 허용할 주소 설정
    # deny 접근을 허용하지 않을 주소 설정
    # access_log off;

    }
  }
  ```

* Dockerfile
  * front
    ```docker
    FROM nginx:1.25.3

    EXPOSE 3001

    CMD ["nginx", "-g", "daemon off;"]
    ```
  * Back
    ```docker
    FROM openjdk:17

    USER root

    WORKDIR /

    COPY ./*.jar /app.jar
    COPY application.yml .

    EXPOSE 8080

    CMD ["java",  "-jar", "/app.jar"]
    ```
  * Jenkins
    ```docker
    FROM jenkins/jenkins:lts

    ENV JENKINS_HOME /var/jenkins_home
    ENV CASC_JENKINS_CONFIG /var/jenkins_home/casc_configs

    USER root
    RUN apt-get update && \
        apt-get -y install apt-transport-https ca-certificates curl gnupg2 software-properties-common && \
        curl -fsSL https://download.docker.com/linux/$(. /etc/os-release; echo "$ID")/gpg > /tmp/dkey && \
        apt-key add /tmp/dkey && \
        add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/$(. /etc/os-release; echo "$ID") $(lsb_release -cs) stable" && \
        apt-get update && \
        apt-get -y install docker-ce docker-ce-cli containerd.io docker-compose docker-compose-plugin

    RUN echo "자바 설치" && sleep 2 && \
        apt-get install -y openjdk-17-jdk

    RUN groupadd -f docker
    RUN usermod -aG docker jenkins

    USER jenkins
    ```

* docker-compose.yml

  ```yaml
  version: '3.1'

  services:
    jenkins:
      container_name: jenkins
      networks:
        - urturn
      build:
        context: ./jenkins
        dockerfile: Dockerfile
      image: ${JENKINS_IMAGE_NAME}
      restart: always
      ports:
        - "3100:8080"
        - "50000:50000"
      volumes:
        - /var/jenkins_home:/var/jenkins_home
        - /var/run/docker.sock:/var/run/docker.sock
      user: jenkins

    db:
      container_name: db
      networks:
        - urturn
      image: mysql:8.0.36
      restart: always
      healthcheck:
        test: /bin/sh -c "mysqladmin ping -h db -u urturn -purturn"
        interval: 20s
        timeout: 5s
        retries: 5
        #start_period: 40s
      environment:
        MYSQL_ROOT_PASSWORD: root
        MYSQL_DATABASE: urturn
        MYSQL_USER: urturn
        MYSQL_PASSWORD: urturn206!@
      volumes:
        - data_volume:/var/lib/mysql

    redis:
      container_name: redis
      hostname: redis
      networks:
        - urturn
      image: redis
      restart: always
      healthcheck:
        test: /bin/sh -c "redis-cli -h redis -a urturn!@ ping"
        interval: 20s
        timeout: 5s
        retries: 5
        #start_period: 40s
      command: redis-server --requirepass urturn!@ --port 6379
      ports:
        - "6379:6379"


    front:
      container_name: front
      networks:
        - urturn
      build:
        context: ./front
        dockerfile: Dockerfile
      image: ${FRONT_IMAGE_NAME}
      restart: always
      ports:
        - "3001:3001"
      volumes:
        - ./front/static:/usr/share/nginx/html
        - ./front/conf.d:/etc/nginx/conf.d

    prometheus:
      container_name: prometheus
      image: prom/prometheus:latest
      volumes:
          - /etc/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml
          - /etc/localtime:/etc/localtime:ro
      ports:
        - "9090:9090"
      networks:
        - urturn
      depends_on:
        - mysqld

    grafana:
      container_name: grafana
      image: grafana/grafana:latest
      volumes:
         - /etc/localtime:/etc/localtime:ro
      user: "urturn:urturn206!@"
      ports:
        - "3000:3000"
      networks:
        - urturn
      depends_on:
        - prometheus

    mysqld:
      container_name: mysqld
      image: quay.io/prometheus/mysqld-exporter
      volumes:
        - /etc/localtime:/etc/localtime:ro
      command:
        - "--mysqld.username=exporter:urturn206!@"
        - "--mysqld.address=db:3306"
      restart: always
      networks:
        - urturn

    loki:
      container_name: loki
      image: grafana/loki:latest
      ports:
        - "3101:3100"
      command:
        -config.file=/etc/loki/local-config.yaml
        -config.expand-env=true
      networks:
        - urturn


    promtail:
      container_name: promtail
      image: grafana/promtail:latest
      volumes:
        - ./monitoring/logs/back:/logs
        - ./monitoring/promtail-local-config.yaml:/etc/promtail/config.yml
      command:
        -config.file=/etc/promtail/config.yml
      networks:
        - urturn


  volumes:
    data_volume:
      external: true

  networks:
    urturn:
      external: true


  ```

- docker-compose.blue.yml (docker-compose.green.yml 은 생략)
  ```yaml
  version: '3.1'

  services:
    back-blue:
      container_name: back-blue
      build:
        context: ./back
        dockerfile: Dockerfile
      image: ${BACK_IMAGE_NAME}:${IMAGE_TAG}
      networks:
        - urturn
      volumes:
        - ./monitoring/logs/back:/logs/springboot

      restart: always
      healthcheck:
        test: "curl --fail --silent back-blue:8080/actuator/health | grep UP || exit 1"
        interval: 20s
        timeout: 5s
        retries: 5
        #start_period: 40s
      ports:
        - "3002:8080"

  networks:
    urturn:
      external: true

  ```

- .env
  ```
  JENKINS_IMAGE_NAME=
  BACK_IMAGE_NAME=
  FRONT_IMAGE_NAME=
  IMAGE_TAG=
  ```


- deploy.sh (무중단 배포)
  ```shell
  #0
  docker pull damongsanga/urturn-back:latest

  #1
  EXIST_BLUE=$(docker-compose -f /var/jenkins_home/settings/docker-compose.blue.yml ps | grep Up)
  #EXIST_BLUE=$(docker ps -a --filter "name=back-blue" --filter "status=running")


  if [ -z "$EXIST_BLUE" ]; then
      docker-compose -f /var/jenkins_home/settings/docker-compose.blue.yml pull
      docker-compose -f /var/jenkins_home/settings/docker-compose.blue.yml up -d
      BEFORE_COLOR="green"
      AFTER_COLOR="blue"
      BEFORE_PORT=3003
      AFTER_PORT=3002
      BEFORE_UPSTREAM="api-green"
      AFTER_UPSTREAM="api-blue"

  else
      docker-compose -f /var/jenkins_home/settings/docker-compose.green.yml pull
      docker-compose -f /var/jenkins_home/settings/docker-compose.green.yml up -d
      BEFORE_COLOR="blue"
      AFTER_COLOR="green"
      BEFORE_PORT=3002
      AFTER_PORT=3003
      BEFORE_UPSTREAM="api-blue"
      AFTER_UPSTREAM="api-green"
  fi

  docker image prune # 기존 BACK 이미지 삭제
  echo "${AFTER_COLOR} server up(port:${AFTER_PORT})"

  # 2
  for cnt in {1..10}
  do
      echo "서버 응답 확인중(${cnt}/10)";
      UP=$(curl -s http://back-${AFTER_COLOR}:8080/check)
      UP=$(curl -s http://localhost:${AFTER_PORT}/check)

  #   UP=$(curl --fail --silent back-${AFTER_COLOR}:8080/actuator/health)
      if [ -z "${UP}" ]
          then
              sleep 10
              continue
          else
              break
      fi
  done

  if [ $cnt -eq 10 ]
  then
      echo "서버가 정상적으로 구동되지 않았습니다."
      echo "${AFTER_COLOR} server down(port:${AFTER_PORT})"

      if [ -z "$EXIST_BLUE" ]; then
          docker-compose -f /var/jenkins_home/settings/docker-compose.blue.yml stop back-blue
          docker-compose -f /var/jenkins_home/settings/docker-compose.blue.yml rm back-blue

      else
          docker-compose -f /var/jenkins_home/settings/docker-compose.green.yml stop back-green
          docker-compose -f /var/jenkins_home/settings/docker-compose.green.yml rm back-green
      fi

      exit 1
  fi

  # 3
  sudo sed -i "s/${BEFORE_UPSTREAM}/${AFTER_UPSTREAM}/" /etc/nginx/conf.d/service-url.inc
  # nginx -s reload
  sudo systemctl restart nginx
  echo "Deploy Completed!!"

  # 4
  echo "$BEFORE_COLOR server down(port:${BEFORE_PORT})"
  docker-compose -f /var/jenkins_home/settings/docker-compose.${BEFORE_COLOR}.yml down

  #5
  docker image prune -f
  ```

# Docker network 정보
* 도커 네트워크 생성

  ```bash
  docker network create urturn
  docker network inspect urturn
  ```

* 네트워크 정보 (컨테이너 명)
  ```jsx
  - front : 프론트 서버
  - back-blue : 백 서버 (개발용)
  - back-green : 백 서버 (배포용)
  - db : MySQL 
  - jenkins : Jenkins
  - redis : Redis
  - prometheus : Prometheus
  - grafana : Grafana
  - mysqld : Mysqld Exporter
  - promtail : Promtail
  - loki : Loki
  ```


# 채점 서버 설치

### [API Docs](https://github.com/judge0/judge0/blob/master/CHANGELOG.md#deployment-procedure)

### version : v1.13.0

1. 릴리스 아카이브를 다운로드하고 추출
  ```
  wget https://github.com/judge0/judge0/releases/download/v1.13.1/judge0-v1.13.1.zip
  unzip judge0-v1.13.1.zip
  ```
2. [임의의 비밀번호 생성](https://www.random.org/passwords/?num=1&len=32&format=plain&rnd=new)
3. REDIS_PASSWORD생성된 비밀번호를 사용하여 파일 의 변수를 업데이트합니다 judge0.conf.
4. [또 다른 임의의 비밀번호 생성](https://www.random.org/passwords/?num=1&len=32&format=plain&rnd=new)
5. POSTGRES_PASSWORD생성된 비밀번호를 사용하여 파일 의 변수를 업데이트합니다 judge0.conf.
6. 모든 서비스를 실행하고 모든 것이 초기화될 때까지 몇 초간 기다립니다.
  ```
  cd judge0-v1.13.1
  docker-compose up -d db redis
  sleep 10s
  docker-compose up -d
  sleep 5s
  ```
7. 실행 확인 :  http://{IP ADDRESS OF YOUR SERVER}:2358/docs.


# OpenVidu 설치
* 포트 설정
```
ufw allow ssh
ufw allow 80/tcp
ufw allow 443/tcp
ufw allow 3478/tcp
ufw allow 3478/udp
ufw allow 40000:57000/tcp
ufw allow 40000:57000/udp
ufw allow 57001:65535/tcp
ufw allow 57001:65535/udp
```
* 폴더 이동 및 설치
```bash
cd /opt
curl https://s3-eu-west-1.amazonaws.com/aws.openvidu.io/install_openvidu_latest.sh | bash
```
* SSL 적용
```bash
cd /opt/openvidu/owncert
# 발급받은 키를 복사 후 해당 폴더에 붙여넣기
```
* OpenVidu 설정 변경
```bash
cd /opt/openvidu
sudo vi .env
```
* 변경사항 적용 후 restart
```
DOMAIN_OR_PUBLIC_IP=urturn.site
OPENVIDU_SECRET={PASSWORD}
CERTIFICATE_TYPE=owncert
HTTP_PORT=4442
HTTPS_PORT=4443
```
* DB 접속 정보
```
USER NAME : a206  
USER PASSWORD: {PASSWORD}
```

## Github OAuth 사용법

- Settings → Developer Settings → OAuth Apps → New OAuth App
- Application name, Homepage URL, Authorization callback URL 지정하여 application 등록
- Client Id 와 Client Secret application.yml 파일에 등록

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          github:
            client-id: {your github client Id}
            client-secret: {your github client secret}
```

