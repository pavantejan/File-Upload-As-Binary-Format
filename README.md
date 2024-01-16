Database - 

1. create database UploadDoc;
2. use uploadDoc;
3. CREATE TABLE pdf_documents (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) unique,
    content mediumblob
);


To check the database column types -> DESCRIBE pdf_documents;

