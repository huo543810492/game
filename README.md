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

CREATE TABLE `csv_import_status` (
`id` bigint(20) NOT NULL,
`start_id` bigint(20) NOT NULL,
`end_id` bigint(20) NOT NULL,
`create_time` datetime NOT NULL,
PRIMARY KEY (`id`),
KEY `idx_start_end` (`start_id`, `end_id`) USING BTREE,
KEY `idx_create_time` (`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

### Database Connection Configuration
Configure your database connection settings in the application.yml. Update the MySQL username and password as per your setup. The project is tested with MySQL version 8.3.0.

## API Endpoints

### Data Generation
GET /generatorData: Generates 1 million records with random data. Data is flushed to the database using batch processing, taking an average of 25 seconds. Performance is enhanced by the Virtual Thread Pool.

### CSV Import
GET /import: Loads data from the demo CSV file game_sales.csv, which contains 1 million records. The CSVReaderBuilder reads lines and converts them to records in batches of 10k. Each batch insertion task is submitted asynchronously to the Virtual Thread Pool. This process also averages 25 seconds.

### Sales Data Retrieval
POST /getGameSales: Retrieves game sales records with pagination. Validates input form parameters to detect errors. This endpoint is efficient as it does not load all records into memory, averaging a response time of 400ms.

### Total Sales Calculation
POST /getTotalSales: Returns the total sales amount directly calculated in the database using a SUM query to avoid loading all records into memory. This operation takes approximately 100ms.

## Result Handling
The ResultMsg utility is used to return standardized response codes, messages, and data, facilitating an easy-to-understand API response.

## Exception Handling
GlobalExceptionHandler: A global exception handler is implemented to provide user-friendly error messages and handle unexpected exceptions, ensuring that all API responses are consistent and informative.
 
