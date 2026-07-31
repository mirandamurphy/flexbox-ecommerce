-- Local/branch-only patch to unblock testing on the Integration branch.
-- Adds identity generation to every primary key that currently has none.
-- To be reconciled with the Data Engineer's schema work before merging into main.

ALTER TABLE webhook_event ALTER COLUMN stripe_webhook_event_id ADD GENERATED ALWAYS AS IDENTITY;
ALTER TABLE "order" ALTER COLUMN order_id ADD GENERATED ALWAYS AS IDENTITY;
ALTER TABLE order_item ALTER COLUMN order_item_id ADD GENERATED ALWAYS AS IDENTITY;
ALTER TABLE checkout_session ALTER COLUMN checkout_session_id ADD GENERATED ALWAYS AS IDENTITY;
ALTER TABLE invoice ALTER COLUMN invoice_id ADD GENERATED ALWAYS AS IDENTITY;
ALTER TABLE payment ALTER COLUMN payment_id ADD GENERATED ALWAYS AS IDENTITY;
ALTER TABLE payment_method ALTER COLUMN payment_method_id ADD GENERATED ALWAYS AS IDENTITY;
ALTER TABLE subscription_box ALTER COLUMN subscription_box_id ADD GENERATED ALWAYS AS IDENTITY;
ALTER TABLE product ALTER COLUMN product_id ADD GENERATED ALWAYS AS IDENTITY;
ALTER TABLE subscription_box_price ALTER COLUMN subscription_box_price_id ADD GENERATED ALWAYS AS IDENTITY;
ALTER TABLE category ALTER COLUMN category_id ADD GENERATED ALWAYS AS IDENTITY;
ALTER TABLE "user" ALTER COLUMN user_id ADD GENERATED ALWAYS AS IDENTITY;
ALTER TABLE role ALTER COLUMN role_id ADD GENERATED ALWAYS AS IDENTITY;
ALTER TABLE tokens ALTER COLUMN token_id ADD GENERATED ALWAYS AS IDENTITY;
ALTER TABLE subscription_plan ALTER COLUMN subscription_plan_id ADD GENERATED ALWAYS AS IDENTITY;
ALTER TABLE address ALTER COLUMN address_id ADD GENERATED ALWAYS AS IDENTITY;
ALTER TABLE cart_item ALTER COLUMN cart_item_id ADD GENERATED ALWAYS AS IDENTITY;
ALTER TABLE cart ALTER COLUMN cart_id ADD GENERATED ALWAYS AS IDENTITY;
ALTER TABLE newsletter ALTER COLUMN newsletter_id ADD GENERATED ALWAYS AS IDENTITY;
ALTER TABLE marketing_consent ALTER COLUMN consent_id ADD GENERATED ALWAYS AS IDENTITY;

-- product_inventory.product_id is a shared primary key with product (1:1),
-- intentionally left out: it should stay tied to product's generated id, not
-- generate independently.
