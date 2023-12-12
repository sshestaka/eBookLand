-- Remove all cart items from shopping cart #1
DELETE FROM cart_items WHERE shopping_cart_id = 1;
-- Remove all shopping carts
DELETE FROM shopping_carts;
-- Remove all from users
DELETE FROM users;
-- Remove all books
DELETE FROM books;
