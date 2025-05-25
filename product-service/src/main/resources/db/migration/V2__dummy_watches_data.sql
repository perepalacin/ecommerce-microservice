-- data.sql

-- Clear existing data (optional, useful for clean re-initialization)
DELETE FROM products;
ALTER TABLE products AUTO_INCREMENT = 1;

-- Rolex
INSERT INTO products (name, brand, mechanism, diameter, price, stock, description, image_url) VALUES
('Submariner Date', 'Rolex', 'Automatic', 41, 15500.00, 10, 'Iconic dive watch with date complication.', 'https://example.com/rolex_submariner.jpg'),
('GMT-Master II', 'Rolex', 'Automatic', 40, 14000.00, 8, 'Traveller''s watch with dual time zone display.', 'https://example.com/rolex_gmt.jpg'),
('Datejust 36', 'Rolex', 'Automatic', 36, 9500.00, 15, 'Classic and versatile dress watch.', 'https://example.com/rolex_datejust36.jpg'),
('Day-Date 40', 'Rolex', 'Automatic', 40, 38000.00, 3, 'The President''s watch, day and date display.', 'https://example.com/rolex_daydate.jpg');

-- Omega
INSERT INTO products (name, brand, mechanism, diameter, price, stock, description, image_url) VALUES
('Speedmaster Professional', 'Omega', 'Manual', 42, 6800.00, 12, 'The legendary Moonwatch.', 'https://example.com/omega_speedmaster.jpg'),
('Seamaster Diver 300M', 'Omega', 'Automatic', 42, 5400.00, 18, 'Professional dive watch, James Bond''s choice.', 'https://example.com/omega_seamaster.jpg'),
('Aqua Terra', 'Omega', 'Automatic', 38, 5000.00, 20, 'Versatile watch for land and sea.', 'https://example.com/omega_aquaterra.jpg'),
('Constellation', 'Omega', 'Automatic', 28, 4500.00, 10, 'Elegant and precise women''s watch.', 'https://example.com/omega_constellation.jpg');

-- Tag Heuer
INSERT INTO products (name, brand, mechanism, diameter, price, stock, description, image_url) VALUES
('Carrera Chronograph', 'Tag Heuer', 'Automatic', 44, 4800.00, 14, 'Racing-inspired chronograph.', 'https://example.com/tag_carrera.jpg'),
('Aquaracer 300M', 'Tag Heuer', 'Automatic', 43, 2800.00, 25, 'Robust dive watch for aquatic adventures.', 'https://example.com/tag_aquaracer.jpg'),
('Monaco Calibre 11', 'Tag Heuer', 'Automatic', 39, 6500.00, 7, 'Iconic square-cased chronograph.', 'https://example.com/tag_monaco.jpg'),
('Link Lady', 'Tag Heuer', 'Quartz', 32, 2200.00, 11, 'Elegant watch with signature S-shaped bracelet.', 'https://example.com/tag_link_lady.jpg');

-- Seiko
INSERT INTO products (name, brand, mechanism, diameter, price, stock, description, image_url) VALUES
('SKX007 Diver', 'Seiko', 'Automatic', 42, 350.00, 30, 'Classic entry-level dive watch, highly modifiable.', 'https://example.com/seiko_skx007.jpg'),
('Presage Cocktail Time', 'Seiko', 'Automatic', 40, 450.00, 28, 'Elegant dress watch with textured dial.', 'https://example.com/seiko_presage.jpg'),
('Prospex Tuna S23629', 'Seiko', 'Quartz', 48, 1200.00, 5, 'Professional diver with unique "tuna can" case.', 'https://example.com/seiko_tuna.jpg'),
('5 Sports SRPE55K1', 'Seiko', 'Automatic', 40, 280.00, 35, 'Modern everyday wear automatic watch.', 'https://example.com/seiko_5sports.jpg');

-- Citizen
INSERT INTO products (name, brand, mechanism, diameter, price, stock, description, image_url) VALUES
('Eco-Drive Promaster Diver', 'Citizen', 'Quartz', 44, 300.00, 22, 'Solar-powered dive watch, never needs a battery.', 'https://example.com/citizen_promaster.jpg'),
('Eco-Drive Atomic Timekeeping', 'Citizen', 'Quartz', 42, 600.00, 15, 'Radio-controlled for ultimate accuracy.', 'https://example.com/citizen_atomic.jpg'),
('Classic Elegance', 'Citizen', 'Quartz', 38, 180.00, 40, 'Simple and stylish everyday watch.', 'https://example.com/citizen_classic.jpg'),
('Ladies Eco-Drive Watch', 'Citizen', 'Quartz', 29, 250.00, 18, 'Elegant solar-powered watch for women.', 'https://example.com/citizen_ladies.jpg');

-- Casio
INSERT INTO products (name, brand, mechanism, diameter, price, stock, description, image_url) VALUES
('G-Shock DW-5600E', 'Casio', 'Quartz', 42, 99.00, 50, 'Legendary shock-resistant digital watch.', 'https://example.com/casio_gshock_5600.jpg'),
('Edifice EFR-556DB', 'Casio', 'Quartz', 48, 150.00, 30, 'Sophisticated chronograph with metal band.', 'https://example.com/casio_edifice.jpg'),
('A168WA Vintage', 'Casio', 'Quartz', 38, 30.00, 70, 'Retro digital watch, highly popular.', 'https://example.com/casio_a168.jpg'),
('Baby-G BGA-250', 'Casio', 'Quartz', 40, 120.00, 25, 'Colorful and tough watch for active women.', 'https://example.com/casio_babyg.jpg');

