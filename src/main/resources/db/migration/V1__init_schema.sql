CREATE TABLE tasks (
                       id BIGSERIAL PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       description TEXT,
                       completed BOOLEAN NOT NULL DEFAULT FALSE,
                       created_at TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP NOT NULL,
                       due_date DATE,
                       priority VARCHAR(50) NOT NULL,
                       tags TEXT
);

CREATE TABLE task_attachments (
                                  id BIGSERIAL PRIMARY KEY,
                                  task_id BIGINT NOT NULL,
                                  file_name VARCHAR(255) NOT NULL,
                                  stored_file_name VARCHAR(255) NOT NULL,
                                  content_type VARCHAR(100),
                                  size_bytes BIGINT NOT NULL,
                                  uploaded_at TIMESTAMP NOT NULL,

                                  CONSTRAINT fk_task_attachments_task
                                      FOREIGN KEY (task_id)
                                          REFERENCES tasks(id)
                                          ON DELETE CASCADE
);