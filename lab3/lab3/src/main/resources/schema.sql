CREATE TABLE IF NOT EXISTS users (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       role VARCHAR(50) DEFAULT 'READER'
);

CREATE TABLE IF NOT EXISTS book_editions (
                               id SERIAL PRIMARY KEY,
                               title VARCHAR(255) NOT NULL,
                               author VARCHAR(255) NOT NULL,
                               isbn VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS copies (
                        id SERIAL PRIMARY KEY,
                        book_edition_id INT NOT NULL REFERENCES book_editions(id) ON DELETE CASCADE,
                        inventory_number VARCHAR(100) UNIQUE NOT NULL,
                        status VARCHAR(50) DEFAULT 'AVAILABLE'
);

CREATE TABLE IF NOT EXISTS loans (
                       id SERIAL PRIMARY KEY,
                       copy_id INT NOT NULL REFERENCES copies(id) ON DELETE CASCADE,
                       user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                       loan_date DATE NOT NULL,
                       return_date DATE
);

CREATE TABLE IF NOT EXISTS waiting_requests (
                                  id SERIAL PRIMARY KEY,
                                  book_edition_id INT NOT NULL REFERENCES book_editions(id) ON DELETE CASCADE,
                                  user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                  request_date DATE NOT NULL,
                                  status VARCHAR(50) DEFAULT 'PENDING'
);


/*CREATE USER "userOK" WITH PASSWORD 'qwe123';
GRANT CONNECT ON DATABASE library_db TO "userOK";
GRANT USAGE ON SCHEMA public TO "userOK";
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO "userOK";

CREATE USER "root" WITH PASSWORD 'root';
GRANT ALL PRIVILEGES ON DATABASE library_db TO "root"; */
