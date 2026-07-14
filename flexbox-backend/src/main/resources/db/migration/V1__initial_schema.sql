CREATE TYPE "role_name" AS ENUM (
    'ROLE_CUSTOMER',
    'ROLE_ADMIN'
    );

CREATE TYPE "token_type" AS ENUM (
    'refresh_token',
    'email_verification',
    'password_reset'
    );

CREATE TYPE "address_type" AS ENUM (
    'shipping',
    'billing'
    );

CREATE TYPE "subscription_plan_status" AS ENUM (
    'active',
    'paused',
    'cancelled',
    'past_due'
    );

CREATE TYPE "payment_status" AS ENUM (
    'pending',
    'succeeded',
    'failed',
    'refunded'
    );

CREATE TYPE "order_status" AS ENUM (
    'pending',
    'completed',
    'cancelled'
    );

CREATE TYPE "cart_status" AS ENUM (
    'active',
    'abandoned'
    );

CREATE TYPE "provinces" AS ENUM (
    'NL',
    'PE',
    'NS',
    'NB',
    'QC',
    'ON',
    'MB',
    'SK',
    'AB',
    'BC',
    'YT',
    'NT',
    'NU'
    );

CREATE TYPE "marketing_consent_action" AS ENUM (
    'subscribe',
    'unsubscribe'
    );

CREATE TYPE "checkout_session_mode" AS ENUM (
    'payment',
    'subscription',
    'setup'
    );

CREATE TYPE "checkout_session_status" AS ENUM (
    'complete',
    'expired',
    'open'
    );

CREATE TABLE "user" (
                        "user_id" bigint PRIMARY KEY,
                        "stripe_customer_id" text UNIQUE,
                        "email" text UNIQUE,
                        "password_hash" text,
                        "first_name" text,
                        "last_name" text,
                        "phone_number" varchar(15),
                        "is_enabled" boolean DEFAULT false,
                        "created_at" timestamptz,
                        "updated_at" timestamptz
);

CREATE TABLE "tokens" (
                          "token_id" bigint PRIMARY KEY,
                          "user_id" bigint,
                          "token_value" text UNIQUE NOT NULL,
                          "type" token_type NOT NULL,
                          "is_revoked" boolean NOT NULL DEFAULT false,
                          "created_at" timestamptz,
                          "expired_at" timestamptz
);

CREATE TABLE "role" (
                        "role_id" bigint PRIMARY KEY,
                        "name" role_name,
                        "description" text
);

CREATE TABLE "user_role" (
                             "user_id" bigint UNIQUE,
                             "role_id" bigint,
                             PRIMARY KEY ("user_id", "role_id")
);

CREATE TABLE "address" (
                           "address_id" bigint PRIMARY KEY,
                           "user_id" bigint,
                           "unit_no" varchar,
                           "civic_no" varchar,
                           "street" text,
                           "po_box_number" text,
                           "city" text,
                           "province" provinces,
                           "postal_code" varchar(7),
                           "country" varchar(2) CHECK (country = 'CA') DEFAULT 'CA',
                           "type" address_type NOT NULL,
                           "is_default" boolean NOT NULL DEFAULT false,
                           "is_active" boolean,
                           "created_at" timestamptz,
                           "updated_at" timestamptz
);

CREATE TABLE "category" (
                            "category_id" bigint PRIMARY KEY,
                            "name" text UNIQUE,
                            "description" text
);

CREATE TABLE "product" (
                           "product_id" bigint PRIMARY KEY,
                           "category_id" bigint,
                           "sku" text UNIQUE,
                           "brand" text,
                           "name" text,
                           "description" text,
                           "cost_per_unit" numeric(5,2),
                           "is_active" boolean,
                           "created_at" timestamptz,
                           "updated_at" timestamptz
);

CREATE TABLE "invoice" (
                           "invoice_id" bigint PRIMARY KEY,
                           "stripe_invoice_id" text UNIQUE,
                           "subscription_plan_id" bigint,
                           "amount_due" numeric(7,2),
                           "currency" varchar(3) DEFAULT 'CAD',
                           "status" text,
                           "created_at" timestamptz,
                           "paid_at" timestamptz
);

