create database userauth charset utf8 collate utf8_bin;


create user 'root'@'localhost' identified by 'root';
grant all privileges on *.* to 'sesp'@'localhost' with grant option;
flush privileges;



"C:\Program Files\Java\jdk-11.0.14\bin\java.exe" -jar .\EventManagement-0.1.jar