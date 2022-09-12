# Простейшее приложение и установка SDKMAN

## Установка SDKMAN

Для установки различных SDK удобно использовать проект SDKMAN - https://sdkman.io/

Выполняем команду curl -s "https://get.sdkman.io" | bash для установки SDKMAN

Далее  /c/Users/User/.sdkman/bin/sdkman-init.sh чтобы можно было запускать командой sdk

**sdk ls** - просмотреть список всех доступных sdk

**sdk i java version** - установить версию Java

**sdk ls java** - просмотреть доступные и установленные версии Java

**sdk u java \<version>** - сменить используемую версию Java.


Зависимости и pom-файл

###parent
**spring-boot-starter-parent** - оборачивает всю мощь Spring для легкого использования

###dependency
**spring-boot-starter-web** - позволяет использовать web

**spring-boot-starter-thymeleaf** - шаблонизатор

**spring-boot-devtools** - позволяет быстро перезагружать контейнер приложения при внесении изменений.

## Controller-фаил
**@RequestParam** - ожидается на вход параметр с определенным именем и дефолтным значением

**Model** - "хранилище данных", которые нужно вернуть пользователю.

**return fileName** - метод маппинга возвращает имя файла, который нужно отобразить.

## HTML-файл

Шаблоны **thymeleaf** - тот же **HTML** с подключенным **xml-namespace** 

## Application - файл
**@SpringBootApplication** - добавляет функциональность Spring-boot: поиск контроллеров, файлов подключения к БД, поднимают приложения и так далее.

## Замена Thymeleaf на Mustache

* Заменяем в pom-файле зависимость **spring-boot-starter-mustache**
* Переименовываем html-файл в mustache-файл.

# Подключение БД

## Зависимости и pom-файл

**spring-boot-starter-data-jpa** - без этого ничего работать не будет. Java Persistence API.

**postgresql** - работа с БД Postgresql 

Создаем файл **application.properties**

**spring.jpa.generate-ddl=true** - создание БД мы берём на себя, а структуру создаёт и обслуживает Spring. Такие операции, как создание новых объектов для хранения в БД, добавление полей, переименование и т.д.

Создаём **package domain** в котором будут храниться все файлы для сущностей, чтобы не искать их по всему коду.

**@Entity** - указывает, что это не просто кусок кода, а сущность, которую нужно сохранить в БД

**@Id** - данное поле является идентификатором в БД

**@GeneratedValue(strategy = GenerationType.AUTO)** - Spring и БД сами выбирают способ генерации Id.

Создаём **package repository** в котором будут храниться репозитории.




