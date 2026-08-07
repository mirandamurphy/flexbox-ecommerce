-- V1__initial_schema.sql
-- Baseline schema for flexbox-ecommerce

-- ENUM TYPES
CREATE TYPE public.address_type AS ENUM (
    'BILLING',
    'SHIPPING'
    );

CREATE TYPE public.cart_status AS ENUM (
    'ACTIVE',
    'ABANDONED'
    );

CREATE TYPE public.checkout_session_mode AS ENUM (
    'PAYMENT',
    'SUBSCRIPTION',
    'SETUP'
    );

CREATE TYPE public.checkout_session_status AS ENUM (
    'COMPLETE',
    'EXPIRED',
    'OPEN'
    );

CREATE TYPE public.invoice_status AS ENUM (
    'DRAFT',
    'OPEN',
    'PAID',
    'UNCOLLECTIBLE',
    'VOID'
    );

CREATE TYPE public.marketing_consent_action AS ENUM (
    'SUBSCRIBE',
    'UNSUBSCRIBE'
    );

CREATE TYPE public.newsletter_type AS ENUM (
    'PROMOTIONAL',
    'ABANDONED_CART',
    'WELCOME',
    'BIRTHDAY'
    );

CREATE TYPE public.order_status AS ENUM (
    'PENDING',
    'COMPLETED',
    'CANCELLED'
    );

CREATE TYPE public.payment_method_type AS ENUM (
    'CARD',
    'PAYPAL',
    'PRE_AUTHORIZED_DEBIT'
    );

CREATE TYPE public.payment_status AS ENUM (
    'PENDING',
    'SUCCEEDED',
    'FAILED'
    );

CREATE TYPE public.provinces AS ENUM (
    'NL', 'PE', 'NS', 'NB', 'QC', 'ON',
    'MB', 'SK', 'AB', 'BC', 'YT', 'NT', 'NU'
    );

CREATE TYPE public.role_name AS ENUM (
    'ROLE_CUSTOMER',
    'ROLE_ADMIN'
    );

CREATE TYPE public.subscription_plan_status AS ENUM (
    'ACTIVE',
    'PAUSED',
    'CANCELLED',
    'PAST_DUE'
    );

CREATE TYPE public.token_type AS ENUM (
    'REFRESH',
    'EMAIL_VERIFICATION',
    'PASSWORD_RESET'
    );


-- FUNCTIONS

CREATE FUNCTION public.set_updated_at() RETURNS trigger
    LANGUAGE plpgsql
AS $$
BEGIN
    NEW.updated_at := now();
    RETURN NEW;
END;
$$;


-- TABLES

CREATE TABLE public."user" (
                               user_id bigint GENERATED ALWAYS AS IDENTITY,
                               stripe_customer_id text,
                               email text NOT NULL,
                               password_hash text NOT NULL,
                               first_name text NOT NULL,
                               last_name text NOT NULL,
                               phone_number character varying(15),
                               is_enabled boolean DEFAULT false NOT NULL,
                               created_at timestamp with time zone DEFAULT now() NOT NULL,
                               updated_at timestamp with time zone NOT NULL,
                               CONSTRAINT user_pkey PRIMARY KEY (user_id),
                               CONSTRAINT user_email_key UNIQUE (email),
                               CONSTRAINT user_stripe_customer_id_key UNIQUE (stripe_customer_id)
);

CREATE TABLE public.role (
                             role_id bigint GENERATED ALWAYS AS IDENTITY,
                             name public.role_name NOT NULL,
                             description text,
                             CONSTRAINT role_pkey PRIMARY KEY (role_id),
                             CONSTRAINT role_name_key UNIQUE (name)
);

CREATE TABLE public.user_role (
                                  user_id bigint NOT NULL,
                                  role_id bigint NOT NULL,
                                  CONSTRAINT user_role_pkey PRIMARY KEY (user_id, role_id),
                                  CONSTRAINT user_role_user_id_fkey FOREIGN KEY (user_id) REFERENCES public."user"(user_id) DEFERRABLE,
                                  CONSTRAINT user_role_role_id_fkey FOREIGN KEY (role_id) REFERENCES public.role(role_id) DEFERRABLE
);