CREATE TABLE "product_inventory" (
                                     "product_id" bigint PRIMARY KEY,
                                     "in_stock" integer,
                                     "reserved" integer,
                                     "updated_at" timestamptz
);

CREATE TABLE "subscription_box" (
                                    "subscription_box_id" bigint PRIMARY KEY,
                                    "name" text,
                                    "description" text,
                                    "image_file" text,
                                    "available_units" int,
                                    "is_active" bool,
                                    "created_at" timestamptz,
                                    "updated_at" timestamptz
);

CREATE TABLE "subscription_box_product" (
                                            "subscription_box_id" bigint,
                                            "product_id" bigint,
                                            "quantity" integer NOT NULL CHECK (quantity > 0) DEFAULT 1,
                                            PRIMARY KEY ("subscription_box_id", "product_id")
);

CREATE TABLE "subscription_box_price" (
                                          "subscription_box_price_id" bigint PRIMARY KEY,
                                          "subscription_box_id" bigint,
                                          "amount" numeric(5,2),
                                          "currency" varchar(3) DEFAULT 'CAD',
                                          "starts_at" timestamptz,
                                          "ends_at" timestamptz,
                                          "stripe_price_id" text UNIQUE
);

CREATE TABLE "subscription_plan" (
                                     "subscription_plan_id" bigint PRIMARY KEY,
                                     "user_id" bigint,
                                     "plan_name" text,
                                     "subscription_box_id" bigint,
                                     "shipping_address_id" bigint,
                                     "billing_address_id" bigint,
                                     "stripe_subscription_id" text UNIQUE,
                                     "current_plan_start" timestamptz,
                                     "current_plan_end" timestamptz,
                                     "cancel_at_period_end" bool,
                                     "cancelled_at" timestamptz,
                                     "created_at" timestamptz,
                                     "updated_at" timestamptz,
                                     "status" subscription_plan_status
);

CREATE TABLE "payment_method" (
                                  "payment_method_id" bigint PRIMARY KEY,
                                  "user_id" bigint,
                                  "stripe_payment_method_id" text UNIQUE,
                                  "type" text DEFAULT 'card',
                                  "last_4_digits" varchar(4),
                                  "expiration_month" integer,
                                  "expiration_year" integer,
                                  "is_default" boolean,
                                  "created_at" timestamptz,
                                  "updated_at" timestamptz
);

CREATE TABLE "payment" (
                           "payment_id" bigint PRIMARY KEY,
                           "order_id" bigint,
                           "stripe_payment_intent_id" text UNIQUE,
                           "idempotency_key" uuid,
                           "amount" numeric(7,2),
                           "currency" varchar(3) DEFAULT 'CAD',
                           "paid_at" timestamptz,
                           "status" payment_status
);

CREATE TABLE "order" (
                         "order_id" bigint PRIMARY KEY,
                         "user_id" bigint,
                         "shipping_address_id" bigint,
                         "billing_address_id" bigint,
                         "currency" varchar(3) DEFAULT 'CAD',
                         "total_amount" numeric(7,2),
                         "order_status" order_status,
                         "order_date" timestamptz,
                         "updated_at" timestamptz
);

CREATE TABLE "order_item" (
                              "order_item_id" bigint PRIMARY KEY,
                              "order_id" bigint,
                              "subscription_box_id" bigint,
                              "subscription_box_name_snapshot" text,
                              "quantity" integer,
                              "purchase_price_snapshot" numeric(5,2)
);

CREATE TABLE "cart" (
                        "cart_id" bigint PRIMARY KEY,
                        "user_id" bigint,
                        "status" cart_status,
                        "created_at" timestamptz,
                        "updated_at" timestamptz
);

CREATE TABLE "cart_item" (
                             "cart_item_id" bigint PRIMARY KEY,
                             "cart_id" bigint,
                             "subscription_box_id" bigint,
                             "quantity" integer,
                             "unit_price_snapshot" numeric(5,2),
                             "added_at" timestamptz,
                             "updated_at" timestamptz
);

CREATE TABLE "marketing_consent" (
                                     "consent_id" bigint PRIMARY KEY,
                                     "user_id" bigint,
                                     "action" marketing_consent_action,
                                     "created_at" timestamptz,
                                     "updated_at" timestamptz
);

