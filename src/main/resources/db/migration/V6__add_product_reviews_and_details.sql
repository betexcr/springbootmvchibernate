-- Add reviews, care instructions, and detailed descriptions to products table
ALTER TABLE products 
ADD COLUMN IF NOT EXISTS care_instructions TEXT,
ADD COLUMN IF NOT EXISTS detailed_description TEXT,
ADD COLUMN IF NOT EXISTS image_url VARCHAR(500);

-- Create reviews table
CREATE TABLE IF NOT EXISTS product_reviews (
    id SERIAL PRIMARY KEY,
    product_id INTEGER NOT NULL,
    customer_name VARCHAR(100) NOT NULL,
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    review_text TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

-- Create index for better performance
CREATE INDEX IF NOT EXISTS idx_product_reviews_product_id ON product_reviews(product_id);
CREATE INDEX IF NOT EXISTS idx_product_reviews_rating ON product_reviews(rating);

-- Sample reviews will be added through the API when needed

-- Add care instructions and detailed descriptions for some products
UPDATE products 
SET care_instructions = 'Store in a cool, dry place. Keep away from direct sunlight. Best consumed within 6 months of purchase.',
    detailed_description = 'This premium product is carefully crafted using traditional methods and the finest ingredients. Each item is individually inspected to ensure the highest quality standards. Perfect for both personal use and gifting.',
    image_url = 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=400&h=400&fit=crop'
WHERE product_id = 61;

UPDATE products 
SET care_instructions = 'Refrigerate after opening. Consume within 3 days. Shake well before use.',
    detailed_description = 'Made with natural ingredients and no artificial preservatives. This product offers exceptional taste and nutritional value. Ideal for health-conscious consumers who want quality without compromise.',
    image_url = 'https://images.unsplash.com/photo-1567306226416-28f0efdc88ce?w=400&h=400&fit=crop'
WHERE product_id = 62;

UPDATE products 
SET care_instructions = 'Store at room temperature. Handle with care to avoid breakage.',
    detailed_description = 'Handcrafted with attention to detail, this product combines traditional craftsmanship with modern design. Each piece is unique and tells a story of quality and dedication.',
    image_url = 'https://images.unsplash.com/photo-1441986300917-64674bd600d8?w=400&h=400&fit=crop'
WHERE product_id = 63;
