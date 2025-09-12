-- Add more products to categories that have fewer products
-- Categories 5 (Grains/Cereals), 6 (Meat/Poultry), and 7 (Produce) need more products

-- Additional products for Category 38 (Grains/Cereals)
INSERT INTO products (product_id, product_name, supplier_id, category_id, quantity_per_unit, unit_price, units_in_stock, units_on_order, reorder_level, discontinued) VALUES (78, 'Artisan Sourdough Bread', 1, 38, '1 loaf', 4.50, 25, 0, 5, false);
INSERT INTO products (product_id, product_name, supplier_id, category_id, quantity_per_unit, unit_price, units_in_stock, units_on_order, reorder_level, discontinued) VALUES (79, 'Whole Wheat Pasta', 2, 38, '500g package', 3.25, 40, 0, 10, false);
INSERT INTO products (product_id, product_name, supplier_id, category_id, quantity_per_unit, unit_price, units_in_stock, units_on_order, reorder_level, discontinued) VALUES (80, 'Quinoa Grain', 3, 38, '1kg bag', 8.75, 30, 0, 15, false);
INSERT INTO products (product_id, product_name, supplier_id, category_id, quantity_per_unit, unit_price, units_in_stock, units_on_order, reorder_level, discontinued) VALUES (81, 'Steel Cut Oats', 4, 38, '2kg container', 6.50, 50, 0, 20, false);
INSERT INTO products (product_id, product_name, supplier_id, category_id, quantity_per_unit, unit_price, units_in_stock, units_on_order, reorder_level, discontinued) VALUES (82, 'Brown Rice', 5, 38, '2kg bag', 5.25, 35, 0, 15, false);
INSERT INTO products (product_id, product_name, supplier_id, category_id, quantity_per_unit, unit_price, units_in_stock, units_on_order, reorder_level, discontinued) VALUES (83, 'Barley Flour', 6, 38, '1kg bag', 4.75, 20, 0, 10, false);
INSERT INTO products (product_id, product_name, supplier_id, category_id, quantity_per_unit, unit_price, units_in_stock, units_on_order, reorder_level, discontinued) VALUES (84, 'Rye Crackers', 7, 38, '200g box', 3.95, 45, 0, 15, false);
INSERT INTO products (product_id, product_name, supplier_id, category_id, quantity_per_unit, unit_price, units_in_stock, units_on_order, reorder_level, discontinued) VALUES (85, 'Buckwheat Groats', 8, 38, '500g package', 7.25, 25, 0, 10, false);
INSERT INTO products (product_id, product_name, supplier_id, category_id, quantity_per_unit, unit_price, units_in_stock, units_on_order, reorder_level, discontinued) VALUES (86, 'Couscous', 9, 38, '500g box', 4.50, 30, 0, 12, false);
INSERT INTO products (product_id, product_name, supplier_id, category_id, quantity_per_unit, unit_price, units_in_stock, units_on_order, reorder_level, discontinued) VALUES (87, 'Millet Seeds', 1, 38, '1kg bag', 6.75, 20, 0, 8, false);

