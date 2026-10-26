# Dependency Visualizer (Вариант №16)

## Общее описание

Инструмент визуализации графа зависимостей для менеджера пакетов.
Этап 1: минимальный CLI-прототип с конфигурацией через JSON-файл.

## Настройки (config.json)

| Параметр             | Тип     | Обязательный | Описание                             |
|----------------------|---------|--------------|--------------------------------------|
| `packageName`        | string  | да           | Имя анализируемого пакета            |
| `repositoryUrl`      | string  | да*          | URL репозитория                      |
| `testRepositoryPath` | string  | да*          | Путь к файлу тестового репозитория   |
| `testMode`           | boolean | да           | Режим работы с тестовым репозиторием |
| `packageVersion`     | string  | да           | Версия пакета                        |
| `filterSubstring`    | string  | нет          | Подстрока для фильтрации пакетов     |

\* Должен быть указан хотя бы один из двух: `repositoryUrl` или `testRepositoryPath`.

## Сборка и запуск

```
run.bat
```

С указанием другого конфига:
```
run.bat my-config.json
```

## Запуск тестов

Понадобится `junit-platform-console-standalone.jar` (положить рядом с проектом):

```
javac -encoding UTF-8 -cp "junit-platform-console-standalone.jar" -d out-tests tests\ConfigTest.java
java -jar junit-platform-console-standalone.jar --class-path out;out-tests --scan-class-path
```

## Пример config.json

```json
{
  "packageName": "express",
  "repositoryUrl": "https://registry.npmjs.org",
  "testRepositoryPath": null,
  "testMode": false,
  "packageVersion": "4.18.2",
  "filterSubstring": "types"
}
```

## Пример вывода

```
=== Параметры конфигурации ===
packageName          = express
repositoryUrl        = https://registry.npmjs.org
testRepositoryPath   = (не задано)
testMode             = false
packageVersion       = 4.18.2
filterSubstring      = types
==============================
```