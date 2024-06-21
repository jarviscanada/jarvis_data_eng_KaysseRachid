# Introduction
This project is designed to monitor and collect hardware specification data and resource usage data from Linux servers. 
The main users of this project are system administrators and developers who need to monitor server performance and specifications in real-time. 
The project is implemented using various technologies such as Bash scripts for data collection, PostgreSQL for data storage, Docker for containerization, and Git for version control. 
The project consists of two main scripts: host_info.sh for collecting static hardware specifications and host_usage.sh for collecting dynamic resource usage data.

# Quick Start
```
# Start a psql instance using psql_docker.sh
./scripts/psql_docker.sh start

# Create tables using ddl.sql
psql -h localhost -U postgres -d host_agent -f sql/ddl.sql

# Insert hardware specs data into the DB using host_info.sh
./scripts/host_info.sh "localhost" 5432 "host_agent" "postgres" "password"

# Insert hardware usage data into the DB using host_usage.sh
./scripts/host_usage.sh "localhost" 5432 "host_agent" "postgres" "password"

# Crontab setup to run host_usage.sh every minute
* * * * * /path/to/scripts/host_usage.sh "localhost" 5432 "host_agent" "postgres" "password"
```

# Implemenation

## Architecture
The project architecture consists of three Linux hosts, each running the monitoring agent scripts, and a central PostgreSQL database where the collected data is stored. 
The architecture diagram is saved in the assets directory.

## Scripts
Shell script description and usage (use markdown code block for script usage)
- psql_docker.sh
- host_info.sh
- host_usage.sh
- crontab
- queries.sql (describe what business problem you are trying to resolve)

## Database Modeling

host_info
|Column	            |Type	              |Constraints      |
|-------------------|-------------------|-----------------|
|id	                |SERIAL	            |PRIMARY KEY      |
|hostname	          |VARCHAR	          |NOT NULL, UNIQUE |
|cpu_number	        |SMALLINT	          |NOT NULL         |
|cpu_architecture	  |VARCHAR	          |NOT NULL         |
|cpu_model	        |VARCHAR	          |NOT NULL         |
|cpu_mhz	          |FLOAT8	            |NOT NULL         |
|l2_cache	          |INTEGER	          |NOT NULL         |
|total_mem	        |INTEGER	          |NOT NULL         |
|timestamp	        |TIMESTAMP	        |NOT NULL         |

host_usage
|Column	            |Type	              |Constraints      |
|-------------------|-------------------|-----------------|
|id	                |SERIAL	            |PRIMARY KEY      |
|hostname	          |VARCHAR	          |NOT NULL, UNIQUE |
|cpu_number	        |SMALLINT	          |NOT NULL         |
|cpu_architecture	  |VARCHAR	          |NOT NULL         |
|cpu_model	        |VARCHAR	          |NOT NULL         |
|cpu_mhz	          |FLOAT8	            |NOT NULL         |
|l2_cache	          |INTEGER	          |NOT NULL         |
|total_mem	        |INTEGER	          |NOT NULL         |
|timestamp	        |TIMESTAMP	        |NOT NULL         |

# Test
The Bash scripts and DDL were tested by manually executing the scripts and verifying the results in the PostgreSQL database. 
The test results confirmed that the scripts correctly insert data into the database tables and that the tables' schemas match the requirements.

# Deployment
The application was deployed using Git for version control, Docker for containerization of the PostgreSQL instance, and crontab for scheduling the host_usage.sh script. 
The deployment steps are documented in the quick start section.

# Improvements
- Handle hardware updates dynamically, allowing host_info.sh to be re-run safely.
- Implement error handling and logging for better debugging and monitoring.
- Enhance security by encrypting the database credentials and using environment variables. (Using cloud vaults can be an option)
