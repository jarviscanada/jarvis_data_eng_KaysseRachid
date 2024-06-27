# Introduction
The project is a learning activity which allow you to learn SQL and RDBMS by solving SQL queries. It leverages technologies used such as PostgreSQL, Docker, and Git. This README provides essential information for setting up the project environment, including database schema creation and data initialization.

# SQL Queries

###### Table Setup (DDL)
```sql
CREATE TABLE cd.members
(
   memid INTEGER NOT NULL,
   surname VARCHAR(200) NOT NULL,
   firstname VARCHAR(200) NOT NULL,
   address VARCHAR(300) NOT NULL,
   zipcode INTEGER NOT NULL,
   telephone VARCHAR(20) NOT NULL,
   recommendedby INTEGER,
   joindate TIMESTAMP NOT NULL,
   CONSTRAINT members_pk PRIMARY KEY (memid),
   CONSTRAINT fk_members_recommendedby FOREIGN KEY (recommendedby)
       REFERENCES cd.members(memid) ON DELETE SET NULL
);

CREATE TABLE cd.facilities
(
   facid INTEGER NOT NULL,
   name VARCHAR(100) NOT NULL,
   membercost NUMERIC NOT NULL,
   guestcost NUMERIC NOT NULL,
   initialoutlay NUMERIC NOT NULL,
   monthlymaintenance NUMERIC NOT NULL,
   CONSTRAINT facilities_pk PRIMARY KEY (facid)
);

CREATE TABLE cd.bookings
(
   bookid INTEGER NOT NULL,
   facid INTEGER NOT NULL,
   memid INTEGER NOT NULL,
   starttime TIMESTAMP NOT NULL,
   slots INTEGER NOT NULL,
   CONSTRAINT bookings_pk PRIMARY KEY (bookid),
   CONSTRAINT fk_bookings_facid FOREIGN KEY (facid) REFERENCES cd.facilities(facid),
   CONSTRAINT fk_bookings_memid FOREIGN KEY (memid) REFERENCES cd.members(memid)
);
```

###### SQL Queries

Check `queries.sql` file to look at all the implemented queries for the project

###### Verification

Verify your SQL solutions using [pgexercises.com](pgexercises.com) to ensure correctness and best practices.

# For Developers

###### Setting Up PostrgreSQL

1. Ensure Docker is installed on your system
2. Start a PostgreSQL instance using Docker: `docker run --name my-postgres-db -e POSTGRES_PASSWORD=mysecretpassword -d postgres
`
3. Download [`clubdata.sql`](https://file.notion.so/f/f/4bb008e8-7ec5-4576-a6da-d22513aa22fa/74039e94-0a7b-44af-8590-505f9c690a5d/clubdata.sql?id=f74ad507-e652-4586-9d45-427273bd7c77&table=block&spaceId=4bb008e8-7ec5-4576-a6da-d22513aa22fa&expirationTimestamp=1719619200000&signature=y3YObe0VLNYKD21qqiz-SH7esf7Xt_5iSq-zV5JtwgE&downloadName=clubdata.sql) and execute command: `psql -U <username> -f clubdata.sql -d postgres -x -q`
