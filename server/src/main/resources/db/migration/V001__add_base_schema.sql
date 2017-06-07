CREATE TABLE api_review (
  id                    BIGSERIAL PRIMARY KEY,
  name                  TEXT,
  json_payload          TEXT NOT NULL,
  api_definition        TEXT,
  successful_processed  BOOLEAN NOT NULL,
  day                   DATE NOT NULL,
  created               TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  number_of_endpoints   INT,
  must_violations       INT,
  should_violations     INT,
  may_violations        INT,
  hint_violations       INT
);

CREATE TABLE rule_violation (
  id            BIGSERIAL PRIMARY KEY,
  api_review_id BIGINT REFERENCES api_review (id),
  name          TEXT NOT NULL,
  type          VARCHAR(10) NOT NULL,
  occurrence    INT NOT NULL
);

CREATE INDEX api_review_day_idx ON api_review (day);
