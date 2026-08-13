-- Flyway Version Migration V2
-- Target: PostgreSQL
-- Description: Adds seed data for the product catalog

-- 1. Category Seed Data
INSERT INTO public.category (category_id, name, description)
    OVERRIDING SYSTEM VALUE
VALUES (1, 'snacks', 'Convenient snacks that fit active lifestyles.'),
       (2, 'hydration', 'Products that help maintain hydration during exercise.'),
       (3, 'personal care', 'Gym-friendly hygiene and self-care products.'),
       (4, 'apparel', 'Useful clothing items for an active lifestyle.')
ON CONFLICT (category_id) DO NOTHING;

-- Synchronize sequence state
SELECT setval(
               pg_get_serial_sequence('public.category', 'category_id'),
               COALESCE(MAX(category_id), 1)
       )
FROM public.category;

-- 2. Product Seed Data
INSERT INTO public.product (product_id, category_id, sku, brand, name, description, cost_per_unit, is_active,
                            created_at, updated_at)
    OVERRIDING SYSTEM VALUE
VALUES (4, 1, 'FF-PB-001', 'FitFuel', 'Chocolate Whey Protein Packet', 'Single-serve whey protein powder, 25g protein',
        0.80, true, '2026-08-02 19:55:40.306953-03', '2026-08-02 19:55:40.306953-03'),
       (5, 1, 'FF-PB-002', 'FitFuel', 'Vanilla Whey Protein Packet', 'Single-serve whey protein powder, 25g protein',
        0.80, true, '2026-08-02 19:55:40.306953-03', '2026-08-02 19:55:40.306953-03'),
       (6, 1, 'FF-PB-003', 'FitFuel', 'Strawberry Whey Protein Packet', 'Single-serve whey protein powder, 25g protein',
        0.80, true, '2026-08-02 19:55:40.306953-03', '2026-08-02 19:55:40.306953-03'),
       (7, 1, 'MM-RS-001', 'MuscleMaker', 'Recovery Drink Mix',
        'Post-workout recovery drink with protein and electrolytes', 1.10, true, '2026-08-02 19:55:40.306953-03',
        '2026-08-02 19:55:40.306953-03'),
       (8, 1, 'PB-PC-001', 'PowerBar Pro', 'PB Protein Crunch Bar', 'Chocolate peanut butter protein bar, 20g protein',
        1.10, true, '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (9, 1, 'AL-PC-001', 'ActiveLife', 'Protein Cookie', 'Soft-baked protein cookie, 15g protein', 1.35, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (10, 1, 'TB-NM-001', 'TrailBlaze', 'Adventure Nut Mix', 'Almonds, cashews, dried cranberries', 0.95, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (11, 1, 'EB-EB-001', 'EnergyBurst', 'Honey Almond Energy Bites', 'Natural snack bites for pre-workout energy',
        0.80, true, '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (12, 1, 'RS-ES-001', 'Roasted Snack Co.', 'Sea Salt Edamame', 'High-protein roasted edamame snack pack', 0.90,
        true, '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (13, 1, 'HC-PC-001', 'Harvest Crunch', 'Protein Clusters', 'Coconut and cashew protein clusters', 1.05, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (14, 2, 'HB-HD-001', 'HydroBoost', 'Electrolyte Drink Mix', 'Single-serve hydration packet', 0.55, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (15, 2, 'PH-HT-001', 'PeakHydrate', 'Hydration Tablets', 'Dissolvable hydration tablet tube', 1.75, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (16, 2, 'CS-HP-001', 'CitrusSport', 'Hydration Packets', 'Citrus-flavored sports hydration mix', 0.60, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (17, 2, 'BE-EP-001', 'BerryElectro', 'Electrolyte Powder Stick', 'Berry-flavored electrolyte supplement', 0.65,
        true, '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (18, 3, 'OV-OS-001', 'OmegaVital', 'Omega-3 Trial Pack', '3-day omega-3 softgel sample', 0.85, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (19, 3, 'CP-CS-001', 'CollagenPlus', 'Collagen Beauty Stick', 'Single-serve collagen supplement', 0.90, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (20, 3, 'AF-DS-001', 'ActiveFresh', 'Sport Deodorant', 'Travel-size sport deodorant stick', 1.20, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (21, 3, 'RG-BW-001', 'RefreshGo', 'Body Wipes', 'Pack of 10 post-workout wipes', 0.95, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (22, 3, 'AW-FC-001', 'AfterWorkout', 'Face Cleanser', 'Travel-size facial cleanser', 1.10, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (23, 3, 'QC-DS-001', 'QuickClean', 'Dry Shampoo', 'Travel-size dry shampoo spray', 1.30, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (24, 4, 'AT-SK-001', 'ActiveThreads', 'Performance Crew Socks', 'Moisture-wicking athletic socks', 1.10, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (25, 4, 'AT-SK-002', 'ActiveThreads', 'Ankle Training Socks', 'Lightweight ankle socks for workouts', 0.95, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (26, 4, 'AT-SK-003', 'ActiveThreads', 'Compression Socks', 'Mid-calf compression recovery socks', 2.40, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (27, 4, 'FT-HC-001', 'FitTech', 'Running Cap', 'Lightweight moisture-wicking athletic cap', 3.80, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (28, 4, 'AT-HD-001', 'ActiveThreads', 'Fitness Hoodie', 'Midweight performance hoodie', 11.50, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (29, 4, 'AT-JK-001', 'ActiveThreads', 'Windbreaker Jacket', 'Packable running jacket', 10.25, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (30, 4, 'AT-TS-001', 'ActiveThreads', 'Women''s Performance Tank', 'Lightweight moisture-wicking tank top', 4.25,
        true, '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (31, 4, 'AT-TS-002', 'ActiveThreads', 'Men''s Workout Tank', 'Sleeveless athletic training shirt', 4.50, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (32, 4, 'AT-TS-003', 'ActiveThreads', 'Unisex Performance Tee', 'Breathable polyester workout t-shirt', 4.85,
        true, '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (33, 4, 'AT-TS-004', 'ActiveThreads', 'Long Sleeve Training Tee', 'Lightweight long-sleeve fitness shirt', 5.90,
        true, '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (34, 4, 'AT-LG-001', 'ActiveThreads', 'Women''s Training Leggings', 'High-stretch workout leggings', 8.50, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (35, 4, 'AT-LG-002', 'ActiveThreads', 'Compression Leggings', 'Performance compression leggings', 9.75, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (36, 4, 'AT-SH-001', 'ActiveThreads', 'Athletic Shorts', 'Lightweight gym shorts with pockets', 5.25, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (37, 4, 'AT-SH-002', 'ActiveThreads', 'Running Shorts', 'Breathable running shorts', 4.90, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (38, 4, 'AT-TP-001', 'ActiveThreads', 'Quarter-Zip Training Top', 'Lightweight warm-up pullover', 8.75, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (39, 4, 'PF-BR-001', 'PeakForm', 'Women''s Sports Bra', 'Medium-support fitness sports bra', 6.75, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (40, 4, 'PF-BR-002', 'PeakForm', 'High Impact Sports Bra', 'High-support workout bra', 8.25, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (41, 4, 'EL-JK-001', 'Elevate Athletics', 'Reflective Running Jacket', 'Lightweight reflective running jacket',
        12.50, true, '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (42, 4, 'EL-LG-001', 'Elevate Athletics', 'Premium Sculpt Leggings', 'High-waist premium leggings', 11.25, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (43, 4, 'EL-TS-001', 'Elevate Athletics', 'Seamless Performance Tee', 'Seamless moisture-wicking shirt', 7.40,
        true, '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (44, 4, 'EL-HD-001', 'Elevate Athletics', 'Premium Training Hoodie', 'Soft performance fabric hoodie', 14.00,
        true, '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (45, 1, 'FN-BC-001', 'FitNaturals', 'Blueberry Protein Bites', 'Soft protein snack bites with blueberries', 0.85,
        true, '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (46, 1, 'FN-BC-002', 'FitNaturals', 'Peanut Butter Energy Bites', 'High-protein peanut butter snack bites', 0.80,
        true, '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (47, 1, 'CR-PC-001', 'CrunchRight', 'Cinnamon Protein Crisps', 'Crunchy cinnamon protein snack', 0.95, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (48, 1, 'CR-BC-001', 'CrunchRight', 'BBQ Protein Chips', 'Baked protein chips, BBQ flavor', 1.10, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (49, 1, 'NS-PA-001', 'NutriSnack', 'Protein Almond Pack', 'Roasted almonds with added protein', 0.90, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (50, 1, 'NS-CS-001', 'NutriSnack', 'Cashew Crunch Pack', 'Lightly salted cashew snack pack', 1.00, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (51, 1, 'PB-MB-001', 'PowerBite', 'Maple Protein Bar', 'Maple oat protein bar, 18g protein', 1.05, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (52, 1, 'PB-CB-001', 'PowerBite', 'Cookies & Cream Protein Bar', 'Protein bar, 20g protein', 1.15, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (53, 1, 'GP-FB-001', 'GreenPeak', 'Fruit & Nut Trail Mix', 'Mixed fruit and nuts snack pack', 0.85, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (54, 1, 'GP-AP-001', 'GreenPeak', 'Apple Cinnamon Crisps', 'Dried apple snack with cinnamon', 0.75, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (55, 1, 'MW-PS-001', 'MuscleWorks', 'Protein Pretzels', 'High-protein baked pretzels', 0.95, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (56, 1, 'MW-PC-001', 'MuscleWorks', 'Chocolate Protein Clusters', 'Chocolate-coated protein clusters', 1.10,
        true, '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (57, 1, 'BF-RC-001', 'BetterFuel', 'Rice Protein Crisps', 'Light crispy protein snack', 0.90, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (58, 1, 'BF-OC-001', 'BetterFuel', 'Oat Crunch Squares', 'Oat-based protein snack squares', 0.85, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (59, 3, 'AF-LB-001', 'ActiveFresh', 'Lip Balm', 'Moisturizing sport lip balm', 0.55, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (60, 3, 'AF-HS-001', 'ActiveFresh', 'Hand Sanitizer', 'Travel-size hand sanitizer', 0.60, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (61, 3, 'RG-FW-001', 'RefreshGo', 'Facial Wipes', 'Pack of 15 refreshing face wipes', 0.85, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (62, 3, 'RG-CW-001', 'RefreshGo', 'Cooling Wipes', 'Menthol cooling body wipes', 1.00, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (63, 3, 'AW-FM-001', 'AfterWorkout', 'Facial Moisturizer', 'Travel-size daily moisturizer', 1.20, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (64, 3, 'AW-FS-001', 'AfterWorkout', 'Facial Scrub', 'Travel-size exfoliating face scrub', 1.15, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (65, 3, 'FR-MC-001', 'FitRecovery', 'Muscle Cream', 'Warming muscle recovery cream', 1.40, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (66, 3, 'NS-SP-001', 'NatureShield', 'SPF 50 Sport Sunscreen', 'Travel-size sport sunscreen', 1.25, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (67, 3, 'NS-AS-001', 'NatureShield', 'After Sun Gel', 'Aloe-based recovery gel', 1.15, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (68, 3, 'PG-FR-001', 'PureGlow', 'Face Mask', 'Single-use hydrating face mask', 0.90, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03'),
       (69, 3, 'PG-EC-001', 'PureGlow', 'Eye Care Patches', 'Refreshing under-eye patches', 0.80, true,
        '2026-08-02 20:20:27.948923-03', '2026-08-02 20:20:27.948923-03')
