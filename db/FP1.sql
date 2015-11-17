create TABLE PERSON(
email VARCHAR(30),
password VARCHAR(30),
given_name VARCHAR(30),
surname VARCHAR(30),
date_of_birth CHAR(8),
PRIMARY KEY(email)
);

create TABLE GROUPS(
id INT(3) NOT NULL AUTO_INCREMENT,
name VARCHAR(30),
PRIMARY KEY(id)
);

create TABLE NOTIFICATION(
id INT(4),
message VARCHAR(140),
time CHAR(4),
day CHAR(8),
PRIMARY KEY (id)
);

create TABLE ROOM(
id INT(4),
capasity INT(3),
name VARCHAR(30),
PRIMARY KEY(id)
);

create TABLE APPOINTMENT(
id INT(4),
day CHAR(8),
start_time CHAR(4),
duration INT(6),
public BOOLEAN,
title VARCHAR(30),
description VARCHAR(140),
hidden BOOLEAN,
place VARCHAR(30),
room_id INT(4),
owner_email VARCHAR(30),
PRIMARY KEY(id),
FOREIGN KEY(room_id) REFERENCES ROOM(id),
FOREIGN KEY(owner_email) REFERENCES PERSON(email)
);

create TABLE INVITED_TO(
is_going BOOLEAN,
person_email VARCHAR(30),
appointment_id INT(4),
notification_id INT(4),
FOREIGN KEY(notification_id) REFERENCES NOTIFICATION(id),
FOREIGN KEY(person_email) REFERENCES PERSON(email),
FOREIGN KEY(appointment_id) REFERENCES APPOINTMENT(id)
);

create TABLE MEMBER_OF(
group_id INT (3),
person_email VARCHAR(30),
FOREIGN KEY(group_id) REFERENCES GROUPS(id),
FOREIGN KEY(person_email) REFERENCES PERSON(email)
);