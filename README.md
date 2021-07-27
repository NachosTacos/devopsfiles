# Configuring & Running the Spring Canary App on Linux 
This canary app demonstrates the ability to perform CRUD operations on a persistent database. It is built on the Java Spring framework and requires no login.

## Step 1: Setting up the application
To use this application, either download it or clone it onto a Linux-based machine. This example uses an Amazon Linux 2 AMI with Java 8 installed.

The application comes with its own tomcat server and its dependencies are handled by a maven wrapper (you do not need to download maven). You may need to manage machine-specific configurations to allow for web traffic.

To build the application, run ```./mvnw install```.  This will download all the dependencies specified in **pom.xml**, then build and test the application.  You may need to run ```chmod +x mvnw``` to make the maven wrapper executable.

If the dependencies do not download, it may be because the maven wrapper is not using your machine's proxy settings.  You can resolve this by using an installed version of maven, or by running ```sh ./mvnw -Dhttp.proxyHost=??? -Dhttp.proxyPort=??? -Dhttps.proxyHost=??? -Dhttps.proxyPort=??? install```, replacing ??? with your respective host and port. (For the MITRE proxy use gatekeeper.mitre.org and 80, and for MITRE EC2 instances use 10.202.1.215 and 3128, and see [https://confluence.cit.mitre.org/display/ECS/Configure+Proxy+Settings+on+EC2+Instance](https://confluence.cit.mitre.org/display/ECS/Configure+Proxy+Settings+on+EC2+Instance).)

Maven runs the tests in **CanaryApplicationTests.java** using a temporary in-memory H2 database as specified in **/src/test/resources/application.properties**.  The deployed version uses the connection to a persistent MySQL server specified in **/src/main/resources/application.properties**.

If you run maven again, use ```./mvnw clean``` to remove artifacts created by prior builds.  For a list of common maven commands such as `test` and `package`, see [https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html).

## Step 2: Setting up the database
### Installing MySQL
You'll need a MySQL database to run the canary app.  This example uses MySQL version 8.0.13 for Linux on x86_64 (MySQL Community Server - GPL).  There is no explicit requirement to use this version.

The instructions below are detailed here: [https://www.linode.com/docs/databases/mysql/how-to-install-mysql-on-centos-7](https://www.linode.com/docs/databases/mysql/how-to-install-mysql-on-centos-7).  You do not need to explicitly follow these installation instructions.

Check [https://dev.mysql.com/downloads/repo/yum](https://dev.mysql.com/downloads/repo/yum) for the latest release, then execute the following commands (replacing the mysql version as desired):

```
wget http://repo.mysql.com/mysql80-community-release-el7-2.noarch.rpm
sudo rpm -ivh mysql80-community-release-el7-2.noarch.rpm
yum update
sudo yum install mysql-server
sudo systemctl start mysqld
```

To get the temporary password, run ```sudo grep 'temporary password' /var/log/mysqld.log```. Although optional, I recommend configuring mysql with ```sudo mysql_secure_installation```.

Now, log in with ```mysql -u root -p``` and enter the temporary password (or the password you set during msyql_secure_installation) when prompted. 

### Configure MySQL for the Canary App
Create a database, and a user with access to that database. While it is possible for the application to create the database, it would require setting the root username and password as environment variables, which is inadvisable.

Feel free to alter the database name, username, or password.
```
mysql> create database mydb;`
mysql> create user 'myuser'@'%' identified by 'Th3P@ssw0rd';`
mysql> grant all on mydb.* to 'myuser'@'%';`
```
You do not need to create any tables; the canary app will do that automatically.

Set up the following environment variables (matching the names you used earlier). I recommending adding the following lines to your ~/.bashrc file so they do not go away when you close the terminal/connection:

```
export MYSQL_DB=mydb
export MYSQL_HOST=localhost
export MYSQL_PASSWORD=Th3P@ssw0rd
export MYSQL_PORT=3306
export MYSQL_USER=myuser
```

If you added the lines to ~/.bashrc, load the values by running ```source ~/.bashrc```.

## Step 3: Running the Application
To run the application using the maven wrapper, use ```./mvnw spring-boot:run``` ("spring-boot:run" is the specific maven command for running a spring application).  

Alternatively, you can run the application via the .jar with ```java -jar target/paper-0.0.1-SNAPSHOT.jar```.

If you would like the application to remain running when you close the terminal or connection to the instance, look into using the **screen** command, or some equivalent.

If you run into an error regarding time zones, you may need to run the mysql command to set the time zone (replace "-4:00" with your appropriate time zone): 
```mysql> SET GLOBAL time_zone = '-4:00';```

If you are running the application on a local machine, you should be able to see the application by going to [http://localhost:8080](http://localhost:8080).

If you are running the application on a remote instance, you should go to the application's IPv4 public IP or DNS, appended with ":8080", e.g.:
```
34.229.64.148:8080
ec2-34-229-64-148.compute-1.amazonaws.com:8080
```
You should see a landing page with a large crest reading "Department of Paper".

From here you can navigate to the **Users** page via the link in the navbar.  It is a simple page that lets you create, edit, and delete users.

These are **not** actual users.  The password and email fields are entirely unused.  It is only for demonstrating the application's ability to interact with a persistent database.

To stop the server, hit **CTRL+C** while in the terminal.