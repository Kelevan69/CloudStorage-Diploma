INSERT INTO users (email, password_hash, role)
VALUES
    (
        'test1@mail.net',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', -- test1
        'USER'
    ),
    (
        'test2@mail.net',
        '$2a$10$qCA7JY6Z8QbR3XUv4fzKv.L7d6WjZ9Jz1UoGjKp9yRtN7Jm1lZ6O2', -- test2
        'USER'
    )
ON CONFLICT (email) DO NOTHING;