Curl запросы к REST контроллерам
======================================

Запросы пользователя
--------------------------------------
### Получение текущего активного пользователя
    curl --request GET "http://localhost:8080/topjava/rest/profile"

### Получение текущего активного пользователя с записями еды
    curl --request GET "http://localhost:8080/topjava/rest/profile/with-meals"

### Изменение данных текущего активного пользователя
###### передаваемые данные: {"id":100000, "name": "UpdatedName", "email": "update@gmail.com", "password": "newPass", "enabled": false, "registered": "2022-03-29T11:06:54.320+00:00", "roles": ["ADMIN"], "caloriesPerDay": 330}
    curl --request PUT "http://localhost:8080/topjava/rest/profile" --header "Content-Type: application/json; charset=ISO-8859-1" --data-raw "{\"id\": 100000, \"name\": \"UpdatedName\", \"email\": \"update@gmail.com\", \"password\": \"newPass\", \"enabled\": false, \"registered\": \"2022-03-29T11:06:54.320+00:00\", \"roles\": [\"ADMIN\"], \"caloriesPerDay\": 330}"

### Удаление текущего активного пользователя
    curl --request DELETE "http://localhost:8080/topjava/rest/profile"
--------------------------------------

Запросы к еде текущего активного пользователя
--------------------------------------
### Получение всей еды
    curl --request GET "http://localhost:8080/topjava/rest/meals"

### Получение записи еды по id
######id=100003
    curl --request GET "http://localhost:8080/topjava/rest/meals/100003"

### Получение всей еды в промежутке между датами, с выборкой по времени
###### дата: startDate=2020-01-30, endDate=2020-01-30 - полуоткрытый интервал <br> время: startTime=10:00:00, endTime=20:00:00 - закрытый интервал
    curl --request GET "http://localhost:8080/topjava/rest/meals/filtered?startDate=2020-01-30&startTime=10:00:00&endDate=2020-01-30&endTime=20:00:00"

### Создание новой записи еды
###### передаваемые данные: {"dateTime": "2020-02-01T18:00:00", "description": "SomeFood", "calories": 300}
    curl --request POST "http://localhost:8080/topjava/rest/meals" --header "Content-Type: application/json; charset=ISO-8859-1" --data-raw "{\"dateTime\": \"2020-02-01T18:00:00\", \"description\": \"SomeFood\", \"calories\": 300}"

### Изменение данных еды по id
###### id=100003; <br> передаваемые данные: {"id": 100003, "dateTime": "2020-01-30T10:02:00", "description": "UpdatedFood", "calories": 200}
    curl --request PUT "http://localhost:8080/topjava/rest/meals/100003" --header "Content-Type: application/json; charset=ISO-8859-1" --data-raw "{\"id\": 100003, \"dateTime\": \"2020-01-30T10:02:00\", \"description\": \"UpdatedFood\", \"calories\": 200}"

### Удаление еды по id
###### id=100003
    curl --request DELETE "http://localhost:8080/topjava/rest/meals/100003"
--------------------------------------

Запросы админа
--------------------------------------
###  Получение всех пользователей
     curl --request GET "http://localhost:8080/topjava/rest/admin/users"

###  Получение пользователя по id 
###### id=100001
    curl --request GET "http://localhost:8080/topjava/rest/admin/users/100001"

###  Получение пользователя по email 
###### email=user@yandex.ru
    curl --request GET "http://localhost:8080/topjava/rest/admin/users/by-email?email=user@yandex.ru"

###  Получение пользователя по id с записями еды
###### id=100001
    curl --request GET "http://localhost:8080/topjava/rest/admin/users/100001/with-meals"

### Создание нового пользователя
###### передаваемые данные: { "name": "New", "email": "new@gmail.com", "password": "newPass", "enabled": false, "registered": "2022-03-29T11:55:53.231+00:00", "roles": ["USER"], "caloriesPerDay": 1555}
    curl --request POST "http://localhost:8080/topjava/rest/admin/users" --header "Content-Type: application/json; charset=ISO-8859-1" --data-raw "{\"name\": \"New\", \"email\": \"new@gmail.com\", \"password\": \"newPass\", \"enabled\": false, \"registered\": \"2022-03-29T11:55:53.231+00:00\", \"roles\": [\"USER\"], \"caloriesPerDay\": 1555}"

### Изменение данных пользователя по id
###### id=100000; <br> передаваемые данные: { "id": 100000, "name": "UpdatedName", "email": "update@gmail.com", "password": "newPass", "enabled": false, "registered": "2022-03-29T12:07:35.665+00:00", "roles": ["ADMIN"], "caloriesPerDay": 330}
     curl --request PUT "http://localhost:8080/topjava/rest/admin/users/100000" --header "Content-Type: application/json; charset=ISO-8859-1" --data-raw "{\"id\": 100000, \"name\": \"UpdatedName\", \"email\": \"update@gmail.com\", \"password\": \"newPass\", \"enabled\": false, \"registered\": \"2022-03-29T12:07:35.665+00:00\", \"roles\": [\"ADMIN\"], \"caloriesPerDay\": 330}"

### Удаление пользователя по id
###### id=100000
     curl --request DELETE "http://localhost:8080/topjava/rest/admin/users/100000"