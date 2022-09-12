## Установка SDKMAN

Для установки различных SDK удобно использовать проект SDKMAN - https://sdkman.io/

Выполняем команду curl -s "https://get.sdkman.io" | bash для установки SDKMAN

Далее  /c/Users/User/.sdkman/bin/sdkman-init.sh чтобы можно было запускать командой sdk

**sdk ls** - просмотреть список всех доступных sdk

**sdk i java version** - установить версию Java

**sdk ls java** - просмотреть доступные и установленные версии Java

**sdk u java \<version>** - сменить используемую версию Java.


## Зависимости и pom-файл

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





