-- 초기 테스트 데이터 (비밀번호는 BCrypt로 인코딩된 값)
-- 원본 비밀번호: password123
INSERT INTO users (username, password, email, phone) VALUES
    ('hong', '$2a$10$ceh87tmBoR1Zdfe1BrHjcOe4H96DVd66xxj7UU2eBTFxjcK9stbTi', 'hong@example.com', '010-1234-5678');

-- 원본 비밀번호: password456
INSERT INTO users (username, password, email, phone) VALUES
    ('kim', '$2a$10$YLR./8Kc7MhS3N13hXuGSeLxbNObn0kUJ/0i5ZO6Ot02Izym/iqu2', 'kim@example.com', '010-9876-5432');

-- 원본 비밀번호: admin123 (새로 생성된 해시로 교체 필요)
INSERT INTO users (username, password, email, phone) VALUES
    ('admin', '$2a$10$qbqk.q0R2PebzIFHwqJ/BeZy2z4l9SrQ7z9GNMcDb.9SAwU4pfCqW', 'admin@example.com', '010-0000-0000');

-- 원본 비밀번호: user123
INSERT INTO users (username, password, email, phone) VALUES
    ('lee', '$2a$10$FDhZsuShvLfM3TAV.eMEl.8NYjOWzrvaq2fsvauPgc.1kB2QfMBbi', 'lee@example.com', '010-5555-1234');