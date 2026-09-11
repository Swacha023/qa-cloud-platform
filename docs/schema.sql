CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  display_name VARCHAR(255) NOT NULL,
  role VARCHAR(50) NOT NULL
);
CREATE TABLE projects (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  created_at TIMESTAMP NOT NULL,
  created_by_id BIGINT NOT NULL REFERENCES users(id)
);
CREATE TABLE test_suites (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  project_id BIGINT NOT NULL REFERENCES projects(id)
);
CREATE TABLE test_cases (
  id BIGSERIAL PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  preconditions TEXT,
  steps TEXT,
  expected_result TEXT,
  priority VARCHAR(20) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  suite_id BIGINT NOT NULL REFERENCES test_suites(id)
);
CREATE TABLE test_runs (
  id BIGSERIAL PRIMARY KEY,
  project_id BIGINT NOT NULL REFERENCES projects(id),
  started_at TIMESTAMP NOT NULL,
  completed_at TIMESTAMP,
  status VARCHAR(20) NOT NULL,
  commit_hash VARCHAR(255)
);
CREATE TABLE test_results (
  id BIGSERIAL PRIMARY KEY,
  run_id BIGINT NOT NULL REFERENCES test_runs(id),
  test_case_id BIGINT NOT NULL REFERENCES test_cases(id),
  status VARCHAR(20) NOT NULL,
  duration_ms BIGINT,
  error_message TEXT
);
CREATE TABLE bugs (
  id BIGSERIAL PRIMARY KEY,
  project_id BIGINT NOT NULL REFERENCES projects(id),
  title VARCHAR(255) NOT NULL,
  description TEXT,
  severity VARCHAR(20) NOT NULL,
  status VARCHAR(20) NOT NULL,
  assigned_to_id BIGINT REFERENCES users(id),
  created_at TIMESTAMP NOT NULL,
  resolved_at TIMESTAMP
);

-- Example QA dashboard aggregate query
SELECT
  COUNT(*) AS total,
  COUNT(*) FILTER (WHERE status = 'PASSED') AS passed,
  COUNT(*) FILTER (WHERE status = 'FAILED') AS failed,
  COUNT(*) FILTER (WHERE status = 'SKIPPED') AS skipped,
  ROUND(
    100.0 * COUNT(*) FILTER (WHERE status = 'PASSED') / NULLIF(COUNT(*), 0),
    1
  ) AS pass_rate
FROM test_results
WHERE run_id = 142;