-- Additional products for Category 39 (Meat/Poultry)
INSERT INTO products (product_id, product_name, supplier_id, category_id, quantity_per_unit, unit_price, units_in_stock, units_on_order, reorder_level, discontinued) VALUES (88, 'Organic Chicken Breast', 2, 39, '1kg package', 12.50, 15, 0, 5, false);
INSERT INTO products (product_id, product_name, supplier_id, category_id, quantity_per_unit, unit_price, units_in_stock, units_on_order, reorder_level, discontinued) VALUES (89, 'Grass-Fed Beef Steak', 3, 39, '500g package', 18.75, 8, 0, 3, false);
INSERT INTO products (product_id, product_name, supplier_id, category_id, quantity_per_unit, unit_price, units_in_stock, units_on_order, reorder_level, discontinued) VALUES (90, 'Free-Range Turkey', 4, 39, '2kg whole bird', 24.50, 6, 0, 2, false);
INSERT INTO products (product_id, product_name, supplier_id, category_id, quantity_per_unit, unit_price, units_in_stock, units_on_order, reorder_level, discontinued) VALUES (91, 'Lamb Chops', 5, 39, '4 pieces', 16.25, 12, 0, 4, false);
INSERT INTO products (product_id, product_name, supplier_id, category_id, quantity_per_unit, unit_price, units_in_stock, units_on_order, reorder_level, discontinued) VALUES (92, 'Pork Tenderloin', 6, 39, '1kg package', 14.50, 10, 0, 3, false);
INSERT INTO products (product_id, product_name, supplier_id, category_id, quantity_per_unit, unit_price, units_in_stock, units_on_order, reorder_level, discontinued) VALUES (93, 'Duck Breast', 7, 39, '2 pieces', 22.75, 5, 0, 2, false);
INSERT INTO products (product_id, product_name, supplier_id, category_id, quantity_per_unit, unit_price, units_in_stock, units_on_order, reorder_level, discontinued) VALUES (94, 'Veal Cutlets', 8, 39, '4 pieces', 19.25, 7, 0, 3, false);
INSERT INTO products (product_id, product_name, supplier_id, category_id, quantity_per_unit, unit_price, units_in_stock, units_on_order, reorder_level, discontinued) VALUES (95, 'Bison Ground Meat', 9, 39, '500g package', 21.50, 4, 0, 2, false);
INSERT INTO products (product_id, product_name, supplier_id, category_id, quantity_per_unit, unit_price, units_in_stock, units_on_order, reorder_level, discontinued) VALUES (96, 'Venison Sausage', 1, 39, '6 links', 15.75, 8, 0, 3, false);
INSERT INTO products (product_id, product_name, supplier_id, category_id, quantity_per_unit, unit_price, units_in_stock, units_on_order, reorder_level, discontinued) VALUES (97, 'Rabbit Legs', 2, 39, '4 pieces', 17.25, 6, 0, 2, false);

-- Additional products for Category 40 (Produce)
INSERT INTO products (product_id, product_name, supplier_id, category_id, quantity_per_unit, unit_price, units_in_stock, units_on_order, reorder_level, discontinued) VALUES (98, 'Organic Spinach', 3, 40, '200g bag', 3.25, 30, 0, 10, false);
INSERT INTO products (product_id, product_name, supplier_id, category_id, quantity_per_unit, unit_price, units_in_stock, units_on_order, reorder_level, discontinued) VALUES (99, 'Fresh Kale', 4, 40, '150g bunch', 2.75, 25, 0, 8, false);
INSERT INTO products (product_id, product_name, supplier_id, category_id, quantity_per_unit, unit_price, units_in_stock, units_on_order, reorder_level, discontinued) VALUES (100, 'Organic Carrots', 5, 40, '1kg bag', 2.50, 40, 0, 15, false);
INSERT INTO products (product_id, product_name, supplier_id, category_id, quantity_per_unit, unit_price, units_in_stock, units_on_order, reorder_level, discontinued) VALUES (101, 'Red Bell Peppers', 6, 40, '4 pieces', 4.25, 20, 0, 8, false);
INSERT INTO products (product_id, product_name, supplier_id, category_id, quantity_per_unit, unit_price, units_in_stock, units_on_order, reorder_level, discontinued) VALUES (102, 'Organic Tomatoes', 7, 40, '500g package', 3.75, 35, 0, 12, false);
INSERT INTO products (product_id, product_name, supplier_id, category_id, quantity_per_unit, unit_price, units_in_stock, units_on_order, reorder_level, discontinued) VALUES (103, 'Fresh Broccoli', 8, 40, '1 head', 2.95, 25, 0, 10, false);
INSERT INTO products (product_id, product_name, supplier_id, category_id, quantity_per_unit, unit_price, units_in_stock, units_on_order, reorder_level, discontinued) VALUES (104, 'Organic Cucumbers', 9, 40, '3 pieces', 3.50, 30, 0, 12, false);
INSERT INTO products (product_id, product_name, supplier_id, category_id, quantity_per_unit, unit_price, units_in_stock, units_on_order, reorder_level, discontinued) VALUES (105, 'Fresh Lettuce', 1, 40, '1 head', 2.25, 40, 0, 15, false);
INSERT INTO products (product_id, product_name, supplier_id, category_id, quantity_per_unit, unit_price, units_in_stock, units_on_order, reorder_level, discontinued) VALUES (106, 'Organic Onions', 2, 40, '1kg bag', 2.95, 50, 0, 20, false);
INSERT INTO products (product_id, product_name, supplier_id, category_id, quantity_per_unit, unit_price, units_in_stock, units_on_order, reorder_level, discontinued) VALUES (107, 'Fresh Garlic', 3, 40, '100g package', 1.75, 60, 0, 25, false);
