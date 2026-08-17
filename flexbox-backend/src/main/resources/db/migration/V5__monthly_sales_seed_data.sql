-- Flyway Version Migration V5
-- Target: PostgreSQL
-- Description: Seed data to populate monthly sales view.

-- 1. Add 'order' data
INSERT INTO public."order"(order_id,
                           user_id,
                           shipping_address_id,
                           billing_address_id,
                           currency,
                           total_amount,
                           order_date,
                           updated_at,
                           status)
OVERRIDING SYSTEM VALUE
VALUES (2, 2, 234, 234, 'CAD', 59.98, '2026-08-10 18:07:40.823000 +00:00', '2026-08-17 21:13:15.677545 +00:00', 'COMPLETED'),
       (4, 8, 240, 240, 'CAD', 199.98, '2026-08-17 18:09:57.172000 +00:00', '2026-08-17 21:13:32.250993 +00:00', 'COMPLETED'),
       (3, 20, 252, 252, 'CAD', 29.99, '2026-08-11 18:09:16.639000 +00:00', '2026-08-17 21:13:41.642350 +00:00', 'COMPLETED')
ON CONFLICT (order_id) DO UPDATE SET
                                     user_id = excluded.user_id,
                                     shipping_address_id = excluded.shipping_address_id,
                                     billing_address_id = excluded.billing_address_id,
                                     currency = excluded.currency,
                                     total_amount = excluded.total_amount,
                                     order_date = excluded.order_date,
                                     updated_at = excluded.updated_at;


-- 2. Add 'order_item' data
insert into public.order_item (order_item_id, order_id, subscription_box_id, subscription_box_name_snapshot, quantity, purchase_price_snapshot)
OVERRIDING SYSTEM VALUE
values  (2, 2, 1, 'Essential Fitness Box', 2, 29.99),
        (3, 4, 4, 'Elite Athlete Box', 2, 99.99),
        (4, 3, 1, 'Essential Fitness Box', 1, 29.99)
ON CONFLICT (order_item_id) DO UPDATE SET
                                          order_id = excluded.order_id,
                                          subscription_box_id = excluded.subscription_box_id,
                                          subscription_box_name_snapshot = excluded.subscription_box_name_snapshot,
                                          quantity = excluded.quantity,
                                          purchase_price_snapshot = excluded.purchase_price_snapshot;

-- 3. Add 'payment' data
insert into public.payment (payment_id, order_id, stripe_payment_intent_id, idempotency_key, amount, currency, paid_at, status, created_at, updated_at)
OVERRIDING SYSTEM VALUE
values  (1, 2, '"stripe-123-123"', 'e5c6db8b-0907-4f9b-a2cb-a9ded003dd08', 59.98, 'CAD', '2026-08-10 18:17:14.943000 +00:00', 'SUCCEEDED', '2026-08-17 21:17:42.486942 +00:00', '2026-08-17 21:17:42.486942 +00:00'),
        (2, 4, '"stripe-123-125"', 'da579c91-4967-464b-82f3-7eb7a83c2aab', 199.98, 'CAD', '2026-08-17 18:18:21.009000 +00:00', 'SUCCEEDED', '2026-08-17 21:18:32.668986 +00:00', '2026-08-17 21:18:32.668986 +00:00'),
        (3, 3, '"stripe-123-124"', '10ab5419-a4e9-4e71-99d8-fa055c035aaf', 29.99, 'CAD', '2026-08-11 18:19:11.295000 +00:00', 'SUCCEEDED', '2026-08-17 21:19:18.765280 +00:00', '2026-08-17 21:19:18.765280 +00:00')
ON CONFLICT (payment_id) DO UPDATE SET
                                       order_id = excluded.order_id,
                                       stripe_payment_intent_id = excluded.stripe_payment_intent_id,
                                       idempotency_key = excluded.idempotency_key,
                                       amount = excluded.amount,
                                       currency = excluded.currency,
                                       paid_at = excluded.paid_at,
                                       status = excluded.status,
                                       created_at = excluded.created_at,
                                       updated_at = excluded.updated_at;

-- 4. Re-synchronize Primary Key Auto-Increment Sequences
SELECT pg_catalog.setval('public.order_order_id_seq', (SELECT MAX("order".order_id) FROM public."order"));
SELECT pg_catalog.setval('public.order_item_order_item_id_seq',
                         (SELECT MAX(order_item_id) FROM public.order_item));
SELECT pg_catalog.setval('public.payment_payment_id_seq', (SELECT MAX(payment.payment_id) FROM public.payment));