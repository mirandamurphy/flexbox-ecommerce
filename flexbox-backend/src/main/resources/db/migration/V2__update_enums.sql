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

CREATE TABLE public.address
(
    address_id    BIGINT                   NOT NULL,
    user_id       BIGINT,
    unit_no       TEXT,
    civic_no      TEXT,
    street        TEXT,
    po_box_number TEXT,
    city          TEXT,
    province PROVINCES,
    postal_code   VARCHAR(7),
    country       VARCHAR(2) DEFAULT 'CA',
    type ADDRESS_TYPE NOT NULL,
    is_default    BOOLEAN    DEFAULT FALSE NOT NULL,
    is_active     BOOLEAN,
    created_at    TIMESTAMP WITHOUT TIME ZONE,
    updated_at    TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_address PRIMARY KEY (address_id)
);

CREATE TABLE public.cart
(
    cart_id    BIGINT NOT NULL,
    user_id    BIGINT,
    status CART_STATUS,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_cart PRIMARY KEY (cart_id)
);

CREATE TABLE public.cart_item
(
    cart_item_id        BIGINT NOT NULL,
    cart_id             BIGINT,
    subscription_box_id BIGINT,
    quantity            INTEGER,
    unit_price_snapshot DECIMAL(5, 2),
    added_at            TIMESTAMP WITHOUT TIME ZONE,
    updated_at          TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_cart_item PRIMARY KEY (cart_item_id)
);

CREATE TABLE public.category
(
    category_id BIGINT NOT NULL,
    name        TEXT,
    description TEXT,
    CONSTRAINT pk_category PRIMARY KEY (category_id)
);

CREATE TABLE public.checkout_session
(
    checkout_session_id  BIGINT NOT NULL,
    user_id              BIGINT,
    stripe_session_id    TEXT,
    subscription_plan_id BIGINT,
    payment_id           BIGINT,
    mode CHECKOUT_SESSION_MODE,
    status CHECKOUT_SESSION_STATUS,
    amount_subtotal      DECIMAL(7, 2),
    amount_tax           DECIMAL(7, 2),
    amount_total         DECIMAL(7, 2),
    currency             VARCHAR(3),
    success_url          TEXT,
    cancel_url           TEXT,
    expires_at           TIMESTAMP WITHOUT TIME ZONE,
    completed_at         TIMESTAMP WITHOUT TIME ZONE,
    created_at           TIMESTAMP WITHOUT TIME ZONE,
    updated_at           TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_checkout_session PRIMARY KEY (checkout_session_id)
);

CREATE TABLE public.invoice
(
    invoice_id           BIGINT NOT NULL,
    stripe_invoice_id    TEXT,
    subscription_plan_id BIGINT,
    amount_due           DECIMAL(7, 2),
    currency             VARCHAR(3) DEFAULT 'CAD',
    status               TEXT,
    created_at           TIMESTAMP WITHOUT TIME ZONE,
    paid_at              TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_invoice PRIMARY KEY (invoice_id)
);

CREATE TABLE public.marketing_consent
(
    consent_id BIGINT NOT NULL,
    user_id    BIGINT,
    action MARKETING_CONSENT_ACTION,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_marketing_consent PRIMARY KEY (consent_id)
);

CREATE TABLE public.newsletter
(
    newsletter_id BIGINT NOT NULL,
    name          TEXT,
    subject       TEXT,
    html_file     TEXT,
    type          TEXT,
    created_date  TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_newsletter PRIMARY KEY (newsletter_id)
);

CREATE TABLE public.newsletter_subscribers
(
    sent_at       TIMESTAMP WITHOUT TIME ZONE,
    newsletter_id BIGINT NOT NULL,
    user_id       BIGINT NOT NULL,
    CONSTRAINT pk_newsletter_subscribers PRIMARY KEY (newsletter_id, user_id)
);

CREATE TABLE public."order"
(
    order_id            BIGINT NOT NULL,
    user_id             BIGINT,
    shipping_address_id BIGINT,
    billing_address_id  BIGINT,
    currency            VARCHAR(3) DEFAULT 'CAD',
    total_amount        DECIMAL(7, 2),
    order_status ORDER_STATUS,
    order_date          TIMESTAMP WITHOUT TIME ZONE,
    updated_at          TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_order PRIMARY KEY (order_id)
);

CREATE TABLE public.order_item
(
    order_item_id                  BIGINT NOT NULL,
    order_id                       BIGINT,
    subscription_box_id            BIGINT,
    subscription_box_name_snapshot TEXT,
    quantity                       INTEGER,
    purchase_price_snapshot        DECIMAL(5, 2),
    CONSTRAINT pk_order_item PRIMARY KEY (order_item_id)
);

