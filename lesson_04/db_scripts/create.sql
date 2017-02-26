drop database if exists my_study; -- удаляем базу, если существует (удалит все данные!)
create database my_study 
default character set utf8       -- кодировка и набор символов
default collate utf8_general_ci; -- набор правил для сравнения символов данной кодировки
use my_study; -- переключаемся на базу
drop table if exists teachers;                      -- удаляем таблицу, если она уже существует
create table teachers (
             id int auto_increment primary key,     -- автоматически сгенерированный уникальный идентификатор
     first_name varchar(100) not null,              -- имя учителя (непустое)
      last_name varchar(100) not null,              -- фамилия учителя (непустая)
    middle_name varchar(100) not null,              -- отчество учителя (непустое)
          phone varchar(50) unique,                 -- телефон учителя (может быть пустым, но не доолжен повторяться)

         unique(first_name, middle_name, last_name) -- требуем, чтобы ФИО было уникальным
);
drop table if exists courses;                                  -- удаляем таблицу, если она уже существует
create table courses (
            id int auto_increment primary key,                 -- уникальный идентификатор курса, сгенерированный вручную
         title varchar(200) not null,                          -- название курса (непустое)
    study_year int check (study_year > 0 and study_year <= 6), -- год обучения, на котором проходится курс. От 1 до 6 включительно. 

        unique(title, study_year)                              -- требование, чтобы курс с одним названием и одним годом не повторялся в таблице
);
drop table if exists teaching_courses;                -- удаляем таблицу, если она уже существует
create table teaching_courses (
    teacher_id int not null,                          -- уникальный идентификатор учителя, который ведет этот курс (непустой)
     course_id int not null,                          -- уникальный идентификатор курса (непустой)

   foreign key (teacher_id) references teachers(id),  -- допускается учитель только из таблицы учителей.
   foreign key (course_id) references courses(id),    -- допускается курс только из таблицы курсов.
        primary key(teacher_id, course_id)            -- уникальным идентификатором в для этой таблицы будет пара из учителя и курса.
);
drop table if exists students;                       -- удаляем таблицу, если она уже существует
create table students (
             id int auto_increment primary key,      -- автоматически сгенерированный уникальный идентификатор
     first_name varchar(100) not null,               -- имя ученика (непустое)
      last_name varchar(100) not null,               -- фамилия ученика (непустая)
    middle_name varchar(100) not null,               -- отчество ученика (непустое)

         unique(first_name, middle_name, last_name)  -- требуем, чтобы ФИО было уникальным
);
drop table if exists course_session;           -- удаляем таблицу, если она уже существует
create table course_session (
            id int auto_increment primary key, -- автоматически сгенерированный уникальный идентификатор
     course_id int not null,                   -- идентификатор курса (непустой)
    teacher_id int not null,                   -- идентификатор учителя (непустой)
    date_start datetime not null,              -- дата начала курса (непустая)
      date_end datetime not null,              -- дата конца курса (непустая)

       -- пара "курс и учитель" должны быть из соответствующей таблицы `teaching_courses`
   foreign key(course_id, teacher_id) references teaching_courses(course_id, teacher_id)
);
drop table if exists course_student;                                    -- удаляем таблицу, если она уже существует
create table course_student (
                   id int auto_increment primary key,                   -- автоматически сгенерированный уникальный идентификатор
           student_id int not null,                                     -- уникальный идентификатор студента
    course_session_id int not null,                                     -- уникальный идентификатор трека курса
          result_mark int check(result_mark >= 2 and result_mark <= 5), -- оценка студента, от 2 до 5

          foreign key(student_id) references students(id),              -- ученики должны быть только из таблицы учеников
          foreign key(course_session_id) references course_session(id), -- треки курса должны быть только из таблицы треков курсов
               unique(student_id, course_session_id)                    -- каждый студент может пройти курс только один раз
);
drop table if exists network_access;                  -- удаляем таблицу, если она уже существует
create table network_access (
              id int auto_increment primary key,      -- автоматически сгенерированный уникальный идентификатор записи в таблице
      student_id int not null unique,                 -- идентификатор ученика (непустой, уникальный)
           login varchar(100) not null unique,        -- логин ученика (непустой, уникальный)
         created datetime not null default now(),     -- дата создания учетной записи, непустая, по умолчанию - "сегодня"

      foreign key(student_id) references students(id) -- ученики должны быть только из таблицы учеников
);

