CREATE TABLE dev (
    id       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name     VARCHAR(64) NOT NULL,
    email    VARCHAR(64) NOT NULL UNIQUE,
    password TEXT NOT NULL
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
    dev_id      UUID REFERENCES dev(id) ON DELETE RESTRICT SET NULL
);

CREATE TABLE project_members (
    project_id UUID NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    dev_id     UUID NOT NULL REFERENCES dev(id) ON DELETE CASCADE,
    PRIMARY KEY (project_id, dev_id)
);

CREATE INDEX idx_tasks_project_id ON tasks(project_id);
CREATE INDEX idx_tasks_status ON tasks(status);
CREATE INDEX idx_tasks_dev_id ON tasks(dev_id);
CREATE INDEX idx_dev_email ON dev(email);
CREATE INDEX idx_project_members_dev_id ON project_members(dev_id);