CREATE TABLE public.payment
(
    payment_id               BIGINT NOT NULL,
    order_id                 BIGINT,
    stripe_payment_intent_id TEXT,
    idempotency_key UUID,
    amount                   DECIMAL(7, 2),
    currency                 VARCHAR(3) DEFAULT 'CAD',
    paid_at                  TIMESTAMP WITHOUT TIME ZONE,
    status PAYMENT_STATUS,
    CONSTRAINT pk_payment PRIMARY KEY (payment_id)
);

CREATE TABLE public.payment_method
(
    payment_method_id        BIGINT NOT NULL,
    user_id                  BIGINT,
    stripe_payment_method_id TEXT,
    type                     TEXT DEFAULT 'card',
    last_4_digits            VARCHAR(4),
    expiration_month         INTEGER,
    expiration_year          INTEGER,
    is_default               BOOLEAN,
    created_at               TIMESTAMP WITHOUT TIME ZONE,
    updated_at               TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_payment_method PRIMARY KEY (payment_method_id)
);

CREATE TABLE public.product
(
    product_id    BIGINT NOT NULL,
    category_id   BIGINT,
    sku           TEXT,
    brand         TEXT,
    name          TEXT,
    description   TEXT,
    cost_per_unit DECIMAL(5, 2),
    is_active     BOOLEAN,
    created_at    TIMESTAMP WITHOUT TIME ZONE,
    updated_at    TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_product PRIMARY KEY (product_id)
);

CREATE TABLE public.product_inventory
(
    product_id BIGINT NOT NULL,
    in_stock   INTEGER,
    reserved   INTEGER,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_product_inventory PRIMARY KEY (product_id)
);

CREATE TABLE public.role
(
    role_id     BIGINT NOT NULL,
    name ROLE_NAME,
    description TEXT,
    CONSTRAINT pk_role PRIMARY KEY (role_id)
);

CREATE TABLE public.subscription_box
(
    subscription_box_id BIGINT NOT NULL,
    name                TEXT,
    description         TEXT,
    image_file          TEXT,
    available_units     INTEGER,
    is_active           BOOLEAN,
    created_at          TIMESTAMP WITHOUT TIME ZONE,
    updated_at          TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_subscription_box PRIMARY KEY (subscription_box_id)
);

CREATE TABLE public.subscription_box_price
(
    subscription_box_price_id BIGINT NOT NULL,
    subscription_box_id       BIGINT,
    amount                    DECIMAL(5, 2),
    currency                  VARCHAR(3) DEFAULT 'CAD',
    starts_at                 TIMESTAMP WITHOUT TIME ZONE,
    ends_at                   TIMESTAMP WITHOUT TIME ZONE,
    stripe_price_id           TEXT,
    CONSTRAINT pk_subscription_box_price PRIMARY KEY (subscription_box_price_id)
);

CREATE TABLE public.subscription_box_product
(
    quantity            INTEGER DEFAULT 1 NOT NULL,
    subscription_box_id BIGINT            NOT NULL,
    product_id          BIGINT            NOT NULL,
    CONSTRAINT pk_subscription_box_product PRIMARY KEY (subscription_box_id, product_id)
);

CREATE TABLE public.subscription_plan
(
    subscription_plan_id   BIGINT NOT NULL,
    user_id                BIGINT,
    plan_name              TEXT,
    subscription_box_id    BIGINT,
    shipping_address_id    BIGINT,
    billing_address_id     BIGINT,
    stripe_subscription_id TEXT,
    current_plan_start     TIMESTAMP WITHOUT TIME ZONE,
    current_plan_end       TIMESTAMP WITHOUT TIME ZONE,
    cancel_at_period_end   BOOLEAN,
    cancelled_at           TIMESTAMP WITHOUT TIME ZONE,
    created_at             TIMESTAMP WITHOUT TIME ZONE,
    updated_at             TIMESTAMP WITHOUT TIME ZONE,
    status SUBSCRIPTION_PLAN_STATUS,
    CONSTRAINT pk_subscription_plan PRIMARY KEY (subscription_plan_id)
);

CREATE TABLE public.tokens
(
    token_id    BIGINT                NOT NULL,
    user_id     BIGINT,
    token_value TEXT                  NOT NULL,
    type TOKEN_TYPE NOT NULL,
    is_revoked  BOOLEAN DEFAULT FALSE NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE,
    expired_at  TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_tokens PRIMARY KEY (token_id)
);

