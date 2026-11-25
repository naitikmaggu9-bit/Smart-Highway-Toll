-- =====================================================================
-- SMART HIGHWAY TOLL PLAZA & FASTAG WALLET MANAGER
-- Database Schema Script (MySQL 8.0+)
-- =====================================================================

CREATE DATABASE IF NOT EXISTS tollplaza_db;
USE tollplaza_db;

-- 1. VEHICLE FLEET TABLE (Member 1: Vehicle & Blacklist Module)
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS wallets;
DROP TABLE IF EXISTS blacklist;
DROP TABLE IF EXISTS vehicles;

CREATE TABLE vehicles (
    plate_number VARCHAR(20) PRIMARY KEY,
    owner_name VARCHAR(100) NOT NULL,
    vehicle_type VARCHAR(20) NOT NULL,            -- CAR, BIKE, TRUCK
    registration_date DATE NOT NULL,
    is_emergency BOOLEAN DEFAULT FALSE,
    extra_field1 INT,                             -- Seating capacity (Car) / Engine CC (Bike) / Axles (Truck)
    extra_field2 DOUBLE,                          -- Commercial boolean (Car) / Load weight tons (Truck)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. PREPAID FASTAG WALLETS TABLE (Member 2: FASTag Wallet Module)
CREATE TABLE wallets (
    wallet_id VARCHAR(20) PRIMARY KEY,
    vehicle_plate VARCHAR(20) NOT NULL UNIQUE,
    balance DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    is_active BOOLEAN DEFAULT TRUE,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_wallet_vehicle FOREIGN KEY (vehicle_plate) 
        REFERENCES vehicles(plate_number) ON DELETE CASCADE
);

-- 3. FINANCIAL TRANSACTIONS AUDIT TRAIL TABLE (Member 2 & 4)
CREATE TABLE transactions (
    transaction_id VARCHAR(40) PRIMARY KEY,
    wallet_id VARCHAR(20) NOT NULL,
    vehicle_plate VARCHAR(20) NOT NULL,
    category VARCHAR(30) DEFAULT 'GENERAL',
    amount DECIMAL(10, 2) NOT NULL,
    type VARCHAR(30) NOT NULL,                    -- TOLL_PAYMENT, WALLET_RECHARGE
    status VARCHAR(20) NOT NULL,                  -- SUCCESS, FAILED
    timestamp DATETIME NOT NULL,
    remarks VARCHAR(255),
    CONSTRAINT fk_txn_wallet FOREIGN KEY (wallet_id) 
        REFERENCES wallets(wallet_id) ON DELETE CASCADE
);

-- 4. SECURITY BLACKLIST TABLE (Member 1: Vehicle & Blacklist Module)
CREATE TABLE blacklist (
    plate_number VARCHAR(20) PRIMARY KEY,
    reason VARCHAR(255) NOT NULL,
    flagged_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================================
-- SEED INITIAL ACADEMIC TEST DATA
-- =====================================================================

INSERT INTO vehicles (plate_number, owner_name, vehicle_type, registration_date, is_emergency, extra_field1, extra_field2) VALUES
('DL01AB1234', 'Rahul Sharma', 'CAR', '2025-01-10', FALSE, 5, 0),
('MH02CD5678', 'Priya Verma', 'CAR', '2025-03-15', FALSE, 4, 1),
('KA03EF9012', 'Emergency Ambulance', 'CAR', '2025-02-20', TRUE, 6, 0),
('PB65CD1122', 'Simran Kaur', 'BIKE', '2025-04-05', FALSE, 150, 0),
('CH01GH3344', 'Arjun Singh', 'BIKE', '2025-05-12', FALSE, 650, 0),
('HR26IJ5566', 'National Logistics', 'TRUCK', '2024-11-01', FALSE, 4, 18.50),
('RJ14KL7788', 'Express Cargo', 'TRUCK', '2024-12-18', FALSE, 2, 8.00);

INSERT INTO wallets (wallet_id, vehicle_plate, balance, is_active) VALUES
('W-1001', 'DL01AB1234', 500.00, TRUE),
('W-1002', 'MH02CD5678', 300.00, TRUE),
('W-1003', 'KA03EF9012', 0.00, TRUE),
('W-1004', 'PB65CD1122', 100.00, TRUE),
('W-1005', 'CH01GH3344', 100.00, TRUE),
('W-1006', 'HR26IJ5566', 1500.00, TRUE),
('W-1007', 'RJ14KL7788', 50.00, TRUE);

INSERT INTO blacklist (plate_number, reason) VALUES
('DL09ZZ9999', 'Reported Stolen by Delhi Traffic Police'),
('HR99XX0001', 'Multiple unpaid toll evasion notices issued by NHAI');
