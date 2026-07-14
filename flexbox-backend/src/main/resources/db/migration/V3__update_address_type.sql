CREATE
SEQUENCE IF NOT EXISTS revinfo_seq START
WITH 1 INCREMENT BY 50;

CREATE TABLE revchanges
(
    rev        BIGINT NOT NULL,
    entityname VARCHAR(255)
);

CREATE TABLE revinfo
(
    rev      BIGINT NOT NULL,
    revtstmp BIGINT,
    CONSTRAINT pk_revinfo PRIMARY KEY (rev)
);

ALTER TABLE revchanges
    ADD CONSTRAINT fk_revchanges_on_default_tracking_modified_entities_changelog FOREIGN KEY (rev) REFERENCES revinfo (rev);

ALTER TABLE public.address
    ALTER COLUMN civic_no TYPE TEXT USING (civic_no:: TEXT);

ALTER TABLE public.address
    ALTER COLUMN type TYPE VARCHAR (255) USING (type :: VARCHAR (255));

ALTER TABLE public.address
    ALTER COLUMN unit_no TYPE TEXT USING (unit_no:: TEXT);

ALTER TABLE public.user_role
    ADD CONSTRAINT pk_user_role PRIMARY KEY (user_id);