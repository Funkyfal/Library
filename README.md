Библиотека

Приложение реализовано на Java при помощи SpringBoot, Kafka, Docker, Maven. Дает возможность добавлять книги на абстрактный склад и выставлять книги со склада на полки библиотеки, 
если вы являетесь администратором в приложении. Если вы обычный пользователь, то позволяет вам увидеть список доступных книг и взять какую-либо из них (может и несколько), а после прочтения вернуть.

Инструкция по запуску приложения:
1. Клонируйте репозиторий в удобную для вас директорию.
2. В каталоге Library находятся все необходимые файлы, в том числе и docker-compose.yml. Запустите терминал в каталоге Library и введите команду docker login -u <username>, если необходимо.
Далее введите docker-compose up --build. Начнется подгрузка нужных зависимостей и докер-образов, ошибок возникнуть не должно. (проверял на трех разных компьютерах, все работало :))
3. После запуска приложения импортируйте postman коллекцию из каталога Library в Postman.

Инструкция по работе с запущенным приложением:
1. Сначала нужно зарегистрироваться д










