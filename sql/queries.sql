-- Q1: Insert

INSERT INTO cd.facilities
    (facid, "name", membercost, guestcost, initialoutlay, monthlymaintenance)
    VALUES (9, 'Spa', 20, 30, 100000, 800);

-- Q2: Insert next value
INSERT INTO cd.facilities
    (facid, "name", membercost, guestcost, initialoutlay, monthlymaintenance)
    SELECT (SELECT max(facid) FROM cd.facilities)+1, 'Spa', 20, 30, 100000, 800;

-- Q3: Update
UPDATE
    cd.facilities
        SET initialoutlay = 1000 WHERE facid = 1;

-- Q4: Update with calculation
UPDATE
    cd.facilities
        SET
            membercost = (SELECT membercost * 1.1 FROM cd.facilities WHERE facid = 0),
            guestcost = (SELECT guestcost * 1.1 FROM cd.facilities WHERE facid = 0)
        WHERE facid = 1;

-- Q5: Delete
DELETE FROM cd.bookings

-- Q6: Delete with condition
DELETE FROM cd.members WHERE memid = 37;

-- Q7: https://pgexercises.com/questions/basic/where2.html
SELECT facid, name, membercost, monthlymaintenance FROM cd.facilities
	WHERE membercost > 0 AND membercost < (monthlymaintenance / 50);

-- Q8: https://pgexercises.com/questions/basic/where3.html
SELECT * FROM cd.facilities
    WHERE "name" like '%Tennis%';

-- Q9: https://pgexercises.com/questions/basic/where4.html
SELECT * FROM cd.facilities
    WHERE facid in (1, 5)

-- Q10: https://pgexercises.com/questions/basic/date.html
SELECT
    memid, surname, firstname, joindate
	FROM
        cd.members
	        WHERE joindate >= '2012-09-01';

-- Q11: https://pgexercises.com/questions/basic/union.html
SELECT
    surname
    FROM
        cd.members
            UNION
                SELECT "name" FROM cd.facilities;

-- Q12: https://pgexercises.com/questions/joins/simplejoin.html
SELECT
    starttime
    FROM
        cd.bookings
            INNER JOIN cd.members ON members.memid = bookings.memid
                WHERE members.firstname = 'David' AND members.surname = 'Farrell';

-- Q13: https://pgexercises.com/questions/joins/simplejoin2.html
SELECT
    cd.bookings.starttime, cd.facilities.name
FROM
    cd.facilities
	INNER JOIN
		cd.bookings ON cd.bookings.facid = cd.facilities.facid
	WHERE
		cd.facilities.name in ('Tennis Court 2','Tennis Court 1')
		AND cd.bookings.starttime >= '2012-09-21'
		AND cd.bookings.starttime < '2012-09-22'
ORDER BY starttime;

-- Q14: https://pgexercises.com/questions/joins/self2.html
SELECT
    cd.members.firstname AS member_firstname,
    cd.members.surname AS member_surname,
    recommender.firstname AS recommender_firstname,
    recommender.surname AS recommender_surname
    FROM
    cd.members
        LEFT JOIN
            cd.members AS recommender
            ON cd.members.recommendedby = recommender.memid
ORDER BY cd.members.surname, cd.members.firstname;

-- Q15: https://pgexercises.com/questions/joins/self.html
SELECT DISTINCT
    cd.members.firstname AS member_firstname,
    cd.members.surname AS member_surname
FROM
    cd.members
WHERE
    cd.members.memid IN (SELECT DISTINCT cd.members.recommendedby FROM cd.members WHERE cd.members.recommendedby IS NOT NULL)
ORDER BY
    cd.members.surname, cd.members.firstname;

--  Q16: https://pgexercises.com/questions/joins/sub.html
SELECT DISTINCT
    (cd.members.firstname || ' ' || cd.members.surname) AS member,
    (SELECT recommender.firstname || ' ' || recommender.surname
     FROM cd.members AS recommender
     WHERE recommender.memid = cd.members.recommendedby) AS recommender
