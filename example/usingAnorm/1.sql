# --- !Ups

CREATE TABLE conference (
  id long NOT NULL,
  name varchar(255) NOT NULL,
  attendees long NOT NULL,
  date DATE NOT NULL
);


# --- !Downs
DROP TABLE IF EXISTS conference;