CREATE TABLE public.address (
                                address_id bigint GENERATED ALWAYS AS IDENTITY,
                                user_id bigint NOT NULL,
                                unit_no character varying,
                                civic_no character varying,
                                street text,
                                po_box_number text,
                                city text NOT NULL,
                                province public.provinces NOT NULL,
                                postal_code character varying(7) NOT NULL,
                                country character varying(2) DEFAULT 'CA'::character varying NOT NULL,
                                is_default boolean DEFAULT true NOT NULL,
                                is_active boolean DEFAULT true NOT NULL,
                                created_at timestamp with time zone DEFAULT now() NOT NULL,
                                updated_at timestamp with time zone NOT NULL,
                                type public.address_type NOT NULL,
                                CONSTRAINT address_pkey PRIMARY KEY (address_id),
                                CONSTRAINT address_country_check CHECK ((country)::text = 'CA'::text),
                                CONSTRAINT address_user_id_fkey FOREIGN KEY (user_id) REFERENCES public."user"(user_id) DEFERRABLE
);

CREATE UNIQUE INDEX address_user_id_type_idx
    ON public.address USING btree (user_id, type)
    WHERE (is_default = true);

CREATE TABLE public.category (
                                 category_id bigint GENERATED ALWAYS AS IDENTITY,
                                 name text NOT NULL,
                                 description text,
                                 CONSTRAINT category_pkey PRIMARY KEY (category_id),
                                 CONSTRAINT category_name_key UNIQUE (name)
);

CREATE TABLE public.product (
                                product_id bigint GENERATED ALWAYS AS IDENTITY,
                                category_id bigint NOT NULL,
                                sku text NOT NULL,
                                brand text,
                                name text NOT NULL,
                                description text,
                                cost_per_unit numeric(5,2) NOT NULL,
                                is_active boolean DEFAULT true NOT NULL,
                                created_at timestamp with time zone DEFAULT now() NOT NULL,
                                updated_at timestamp with time zone NOT NULL,
                                CONSTRAINT product_pkey PRIMARY KEY (product_id),
                                CONSTRAINT product_sku_key UNIQUE (sku),
                                CONSTRAINT product_category_id_fkey FOREIGN KEY (category_id) REFERENCES public.category(category_id) DEFERRABLE
);

CREATE INDEX idx_product_category_id ON public.product USING btree (category_id);

CREATE TABLE public.product_inventory (
                                          inventory_id bigint GENERATED ALWAYS AS IDENTITY,
                                          product_id bigint NOT NULL,
                                          in_stock integer NOT NULL,
                                          reserved integer DEFAULT 0 NOT NULL,
                                          created_at timestamp with time zone DEFAULT now() NOT NULL,
                                          updated_at timestamp with time zone NOT NULL,
                                          CONSTRAINT product_inventory_pkey PRIMARY KEY (inventory_id),
                                          CONSTRAINT product_inventory_product_id_key UNIQUE (product_id),
                                          CONSTRAINT product_inventory_in_stock_check CHECK (in_stock >= 0),
                                          CONSTRAINT product_inventory_reserved_check CHECK (reserved >= 0),
                                          CONSTRAINT product_inventory_reserved_in_stock_check CHECK (in_stock >= reserved),
                                          CONSTRAINT product_inventory_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.product(product_id) DEFERRABLE
);

CREATE TABLE public.subscription_box (
                                         subscription_box_id bigint GENERATED ALWAYS AS IDENTITY,
                                         name text NOT NULL,
                                         description text,
                                         image_file text,
                                         available_units integer NOT NULL,
                                         is_active boolean DEFAULT true NOT NULL,
                                         created_at timestamp with time zone DEFAULT now() NOT NULL,
                                         updated_at timestamp with time zone NOT NULL,
                                         CONSTRAINT subscription_box_pkey PRIMARY KEY (subscription_box_id),
                                         CONSTRAINT subscription_box_available_units_check CHECK (available_units >= 0)
);