FROM
    cd.members
ORDER BY
    member;


-- Q17: https://pgexercises.com/questions/aggregates/count3.html
SELECT cd.members.recommendedby, count(*)
	FROM cd.members
		WHERE recommendedby IS NOT NULL
			GROUP BY recommendedby
ORDER BY recommendedby;

--  Q18: https://pgexercises.com/questions/aggregates/fachours.html
SELECT facid, SUM(slots)
	FROM cd.bookings
		GROUP BY facid
ORDER BY facid

-- Q19: https://pgexercises.com/questions/aggregates/fachoursbymonth.html
SELECT facid, SUM(slots)
	FROM cd.bookings
	WHERE
		starttime >= '2012-09-01' AND starttime <= '2012-10-01'
	GROUP BY facid
ORDER BY SUM(slots);

-- Q20: https://pgexercises.com/questions/aggregates/fachoursbymonth2.html
SELECT
    cd.bookings.facid,
    EXTRACT(MONTH FROM cd.bookings.starttime) AS month,
    SUM(cd.bookings.slots) AS total_slots
	FROM
    	cd.bookings
		WHERE
			EXTRACT(YEAR FROM cd.bookings.starttime) = 2012
	GROUP BY
		cd.bookings.facid, month
ORDER BY
    cd.bookings.facid, month;


-- Q21: https://pgexercises.com/questions/aggregates/members1.html
SELECT
    COUNT(DISTINCT cd.bookings.memid) AS total_members
    FROM
        cd.bookings;

-- Q22: https://pgexercises.com/questions/aggregates/nbooking.html
SELECT
    cd.members.surname,
    cd.members.firstname,
	cd.members.memid,
    first_bookings.first_booking
FROM
    cd.members
JOIN
    (
        SELECT
            cd.bookings.memid,
            MIN(cd.bookings.starttime) AS first_booking
        FROM
            cd.bookings
        WHERE
            cd.bookings.starttime > '2012-09-01'
        GROUP BY
            cd.bookings.memid
    ) AS first_bookings
ON
    cd.members.memid = first_bookings.memid
ORDER BY
    cd.members.memid;

-- Q23: https://pgexercises.com/questions/aggregates/countmembers.html
SELECT
	(SELECT COUNT(*) FROM cd.members) AS total_members,
    cd.members.firstname,
	cd.members.surname
FROM
    cd.members
ORDER BY
    cd.members.joindate;

-- Q24: https://pgexercises.com/questions/aggregates/nummembers.html
SELECT
    ROW_NUMBER() OVER (ORDER BY cd.members.joindate) AS member_number,
    cd.members.firstname,
    cd.members.surname
    FROM
        cd.members
ORDER BY
    cd.members.joindate;

-- Q25: https://pgexercises.com/questions/aggregates/fachours4.html
SELECT
    cd.bookings.facid,
    SUM(cd.bookings.slots) AS total_slots
FROM
    cd.bookings
GROUP BY
    cd.bookings.facid
HAVING
    SUM(cd.bookings.slots) = (
        SELECT
            MAX(sub.total_slots)
        FROM
            (
                SELECT
                    cd.bookings.facid,
                    SUM(cd.bookings.slots) AS total_slots
                FROM
                    cd.bookings
                GROUP BY
                    cd.bookings.facid
            ) AS sub
    );

-- Q26: https://pgexercises.com/questions/string/concat.html
SELECT surname || ', ' || firstname AS "name" FROM cd.members

-- Q27: https://pgexercises.com/questions/string/reg.html
SELECT
    cd.members.memid,
    cd.members.telephone
    FROM
        cd.members
    WHERE
        cd.members.telephone LIKE '%(%'
ORDER BY
    cd.members.memid;

-- Q28: https://pgexercises.com/questions/string/substr.html
SELECT substr (mems.surname,1,1) AS letter, COUNT(*) AS count
    FROM cd.members mems
        GROUP BY letter
    ORDER BY letter
