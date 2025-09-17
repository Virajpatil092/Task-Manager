CREATE TABLE IF NOT EXISTS users (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(200) NOT NULL,
    display_name VARCHAR(200),
    role VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS projects (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description VARCHAR(500),
    owner_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_project_owner FOREIGN KEY (owner_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS tasks (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description VARCHAR(1000),
    project_id UUID NOT NULL,
    assignee_id UUID,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_task_project FOREIGN KEY (project_id) REFERENCES projects(id),
    CONSTRAINT fk_task_assignee FOREIGN KEY (assignee_id) REFERENCES users(id)
);