CREATE TABLE "newsletter" (
                              "newsletter_id" bigint PRIMARY KEY,
                              "name" text,
                              "subject" text,
                              "html_file" text,
                              "type" text,
                              "created_date" timestamptz
);

CREATE TABLE "newsletter_subscribers" (
                                          "newsletter_id" bigint,
                                          "user_id" bigint,
                                          "sent_at" timestamptz,
                                          PRIMARY KEY ("newsletter_id", "user_id")
);

CREATE TABLE "webhook_event" (
                                 "stripe_webhook_event_id" bigint PRIMARY KEY,
                                 "stripe_event_id" text UNIQUE,
                                 "event_type" text,
                                 "payload" jsonb,
                                 "is_processed" boolean,
                                 "received_at" timestamptz,
                                 "processed_at" timestamptz
);

CREATE TABLE "checkout_session" (
                                    "checkout_session_id" bigint PRIMARY KEY,
                                    "user_id" bigint,
                                    "stripe_session_id" text UNIQUE,
                                    "subscription_plan_id" bigint,
                                    "payment_id" bigint,
                                    "mode" checkout_session_mode,
                                    "status" checkout_session_status,
                                    "amount_subtotal" numeric(7,2),
                                    "amount_tax" numeric(7,2),
                                    "amount_total" numeric(7,2),
                                    "currency" varchar(3),
                                    "success_url" text,
                                    "cancel_url" text,
                                    "expires_at" timestamptz,
                                    "completed_at" timestamptz,
                                    "created_at" timestamptz,
                                    "updated_at" timestamptz
);

