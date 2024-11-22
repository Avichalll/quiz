-- V1__init_database.sql
CREATE SEQUENCE qns_id_seq START WITH 1000 INCREMENT BY 1;
-- Create table for questions
CREATE TABLE IF NOT EXISTS questions (
    id INTEGER PRIMARY KEY DEFAULT nextval('qns_id_seq'),
    
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP,
    created_by INTEGER NOT NULL,
    last_modified_by INTEGER

    question TEXT NOT NULL,
    answer1 TEXT NOT NULL,
    answer2 TEXT NOT NULL,
    answer3 TEXT NOT NULL,
    answer4 TEXT NOT NULL,
    category TEXT NOT NULL,
    correct_answer TEXT NOT NULL,
    difficulty_level TEXT NOT NULL

);