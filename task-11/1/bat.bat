@echo off

set DB_HOST=localhost
set DB_PORT=5432
set DB_NAME=task-11
set DB_USER=postgres
set DB_PASS=postgres
set PGPASSWORD=postgres
set PSQL="C:\Program Files\PostgreSQL\17\bin\psql.exe"
set DDL_FILE=ddl.sql
set DML_FILE=dml.sql

echo Applying DDL script
%PSQL% -h %DB_HOST% -p %DB_PORT% -U %DB_USER% -d "%DB_NAME%" -f "%DDL_FILE%" -w
echo Applying DML script
%PSQL% -h %DB_HOST% -p %DB_PORT% -U %DB_USER% -d "%DB_NAME%" -f "%DML_FILE%" -w
echo Database setup completed