ON CONFLICT (product_id) DO NOTHING;

-- Synchronize sequence state
SELECT setval(
               pg_get_serial_sequence('public.product', 'product_id'),
               COALESCE(MAX(product_id), 1)
       )
FROM public.product;

-- 3. Product Inventory Seed Data
INSERT INTO public.product_inventory (inventory_id, in_stock, reserved, updated_at, product_id, created_at)
    OVERRIDING SYSTEM VALUE
VALUES (56, 94, 7, '2026-08-02 20:27:37.605388-03', 4, '2026-08-02 20:27:37.605388-03'),
       (57, 201, 14, '2026-08-02 20:27:37.605388-03', 5, '2026-08-02 20:27:37.605388-03'),
       (58, 167, 11, '2026-08-02 20:27:37.605388-03', 6, '2026-08-02 20:27:37.605388-03'),
       (59, 183, 15, '2026-08-02 20:27:37.605388-03', 7, '2026-08-02 20:27:37.605388-03'),
       (60, 121, 8, '2026-08-02 20:27:37.605388-03', 8, '2026-08-02 20:27:37.605388-03'),
       (61, 156, 13, '2026-08-02 20:27:37.605388-03', 9, '2026-08-02 20:27:37.605388-03'),
       (62, 141, 10, '2026-08-02 20:27:37.605388-03', 10, '2026-08-02 20:27:37.605388-03'),
       (63, 98, 5, '2026-08-02 20:27:37.605388-03', 11, '2026-08-02 20:27:37.605388-03'),
       (64, 114, 7, '2026-08-02 20:27:37.605388-03', 12, '2026-08-02 20:27:37.605388-03'),
       (65, 127, 9, '2026-08-02 20:27:37.605388-03', 13, '2026-08-02 20:27:37.605388-03'),
       (66, 136, 12, '2026-08-02 20:27:37.605388-03', 14, '2026-08-02 20:27:37.605388-03'),
       (67, 89, 4, '2026-08-02 20:27:37.605388-03', 15, '2026-08-02 20:27:37.605388-03'),
       (68, 105, 6, '2026-08-02 20:27:37.605388-03', 16, '2026-08-02 20:27:37.605388-03'),
       (69, 72, 3, '2026-08-02 20:27:37.605388-03', 17, '2026-08-02 20:27:37.605388-03'),
       (70, 118, 8, '2026-08-02 20:27:37.605388-03', 18, '2026-08-02 20:27:37.605388-03'),
       (71, 83, 5, '2026-08-02 20:27:37.605388-03', 19, '2026-08-02 20:27:37.605388-03'),
       (72, 91, 6, '2026-08-02 20:27:37.605388-03', 20, '2026-08-02 20:27:37.605388-03'),
       (73, 76, 4, '2026-08-02 20:27:37.605388-03', 21, '2026-08-02 20:27:37.605388-03'),
       (74, 65, 2, '2026-08-02 20:27:37.605388-03', 22, '2026-08-02 20:27:37.605388-03'),
       (75, 28, 1, '2026-08-02 20:27:37.605388-03', 23, '2026-08-02 20:27:37.605388-03'),
       (76, 33, 2, '2026-08-02 20:27:37.605388-03', 24, '2026-08-02 20:27:37.605388-03'),
       (77, 41, 2, '2026-08-02 20:27:37.605388-03', 25, '2026-08-02 20:27:37.605388-03'),
       (78, 37, 1, '2026-08-02 20:27:37.605388-03', 26, '2026-08-02 20:27:37.605388-03'),
       (79, 54, 3, '2026-08-02 20:27:37.605388-03', 27, '2026-08-02 20:27:37.605388-03'),
       (80, 48, 2, '2026-08-02 20:27:37.605388-03', 28, '2026-08-02 20:27:37.605388-03'),
       (81, 22, 1, '2026-08-02 20:27:37.605388-03', 29, '2026-08-02 20:27:37.605388-03'),
       (82, 19, 0, '2026-08-02 20:27:37.605388-03', 30, '2026-08-02 20:27:37.605388-03'),
       (83, 31, 1, '2026-08-02 20:27:37.605388-03', 31, '2026-08-02 20:27:37.605388-03'),
       (84, 26, 1, '2026-08-02 20:27:37.605388-03', 32, '2026-08-02 20:27:37.605388-03'),
       (85, 17, 0, '2026-08-02 20:27:37.605388-03', 33, '2026-08-02 20:27:37.605388-03'),
       (86, 14, 0, '2026-08-02 20:27:37.605388-03', 34, '2026-08-02 20:27:37.605388-03'),
       (87, 159, 14, '2026-08-02 20:27:37.605388-03', 35, '2026-08-02 20:27:37.605388-03'),
       (88, 145, 11, '2026-08-02 20:27:37.605388-03', 36, '2026-08-02 20:27:37.605388-03'),
       (89, 136, 10, '2026-08-02 20:27:37.605388-03', 37, '2026-08-02 20:27:37.605388-03'),
       (90, 127, 9, '2026-08-02 20:27:37.605388-03', 38, '2026-08-02 20:27:37.605388-03'),
       (91, 172, 15, '2026-08-02 20:27:37.605388-03', 39, '2026-08-02 20:27:37.605388-03'),
       (92, 154, 12, '2026-08-02 20:27:37.605388-03', 40, '2026-08-02 20:27:37.605388-03'),
       (93, 111, 8, '2026-08-02 20:27:37.605388-03', 41, '2026-08-02 20:27:37.605388-03'),
       (94, 124, 9, '2026-08-02 20:27:37.605388-03', 42, '2026-08-02 20:27:37.605388-03')
