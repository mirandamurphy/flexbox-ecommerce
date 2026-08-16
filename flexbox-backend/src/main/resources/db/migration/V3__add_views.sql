-- Flyway Version Migration V3
-- Target: PostgreSQL
-- Description: Add views that will be used for the admin dashboard/analytics;
--              syncs updated product quantities for sub-boxes so metrics look realistic.

-- 1. Active Subscription Box Products View
CREATE OR REPLACE VIEW view_subscription_box_product_cost AS
SELECT sb.subscription_box_id,
       sb.name                                                   AS box_name,
       p.product_id,
       p.brand,
       p.name                                                    AS product_name,
       c.category_id,
       c.name                                                    AS category_name,
       sbp.quantity,
       (p.cost_per_unit * sbp.quantity::numeric)::numeric(12, 2) AS product_cost
FROM subscription_box sb
         JOIN subscription_box_product sbp ON sb.subscription_box_id = sbp.subscription_box_id
         JOIN product p ON sbp.product_id = p.product_id
         JOIN category c ON p.category_id = c.category_id
WHERE p.is_active = true;


-- 2. Subscription Box Cost View
CREATE OR REPLACE VIEW view_subscription_box_cost AS
SELECT subscription_box_id,
       box_name,
       sum(product_cost)::numeric(12, 2) AS box_cost
FROM view_subscription_box_product_cost
GROUP BY subscription_box_id, box_name;


-- 3. Monthly Sales View
CREATE OR REPLACE VIEW view_monthly_sales AS
WITH sales AS (SELECT o.order_date,
                      sb.subscription_box_id,
                      sb.name                                           AS box_name,
                      oi.quantity,
                      oi.purchase_price_snapshot,
                      vbc.box_cost,
                      oi.purchase_price_snapshot * oi.quantity::numeric AS revenue,
                      vbc.box_cost * oi.quantity::numeric               AS product_cost
               FROM "order" o
                        JOIN order_item oi ON o.order_id = oi.order_id
                        JOIN subscription_box sb ON oi.subscription_box_id = sb.subscription_box_id
                        JOIN view_subscription_box_cost vbc ON sb.subscription_box_id = vbc.subscription_box_id
               WHERE (EXISTS (SELECT 1
                              FROM payment p
                              WHERE p.order_id = o.order_id
                                AND p.status = 'SUCCEEDED'::payment_status)))
SELECT date_trunc('month'::text, order_date) AS month,
       subscription_box_id,
       box_name,
       sum(quantity)                         AS units_sold,
       sum(revenue)                          AS gross_revenue,
       sum(product_cost)                     AS product_cost,
       sum(revenue - product_cost)           AS gross_profit
FROM sales
GROUP BY (date_trunc('month'::text, order_date)), subscription_box_id, box_name;

-- Sync updated quantities
INSERT INTO public.subscription_box_product (subscription_box_id, product_id, quantity)
VALUES (1, 8, 2),
       (1, 9, 2),
       (1, 14, 2),
       (1, 20, 2),
       (1, 21, 3),
       (1, 24, 1),
       (1, 45, 1),
       (1, 54, 1),
       (2, 4, 1),
       (2, 15, 2),
       (2, 18, 1),
       (2, 22, 1),
       (2, 24, 2),
       (2, 27, 1),
       (2, 51, 3),
       (2, 65, 1),
       (2, 68, 2),
       (2, 49, 4),
       (3, 4, 3),
       (3, 5, 3),
       (3, 15, 1),
       (3, 19, 1),
       (3, 65, 1),
       (3, 24, 2),
       (3, 30, 2),
       (3, 36, 2),
       (3, 52, 3),
       (3, 56, 1),
       (3, 60, 1),
       (3, 66, 2),
       (4, 4, 2),
       (4, 5, 2),
       (4, 6, 1),
       (4, 15, 2),
       (4, 18, 2),
       (4, 19, 3),
       (4, 65, 3),
       (4, 69, 2),
       (4, 41, 1),
       (4, 43, 1),
       (4, 34, 1),
       (4, 39, 1),
       (4, 52, 2),
       (4, 56, 2),
       (4, 49, 2),
       (4, 66, 1)
ON CONFLICT (subscription_box_id, product_id)
    DO UPDATE
    SET quantity = EXCLUDED.quantity;