CREATE TABLE public."user"
(
    user_id            BIGINT NOT NULL,
    stripe_customer_id TEXT,
    email              TEXT,
    password_hash      TEXT,
    first_name         TEXT,
    last_name          TEXT,
    phone_number       VARCHAR(15),
    is_enabled         BOOLEAN DEFAULT FALSE,
    created_at         TIMESTAMP WITHOUT TIME ZONE,
    updated_at         TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_user PRIMARY KEY (user_id)
);

CREATE TABLE public.user_role
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    CONSTRAINT pk_user_role PRIMARY KEY (user_id)
);

CREATE TABLE public.webhook_event
(
    stripe_webhook_event_id BIGINT NOT NULL,
    stripe_event_id         TEXT,
    event_type              TEXT,
    payload JSONB,
    is_processed            BOOLEAN,
    received_at             TIMESTAMP WITHOUT TIME ZONE,
    processed_at            TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_webhook_event PRIMARY KEY (stripe_webhook_event_id)
);

ALTER TABLE public.category
    ADD CONSTRAINT category_name_key UNIQUE (name);

ALTER TABLE public.checkout_session
    ADD CONSTRAINT checkout_session_stripe_session_id_key UNIQUE (stripe_session_id);

ALTER TABLE public.invoice
    ADD CONSTRAINT invoice_stripe_invoice_id_key UNIQUE (stripe_invoice_id);

ALTER TABLE public.payment_method
    ADD CONSTRAINT payment_method_stripe_payment_method_id_key UNIQUE (stripe_payment_method_id);

ALTER TABLE public.payment
    ADD CONSTRAINT payment_stripe_payment_intent_id_key UNIQUE (stripe_payment_intent_id);

ALTER TABLE public.product
    ADD CONSTRAINT product_sku_key UNIQUE (sku);

ALTER TABLE public.subscription_box_price
    ADD CONSTRAINT subscription_box_price_stripe_price_id_key UNIQUE (stripe_price_id);

ALTER TABLE public.subscription_plan
    ADD CONSTRAINT subscription_plan_stripe_subscription_id_key UNIQUE (stripe_subscription_id);

ALTER TABLE public.tokens
    ADD CONSTRAINT tokens_token_value_key UNIQUE (token_value);

ALTER TABLE public."user"
    ADD CONSTRAINT user_email_key UNIQUE (email);

ALTER TABLE public.user_role
    ADD CONSTRAINT user_role_user_id_key UNIQUE (user_id);

ALTER TABLE public."user"
    ADD CONSTRAINT user_stripe_customer_id_key UNIQUE (stripe_customer_id);

ALTER TABLE public.webhook_event
    ADD CONSTRAINT webhook_event_stripe_event_id_key UNIQUE (stripe_event_id);

ALTER TABLE public.address
    ADD CONSTRAINT FK_ADDRESS_ON_USER FOREIGN KEY (user_id) REFERENCES public."user" (user_id);

ALTER TABLE public.cart_item
    ADD CONSTRAINT FK_CART_ITEM_ON_CART FOREIGN KEY (cart_id) REFERENCES public.cart (cart_id);

ALTER TABLE public.cart_item
    ADD CONSTRAINT FK_CART_ITEM_ON_SUBSCRIPTION_BOX FOREIGN KEY (subscription_box_id) REFERENCES public.subscription_box (subscription_box_id);

ALTER TABLE public.cart
    ADD CONSTRAINT FK_CART_ON_USER FOREIGN KEY (user_id) REFERENCES public."user" (user_id);

ALTER TABLE public.checkout_session
    ADD CONSTRAINT FK_CHECKOUT_SESSION_ON_PAYMENT FOREIGN KEY (payment_id) REFERENCES public.payment (payment_id);

ALTER TABLE public.checkout_session
    ADD CONSTRAINT FK_CHECKOUT_SESSION_ON_SUBSCRIPTION_PLAN FOREIGN KEY (subscription_plan_id) REFERENCES public.subscription_plan (subscription_plan_id);

ALTER TABLE public.checkout_session
    ADD CONSTRAINT FK_CHECKOUT_SESSION_ON_USER FOREIGN KEY (user_id) REFERENCES public."user" (user_id);

ALTER TABLE public.invoice
    ADD CONSTRAINT FK_INVOICE_ON_SUBSCRIPTION_PLAN FOREIGN KEY (subscription_plan_id) REFERENCES public.subscription_plan (subscription_plan_id);

