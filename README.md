# Game Project

This document describes the setup for the game project.

## Database Settings

### Create Database

To create the database, use the following SQL command:

```sql
-- CREATE DATABASE
CREATE DATABASE game;

-- CREATE TABLE
CREATE TABLE `game_sales` (
`id` bigint(20) NOT NULL,
`game_no` int NOT NULL,
`game_name` varchar(20) NOT NULL,
`game_code` varchar(5) NOT NULL,
`type` tinyint(1) NOT NULL COMMENT '1 for online, 2 for offline',
`cost_price` decimal(16,4) NOT NULL,
`tax` decimal(16,4) NOT NULL,
`sale_price` decimal(16,4) NOT NULL,
`date_of_sale` datetime NOT NULL,
`create_time` datetime NOT NULL,
PRIMARY KEY (`id`),
KEY `idx_game_no` (`game_no`) USING BTREE,
KEY `idx_date_of_sale` (`date_of_sale`) USING BTREE,
KEY `idx_sale_price` (`sale_price`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

