CREATE TABLE `user` (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        first_name VARCHAR(255),
                        last_name VARCHAR(255),
                        username VARCHAR(255) UNIQUE NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        role VARCHAR(255),
                        science_thread VARCHAR(255),
                        medical_thread VARCHAR(255)
);

-- Create token table
CREATE TABLE `token` (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         access_token VARCHAR(255),
                         refresh_token VARCHAR(255),
                         is_logged_out BOOLEAN DEFAULT FALSE,
                         user_id INT,
                         FOREIGN KEY (user_id) REFERENCES `user`(id) ON DELETE CASCADE
);

CREATE TABLE `chats` (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         chat_name VARCHAR(255),
                         is_group BOOLEAN DEFAULT FALSE,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `chat_members` (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                chat_id BIGINT,
                                user_id INT,
                                joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                FOREIGN KEY (chat_id) REFERENCES `chats`(id) ON DELETE CASCADE,
                                FOREIGN KEY (user_id) REFERENCES `user`(id) ON DELETE CASCADE
);

CREATE TABLE `messages` (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            chat_id BIGINT NOT NULL,
                            sender_id INT NOT NULL,
                            content TEXT NOT NULL,
                            sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (chat_id) REFERENCES `chats`(id) ON DELETE CASCADE,
                            FOREIGN KEY (sender_id) REFERENCES `user`(id) ON DELETE CASCADE
);

CREATE TABLE `message_receipts` (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    message_id BIGINT NOT NULL,
                                    user_id INT NOT NULL,
                                    read_at TIMESTAMP NULL,
                                    FOREIGN KEY (message_id) REFERENCES `messages`(id) ON DELETE CASCADE,
                                    FOREIGN KEY (user_id) REFERENCES `user`(id) ON DELETE CASCADE
);