ON CONFLICT (inventory_id) DO NOTHING;

-- Synchronize sequence state
SELECT setval(
               pg_get_serial_sequence('public.product_inventory', 'inventory_id'),
               COALESCE(MAX(inventory_id), 1)
       )
FROM public.product_inventory;

-- 4. Subscription Box Seed Data
INSERT INTO public.subscription_box (subscription_box_id, name, description, image_file, available_units, is_active,
                                     created_at, updated_at)
    OVERRIDING SYSTEM VALUE
VALUES (1, 'Essential Fitness Box',
        'Entry-level box featuring a mix of protein snacks, hydration products, personal care items, and basic fitness accessories.',
        'essential-fitness-box.jpg', 200, true, '2026-08-02 20:33:16.552639-03', '2026-08-02 20:33:16.552639-03'),
       (2, 'Active Lifestyle Box',
        'Mid-tier box combining premium snacks, hydration, wellness products, personal care items, and select apparel.',
        'active-lifestyle-box.jpg', 150, true, '2026-08-02 20:33:16.552639-03', '2026-08-02 20:33:16.552639-03'),
       (3, 'Performance Box',
        'Designed for active gym-goers with a curated mix of nutrition, recovery, hydration, personal care, and performance apparel.',
        'performance-box.jpg', 100, true, '2026-08-02 20:33:16.552639-03', '2026-08-02 20:33:16.552639-03'),
       (4, 'Elite Athlete Box',
        'Premium box featuring top-tier apparel, advanced recovery products, high-quality nutrition, wellness essentials, and exclusive fitness gear.',
        'elite-athlete-box.jpg', 75, true, '2026-08-02 20:33:16.552639-03', '2026-08-02 20:33:16.552639-03')