CREATE TABLE public.subscription_box_price (
                                               subscription_box_price_id bigint GENERATED ALWAYS AS IDENTITY,
                                               subscription_box_id bigint NOT NULL,
                                               amount numeric(5,2) NOT NULL,
                                               currency character varying(3) DEFAULT 'CAD'::character varying NOT NULL,
                                               starts_at timestamp with time zone NOT NULL,
                                               ends_at timestamp with time zone,
                                               stripe_price_id text,
                                               CONSTRAINT subscription_box_price_pkey PRIMARY KEY (subscription_box_price_id),
                                               CONSTRAINT subscription_box_price_stripe_price_id_key UNIQUE (stripe_price_id),
                                               CONSTRAINT subscription_box_price_starts_at_ends_at_check CHECK (ends_at > starts_at),
                                               CONSTRAINT subscription_box_price_subscription_box_id_fkey FOREIGN KEY (subscription_box_id) REFERENCES public.subscription_box(subscription_box_id) DEFERRABLE
);

CREATE TABLE public.subscription_box_product (
                                                 subscription_box_id bigint NOT NULL,
                                                 product_id bigint NOT NULL,
                                                 quantity integer DEFAULT 1 NOT NULL,
                                                 CONSTRAINT subscription_box_product_pkey PRIMARY KEY (subscription_box_id, product_id),
                                                 CONSTRAINT subscription_box_product_quantity_check CHECK (quantity > 0),
                                                 CONSTRAINT subscription_box_product_subscription_box_id_fkey FOREIGN KEY (subscription_box_id) REFERENCES public.subscription_box(subscription_box_id) DEFERRABLE,
                                                 CONSTRAINT subscription_box_product_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.product(product_id) DEFERRABLE
);

CREATE TABLE public.subscription_plan (
                                          subscription_plan_id bigint GENERATED ALWAYS AS IDENTITY,
                                          user_id bigint NOT NULL,
                                          plan_name text NOT NULL,
                                          subscription_box_id bigint NOT NULL,
                                          shipping_address_id bigint NOT NULL,
                                          billing_address_id bigint NOT NULL,
                                          stripe_subscription_id text,
                                          current_plan_start timestamp with time zone NOT NULL,
                                          current_plan_end timestamp with time zone NOT NULL,
                                          cancel_at_period_end boolean DEFAULT false NOT NULL,
                                          cancelled_at timestamp with time zone,
                                          created_at timestamp with time zone DEFAULT now() NOT NULL,
                                          updated_at timestamp with time zone NOT NULL,
                                          status public.subscription_plan_status NOT NULL,
                                          CONSTRAINT subscription_plan_pkey PRIMARY KEY (subscription_plan_id),
                                          CONSTRAINT subscription_plan_stripe_subscription_id_key UNIQUE (stripe_subscription_id),
                                          CONSTRAINT subscription_plan_user_id_fkey FOREIGN KEY (user_id) REFERENCES public."user"(user_id) DEFERRABLE,
                                          CONSTRAINT subscription_plan_subscription_box_id_fkey FOREIGN KEY (subscription_box_id) REFERENCES public.subscription_box(subscription_box_id) DEFERRABLE,
                                          CONSTRAINT subscription_plan_shipping_address_id_fkey FOREIGN KEY (shipping_address_id) REFERENCES public.address(address_id) DEFERRABLE,
                                          CONSTRAINT subscription_plan_billing_address_id_fkey FOREIGN KEY (billing_address_id) REFERENCES public.address(address_id) DEFERRABLE
);

CREATE TABLE public.cart (
                             cart_id bigint GENERATED ALWAYS AS IDENTITY,
                             user_id bigint NOT NULL,
                             created_at timestamp with time zone DEFAULT now() NOT NULL,
                             updated_at timestamp with time zone NOT NULL,
                             status public.cart_status NOT NULL,
                             CONSTRAINT cart_pkey PRIMARY KEY (cart_id),
                             CONSTRAINT cart_user_id_fkey FOREIGN KEY (user_id) REFERENCES public."user"(user_id) DEFERRABLE
);

CREATE TABLE public.cart_item (
                                  cart_item_id bigint GENERATED ALWAYS AS IDENTITY,
                                  cart_id bigint NOT NULL,
                                  subscription_box_id bigint NOT NULL,
                                  quantity integer NOT NULL,
                                  unit_price_snapshot numeric(5,2) NOT NULL,
                                  added_at timestamp with time zone DEFAULT now() NOT NULL,
                                  updated_at timestamp with time zone NOT NULL,
                                  CONSTRAINT cart_item_pkey PRIMARY KEY (cart_item_id),
                                  CONSTRAINT cart_item_quantity_check CHECK (quantity > 0),
                                  CONSTRAINT unit_price_snapshot_check CHECK (unit_price_snapshot > (0)::numeric),
                                  CONSTRAINT cart_item_cart_id_fkey FOREIGN KEY (cart_id) REFERENCES public.cart(cart_id) DEFERRABLE,
                                  CONSTRAINT cart_item_subscription_box_id_fkey FOREIGN KEY (subscription_box_id) REFERENCES public.subscription_box(subscription_box_id) DEFERRABLE
);

