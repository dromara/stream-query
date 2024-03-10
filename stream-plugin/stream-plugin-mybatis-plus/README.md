mysql:

```bash
docker run --name stream-query-mysql -e MYSQL_ROOT_PASSWORD=stream-query -e MYSQL_DATABASE=stream-query -p 3306:3306 -d mysql
```

pgsql:

```bash
docker run --name stream-query-postgres -e POSTGRES_PASSWORD=stream-query -e POSTGRES_DB=stream-query -d -p 5432:5432 postgres
```
