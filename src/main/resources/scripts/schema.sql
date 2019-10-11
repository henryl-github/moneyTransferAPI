CREATE TABLE IF NOT EXISTS bank_account
(
    id IDENTITY,
    account_id LONG UNIQUE NOT NULL,
    account_owner_name VARCHAR(256) NOT NULL,
    balance DECIMAL(19,4) NOT NULL,
    currency_id INT NOT NULL,
);

CREATE TABLE IF NOT EXISTS transfer
(
    id IDENTITY,
    transfer_id LONG UNIQUE NOT NULL,
    from_account_id BIGINT NOT NULL,
    to_account_id BIGINT NOT NULL,
    amount DECIMAL(19,4) NOT NULL,
    currency_id INT NOT NULL,
    FOREIGN KEY(from_account_id) REFERENCES bank_account(id),
    FOREIGN KEY(to_account_id) REFERENCES bank_account(id),
)