-- Longines
INSERT INTO products (name, brand, mechanism, diameter, price, stock, description, image_url) VALUES
('HydroConquest Automatic', 'Longines', 'Automatic', 41, 1500.00, 10, 'Sporty dive watch with Swiss automatic movement.', 'https://example.com/longines_hydro.jpg'),
('Master Collection', 'Longines', 'Automatic', 40, 2200.00, 8, 'Classic dress watch with moon phase complication.', 'https://example.com/longines_master.jpg'),
('DolceVita Quartz', 'Longines', 'Quartz', 28, 1000.00, 12, 'Elegant rectangular watch for women.', 'https://example.com/longines_dolcevita.jpg'),
('Conquest Classic', 'Longines', 'Automatic', 34, 1300.00, 15, 'Understated elegance for everyday wear.', 'https://example.com/longines_conquest.jpg');

-- Tissot
INSERT INTO products (name, brand, mechanism, diameter, price, stock, description, image_url) VALUES
('PRX Powermatic 80', 'Tissot', 'Automatic', 40, 700.00, 20, 'Retro-inspired integrated bracelet watch.', 'https://example.com/tissot_prx.jpg'),
('Le Locle Automatic', 'Tissot', 'Automatic', 39, 550.00, 18, 'Classic dress watch named after Tissot''s home town.', 'https://example.com/tissot_lelocle.jpg'),
('Chronograph XL', 'Tissot', 'Quartz', 45, 400.00, 25, 'Large, sporty quartz chronograph.', 'https://example.com/tissot_chrono_xl.jpg'),
('Lovely Square', 'Tissot', 'Quartz', 20, 350.00, 15, 'Delicate square watch for women.', 'https://example.com/tissot_lovely_square.jpg');

-- Patek Philippe (High-end examples for price range testing)
INSERT INTO products (name, brand, mechanism, diameter, price, stock, description, image_url) VALUES
('Nautilus 5711/1A', 'Patek Philippe', 'Automatic', 40, 80000.00, 1, 'Highly sought-after luxury sports watch.', 'https://example.com/patek_nautilus.jpg'),
('Calatrava', 'Patek Philippe', 'Automatic', 39, 28000.00, 2, 'Timeless and elegant dress watch.', 'https://example.com/patek_calatrava.jpg');

-- Hublot
INSERT INTO products (name, brand, mechanism, diameter, price, stock, description, image_url) VALUES
('Big Bang Unico', 'Hublot', 'Automatic', 42, 18000.00, 4, 'Bold and modern skeleton chronograph.', 'https://example.com/hublot_bigbang.jpg'),
('Classic Fusion Titanium', 'Hublot', 'Automatic', 45, 9000.00, 6, 'Sleek and versatile luxury watch.', 'https://example.com/hublot_classic_fusion.jpg');

-- Audemars Piguet
INSERT INTO products (name, brand, mechanism, diameter, price, stock, description, image_url) VALUES
('Royal Oak Jumbo', 'Audemars Piguet', 'Automatic', 39, 70000.00, 1, 'Iconic luxury sports watch with integrated bracelet.', 'https://example.com/ap_royaloak.jpg'),
('Code 11.59 Chronograph', 'Audemars Piguet', 'Automatic', 41, 35000.00, 2, 'Modern and elegant chronograph with unique case.', 'https://example.com/ap_code1159.jpg');

-- Low stock watches for testing
INSERT INTO products (name, brand, mechanism, diameter, price, stock, description, image_url) VALUES
('Limited Edition Diver', 'Seiko', 'Automatic', 44, 750.00, 2, 'A rare limited edition diver from Seiko.', 'https://example.com/seiko_limited.jpg'),
('Vintage Chronograph', 'Omega', 'Manual', 36, 4000.00, 1, 'A unique vintage Omega piece.', 'https://example.com/omega_vintage_chrono.jpg');

-- Additional diverse products
INSERT INTO products (name, brand, mechanism, diameter, price, stock, description, image_url) VALUES
('Heritage Black Bay', 'Tudor', 'Automatic', 41, 3500.00, 7, 'Popular vintage-inspired dive watch.', 'https://example.com/tudor_blackbay.jpg'),
('Pilot''s Watch Mark XVIII', 'IWC', 'Automatic', 40, 4800.00, 5, 'Classic pilot''s watch with clear readability.', 'https://example.com/iwc_pilot.jpg'),
('Master Ultra Thin Moon', 'Jaeger-LeCoultre', 'Automatic', 39, 10000.00, 3, 'Elegant and thin dress watch with moon phase.', 'https://example.com/jlc_ultrathin.jpg'),
('Classima Watch', 'Baume & Mercier', 'Quartz', 33, 1200.00, 10, 'Refined and timeless quartz watch.', 'https://example.com/bm_classima.jpg');