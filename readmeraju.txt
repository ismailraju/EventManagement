create database userauth charset utf8 collate utf8_bin;


create user 'root'@'localhost' identified by 'root';
grant all privileges on *.* to 'sesp'@'localhost' with grant option;
flush privileges;