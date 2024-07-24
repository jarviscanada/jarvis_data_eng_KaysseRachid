#!/bin/bash

# Script usage
# ./scripts/host_info.sh psql_host psql_port db_name psql_user psql_password

# Setup and validate arguments
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

# Check number of arguments
if [ "$#" -ne 5 ]; then
    echo "Illegal number of parameters"
    exit 1
fi

# Save machine statistics and current machine hostname to variables
hostname=$(hostname -f)
lscpu_out=$(lscpu)
meminfo_out=$(cat /proc/meminfo)

# Retrieve hardware specification variables
cpu_number=$(echo "$lscpu_out" | awk '/^CPU\(s\):/ {print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out" | awk '/^Architecture:/ {print $2}' | xargs)
cpu_model=$(echo "$lscpu_out" | grep "Model name:" | awk -F': ' '{print $2}' | xargs)
cpu_mhz=$(echo "$cpu_model" | grep -oP '\d+\.\d+GHz' | sed 's/GHz//' | awk '{printf "%.0f", $1 * 1000}')
l2_cache=$(echo "$lscpu_out" | awk '/^L2 cache:/ {print $3}' | sed 's/K//' | xargs)
total_mem=$(echo "$meminfo_out" | awk '/^MemTotal:/ {print $2}' | xargs)
timestamp=$(date -u +"%Y-%m-%d %H:%M:%S")

# Construct the INSERT statement from specification variables
insert_stmt="INSERT INTO host_info (hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, total_mem, timestamp) VALUES ('$hostname', $cpu_number, '$cpu_architecture', '$cpu_model', $cpu_mhz, $l2_cache, $total_mem, '$timestamp');"

# Set up environment variable for psql command
export PGPASSWORD=$psql_password

# Insert data into database
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?
