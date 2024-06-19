-- Switch to host_agent database
\c host_agent

-- Create host_info table if it does not exist
CREATE TABLE IF NOT EXISTS host_info (
    id SERIAL PRIMARY KEY,
    hostname VARCHAR NOT NULL,
    cpu_number SMALLINT NOT NULL,
    cpu_architecture VARCHAR NOT NULL,
    cpu_model VARCHAR NOT NULL,
    cpu_mhz FLOAT8 NOT NULL,
    l2_cache INTEGER NOT NULL,
    total_mem INTEGER NOT NULL,
    timestamp TIMESTAMP NOT NULL
);

-- Create host_usage table if it does not exist
CREATE TABLE IF NOT EXISTS host_usage (
    "timestamp" TIMESTAMP NOT NULL,
    host_id SERIAL NOT NULL,
    memory_free INT4 NOT NULL,
    cpu_idle INT2 NOT NULL,
    cpu_kernel INT2 NOT NULL,
    disk_io INT4 NOT NULL,
    disk_available INT4 NOT NULL,
    CONSTRAINT host_usage_host_info_fk FOREIGN KEY (host_id) REFERENCES host_info(id)
);
