drop table IF exists movie cascade;

create TABLE movie AS select * from CSVREAD('classpath:movie.csv');

insert into movie (id, title, release_year, rating,genre) values
(1, 'qwerty', 1900, 8.0, 'COMEDY');