-- 초기 테스트 데이터 (비밀번호는 BCrypt로 인코딩된 값)
-- 원본 비밀번호: password123
INSERT INTO users (username, password, email, phone) VALUES 
('hong', '$2a$10$rjK8ZqHgJ0nVgXAjdHZ4.O5nDQJj7q7fC5z.xP4DQX8NNwXSfwkG6', 'hong@example.com', '010-1234-5678');

-- 원본 비밀번호: password456  
INSERT INTO users (username, password, email, phone) VALUES 
('kim', '$2a$10$X9qhGz7VqOuRsKnI5FwJ.eV8nQHjK9qO6tN3xM9SqF4LwXyZaBcDe', 'kim@example.com', '010-9876-5432');

-- 원본 비밀번호: admin123
INSERT INTO users (username, password, email, phone) VALUES 
('admin', '$2a$10$H3nK2bG8QzN7RjF9WsE4.eP5mQVjO7pR8vS6xL2TqE9MwYxZcFgHi', 'admin@example.com', '010-0000-0000');

-- 원본 비밀번호: user123
INSERT INTO users (username, password, email, phone) VALUES 
('lee', '$2a$10$Q4pL6cH9RzO8TkG2YwF5.eM7nRWkP8qS9uV7xN3UqG6NzXyZdFjKl', 'lee@example.com', '010-5555-1234');
