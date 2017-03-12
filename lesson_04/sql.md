Создание нашей базы
===


Удаление базы данных, если она уже существует.
---
```sql
drop database if exists my_study;
```


Создание базы данных
---
```sql
 create database my_study 
default character set utf8
default collate utf8_general_ci;
```

Переключиться на текущую базу данных
---
```sql
use my_study
```

Создаем первую таблицу (учителя) с уникальным ФИО и уникальным телефоном. Перед этим удаляем, если она уже существует.
---
```sql
drop table if exists teachers;
create table teachers (
             id int auto_increment primary key,
     first_name varchar(100) not null,
      last_name varchar(100) not null,
    middle_name varchar(100) not null,
          phone varchar(50) unique,
          
         unique(first_name, middle_name, last_name)
)
```

Создаем вторую таблицу (курсы), которая будет хранить информацию о курсе. Добавляем так же год обучения (от 1 до 6)
---
```sql
drop table if exists courses;
create table courses (
            id int auto_increment primary key,
         title varchar(200) not null,
    study_year int check (study_year > 0 and study_year <= 6),

        unique(title, study_year)
)
```

Создаем таблицу, которая будет хранить, какой из курсов какой учитель может вести. У нас ситуация, когда каждый учитель может вести несколько курсов и каждый курс может вести несколько учителей.
---
```sql
drop table if exists teaching_courses;
create table teaching_courses (
    teacher_id int not null,
     course_id int not null,
       
   foreign key (teacher_id) references teachers(id),
   foreign key (course_id) references courses(id),
        primary key(teacher_id, course_id)
)
```


Создаем четвертую таблицу (ученики) с уникальным ФИО. Перед этим удаляем, если она уже существует.
---
```sql
drop table if exists students;
create table students (
             id int auto_increment primary key,
     first_name varchar(100) not null,
      last_name varchar(100) not null,
    middle_name varchar(100) not null,
          
         unique(first_name, middle_name, last_name)
)
```

Создаем пятую таблицу с треком курса (то есть место, где будет лежать информация о начале, конце курса (в т.ч. предполагаемом), учителем, который его ведет.
---
```sql
drop table if exists course_session;
create table course_session (
            id int auto_increment primary key,
     course_id int not null,
    teacher_id int not null,
    date_start datetime not null,
      date_end datetime not null,
       
   foreign key(course_id, teacher_id) references teaching_courses(course_id, teacher_id),
         check(date_start < date_end)
)
```

Теперь создаем таблицу с оценками ученика по каждому пройденному треку
---
```sql
drop table if exists course_student;
create table course_student (
                   id int auto_increment primary key,
           student_id int not null,
    course_session_id int not null,
          result_mark int check(result_mark >= 2 and result_mark <= 5),
          
          foreign key(student_id) references students(id),
          foreign key(course_session_id) references course_session(id),
               unique(student_id, course_session_id)
) 
```


Создаем таблицу с учетными данными в компьютерном класса для каждого ученика
---
```sql
drop table if exists network_access;
create table network_access (
              id int auto_increment primary key,
      student_id int not null unique,
           login varchar(100) not null unique,
         created datetime not null default now(),
      
      foreign key(student_id) references students(id)
) 
```