ON CONFLICT (subscription_box_id) DO NOTHING;

-- Synchronize sequence state
SELECT setval(
               pg_get_serial_sequence('public.subscription_box', 'subscription_box_id'),
               COALESCE(MAX(subscription_box_id), 1)
       )
FROM public.subscription_box;

-- 5. Subscription Box Price Seed Data
INSERT INTO public.subscription_box_price (subscription_box_price_id, subscription_box_id, amount, currency, starts_at,
                                           ends_at, stripe_price_id)
    OVERRIDING SYSTEM VALUE
VALUES (1, 1, 29.99, 'CAD', '2026-08-02 20:38:39.65765-03', NULL, NULL),
       (2, 2, 49.99, 'CAD', '2026-08-02 20:38:39.65765-03', NULL, NULL),
       (3, 3, 69.99, 'CAD', '2026-08-02 20:38:39.65765-03', NULL, NULL),
       (4, 4, 99.99, 'CAD', '2026-08-02 20:38:39.65765-03', NULL, NULL)
ON CONFLICT (subscription_box_price_id) DO NOTHING;

-- Synchronize sequence state
SELECT setval(
               pg_get_serial_sequence('public.subscription_box_price', 'subscription_box_price_id'),
               COALESCE(MAX(subscription_box_price_id), 1)
       )
