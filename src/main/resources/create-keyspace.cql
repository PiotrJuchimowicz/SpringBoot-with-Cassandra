CREATE KEYSPACE IF NOT EXISTS test
WITH durable_writes = true
AND replication = {'class' : 'SimpleStrategy', 'replication_factor' : 1};