# common-admin
Provides reusable components for simplifying user permissions and applications management.

## Setup Postgres DB

### Create and start the `postgres` docker container
```bash
docker run -d -n postgres -p 5432:5432 -e "POSTGRES_PASSWORD=mysecretpassword" postgres:9.5.3
```

### Create the `admin_db`
```bash
docker exec -it postgres psql -U postgres -w
```
Then copy and paste the content of the file `./dao/src/main/resources/common/admin/changelog/createDb.sql`

Exit the `psql` console by typing `\q` and then `enter`.

### Create initial schema in the `admin_db`
```bash
docker exec -it postgres psql -U admin_admin -w -d admin_db
```
and paste the content of the file `./dao/src/main/resources/common/admin/changelog/initialSql.sql`

Exit the `psql` console by typing `\q` and then `enter`.

### Create initial test user
The following script will crate `test` user with `test` password and `SUPERUSER` access
```bash
docker exec -it postgres psql -U admin_admin -w -d admin_db
```
and paste the content of the file `./admin/src/test/resources/demo/demo-data.sql`

Exit the `psql` console by typing `\q` and then `enter`.

## Build and Run
```bash
mvn clean install
cd admin
mvn cargo:run
```
Then open the following url in the Browser: `http://localhost:9090/admin/admin.html`

To login use `test/test` as login/password.

## REST API Documentation
```
http://localhost:9090/admin/swagger-ui.html
```

Screenshots
==============

*Assign users to applications*

![](https://github.com/viktor-podzigun/common-admin/blob/master/doc/screenshots/user-apps.png)

*Assign roles to users*

![](https://github.com/viktor-podzigun/common-admin/blob/master/doc/screenshots/user-roles.png)

*Assign permissions to roles*

![](https://github.com/viktor-podzigun/common-admin/blob/master/doc/screenshots/role-permissions.png)