ALTER TABLE "tokens" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("user_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "user_role" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("user_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "user_role" ADD FOREIGN KEY ("role_id") REFERENCES "role" ("role_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "address" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("user_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "product" ADD FOREIGN KEY ("category_id") REFERENCES "category" ("category_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "invoice" ADD FOREIGN KEY ("subscription_plan_id") REFERENCES "subscription_plan" ("subscription_plan_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "product_inventory" ADD FOREIGN KEY ("product_id") REFERENCES "product" ("product_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "subscription_box_product" ADD FOREIGN KEY ("subscription_box_id") REFERENCES "subscription_box" ("subscription_box_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "subscription_box_product" ADD FOREIGN KEY ("product_id") REFERENCES "product" ("product_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "subscription_box_price" ADD FOREIGN KEY ("subscription_box_id") REFERENCES "subscription_box" ("subscription_box_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "subscription_plan" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("user_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "subscription_plan" ADD FOREIGN KEY ("subscription_box_id") REFERENCES "subscription_box" ("subscription_box_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "subscription_plan" ADD FOREIGN KEY ("shipping_address_id") REFERENCES "address" ("address_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "subscription_plan" ADD FOREIGN KEY ("billing_address_id") REFERENCES "address" ("address_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "payment_method" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("user_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "payment" ADD FOREIGN KEY ("order_id") REFERENCES "order" ("order_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "order" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("user_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "order" ADD FOREIGN KEY ("shipping_address_id") REFERENCES "address" ("address_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "order" ADD FOREIGN KEY ("billing_address_id") REFERENCES "address" ("address_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "order_item" ADD FOREIGN KEY ("order_id") REFERENCES "order" ("order_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "order_item" ADD FOREIGN KEY ("subscription_box_id") REFERENCES "subscription_box" ("subscription_box_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "cart" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("user_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "cart_item" ADD FOREIGN KEY ("cart_id") REFERENCES "cart" ("cart_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "cart_item" ADD FOREIGN KEY ("subscription_box_id") REFERENCES "subscription_box" ("subscription_box_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "marketing_consent" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("user_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "newsletter_subscribers" ADD FOREIGN KEY ("newsletter_id") REFERENCES "newsletter" ("newsletter_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "newsletter_subscribers" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("user_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "checkout_session" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("user_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "checkout_session" ADD FOREIGN KEY ("subscription_plan_id") REFERENCES "subscription_plan" ("subscription_plan_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "checkout_session" ADD FOREIGN KEY ("payment_id") REFERENCES "payment" ("payment_id") DEFERRABLE INITIALLY IMMEDIATE;
CREATE TYPE "role_name" AS ENUM (
    'ROLE_CUSTOMER',
    'ROLE_ADMIN'
    );

CREATE TYPE "token_type" AS ENUM (
    'refresh_token',
    'email_verification',
    'password_reset'
    );

CREATE TYPE "address_type" AS ENUM (
    'shipping',
    'billing'
    );

CREATE TYPE "subscription_plan_status" AS ENUM (
    'active',
    'paused',
    'cancelled',
    'past_due'
    );

CREATE TYPE "payment_status" AS ENUM (
    'pending',
    'succeeded',
    'failed',
    'refunded'
    );

CREATE TYPE "order_status" AS ENUM (
    'pending',
    'completed',
    'cancelled'
    );

CREATE TYPE "cart_status" AS ENUM (
    'active',
    'abandoned'
    );

CREATE TYPE "provinces" AS ENUM (
    'NL',
    'PE',
    'NS',
    'NB',
    'QC',
    'ON',
    'MB',
    'SK',
    'AB',
    'BC',
    'YT',
    'NT',
    'NU'
    );

CREATE TYPE "marketing_consent_action" AS ENUM (
    'subscribe',
    'unsubscribe'
    );

CREATE TYPE "checkout_session_mode" AS ENUM (
    'payment',
    'subscription',
    'setup'
    );

CREATE TYPE "checkout_session_status" AS ENUM (
    'complete',
    'expired',
    'open'
    );

CREATE TABLE "user" (
                        "user_id" bigint PRIMARY KEY,
                        "stripe_customer_id" text UNIQUE,
                        "email" text UNIQUE,
                        "password_hash" text,
                        "first_name" text,
                        "last_name" text,
                        "phone_number" varchar(15),
                        "is_enabled" boolean DEFAULT false,
                        "created_at" timestamptz,
                        "updated_at" timestamptz
);

CREATE TABLE "tokens" (
                          "token_id" bigint PRIMARY KEY,
                          "user_id" bigint,
                          "token_value" text UNIQUE NOT NULL,
                          "type" token_type NOT NULL,
                          "is_revoked" boolean NOT NULL DEFAULT false,
                          "created_at" timestamptz,
                          "expired_at" timestamptz
);

CREATE TABLE "role" (
                        "role_id" bigint PRIMARY KEY,
                        "name" role_name,
                        "description" text
);

CREATE TABLE "user_role" (
                             "user_id" bigint UNIQUE,
                             "role_id" bigint,
                             PRIMARY KEY ("user_id", "role_id")
);

CREATE TABLE "address" (
                           "address_id" bigint PRIMARY KEY,
                           "user_id" bigint,
                           "unit_no" varchar,
                           "civic_no" varchar,
                           "street" text,
                           "po_box_number" text,
                           "city" text,
                           "province" provinces,
                           "postal_code" varchar(7),
                           "country" varchar(2) CHECK (country = 'CA') DEFAULT 'CA',
                           "type" address_type NOT NULL,
                           "is_default" boolean NOT NULL DEFAULT false,
                           "is_active" boolean,
                           "created_at" timestamptz,
                           "updated_at" timestamptz
);

CREATE TABLE "category" (
                            "category_id" bigint PRIMARY KEY,
                            "name" text UNIQUE,
                            "description" text
);

CREATE TABLE "product" (
                           "product_id" bigint PRIMARY KEY,
                           "category_id" bigint,
                           "sku" text UNIQUE,
                           "brand" text,
                           "name" text,
                           "description" text,
                           "cost_per_unit" numeric(5,2),
                           "is_active" boolean,
                           "created_at" timestamptz,
                           "updated_at" timestamptz
);

CREATE TABLE "invoice" (
                           "invoice_id" bigint PRIMARY KEY,
                           "stripe_invoice_id" text UNIQUE,
                           "subscription_plan_id" bigint,
                           "amount_due" numeric(7,2),
                           "currency" varchar(3) DEFAULT 'CAD',
                           "status" text,
                           "created_at" timestamptz,
                           "paid_at" timestamptz
);

CREATE TABLE "product_inventory" (
                                     "product_id" bigint PRIMARY KEY,
                                     "in_stock" integer,
                                     "reserved" integer,
                                     "updated_at" timestamptz
);

CREATE TABLE "subscription_box" (
                                    "subscription_box_id" bigint PRIMARY KEY,
                                    "name" text,
                                    "description" text,
                                    "image_file" text,
                                    "available_units" int,
                                    "is_active" bool,
                                    "created_at" timestamptz,
                                    "updated_at" timestamptz
);

CREATE TABLE "subscription_box_product" (
                                            "subscription_box_id" bigint,
                                            "product_id" bigint,
                                            "quantity" integer NOT NULL CHECK (quantity > 0) DEFAULT 1,
                                            PRIMARY KEY ("subscription_box_id", "product_id")
);

CREATE TABLE "subscription_box_price" (
                                          "subscription_box_price_id" bigint PRIMARY KEY,
                                          "subscription_box_id" bigint,
                                          "amount" numeric(5,2),
                                          "currency" varchar(3) DEFAULT 'CAD',
                                          "starts_at" timestamptz,
                                          "ends_at" timestamptz,
                                          "stripe_price_id" text UNIQUE
);

CREATE TABLE "subscription_plan" (
                                     "subscription_plan_id" bigint PRIMARY KEY,
                                     "user_id" bigint,
                                     "plan_name" text,
                                     "subscription_box_id" bigint,
                                     "shipping_address_id" bigint,
                                     "billing_address_id" bigint,
                                     "stripe_subscription_id" text UNIQUE,
                                     "current_plan_start" timestamptz,
                                     "current_plan_end" timestamptz,
                                     "cancel_at_period_end" bool,
                                     "cancelled_at" timestamptz,
                                     "created_at" timestamptz,
                                     "updated_at" timestamptz,
                                     "status" subscription_plan_status
);

CREATE TABLE "payment_method" (
                                  "payment_method_id" bigint PRIMARY KEY,
                                  "user_id" bigint,
                                  "stripe_payment_method_id" text UNIQUE,
                                  "type" text DEFAULT 'card',
                                  "last_4_digits" varchar(4),
                                  "expiration_month" integer,
                                  "expiration_year" integer,
                                  "is_default" boolean,
                                  "created_at" timestamptz,
                                  "updated_at" timestamptz
);

CREATE TABLE "payment" (
                           "payment_id" bigint PRIMARY KEY,
                           "order_id" bigint,
                           "stripe_payment_intent_id" text UNIQUE,
                           "idempotency_key" uuid,
                           "amount" numeric(7,2),
                           "currency" varchar(3) DEFAULT 'CAD',
                           "paid_at" timestamptz,
                           "status" payment_status
);

CREATE TABLE "order" (
                         "order_id" bigint PRIMARY KEY,
                         "user_id" bigint,
                         "shipping_address_id" bigint,
                         "billing_address_id" bigint,
                         "currency" varchar(3) DEFAULT 'CAD',
                         "total_amount" numeric(7,2),
                         "order_status" order_status,
                         "order_date" timestamptz,
                         "updated_at" timestamptz
);

CREATE TABLE "order_item" (
                              "order_item_id" bigint PRIMARY KEY,
                              "order_id" bigint,
                              "subscription_box_id" bigint,
                              "subscription_box_name_snapshot" text,
                              "quantity" integer,
                              "purchase_price_snapshot" numeric(5,2)
);

CREATE TABLE "cart" (
                        "cart_id" bigint PRIMARY KEY,
                        "user_id" bigint,
                        "status" cart_status,
                        "created_at" timestamptz,
                        "updated_at" timestamptz
);

CREATE TABLE "cart_item" (
                             "cart_item_id" bigint PRIMARY KEY,
                             "cart_id" bigint,
                             "subscription_box_id" bigint,
                             "quantity" integer,
                             "unit_price_snapshot" numeric(5,2),
                             "added_at" timestamptz,
                             "updated_at" timestamptz
);

CREATE TABLE "marketing_consent" (
                                     "consent_id" bigint PRIMARY KEY,
                                     "user_id" bigint,
                                     "action" marketing_consent_action,
                                     "created_at" timestamptz,
                                     "updated_at" timestamptz
);

CREATE TABLE "newsletter" (
                              "newsletter_id" bigint PRIMARY KEY,
                              "name" text,
                              "subject" text,
                              "html_file" text,
                              "type" text,
                              "created_date" timestamptz
);

CREATE TABLE "newsletter_subscribers" (
                                          "newsletter_id" bigint,
                                          "user_id" bigint,
                                          "sent_at" timestamptz,
                                          PRIMARY KEY ("newsletter_id", "user_id")
);

CREATE TABLE "webhook_event" (
                                 "stripe_webhook_event_id" bigint PRIMARY KEY,
                                 "stripe_event_id" text UNIQUE,
                                 "event_type" text,
                                 "payload" jsonb,
                                 "is_processed" boolean,
                                 "received_at" timestamptz,
                                 "processed_at" timestamptz
);

CREATE TABLE "checkout_session" (
                                    "checkout_session_id" bigint PRIMARY KEY,
                                    "user_id" bigint,
                                    "stripe_session_id" text UNIQUE,
                                    "subscription_plan_id" bigint,
                                    "payment_id" bigint,
                                    "mode" checkout_session_mode,
                                    "status" checkout_session_status,
                                    "amount_subtotal" numeric(7,2),
                                    "amount_tax" numeric(7,2),
                                    "amount_total" numeric(7,2),
                                    "currency" varchar(3),
                                    "success_url" text,
                                    "cancel_url" text,
                                    "expires_at" timestamptz,
                                    "completed_at" timestamptz,
                                    "created_at" timestamptz,
                                    "updated_at" timestamptz
);


ALTER TABLE "tokens" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("user_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "user_role" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("user_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "user_role" ADD FOREIGN KEY ("role_id") REFERENCES "role" ("role_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "address" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("user_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "product" ADD FOREIGN KEY ("category_id") REFERENCES "category" ("category_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "invoice" ADD FOREIGN KEY ("subscription_plan_id") REFERENCES "subscription_plan" ("subscription_plan_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "product_inventory" ADD FOREIGN KEY ("product_id") REFERENCES "product" ("product_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "subscription_box_product" ADD FOREIGN KEY ("subscription_box_id") REFERENCES "subscription_box" ("subscription_box_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "subscription_box_product" ADD FOREIGN KEY ("product_id") REFERENCES "product" ("product_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "subscription_box_price" ADD FOREIGN KEY ("subscription_box_id") REFERENCES "subscription_box" ("subscription_box_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "subscription_plan" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("user_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "subscription_plan" ADD FOREIGN KEY ("subscription_box_id") REFERENCES "subscription_box" ("subscription_box_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "subscription_plan" ADD FOREIGN KEY ("shipping_address_id") REFERENCES "address" ("address_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "subscription_plan" ADD FOREIGN KEY ("billing_address_id") REFERENCES "address" ("address_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "payment_method" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("user_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "payment" ADD FOREIGN KEY ("order_id") REFERENCES "order" ("order_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "order" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("user_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "order" ADD FOREIGN KEY ("shipping_address_id") REFERENCES "address" ("address_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "order" ADD FOREIGN KEY ("billing_address_id") REFERENCES "address" ("address_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "order_item" ADD FOREIGN KEY ("order_id") REFERENCES "order" ("order_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "order_item" ADD FOREIGN KEY ("subscription_box_id") REFERENCES "subscription_box" ("subscription_box_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "cart" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("user_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "cart_item" ADD FOREIGN KEY ("cart_id") REFERENCES "cart" ("cart_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "cart_item" ADD FOREIGN KEY ("subscription_box_id") REFERENCES "subscription_box" ("subscription_box_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "marketing_consent" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("user_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "newsletter_subscribers" ADD FOREIGN KEY ("newsletter_id") REFERENCES "newsletter" ("newsletter_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "newsletter_subscribers" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("user_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "checkout_session" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("user_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "checkout_session" ADD FOREIGN KEY ("subscription_plan_id") REFERENCES "subscription_plan" ("subscription_plan_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "checkout_session" ADD FOREIGN KEY ("payment_id") REFERENCES "payment" ("payment_id") DEFERRABLE INITIALLY IMMEDIATE;
