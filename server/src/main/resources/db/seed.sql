-- Seed data: 2 projects, 5 devs, 12 tasks
-- Uses explicit UUIDs so relationships are readable and referenceable.

-- Clear existing data first (safe for repeated re-seeding in dev)
DELETE FROM tasks;
DELETE FROM devs;
DELETE FROM projects;

-- ============ Projects ============
INSERT INTO projects (id, name, description) VALUES
('11111111-1111-1111-1111-111111111111', 'Website Redesign', 'Revamp the public marketing site'),
('22222222-2222-2222-2222-222222222222', 'Mobile App', 'Native iOS/Android app for customers');

-- ============ Devs ============
-- password_hash values below are placeholders (NOT real bcrypt hashes).
-- Replace with real bcrypt output if you need to actually log in as these
-- users, e.g. via an online bcrypt generator or your app's registration flow.
-- 3 devs on Website Redesign, 2 devs on Mobile App
INSERT INTO devs (id, name, email, password, project_id) VALUES
('aaaaaaaa-0001-0001-0001-000000000001', 'Alice Chen',    'alice@taskflow.dev',   '$2a$10$placeholderHashAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA', '11111111-1111-1111-1111-111111111111'),
('aaaaaaaa-0002-0002-0002-000000000002', 'Ben Osei',      'ben@taskflow.dev',     '$2a$10$placeholderHashBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB', '11111111-1111-1111-1111-111111111111'),
('aaaaaaaa-0003-0003-0003-000000000003', 'Carla Ruiz',    'carla@taskflow.dev',   '$2a$10$placeholderHashCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC', '11111111-1111-1111-1111-111111111111'),
('aaaaaaaa-0004-0004-0004-000000000004', 'Dev Patel',     'dev@taskflow.dev',     '$2a$10$placeholderHashDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD', '22222222-2222-2222-2222-222222222222'),
('aaaaaaaa-0005-0005-0005-000000000005', 'Elena Popescu', 'elena@taskflow.dev',   '$2a$10$placeholderHashEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE', '22222222-2222-2222-2222-222222222222');

-- ============ Tasks ============
-- status: 0 = TODO, 1 = IN_PROGRESS, 2 = DONE  (matches Task.TaskStatusConverter)
-- 8 tasks on Website Redesign, 4 on Mobile App
-- Two tasks (last one per project) are left unassigned (NULL assignee_id)
-- to demonstrate the optional-assignee feature.

-- Website Redesign tasks
INSERT INTO tasks (id, title, description, status, project_id, dev_id) VALUES
('bbbbbbbb-0001-0001-0001-000000000001', 'Design new homepage layout',   'Figma mockups for the hero and nav',        0, '11111111-1111-1111-1111-111111111111', 'aaaaaaaa-0001-0001-0001-000000000001'),
('bbbbbbbb-0002-0002-0002-000000000002', 'Set up CI/CD pipeline',        'GitHub Actions build + deploy',              1, '11111111-1111-1111-1111-111111111111', 'aaaaaaaa-0002-0002-0002-000000000002'),
('bbbbbbbb-0003-0003-0003-000000000003', 'Migrate CMS content',          'Move blog posts to new CMS',                 0, '11111111-1111-1111-1111-111111111111', 'aaaaaaaa-0003-0003-0003-000000000003'),
('bbbbbbbb-0004-0004-0004-000000000004', 'Implement responsive nav',     'Mobile hamburger menu',                      1, '11111111-1111-1111-1111-111111111111', 'aaaaaaaa-0001-0001-0001-000000000001'),
('bbbbbbbb-0005-0005-0005-000000000005', 'Add contact form validation',  'Client + server-side validation',            2, '11111111-1111-1111-1111-111111111111', 'aaaaaaaa-0002-0002-0002-000000000002'),
('bbbbbbbb-0006-0006-0006-000000000006', 'Optimize image loading',       'Lazy loading + WebP conversion',             0, '11111111-1111-1111-1111-111111111111', 'aaaaaaaa-0003-0003-0003-000000000003'),
('bbbbbbbb-0007-0007-0007-000000000007', 'Write accessibility audit',    'WCAG 2.1 AA compliance check',               2, '11111111-1111-1111-1111-111111111111', 'aaaaaaaa-0001-0001-0001-000000000001'),
('bbbbbbbb-0008-0008-0008-000000000008', 'Set up analytics tracking',    NULL,                                          0, '11111111-1111-1111-1111-111111111111', NULL),

-- Mobile App tasks
('bbbbbbbb-0009-0009-0009-000000000009', 'Build login screen',           'Email/password + biometric login',           1, '22222222-2222-2222-2222-222222222222', 'aaaaaaaa-0004-0004-0004-000000000004'),
('bbbbbbbb-0010-0010-0010-000000000010', 'Implement push notifications', 'FCM for Android, APNs for iOS',              0, '22222222-2222-2222-2222-222222222222', 'aaaaaaaa-0005-0005-0005-000000000005'),
('bbbbbbbb-0011-0011-0011-000000000011', 'Add offline mode',             'Local caching with sync on reconnect',       2, '22222222-2222-2222-2222-222222222222', 'aaaaaaaa-0004-0004-0004-000000000004'),
('bbbbbbbb-0012-0012-0012-000000000012', 'App store submission prep',    'Screenshots, metadata, privacy policy',      0, '22222222-2222-2222-2222-222222222222', NULL);