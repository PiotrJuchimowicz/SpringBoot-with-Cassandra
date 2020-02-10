
create table if not exists message
(
	uuid uuid primary key,
	content varchar,
	create_date timestamp,
	email varchar,
	magic_number int,
	title varchar
)
with caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
	and compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy'}
	and compression = {'sstable_compression': 'org.apache.cassandra.io.compress.LZ4Compressor'}
	and speculative_retry = '99.0PERCENTILE';

create index if not exists message_email_idx
	on message (email);

create index if not exists message_magic_number_idx
	on message (magic_number);

