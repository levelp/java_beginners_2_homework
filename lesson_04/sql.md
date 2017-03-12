Задача
===
* Сделать и осознать все то, что здесь написано. :)
* Написать свои запросы
* Возможно, модифицировать генерацию случайных записей так, как вам покажется лучше
* Заполнить (желательно, случайными данными) таблицу **course_student**
* Найти, есть ли у вас "грязные данные" в этой таблице. То есть, например, студент проходил курс 5го курса раньше, чем курс 2го.

Создание нашей базы
===


Удаление базы данных, если она уже существует.
---
```sql
drop database if exists my_study; -- удаляем базу, если существует (удалит все данные!)
```


Создание базы данных
---
```sql
 create database my_study 
default character set utf8       -- кодировка и набор символов
default collate utf8_general_ci; -- набор правил для сравнения символов данной кодировки
```

Переключиться на текущую базу данных
---
```sql
use my_study; -- переключаемся на базу
```

Создаем первую таблицу (учителя) с уникальным ФИО и уникальным телефоном. Перед этим удаляем, если она уже существует.
---
```sql
drop table if exists teachers;                      -- удаляем таблицу, если она уже существует
create table teachers (
             id int auto_increment primary key,     -- автоматически сгенерированный уникальный идентификатор
     first_name varchar(100) not null,              -- имя учителя (непустое)
      last_name varchar(100) not null,              -- фамилия учителя (непустая)
    middle_name varchar(100) not null,              -- отчество учителя (непустое)
          phone varchar(50) unique,                 -- телефон учителя (может быть пустым, но не доолжен повторяться)
          
         unique(first_name, middle_name, last_name) -- требуем, чтобы ФИО было уникальным
)
```

Создаем вторую таблицу (курсы), которая будет хранить информацию о курсе. Добавляем так же год обучения (от 1 до 6)
---
```sql
drop table if exists courses;                                  -- удаляем таблицу, если она уже существует
create table courses (
            id int auto_increment primary key,                 -- уникальный идентификатор курса, сгенерированный вручную
         title varchar(200) not null,                          -- название курса (непустое)
    study_year int check (study_year > 0 and study_year <= 6), -- год обучения, на котором проходится курс. От 1 до 6 включительно. 

        unique(title, study_year)                              -- требование, чтобы курс с одним названием и одним годом не повторялся в таблице
)
```

Создаем таблицу, которая будет хранить, какой из курсов какой учитель может вести. У нас ситуация, когда каждый учитель может вести несколько курсов и каждый курс может вести несколько учителей.
---
```sql
drop table if exists teaching_courses;                -- удаляем таблицу, если она уже существует
create table teaching_courses (
    teacher_id int not null,                          -- уникальный идентификатор учителя, который ведет этот курс (непустой)
     course_id int not null,                          -- уникальный идентификатор курса (непустой)
       
   foreign key (teacher_id) references teachers(id),  -- допускается учитель только из таблицы учителей.
   foreign key (course_id) references courses(id),    -- допускается курс только из таблицы курсов.
        primary key(teacher_id, course_id)            -- уникальным идентификатором в для этой таблицы будет пара из учителя и курса.
)
```


Создаем четвертую таблицу (ученики) с уникальным ФИО. Перед этим удаляем, если она уже существует.
---
```sql
drop table if exists students;                       -- удаляем таблицу, если она уже существует
create table students (
             id int auto_increment primary key,      -- автоматически сгенерированный уникальный идентификатор
     first_name varchar(100) not null,               -- имя ученика (непустое)
      last_name varchar(100) not null,               -- фамилия ученика (непустая)
    middle_name varchar(100) not null,               -- отчество ученика (непустое)
          
         unique(first_name, middle_name, last_name)  -- требуем, чтобы ФИО было уникальным
)
```

