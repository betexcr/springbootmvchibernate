-- Add description and variants columns to products table
ALTER TABLE products 
ADD COLUMN description TEXT,
ADD COLUMN variants TEXT;

-- Update some products with sample descriptions and variants
UPDATE products 
SET description = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.',
    variants = '{"colors": ["White", "Black"], "sizes": ["S", "M", "L", "XL"]}'
WHERE product_id IN (1, 2, 3, 4, 5);

-- Add more specific descriptions for different product types
UPDATE products 
SET description = 'Premium quality beverage with rich flavor and smooth texture. Perfect for any occasion.',
    variants = '{"colors": ["Clear", "Amber"], "sizes": ["250ml", "500ml", "1L"]}'
WHERE category_id = 1; -- Beverages

UPDATE products 
SET description = 'Delicious confectionery treat made with the finest ingredients. A perfect gift for any sweet tooth.',
    variants = '{"colors": ["Brown", "White", "Mixed"], "sizes": ["Small", "Medium", "Large"]}'
WHERE category_id = 3; -- Confections

UPDATE products 
SET description = 'Fresh dairy product with natural goodness. Rich in nutrients and perfect for daily consumption.',
    variants = '{"colors": ["White", "Cream"], "sizes": ["250g", "500g", "1kg"]}'
WHERE category_id = 4; -- Dairy Products

UPDATE products 
SET description = 'High-quality condiment that enhances the flavor of your favorite dishes. Made with traditional recipes.',
    variants = '{"colors": ["Red", "Green", "Yellow"], "sizes": ["100ml", "250ml", "500ml"]}'
WHERE category_id = 2; -- Condiments

UPDATE products 
SET description = 'Nutritious grain product that provides essential vitamins and minerals. Great for a healthy lifestyle.',
    variants = '{"colors": ["Brown", "White"], "sizes": ["500g", "1kg", "2kg"]}'
WHERE category_id = 5; -- Grains/Cereals

UPDATE products 
SET description = 'Fresh meat and poultry products sourced from trusted suppliers. Perfect for family meals.',
    variants = '{"colors": ["Red", "Pink"], "sizes": ["250g", "500g", "1kg"]}'
WHERE category_id = 6; -- Meat/Poultry

UPDATE products 
SET description = 'Fresh produce straight from the farm. Rich in vitamins and perfect for healthy cooking.',
    variants = '{"colors": ["Green", "Red", "Orange", "Yellow"], "sizes": ["500g", "1kg", "2kg"]}'
WHERE category_id = 7; -- Produce

UPDATE products 
SET description = 'Fresh seafood caught from the finest waters. Rich in omega-3 and perfect for gourmet cooking.',
    variants = '{"colors": ["Pink", "White", "Orange"], "sizes": ["250g", "500g", "1kg"]}'
WHERE category_id = 8; -- Seafood
