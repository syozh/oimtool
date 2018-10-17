Формат вызова: oimtool.cmd <command> [args]

Команды:

  1) itresparams <itresname> - Вывод параметров IT-ресурса.

  2) categories - Вывод экспортируемых категорий объектов.
     
  3) export1 [args]
     - Экспорт заданных объектов. Формат спецификации объекта "Category:Name". 
       Категории приведены в файле categories.txt.
       Пример спецификации объекта: "Lookup:Lookup.SAPHCM.OM.ReconAttrMap".
       Все объекты выводятся в один deployment-файл.

     export2 [args]
     - каждый объект выводится в отдельный файл.
   
  4) import [args] - Импорт заданных deployment-файлов. (!!!) Разрешение зависимостей пока не работает.

  5) register [args] - Регистрация плагинов. Указываются zip-архивы плагинов.

  6) unregister [args] - Удаление плагинов. Формат спецификации плагина "PluginClassName:Version".

  7) purge [args] - PurgeСache для заданных объектов.

  8) purgeall - PurgeCache All

Варианты спецификации аргументов.
  - В виде списка в командной строке:  oimtool <command> arg1 arg2 ...
  - Во входном файле, опция -f:        oimtool <command> -f <file>
    Пустые строки и строки, начинающиеся с символа '#', игнорируются.
