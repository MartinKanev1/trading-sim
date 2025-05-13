
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    password TEXT NOT NULL,
    balance_usd DECIMAL(18, 2) DEFAULT 10000.00
);


CREATE TABLE IF NOT EXISTS cryptocurrencies (
    symbol VARCHAR(10) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    rank INT NOT NULL,
    logo_url TEXT,
    kraken_pair VARCHAR(20) NOT NULL
);


CREATE TABLE IF NOT EXISTS wallet_balances (
    user_id BIGINT NOT NULL,
    coin_symbol VARCHAR(10) NOT NULL,
    quantity DECIMAL(18, 8) NOT NULL DEFAULT 0,
    PRIMARY KEY (user_id, coin_symbol),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (coin_symbol) REFERENCES cryptocurrencies(symbol) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS transactions (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    coin_symbol VARCHAR(10) NOT NULL,
    type VARCHAR(10) NOT NULL CHECK (type IN ('BUY', 'SELL')),
    quantity DECIMAL(18, 8) NOT NULL,
    price_at_time DECIMAL(18, 2) NOT NULL,
    total_value DECIMAL(18, 2) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (coin_symbol) REFERENCES cryptocurrencies(symbol) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS price_snapshots (
    id SERIAL PRIMARY KEY,
    coin_symbol VARCHAR(10) NOT NULL,
    price DECIMAL(18, 2) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (coin_symbol) REFERENCES cryptocurrencies(symbol) ON DELETE CASCADE
);
