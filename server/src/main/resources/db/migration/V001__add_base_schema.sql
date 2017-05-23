CREATE TABLE api_review_request (
  id                    BIGSERIAL PRIMARY KEY,
  json_payload          TEXT NOT NULL,
  api_definition        TEXT,
  successful_processed  BOOLEAN NOT NULL,
  created               TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE review_statistic (
  name  TEXT PRIMARY KEY,
  value DOUBLE PRECISION NOT NULL,
  day   DATE NOT NULL
);

ALTER TABLE review_statistic ADD CONSTRAINT unique_value_per_day UNIQUE (name, value, day);

CREATE INDEX api_review_request_created_idx ON api_review_request (created);
CREATE INDEX report_statistic_day_idx ON review_statistic (day);
