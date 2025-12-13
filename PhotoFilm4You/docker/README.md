<div id="top"></div>

<br/>
<div align="center">
  <h3 align="center">Photo&Film4You · Group +SW8 · Infrastructure</h3>
</div>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Contents</summary>
  <ol>
    <li>
      <a href="#about-this-project">About this project</a>
      <ul>
        <li><a href="#made-with">Made with</a></li>
      </ul>
    </li>
    <li><a href="#before-starting">Before starting</a></li>
    <li>
      <a href="#installation">Installation</a>
      <ul>
        <li><a href="#docker-desktop--docker-compose-installation">Docker Desktop / Docker Compose installation</a></li>
        <li><a href="#running-the-environment">Running the environment</a></li>
      </ul>
    </li>
        <li><a href="#tools-libraries-and-modules-used">Tools, links and modules used</a></li>
  </ol>
</details>


## About this project

This repository contains the **docker-compose.yml** file required to start all the infrastructure and services needed for the basic *walking skeleton* of the **Photo&Film4You** system.

It includes:

* Kafka + Zookeeper
* Three PostgreSQL databases (Product, User, Digital)
* The Product microservice
* The Frontend for adding products
* The Adminer database console

This setup allows running the full workflow of **adding a product** through the frontend, saving it into PostgreSQL.

<p align="right">(<a href="#top">go up</a>)</p>


### Made with

* [Docker](https://www.docker.com/) / [Docker Compose](https://github.com/docker/compose)
* [Spring](https://spring.io/) / [Spring Boot](https://spring.io/projects/spring-boot)
* [Maven](https://maven.apache.org/)
* [Apache Kafka](https://kafka.apache.org/)
* [PostgreSQL](https://www.postgresql.org/)
* [Vue.js](https://vuejs.org/)

<p align="right">(<a href="#top">go up</a>)</p>


## Before starting

The following ports are used by this environment:

| Service                  | Port (Host → Container)      |
|--------------------------|------------------------------|
| Zookeeper                | 22181 → 2181                 |
| Kafka                    | 29092 → 9092 & 19092 → 19092 |
| productdb                | 54320 → 5432                 |
| userdb                   | 54321 → 5432                 |
| digitaldb                | 54322 → 5432                 |
| product-solution backend | 18081 → 18081                |
| frontend                 | 80 → 80                      |
| Adminer                  | 18080 → 8080                 |

If any port is already in use, edit the `ports:` section in `docker-compose.yml`.

<p align="right">(<a href="#top">go up</a>)</p>


## Installation

### Docker Desktop / Docker Compose installation

Proceed to install Docker Compose following the steps described in the following guide: https://docs.docker.com/compose/install/ (according to your OS).

Under Windows, registration may be required, as <a href="https://docs.docker.com/desktop/windows/install/">Docker Desktop</a>  requires it for educational/personal/non-commercial projects. On the plus side, it will not be necessary to install anything else because it already includes _Compose_.

It is important that you carefully review the hardware and software requirements described in the installation guides. If your system fails to meet them, even after a successful installation, you will see errors when trying to start containers. An alternative for those with slightly older systems is <a href="https://www.how2shout.com/how-to/how-to-install-docker-toolbox-using-chocolatey-choco-on-windows-10.html">Docker Toolbox</a>.

<p align="right">(<a href="#top">go up</a>)</p>

## Running the environment

Once Docker Compose is installed, to start the infrastructure, you need to run the command from within the folder:

  ```sh
  docker compose up -d
  (Win)
  ```
  ```sh
  docker-compose up -d
  (Linux)
  ```

The stack *photo-and-film-4-you* should start along with these containers:

* frontend-1 - vue.js frontend
* product-solution-1 - product backend
* adminer-1 - adminer, an SQL client
* kafka-1 - the kafka server
* productdb-1 - the postgresql database for the productcatalog service
* userdb-1 - the postgresql database for the user service
* digitaldb-1 - the postgresql database for the digital service
* zookeeper-1 - kafka zookeeper

In order to verify that all containers are up and running, we will execute the following command:

  ```sh
  docker ps -a
  ```

To check the operation, you can access the _Adminer_ panel at http://localhost:18080/ and make a query against the PostgreSQL DB that we have just instantiated with the following connection data:

* productdb
* Engine: PostgreSQL
* Server: productdb
* User: product
* Password: product
* Schema: product

</br>

* userdb
* Engine: PostgreSQL
* Server: userdb
* User: user
* Password: user
* Schema: user

</br>

* digitaldb
* Engine: PostgreSQL
* Server: digitaldb
* User: digital
* Password: digital
* Schema: digital

<p align="right">(<a href="#top">go up</a>)</p>

## Tools, libraries and modules used

* [Docker](https://www.docker.com/) / [Docker Compose](https://github.com/docker/compose)
* [Spring](https://spring.io/) / [Spring Boot](https://spring.io/projects/spring-boot)
  * [spring-data-jpa](https://spring.io/projects/spring-data-jpa)
  * [spring-data-jdbc](https://spring.io/projects/spring-data-jdbc)
  * [spring-kafka](https://spring.io/projects/spring-kafka)
* [Apache Kafka](https://kafka.apache.org/)
* [PostgreSQL](https://www.postgresql.org/)
* [Lombok](https://projectlombok.org/)
* [springdoc-openapi-ui (SwaggerUI for OpenApi 3)](https://github.com/springdoc/springdoc-openapi)
* [Vue.js](https://vuejs.org/)


<p align="right">(<a href="#top">go up</a>)</p>