CREATE TABLE public."order" (
                                order_id bigint GENERATED ALWAYS AS IDENTITY,
                                user_id bigint NOT NULL,
                                shipping_address_id bigint,
                                billing_address_id bigint,
                                currency character varying(3) DEFAULT 'CAD'::character varying NOT NULL,
                                total_amount numeric(7,2) NOT NULL,
                                order_date timestamp with time zone DEFAULT now() NOT NULL,
                                updated_at timestamp with time zone NOT NULL,
                                status public.order_status DEFAULT 'PENDING'::public.order_status NOT NULL,
                                -- shipping/billing address are nullable: not every checkout flow
                                -- collects an address at order-creation time (cart-based checkout
                                -- does not, currently).
                                CONSTRAINT order_pkey PRIMARY KEY (order_id),
                                CONSTRAINT order_user_id_fkey FOREIGN KEY (user_id) REFERENCES public."user"(user_id) DEFERRABLE,
                                CONSTRAINT order_shipping_address_id_fkey FOREIGN KEY (shipping_address_id) REFERENCES public.address(address_id) DEFERRABLE,
                                CONSTRAINT order_billing_address_id_fkey FOREIGN KEY (billing_address_id) REFERENCES public.address(address_id) DEFERRABLE
);

CREATE TABLE public.order_item (
                                   order_item_id bigint GENERATED ALWAYS AS IDENTITY,
                                   order_id bigint NOT NULL,
                                   subscription_box_id bigint NOT NULL,
                                   subscription_box_name_snapshot text NOT NULL,
                                   quantity integer NOT NULL,
                                   purchase_price_snapshot numeric(5,2) NOT NULL,
                                   CONSTRAINT order_item_pkey PRIMARY KEY (order_item_id),
                                   CONSTRAINT order_item_quantity_check CHECK (quantity > 0),
                                   CONSTRAINT order_item_order_id_fkey FOREIGN KEY (order_id) REFERENCES public."order"(order_id) DEFERRABLE,
                                   CONSTRAINT order_item_subscription_box_id_fkey FOREIGN KEY (subscription_box_id) REFERENCES public.subscription_box(subscription_box_id) DEFERRABLE
);

CREATE TABLE public.payment (
                                payment_id bigint GENERATED ALWAYS AS IDENTITY,
                                order_id bigint NOT NULL,
                                stripe_payment_intent_id text,
                                idempotency_key uuid,
                                amount numeric(7,2) NOT NULL,
                                currency character varying(3) DEFAULT 'CAD'::character varying NOT NULL,
                                paid_at timestamp with time zone,
                                status public.payment_status NOT NULL,
                                created_at timestamp with time zone DEFAULT now() NOT NULL,
                                updated_at timestamp with time zone NOT NULL,
                                CONSTRAINT payment_pkey PRIMARY KEY (payment_id),
                                CONSTRAINT payment_stripe_payment_intent_id_key UNIQUE (stripe_payment_intent_id),
                                CONSTRAINT idempotency_key_key UNIQUE (idempotency_key),
                                CONSTRAINT payment_order_id_fkey FOREIGN KEY (order_id) REFERENCES public."order"(order_id) DEFERRABLE
);

CREATE TABLE public.payment_method (
                                       payment_method_id bigint GENERATED ALWAYS AS IDENTITY,
                                       user_id bigint NOT NULL,
                                       stripe_payment_method_id text NOT NULL,
                                       last_4_digits character varying(4),
                                       expiration_month integer,
                                       expiration_year integer,
                                       is_default boolean DEFAULT true NOT NULL,
                                       created_at timestamp with time zone DEFAULT now() NOT NULL,
                                       updated_at timestamp with time zone NOT NULL,
                                       type public.payment_method_type NOT NULL,
                                       CONSTRAINT payment_method_pkey PRIMARY KEY (payment_method_id),
                                       CONSTRAINT payment_method_stripe_payment_method_id_key UNIQUE (stripe_payment_method_id),
                                       CONSTRAINT expiration_month_check CHECK (expiration_month >= 1 AND expiration_month <= 12),
                                       CONSTRAINT expiration_year_check CHECK ((expiration_year)::numeric >= EXTRACT(year FROM now())),
                                       CONSTRAINT payment_method_user_id_fkey FOREIGN KEY (user_id) REFERENCES public."user"(user_id) DEFERRABLE
);