FROM public.subscription_box_price;

-- 6. Subscription Box Product Seed Data
INSERT INTO public.subscription_box_product (subscription_box_id, product_id, quantity)
VALUES (1, 8, 1),
       (1, 9, 1),
       (1, 14, 1),
       (1, 20, 1),
       (1, 21, 1),
       (1, 24, 1),
       (1, 45, 1),
       (1, 54, 1),
       (2, 4, 1),
       (2, 15, 1),
       (2, 18, 1),
       (2, 22, 1),
       (2, 24, 1),
       (2, 27, 1),
       (2, 51, 1),
       (2, 65, 1),
       (2, 68, 1),
       (2, 49, 1),
       (3, 4, 1),
       (3, 5, 1),
       (3, 15, 1),
       (3, 19, 1),
       (3, 65, 1),
       (3, 24, 1),
       (3, 30, 1),
       (3, 36, 1),
       (3, 52, 1),
       (3, 56, 1),
       (3, 60, 1),
       (3, 66, 1),
       (4, 4, 1),
       (4, 5, 1),
       (4, 6, 1),
       (4, 15, 1),
       (4, 18, 1),
       (4, 19, 1),
       (4, 65, 1),
       (4, 69, 1),
       (4, 41, 1),
       (4, 43, 1),
       (4, 34, 1),
       (4, 39, 1),
       (4, 52, 1),
       (4, 56, 1),
       (4, 49, 1),
       (4, 66, 1)
ON CONFLICT (subscription_box_id, product_id) DO NOTHING;