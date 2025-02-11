<h2>Библиотека📚</h2>

<h3>Приложение реализовано на Java при помощи SpringBoot, Kafka, Docker, Maven.</h3>
Дает возможность добавлять книги на абстрактный склад и выставлять книги со склада на полки библиотеки, 
если вы являетесь администратором в приложении. Если вы обычный пользователь, то позволяет вам увидеть список доступных книг и взять какую-либо из них (может и несколько), а после прочтения вернуть.

---
🚀Инструкция по запуску приложения:
1. Клонируйте репозиторий в удобную для вас директорию.
2. В каталоге `Library` находятся все необходимые файлы, в том числе и `docker-compose.yml`. Запустите терминал в каталоге `Library` и введите команду ```docker login -u <username>```, если необходимо.
Далее введите ```docker-compose up --build```. Начнется подгрузка нужных зависимостей и докер-образов, ошибок возникнуть не должно. (проверял на трех разных компьютерах, все работало)
3. После запуска приложения импортируйте postman коллекцию из каталога `Library` в <b>Postman</b>.
---
<h3>🔑В приложении реализовано две роли: ROLE_USER и ROLE_ADMIN</h3>
Администраторы могут использовать все запросы, для них нет ограничений. Обычные пользователи могут использовать только запросы аутентификации (/register, /login) и запросы сервиса book-tracker-service без пометки admin.

---
Далее по тексту будет информация о тестировании приложения и инструкция по запросам.

<h3>⚒️Инструкция по запросам</h3>
Ниже будут приведены названия из коллекции Postman. В некоторых запросах присутствует тело запроса или переменная, которая находится в самом запросе. Я поставил туда самые дефолтные значения, которые можно поменять при необходимости.

1. **auth_admin_register** - регистрация пользователя с ролью ROLE_ADMIN, в теле запроса указаны базовые имя, пароль и роль
2. **auth_user_register** - регистрация пользователя с ролью ROLE_USER, в теле запроса указаны базовые имя и пароль (если роль не указана в теле, то по дефолту будет выставлена роль ROLE_USER)
3. **auth_admin_login** - авторизация пользователя с ролью ROLE_ADMIN, создано для удобства переключения между ролями. После успешного выполнения запроса (правильно указаны логин и пароль) будет выдан токен, который пригодится для тестирования (будет описано ниже).
4. **auth_user_login** - то же самое, что и прошлый запрос, создан для удобства, просто изначально указаны другие данные для удобства тестирования.
5. **storage_get_all_books** - все запросы, которые начинаются со storage, достыпны только администраторам. Этот запрос позволяет увидеть все доступные на складе книги.
6. **storage_get_book_by_id** - позволяет получить книгу по Id этой самой книги. Id прописывается в самом запросе. По дефолту стоит 1.
7. **storage_get_book_by_isbn** - позволяет получить книгу по ISBN (уникален для каждой книги) этой самой книги. ISBN прописывается в самом запросе. По дефолту стоит someUniqueISBN.
8. **storage_add_new_book** - создание новой книги, в теле запроса указаны самые простые данные для теста, их можно менять.
9. **storage_change_book_by_id** - изменение книги по ее Id. В самом запросе по дефолту стоит 1, ее можно поменять в зависимости от того, какую книгу вы хотите поменять. В теле запроса указываете значения всех полей.
10. **storage_delete_book_by_id** - удаляет книгу по ее Id. Реализован soft delete. В самом запросе по дефолту 1.
11. **tracker_get_available** - позволяет пользователю получить список всех доступных ему книг.
12. **tracker_take_book_from_shelf** - пользователь может взять книгу с полки в свое пользование. В самом запросе по дефолту стоит 1, можно брать любые доступные книги. Выборка идет по Id.
13. **tracker_return_book_to_shelf** - пользователь может вернуть книгу, если возвращать нечего, то ничего страшного не произойдет. Выборка идет по Id. По дефолту стоит 1
14. **tracker_admin_add_book** - позволяет администратору перенести книгу со склада на полку. По дефолту в запросе указана 1 - Id книги.
15. **tracker_admin_delete_book** - позволяет администратору перенести книгу с полки на склад. По дефолту в запросе указана 1 - Id книги.
---
<h3>🔧Инструкция по работе с запущенным приложением</h3>
<ol>
  <li>Сначала нужно зарегистрироваться, для этого выполняем один из двух выше описанных запросов для регистрации. В случае успеха будете видны вы как пользователь с зашифрованным паролем.</li>
  <li>Далее нужно залогиниться, для этого выбираем также один из удобных запросов выше, после успеха получаем токен, который нужно ввести в настройках коллекции. <b>LibraryApp</b> -> <b>Authorization</b> -> <b>Bearer Token</b>. В появившееся поле вводим свой токен.</li>
  <li>Начинаем тестировать доступные для нашей роли функции. В роли администратора можно добавлять книги на склад (они будут автоматически добавляться на полку при помощи Kafka), удалять книги со склада и, соответственно, с полки (также при помощи Kafka), изменять книги, брать книги с полок и т.д.</li>
</ol>

---
Спасибо! @funkyfal - телеграм контакт для вопросов












