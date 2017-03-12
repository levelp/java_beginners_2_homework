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
        unique(teacher_id, course_id)
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
