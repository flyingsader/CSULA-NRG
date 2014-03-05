use nrg;

delete from weatherdata where weathertimestamp < '2013-01-01 00:00:00';
delete from griddata where gridtimestamp < '2013-01-01 00:00:00';

select count(*) from weatherdata;
select count(*) from griddata;