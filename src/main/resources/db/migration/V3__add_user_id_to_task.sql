ALTER TABLE task_manager.task ADD COLUMN user_id INT NOT NULL;
UPDATE task_manager.task SET user_id = 2 WHERE id = 2;