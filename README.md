# Тестовое задание для Skytec Games
    В тестовом задании я попытался соответствовоть двум основным требованиям.
    Для соответствия первому требованию я создал ClanDao, в которой потоки синхронизируются на конкретных обьектах. Т.е. блокировка для работы с кланом A не блокирует работу для клана Б
    Я решил выбрать подобную реализацию так как в задании увидел фразу:
    ""Задание не должно быть сконцентрировано вокруг бд, это не то что мы хотим проверить. Мы ожидаем что вы проявите незаурядные знания работы с многопоточностью в java 😉"
    С помощью бд выполнить первое требование можно было бы, настроив блокировку.
    
    Второй пункт я попытался реализовать логированием. Для этого добавил Log4j и настроил логирование в ролинг файл и консоль.
    Логи покрывают далеко не все участки кода, но я поптался показать идею.
    
    Доп балы я решил набрать, добавив gRPC на Netty. 
    Netty имеет два тредпула, которые можно задать в "websocket.properties". Благадоря этому свойству я решил не мудрить с созданием своих собственных пулов и очереей.
    Основне protobuf модельки вынес в модуль "proto"
    
    Перед запуском модуля "app" необходимо собрать модуль "proto"
    Для приведения protobuf моделей к java классам необходимо скачать утилиту protoc и собрть модуль "proto"
    Я выполнял задание на Linux Mint, поэтому устонавливал protoc командой:
```bash
 apt install protobuf-compiler
```

В программе почти нету unit-тестов и много повторяющгося кода, но это именно "тестовое задание" и я не хотел тратить на него много времени.