CREATE TABLE devs (
    id       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name     VARCHAR(64) NOT NULL,
    email    VARCHAR(64) NOT NULL UNIQUE,
    password TEXT NOT NULL
    -- Every dev belongs to exactly one project. can be null initially
    project_id    UUID REFERENCES projects(id) ON DELETE RESTRICT SET NULL
);

CREATE TABLE projects (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(64) NOT NULL,
    description TEXT
);

CREATE TABLE tasks (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    status      SMALLINT NOT NULL DEFAULT 0,
    project_id  UUID NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    dev_id      UUID REFERENCES devs(id) ON DELETE RESTRICT SET NULL
);

CREATE INDEX idx_tasks_project_id ON tasks(project_id);
CREATE INDEX idx_tasks_status ON tasks(status);
CREATE INDEX idx_tasks_dev_id ON tasks(dev_id);
CREATE INDEX idx_dev_email ON devs(email);