 CREATE TABLE user_wallet (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
	balance DECIMAL(19, 10), 
    cryptocurrency VARCHAR(255),
    user_id VARCHAR(255)
);
