CREATE TABLE `user_info` (
                             `id` int NOT NULL,
                             `username` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
                             `age` int DEFAULT NULL,
                             `gender` int DEFAULT NULL,
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;