CREATE TABLE public.checkout_session (
                                         checkout_session_id bigint GENERATED ALWAYS AS IDENTITY,
                                         user_id bigint NOT NULL,
                                         stripe_session_id text,
                                         subscription_plan_id bigint,
                                         payment_id bigint,
                                         amount_subtotal numeric(7,2),
                                         amount_tax numeric(7,2),
                                         amount_total numeric(7,2),
                                         currency character varying(3) DEFAULT 'CAD'::character varying NOT NULL,
                                         success_url text,
                                         cancel_url text,
                                         expires_at timestamp with time zone,
                                         completed_at timestamp with time zone,
                                         created_at timestamp with time zone DEFAULT now() NOT NULL,
                                         updated_at timestamp with time zone NOT NULL,
                                         mode public.checkout_session_mode NOT NULL,
                                         status public.checkout_session_status NOT NULL,
                                         -- subscription_plan_id is nullable: checkout sessions can represent either
                                         -- a single subscription plan purchase or a cart-based multi-item order.
                                         CONSTRAINT checkout_session_pkey PRIMARY KEY (checkout_session_id),
                                         CONSTRAINT checkout_session_stripe_session_id_key UNIQUE (stripe_session_id),
                                         CONSTRAINT amount_subtotal_check CHECK (amount_subtotal > (0)::numeric),
                                         CONSTRAINT amount_total_chck CHECK (amount_total > (0)::numeric),
                                         CONSTRAINT checkout_session_user_id_fkey FOREIGN KEY (user_id) REFERENCES public."user"(user_id) DEFERRABLE,
                                         CONSTRAINT checkout_session_subscription_plan_id_fkey FOREIGN KEY (subscription_plan_id) REFERENCES public.subscription_plan(subscription_plan_id) DEFERRABLE,
                                         CONSTRAINT checkout_session_payment_id_fkey FOREIGN KEY (payment_id) REFERENCES public.payment(payment_id) DEFERRABLE
);

CREATE TABLE public.invoice (
                                invoice_id bigint GENERATED ALWAYS AS IDENTITY,
                                stripe_invoice_id text,
                                subscription_plan_id bigint NOT NULL,
                                amount_due numeric(7,2) NOT NULL,
                                currency character varying(3) DEFAULT 'CAD'::character varying,
                                created_at timestamp with time zone NOT NULL,
                                paid_at timestamp with time zone,
                                status public.invoice_status NOT NULL,
                                updated_at timestamp with time zone NOT NULL,
                                CONSTRAINT invoice_pkey PRIMARY KEY (invoice_id),
                                CONSTRAINT invoice_stripe_invoice_id_key UNIQUE (stripe_invoice_id),
                                CONSTRAINT invoice_subscription_plan_id_fkey FOREIGN KEY (subscription_plan_id) REFERENCES public.subscription_plan(subscription_plan_id) DEFERRABLE
);

CREATE TABLE public.marketing_consent (
                                          consent_id bigint GENERATED ALWAYS AS IDENTITY,
                                          user_id bigint NOT NULL,
                                          created_at timestamp with time zone DEFAULT now() NOT NULL,
                                          updated_at timestamp with time zone NOT NULL,
                                          action public.marketing_consent_action NOT NULL,
                                          CONSTRAINT marketing_consent_pkey PRIMARY KEY (consent_id),
                                          CONSTRAINT marketing_consent_user_id_fkey FOREIGN KEY (user_id) REFERENCES public."user"(user_id) DEFERRABLE
);

CREATE TABLE public.newsletter (
                                   newsletter_id bigint GENERATED ALWAYS AS IDENTITY,
                                   name text,
                                   subject text,
                                   html_file text,
                                   type public.newsletter_type NOT NULL,
                                   created_at timestamp with time zone DEFAULT now() NOT NULL,
                                   updated_at timestamp with time zone NOT NULL,
                                   CONSTRAINT newsletter_pkey PRIMARY KEY (newsletter_id)
);