Создаем пятую таблицу с треком курса (то есть место, где будет лежать информация о начале, конце курса (в т.ч. предполагаемом), учителем, который его ведет.
---
```sql
drop table if exists course_session;           -- удаляем таблицу, если она уже существует
create table course_session (
            id int auto_increment primary key, -- автоматически сгенерированный уникальный идентификатор
     course_id int not null,                   -- идентификатор курса (непустой)
    teacher_id int not null,                   -- идентификатор учителя (непустой)
    date_start datetime not null,              -- дата начала курса (непустая)
      date_end datetime not null,              -- дата конца курса (непустая)
       
       -- пара "курс и учитель" должны быть из соответствующей таблицы `teaching_courses`
   foreign key(course_id, teacher_id) references teaching_courses(course_id, teacher_id)
)
```

Теперь создаем таблицу с оценками ученика по каждому пройденному треку
---
```sql
drop table if exists course_student;                                    -- удаляем таблицу, если она уже существует
create table course_student (
                   id int auto_increment primary key,                   -- автоматически сгенерированный уникальный идентификатор
           student_id int not null,                                     -- уникальный идентификатор студента
    course_session_id int not null,                                     -- уникальный идентификатор трека курса
          result_mark int check(result_mark >= 2 and result_mark <= 5), -- оценка студента, от 2 до 5
          
          foreign key(student_id) references students(id),              -- ученики должны быть только из таблицы учеников
          foreign key(course_session_id) references course_session(id), -- треки курса должны быть только из таблицы треков курсов
               unique(student_id, course_session_id)                    -- каждый студент может пройти курс только один раз
) 
```


Создаем таблицу с учетными данными в компьютерном класса для каждого ученика
---
```sql
drop table if exists network_access;                  -- удаляем таблицу, если она уже существует
create table network_access (
              id int auto_increment primary key,      -- автоматически сгенерированный уникальный идентификатор записи в таблице
      student_id int not null unique,                 -- идентификатор ученика (непустой, уникальный)
           login varchar(100) not null unique,        -- логин ученика (непустой, уникальный)
         created datetime not null default now(),     -- дата создания учетной записи, непустая, по умолчанию - "сегодня"
      
      foreign key(student_id) references students(id) -- ученики должны быть только из таблицы учеников
) 
```

Что получилось?
===

![ER-Диаграмма](/lesson_04/er-diagram.png?raw=true)

Как сгенерировать, по шагам:
* Database
* Reverse Engineering
* выбрать stored connection - ваше подключение
* Next
* Next
* отметить my_study
* Next
* Next
* Execute
* Next
* Close
* Выбрать из **EER Diagrams** соответствующую сгенерированную (она там одна)


Вставка данных
===

Вставка данных в таблицу осуществляется с помощью insert. Например:
```sql
-- Добавляем нового учителя с телефоном
insert into teachers (first_name, last_name, middle_name, phone) 
             values ('Eugene', 'Price', 'Stephanie', '46-(904)714-1532');
-- Здесь мы добавляем нового учителя без телефона
insert into teachers (first_name, last_name, middle_name)
             values ('Kathy', 'Gibson', 'Karen');
```



Заполняем базу случайными данными
---

Здесь я возьму данные из [mockaroo](https://www.mockaroo.com/). Вы можете сгенерировать их сами или добавить их из файлов в репозитории папка **lesson_04/mock**


### Таблица teachers
Я вставляю в нее данные из **teachers.sql**.
После этого можно уже работать с этой таблицей. Например, вывести всех учителей
```sql
select * 
  from teachers;
```
Вывести их же, отсортировав по фамилиям по возрастанию
```sql
  select * 
    from teachers 
order by last_name;
```
Вывести их же, отсортировав по фамилиям по убыванию
```sql
  select * 
    from teachers 
order by last_name desc;
```

Вывести только ФИО всех учителей по возрастанию
```sql
  select first_name, last_name, middle_name 
    from teachers 
order by last_name;
```

Вывести только первые 10 ФИО учителей по возрастанию
```sql
  select first_name, last_name, middle_name 
    from teachers 
order by last_name
   limit 10;
```
Вывести 10 ФИО учителей по возрастанию с 10 по 20
```sql
  select first_name, last_name, middle_name 
    from teachers 
order by last_name
   limit 10 
  offset 10;
```

Вывести 10 фамилий учителей по возрастанию
```sql
  select last_name 
    from teachers 
order by last_name;
```

Вывести 10 уникальных фамилий учителей по возрастанию
```sql
  select distinct last_name 
    from teachers 
order by last_name;
```
Вывести учителя с id = 23
```sql
select * 
  from teachers 
 where id = 23;
```
Вывести всех учителей, чьи фамилии начинаются с буквы **'T'**, отсортировав их по имени в порядке возрастания
```sql
  select * 
    from teachers 
   where last_name like 'T%' 
order by first_name;
```

#### А вот и агрегирующие функции

Можно подсчитать количество учителей (агрегирующая функция _**count**_)
```sql
select count(*) 
  from teachers;
```
Можно подсчитать самую длинную фамилию (агрегирующая функция _**max**_)
```sql
select max(length(last_name)) 
  from teachers;
```
Можно вывести самые длинные фамилии (вложенные запросы, агрегирующая функция _**max**_)
```sql
select last_name 
  from teachers
 where length(last_name) = (
           select max(length(last_name)) 
             from teachers
       );
```
То же самое - самые короткие (вложенные запросы, агрегирующая функция _**min**_)
```sql
select last_name 
  from teachers
 where length(last_name) = (
           select min(length(last_name)) 
             from teachers
       );
```

Можно посчитать количество людей с фамилией меньше, чем 7 символов
```sql
  select count(*) 
    from teachers
   where length(last_name) < 6 
order by last_name;
```
Можно посчитать количество фамилий короче, чем 7 символов
```sql
  select count(distinct last_name) 
    from teachers
   where length(last_name) < 6 
order by last_name;
```


#### Перейдем к запросам еще сложнее
Сгруппируем учителей по имени и выведем, сколько учителей у нас имеют такое имя
```sql
  select first_name, count(*) 
    from teachers
group by first_name;
```

А теперь отсортируем их по количеству человек на имя так, что сверху будут самые частые
```sql
  select first_name, count(*) 
    from teachers
group by first_name
order by count(*) desc;
```

Выкинем из группировки всех тех, у кого отчество от 7 букв и более
```sql
  select first_name, count(*) 
    from teachers
   where length(middle_name) < 7
group by first_name
order by count(*) desc;
```

А теперь еще выведем те группы, в которых оказалось больше одного человека
```sql
  select first_name, count(*) 
    from teachers
   where length(middle_name) < 7
group by first_name
  having count(*) > 1
order by count(*) desc;
```

#### Можно ли одну и ту же таблицу использовать в одном запросе несколько раз?

Давайте проанализируем данные и определим, есть ли у нас среди учителей те, которые потенциально могли бы образовать семью? Используем алиасы, и выведем имена однофамильцев.
```sql
select t1.first_name, t2.first_name, t1.last_name 
  from teachers t1, teachers t2
 where t1.last_name = t2.last_name
```

Мы видим, что у нас в запросе получились пары, где один преподаватель с двух сторон (например, Тереза с ней же самой). Исправим это

```sql
select t1.first_name, t2.first_name, t1.last_name 
  from teachers t1, teachers t2
 where t1.last_name = t2.last_name 
       and not t1.id = t2.id
```

Теперь для каждой пары учителей у меня появилось две строчки. Напремер, Дебора и Стефан + Стефан и Дебора. Исправим это, потребовав, чтобы id слева был строго меньше id справа.

```sql
select t1.first_name, t2.first_name, t1.last_name 
  from teachers t1, teachers t2
 where t1.last_name = t2.last_name 
       and t1.id < t2.id
```

Так как пол нам не известен, то мы больше ничего предположить не можем. :)

#### Как иметь дело со значениями NULL?
```sql
select * 
  from teachers 
 where phone is null;
```
```sql
select * 
  from teachers 
 where phone is not null;
```
```sql
select * 
  from teachers 
 where not phone is null;
```

**Не сработают прямые сравнения**

Например, код ниже в обоих случаях вернет пустой набор, так как `<something> = null` всегда возвращает **NULL**:
```sql
select * 
  from teachers 
 where phone = null;
```
```sql
select * 
  from teachers 
 where not phone = null;
```

И даже так:
```sql
select * 
  from teachers 
 where null = null;
```

Чтобы проверить, что происходит, можно выполнить:
```sql
select null = null, 1 = null, 0 = null, 
       null < null, 1 < null, 0 < null, 
       null > null, 1 > null, 0 > null,
       not null = null, not 1 = null, not 0 = null, not null;
```

### Таблицы courses, students так же заполняю данными из файла в репозитории
Теперь мы можем узнать, кто вообще есть в нашем учебном заведении (все люди):
```sql
select last_name, first_name, middle_name from students
union
select last_name, first_name, middle_name from teachers
```

Можно это использовать как подзапрос. Например, как 
```sql
select count(*) 
  from (
           (
             select last_name, first_name, middle_name from students
              union
             select last_name, first_name, middle_name from teachers
           ) as all_people
       )
```
Хотя это и неэффективный способ выборки подобных данных (подумайте, почему). Правильным будет 
```sql
select (select count(*)  from students) + (select count(*)  from teachers)
```

### Теперь хорошо бы заполнить таблицу teaching_courses
Я могу сделать это просто запросом **SQL**, в котором каждый раз просто случайным образом выбираю или нет запись
```sql
insert into teaching_courses(teacher_id, course_id)
select  teachers.id, courses.id
  from teachers, courses
 where rand() * 100 > 70
```

Проверим, сколько записей у нас появилось в таблице
```sql
select count(*) from teaching_courses
```

А теперь рассмотрим простые запросы, которые могут нам дать какую-то информацию о структуре данных.

Например, сколько учителей у нас могут быть заняты работой.
```sql
select count(distinct teacher_id)
  from teaching_courses
```

Например, сколько учителей у нас могут быть заняты работой.
```sql
select count(distinct teacher_id)
  from teaching_courses
```

Или сколько курсов у нас не попали ни к одному преподавателю
```sql
select count(*)
  from courses  
 where not exists (
				    select * 
                      from teaching_courses 
					 where teaching_courses.teacher_id = courses.id
			      )
```

Можно вывести, какой учитель ведет какой курс в виде, удобном для пользователя
```sql
select concat(teachers.first_name, ' ', teachers.middle_name, ' ', teachers.last_name), 
       concat(courses.title, ' on ', courses.study_year, ' year')
  from courses, teachers, teaching_courses
 where courses.id = teaching_courses.course_id 
       and teachers.id = teaching_courses.teacher_id
 ```
 
 Или даже 
 ```sql
   select concat(teachers.first_name, ' ', teachers.middle_name, ' ', teachers.last_name, 
          ' teaches ', courses.title, ' on ', courses.study_year, ' year')
     from courses, teachers, teaching_courses
    where courses.id = teaching_courses.course_id 
          and teachers.id = teaching_courses.teacher_id
 order by concat(teachers.first_name, ' ', teachers.middle_name, ' ', teachers.last_name, 
                 ' teaches ', courses.title, ' on ', courses.study_year, ' year')
 ```
 
 Но что, если мы хотим вывести данные по всем учителям и курсам, которые они ведут, но нам действительно нужны ВСЕ учителя? Предыдущий запрос вернет информацию лишь по тем, кто действительно ведет курсы. Нам на помощь приходит **join**
 ```sql
  select * 
    from course_session cs 
         right join teachers t on t.id = cs.teacher_id
order by cs.teacher_id
   limit 10
 ```
 
 По этому запросу вам выдаст первые 10 строк, и если у вас есть учителя, которые не ведут курсы, то там будет информация именно по ним. Дело в том, что тогда ```cs.teacher_id``` будет **NULL**, и при сортировке он окажется в самом начале.
 
 
 Немного модифицировав предыдущий запрос, мы можем выполнить запрос
 ```sql
  select * 
    from course_session cs 
         right join teachers t on t.id = cs.teacher_id
         left join courses c on c.id = cs.course_id
order by cs.teacher_id
   limit 10
```
 
 И для полноты картины
 ```sql
  select concat(t.first_name, ' ', t.middle_name, ' ', t.last_name) as 'teacher', 
         concat(c.title, ' at ', c.study_year, ' year') as 'course' 
    from course_session cs 
         right join teachers t on t.id = cs.teacher_id
         left join courses c on c.id = cs.course_id
order by cs.teacher_id
   limit 100
 ```
 
 А теперь добавим **case** и получим
 ```sql
   select concat(t.first_name, ' ', t.middle_name, ' ', t.last_name) as 'teacher', 
		 (
                   case when c.id is null then 'have no courses'
                        else concat('teaches ', c.title, ' at ', c.study_year, ' year') 
                   end
                 ) as 'course'
    from course_session cs 
         right join teachers t on t.id = cs.teacher_id
         left join courses c on c.id = cs.course_id
order by cs.teacher_id
   limit 100

 ```
#### И все же, что такое join.
Допустим, у нас есть таблицы **a(a1, a2)** и **b(b1, b2**.

Простой запрос просто перемножит данные
```sql
select * 
  from a, b;
```

Такой запрос выведет все значения **a** и **b** такие, где **a1** и **b1** совпадают
```sql
select * 
  from a, b
 where a.a1 = b.b1;
```
Ровно то же самое сделает **cross join**: 
```sql
select * 
  from a 
       cross join b on a.a1 = b.b1;
```
А вот **left join** выведет все значения **левой** таблицы, и соответствующие значения **правой** таблицы, если они существуют. Если не существуют - **NULL**
```sql
select * 
  from a left join b on a.a1 = b.b1;
```
Как нетрудно догадаться, то же самое для **right join** - выводятся все значения **правой** таблицы и соответствующие для левой, если существуют
```sql
select * 
  from a right join b on a.a1 = b.b1;
```

[Статья про join'ы](http://www.skillz.ru/dev/php/article-Obyasnenie_SQL_obedinenii_JOIN_INNER_OUTER.html)


### Теперь нам хотелось бы заполнить таблицу с треками курса
Я могу это сделать случайным образом. Я запущу нижеприведенный скрипт 10 раз. Таким образом я сгенерирую (или нет) для каждого курса до 10ти записей. 

```sql
insert into course_session(teacher_id, course_id, date_start, date_end)
select teacher_id, course_id, dates.d, date_sub(dates.d, interval floor(1 + rand() * 200) day)
  from teaching_courses, (
                           select FROM_UNIXTIME(
                                      UNIX_TIMESTAMP('2015-01-01 00:00:00') + FLOOR(0 + (RAND() * 1000)) * 86400
				  ) as d
		          ) dates
 where rand() * 100 > 50
```
Давайте разберем, что тут написано
```sql
-- Генерируем случайную дату в пределах 1000 дней от 1го января 2015 года. 86400 - это количество миллисекунд в дне
select FROM_UNIXTIME(
    UNIX_TIMESTAMP('2015-01-01 00:00:00') + FLOOR(0 + (RAND() * 1000)) * 86400
) 
```
```sql
...
  -- Здесь я перемножают таблицу со всеми активными курсами на таблицу из одной строки (случайным образом сгенерированное время). 
  -- Подумайте, почему я не делаю это прямо в верхнем select'е.
  from teaching_courses, (
                           select FROM_UNIXTIME(
                                      UNIX_TIMESTAMP('2015-01-01 00:00:00') + FLOOR(0 + (RAND() * 1000)) * 86400
				  ) as d
		          ) dates
...
```
```sql
...
-- Внесение случайной компоненты в факт добавления трека. Если убрать ее, то для каждого курса за один запрос сгенерируется
-- один трек. Я же хочу, чтобы каждый запрос добавлял от 0 до 1го трека.
where rand() * 100 > 50
...
```
```sql
...
-- Здесь я к сгенерированной дате прибавляю случайное количество дней (от 1 до 200)
date_sub(dates.d, interval floor(1 + rand() * 200) day)
...
```
Таким образом я генерирую случайное количество треков курсов со случайными датами начала и конца.


#### Давайте, проанализируем получившиеся данные.

Сколько курсов всего вел какой учитель?
```sql
  select t.id, t.last_name, t.first_name, t.middle_name, count(*)
    from course_session cs, teachers t
   where t.id = cs.teacher_id
group by cs.teacher_id
```

Сколько раз каждый учитель вел конкретный курс?
```sql
  select t.id, t.last_name, t.first_name, t.middle_name, 
         concat(c.title, ' on ', c.study_year, ' year') as 'course', 
         concat(cast(count(*) as char), ' times') as 'times'
    from course_session cs, teachers t, courses c
   where t.id = cs.teacher_id 
         and cs.course_id = c.id
group by cs.teacher_id, cs.course_id
```

### И теперь добавим оставшиеся данные в network_access
```sql
insert into network_access(student_id, login)
select s.id, 
    lower(
          concat(
	         substr(s.last_name from 1 for 2), 
		 substr(s.first_name from 1 for 2), 
		 substr(s.middle_name from 1 for 2), 
		 cast(floor(100 + rand() * 899 ) as char)))
  from students s
```
