Alter TABLE books
ADD COLUMN created_By VARCHAR(255);
ALTER TABLE books
ADD COLUMN last_modified_by VARCHAR(255);