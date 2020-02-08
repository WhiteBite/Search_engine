# Search_engine

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/1e3b583799ff4d8a8ed8ac7d03d2c874)](https://app.codacy.com/manual/WhiteBite/Search_engine?utm_source=github.com&utm_medium=referral&utm_content=WhiteBite/Search_engine&utm_campaign=Badge_Grade_Dashboard)

## Задание №1

Написать программу для поиска заданного текста в лог файлах.
Пользователь должен иметь возможность указать папку в сети или на жестком диске, в которой будет
происходить поиск заданного текста, включая все вложенные папки.
Должна быть возможность ввода текста поиска и ввода типа расширения файлов, в которых будет
осуществляться поиск(расширение по умолчанию *.log).
Результаты поиска можно вывести в левой части приложения в виде дерева файловой системы
только те файлы в которых был обнаружен заданный текст.
В правой части приложения выводить содержимое файла с возможностью навигации по найденному
тексту (выделить все, вперед/назад).
Плюсом будет многопоточность приложения, «не замораживание» приложения на время поиска,
возможность открывать «большие» (более 1Г) файлы и осуществлять по ним быструю навигацию,
возможность открывать файлы в новых «табах», т. е. использовать TabFolder или MDI.
Для отображения разрешается использовать любые Java GUI-фреймворки (AWT, Swing, SWT, JavaFX,
NetBeans Platform и т.п.).
Приложение может быть как десктопным, так и веб-клиентом.
 
Задание будет оцениваться по следующим **критериям**:

∙  **скорость поиска в файлах заданного текста и скорость навигации по открытому файлу**

∙  **приятный и интуитивно понятный интерфейс приложения**

∙  **краткий и понятный исходный код.**
