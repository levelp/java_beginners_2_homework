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
   foreign key(course_id, teacher_id) references teaching_courses(course_id, teacher_id),
       -- требование, чтобы начало курса было раньше конца
         check(date_start < date_end)
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
 
 
 