ALTER TABLE public.marketing_consent
    ADD CONSTRAINT FK_MARKETING_CONSENT_ON_USER FOREIGN KEY (user_id) REFERENCES public."user" (user_id);

ALTER TABLE public.newsletter_subscribers
    ADD CONSTRAINT FK_NEWSLETTER_SUBSCRIBERS_ON_NEWSLETTER FOREIGN KEY (newsletter_id) REFERENCES public.newsletter (newsletter_id);

ALTER TABLE public.newsletter_subscribers
    ADD CONSTRAINT FK_NEWSLETTER_SUBSCRIBERS_ON_USER FOREIGN KEY (user_id) REFERENCES public."user" (user_id);

ALTER TABLE public.order_item
    ADD CONSTRAINT FK_ORDER_ITEM_ON_ORDER FOREIGN KEY (order_id) REFERENCES public."order" (order_id);

ALTER TABLE public.order_item
    ADD CONSTRAINT FK_ORDER_ITEM_ON_SUBSCRIPTION_BOX FOREIGN KEY (subscription_box_id) REFERENCES public.subscription_box (subscription_box_id);

ALTER TABLE public."order"
    ADD CONSTRAINT FK_ORDER_ON_BILLING_ADDRESS FOREIGN KEY (billing_address_id) REFERENCES public.address (address_id);

ALTER TABLE public."order"
    ADD CONSTRAINT FK_ORDER_ON_SHIPPING_ADDRESS FOREIGN KEY (shipping_address_id) REFERENCES public.address (address_id);

ALTER TABLE public."order"
    ADD CONSTRAINT FK_ORDER_ON_USER FOREIGN KEY (user_id) REFERENCES public."user" (user_id);

ALTER TABLE public.payment_method
    ADD CONSTRAINT FK_PAYMENT_METHOD_ON_USER FOREIGN KEY (user_id) REFERENCES public."user" (user_id);

ALTER TABLE public.payment
    ADD CONSTRAINT FK_PAYMENT_ON_ORDER FOREIGN KEY (order_id) REFERENCES public."order" (order_id);

ALTER TABLE public.product_inventory
    ADD CONSTRAINT FK_PRODUCT_INVENTORY_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES public.product (product_id);

ALTER TABLE public.product
    ADD CONSTRAINT FK_PRODUCT_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES public.category (category_id);

ALTER TABLE public.subscription_box_price
    ADD CONSTRAINT FK_SUBSCRIPTION_BOX_PRICE_ON_SUBSCRIPTION_BOX FOREIGN KEY (subscription_box_id) REFERENCES public.subscription_box (subscription_box_id);

ALTER TABLE public.subscription_box_product
    ADD CONSTRAINT FK_SUBSCRIPTION_BOX_PRODUCT_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES public.product (product_id);

ALTER TABLE public.subscription_box_product
    ADD CONSTRAINT FK_SUBSCRIPTION_BOX_PRODUCT_ON_SUBSCRIPTION_BOX FOREIGN KEY (subscription_box_id) REFERENCES public.subscription_box (subscription_box_id);

ALTER TABLE public.subscription_plan
    ADD CONSTRAINT FK_SUBSCRIPTION_PLAN_ON_BILLING_ADDRESS FOREIGN KEY (billing_address_id) REFERENCES public.address (address_id);

ALTER TABLE public.subscription_plan
    ADD CONSTRAINT FK_SUBSCRIPTION_PLAN_ON_SHIPPING_ADDRESS FOREIGN KEY (shipping_address_id) REFERENCES public.address (address_id);

ALTER TABLE public.subscription_plan
    ADD CONSTRAINT FK_SUBSCRIPTION_PLAN_ON_SUBSCRIPTION_BOX FOREIGN KEY (subscription_box_id) REFERENCES public.subscription_box (subscription_box_id);

ALTER TABLE public.subscription_plan
    ADD CONSTRAINT FK_SUBSCRIPTION_PLAN_ON_USER FOREIGN KEY (user_id) REFERENCES public."user" (user_id);

ALTER TABLE public.tokens
    ADD CONSTRAINT FK_TOKENS_ON_USER FOREIGN KEY (user_id) REFERENCES public."user" (user_id);

ALTER TABLE public.user_role
    ADD CONSTRAINT FK_USER_ROLE_ON_ROLE FOREIGN KEY (role_id) REFERENCES public.role (role_id);

ALTER TABLE public.user_role
    ADD CONSTRAINT FK_USER_ROLE_ON_USER FOREIGN KEY (user_id) REFERENCES public."user" (user_id);