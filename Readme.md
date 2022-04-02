##Command

Причина, по которой команда работает с аргументами в виде строки, заключается в некоторых сложностях интерпретации для команд.
Главная проблема в том, что некоторым командам важен порядок аргументов. Например, `ffmpeg`.

В таком случае `SortedSet<String>` не имеет принципиальных отличий от `List<String>` (кроме скорости проверки) наличия аргумента,
но в силу малых размеров данных списков это не будет оказывать существенного влияния на производительность.