CREATE TABLE public.newsletter_subscribers (
                                               newsletter_id bigint NOT NULL,
                                               user_id bigint NOT NULL,
                                               sent_at timestamp with time zone,
                                               CONSTRAINT newsletter_subscribers_pkey PRIMARY KEY (newsletter_id, user_id),
                                               CONSTRAINT newsletter_subscribers_newsletter_id_fkey FOREIGN KEY (newsletter_id) REFERENCES public.newsletter(newsletter_id) DEFERRABLE,
                                               CONSTRAINT newsletter_subscribers_user_id_fkey FOREIGN KEY (user_id) REFERENCES public."user"(user_id) DEFERRABLE
);

CREATE TABLE public.tokens (
                               token_id bigint GENERATED ALWAYS AS IDENTITY,
                               user_id bigint NOT NULL,
                               token_value text NOT NULL,
                               is_revoked boolean DEFAULT false NOT NULL,
                               created_at timestamp with time zone DEFAULT now() NOT NULL,
                               expired_at timestamp with time zone,
                               type public.token_type NOT NULL,
                               CONSTRAINT tokens_pkey PRIMARY KEY (token_id),
                               CONSTRAINT tokens_token_value_key UNIQUE (token_value),
                               CONSTRAINT tokens_user_id_fkey FOREIGN KEY (user_id) REFERENCES public."user"(user_id) DEFERRABLE
);

CREATE TABLE public.webhook_event (
                                      stripe_webhook_event_id bigint GENERATED ALWAYS AS IDENTITY,
                                      stripe_event_id text NOT NULL,
                                      event_type text NOT NULL,
                                      payload jsonb NOT NULL,
                                      is_processed boolean DEFAULT false NOT NULL,
                                      received_at timestamp with time zone DEFAULT now() NOT NULL,
                                      processed_at timestamp with time zone,
                                      CONSTRAINT webhook_event_pkey PRIMARY KEY (stripe_webhook_event_id),
                                      CONSTRAINT webhook_event_stripe_event_id_key UNIQUE (stripe_event_id)
);


-- TRIGGERS (auto-update updated_at timestamp on every insert/update)

CREATE TRIGGER trg_user_updated_at BEFORE INSERT OR UPDATE ON public."user"
    FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();

CREATE TRIGGER trg_address_updated_at BEFORE INSERT OR UPDATE ON public.address
    FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();

CREATE TRIGGER trg_product_updated_at BEFORE INSERT OR UPDATE ON public.product
    FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();

CREATE TRIGGER trg_product_inventory_updated_at BEFORE INSERT OR UPDATE ON public.product_inventory
    FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();

CREATE TRIGGER trg_subscription_box_updated_at BEFORE INSERT OR UPDATE ON public.subscription_box
    FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();

CREATE TRIGGER trg_subscription_plan_updated_at BEFORE INSERT OR UPDATE ON public.subscription_plan
    FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();

CREATE TRIGGER trg_cart_updated_at BEFORE INSERT OR UPDATE ON public.cart
    FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();

CREATE TRIGGER trg_cart_item_updated_at BEFORE INSERT OR UPDATE ON public.cart_item
    FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();

CREATE TRIGGER trg_order_updated_at BEFORE INSERT OR UPDATE ON public."order"
    FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();

CREATE TRIGGER trg_payment_updated_at BEFORE INSERT OR UPDATE ON public.payment
    FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();

CREATE TRIGGER trg_payment_method_updated_at BEFORE INSERT OR UPDATE ON public.payment_method
    FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();

CREATE TRIGGER trg_checkout_session_updated_at BEFORE INSERT OR UPDATE ON public.checkout_session
    FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();

CREATE TRIGGER trg_invoice_updated_at BEFORE INSERT OR UPDATE ON public.invoice
    FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();

CREATE TRIGGER trg_marketing_consent_updated_at BEFORE INSERT OR UPDATE ON public.marketing_consent
    FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();

CREATE TRIGGER trg_newsletter_updated_at BEFORE INSERT OR UPDATE ON public.newsletter
    FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();