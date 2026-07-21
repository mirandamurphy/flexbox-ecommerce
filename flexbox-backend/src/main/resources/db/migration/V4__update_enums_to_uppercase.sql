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

ALTER TABLE public."order"
    ADD order_status ORDER_STATUS;

ALTER TABLE public.checkout_session
    ADD status CHECKOUT_SESSION_STATUS;

ALTER TABLE revchanges
    ADD CONSTRAINT fk_revchanges_on_default_tracking_modified_entities_changelog FOREIGN KEY (rev) REFERENCES revinfo (rev);

ALTER TABLE public."order"
    DROP COLUMN status;

ALTER TABLE public.checkout_session
    DROP COLUMN staus;

ALTER TABLE public.address
    ALTER COLUMN civic_no TYPE TEXT USING (civic_no:: TEXT);

ALTER TABLE public.address
    DROP COLUMN type;

ALTER TABLE public.address
    ADD type VARCHAR(255) NOT NULL;

ALTER TABLE public.address
    ALTER COLUMN type SET NOT NULL;

ALTER TABLE public.tokens
    ALTER COLUMN type SET NOT NULL;

ALTER TABLE public.address
    ALTER COLUMN unit_no TYPE TEXT USING (unit_no:: TEXT);

ALTER TABLE public.user_role
    ADD CONSTRAINT pk_user_role PRIMARY KEY (user_id);