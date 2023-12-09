-- Add three default books
INSERT INTO books (id, title, author, isbn, price, description, cover_image)
VALUES (1, 'Red Book', 'Red Author', 'Red-ISBN', 19.99, 'Red description', 'Red cover image');
INSERT INTO books (id, title, author, isbn, price, description, cover_image)
VALUES (2, 'Green Book', 'Green Author', 'Green-ISBN', 19.99, 'Green description', 'Green cover image');
INSERT INTO books (id, title, author, isbn, price, description, cover_image)
VALUES (3, 'Black Book', 'Black Author', 'Black-ISBN', 21.99, 'Black description', 'Black cover image');
-- Add two users
INSERT INTO users (id, email, password, first_name, last_name, shipping_address)
VALUES (1, 'test1@gmail.com', '1234', 'Test', 'Test', 'Test address');
INSERT INTO users (id, email, password, first_name, last_name, shipping_address)
VALUES (2, 'test2@gmail.com', '1234', 'Test', 'Test', 'Test address');
-- Add shopping carts for two users
INSERT INTO shopping_carts (id, user_id) VALUES (1, 1);
INSERT INTO shopping_carts (id, user_id) VALUES (2, 2);
--Add cart item for shopping cart #1
INSERT INTO cart_items (id, shopping_cart_id, book_id, quantity) VALUES (1, 1, 1, 10);
