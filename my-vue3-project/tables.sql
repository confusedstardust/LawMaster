-- 创建数据库
CREATE DATABASE IF NOT EXISTS my_project;

-- 使用数据库
USE my_project;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(50),
    avatar VARCHAR(255),
    role ENUM('admin', 'user') DEFAULT 'user',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 文章表
CREATE TABLE IF NOT EXISTS posts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    images JSON,
    tags JSON,
    views INT DEFAULT 0,
    likes INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 评论表
CREATE TABLE IF NOT EXISTS comments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT,
    user_id INT,
    content TEXT,
    likes INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 收藏表
CREATE TABLE IF NOT EXISTS favorites (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    post_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE
);

-- 关注表
CREATE TABLE IF NOT EXISTS follows (
    id INT AUTO_INCREMENT PRIMARY KEY,
    follower_id INT,
    followed_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (follower_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (followed_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 验证码表
CREATE TABLE IF NOT EXISTS verification_codes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    phone VARCHAR(15) NOT NULL,
    code VARCHAR(10) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP
);

-- 题目表
CREATE TABLE IF NOT EXISTS questions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    text TEXT NOT NULL,
    options JSON,
    correct_answer VARCHAR(255),
    category ENUM('民法', '刑法', '商业法', '劳动法', '房产法') NOT NULL,
    type ENUM('选择题', '填空题', '判断题') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 答题记录表
CREATE TABLE IF NOT EXISTS quiz_records (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    question_id INT,
    selected_option VARCHAR(255),
    is_correct BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE
);


CREATE TABLE categories (
    id INT AUTO_INCREMENT PRIMARY KEY,  -- 分类 ID
    name VARCHAR(255) NOT NULL,         -- 分类名称，例如：商业法律、民法、刑法
    description TEXT                    -- 分类描述
);
CREATE TABLE questions (
    id INT AUTO_INCREMENT PRIMARY KEY,  -- 题目 ID
    category_id INT,                    -- 题目分类 ID，外键关联到 categories 表
    question_text TEXT NOT NULL,         -- 题目内容
    option_a VARCHAR(255),               -- 选项 A
    option_b VARCHAR(255),               -- 选项 B
    option_c VARCHAR(255),               -- 选项 C
    option_d VARCHAR(255),               -- 选项 D
    correct_option CHAR(1) NOT NULL,     -- 正确答案选项（'A', 'B', 'C', 'D'）
    FOREIGN KEY (category_id) REFERENCES categories(id) -- 外键关联分类
);
CREATE TABLE user_answers (
    id INT AUTO_INCREMENT PRIMARY KEY,   -- 答题记录 ID
    user_id INT,                         -- 用户 ID，外键关联到 users 表
    question_id INT,                     -- 题目 ID，外键关联到 questions 表
    selected_option CHAR(1),             -- 用户选择的答案选项（'A', 'B', 'C', 'D'）
    is_correct BOOLEAN,                  -- 是否答对
    answered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 答题时间
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (question_id) REFERENCES questions(id)
);
CREATE TABLE incorrect_answers (
    id INT AUTO_INCREMENT PRIMARY KEY,   -- 错题记录 ID
    user_id INT,                         -- 用户 ID，外键关联到 users 表
    question_id INT,                     -- 题目 ID，外键关联到 questions 表
    selected_option CHAR(1),             -- 用户选择的答案选项
    correct_option CHAR(1),              -- 正确的答案选项
    answered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 错题时间
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (question_id) REFERENCES questions(id)
);
