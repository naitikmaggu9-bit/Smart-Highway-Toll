# Smart Highway Toll & Automated Wallet Manager

A **100% console-based Core Java application** developed for a 3rd-semester BTech academic project.  
The application simulates an automated electronic toll collection plaza (FASTag style) with prepaid digital wallets, vehicle category pricing, security blacklisting, multithreaded toll lane simulations, Stream API analytics, and permanent text-file data storage.

---

## Academic Compliance & Constraints

1. **100% Console-Based**: Runs entirely inside the terminal with an interactive, robust menu-driven interface.
2. **Core Java Only**: Built strictly with standard Java built-in packages (`java.util`, `java.io`, `java.nio`, `java.time`, `java.lang`).
3. **Zero External Dependencies**: No Maven, no Gradle, no Spring Boot, no Hibernate, and no external libraries.
4. **No JDBC / External Database Drivers**: Fully replaced with standard Java File I/O text files.
5. **Permanent Data Storage**: Data is saved to readable text files (`vehicles.txt`, `wallets.txt`, `transactions.txt`, `blacklist.txt`, and generated receipt files in `receipts/`).
6. **Zero AI / LLM APIs**: 100% deterministic, beginner-friendly academic Java code.
7. **Strict 32-Class Distribution**: Exactly **32 Java classes**, partitioned into **4 members with exactly 8 classes per member**.

---

## Project Structure

```
Toll Plaza/
├── compile.bat                  # 1-Click Windows compiler
├── run.bat                      # 1-Click Windows launcher
├── compile.sh                   # Linux/macOS compiler script
├── run.sh                       # Linux/macOS launcher script
├── sources.txt                  # List of all 32 source files
├── TEAM_DISTRIBUTION.md         # Comprehensive 4-member 8-class matrix
├── README.md                    # Project documentation & run guide
│
├── src/
│   └── tollplaza/
│       ├── member1/             # Member 1: Vehicle & Blacklist Module (8 classes)
│       │   ├── Vehicle.java
│       │   ├── Car.java
│       │   ├── Bike.java
│       │   ├── Truck.java
│       │   ├── BlacklistManager.java
│       │   ├── VehicleRegistry.java
│       │   ├── VehicleFileManager.java
│       │   └── InvalidVehicleDataException.java
│       │
│       ├── member2/             # Member 2: Prepaid Wallet & Payments (8 classes)
│       │   ├── Wallet.java
│       │   ├── Transaction.java
│       │   ├── PaymentProcessor.java
│       │   ├── AutomatedTollDeductor.java
│       │   ├── WalletManager.java
│       │   ├── WalletFileManager.java
│       │   ├── TransactionFileManager.java
│       │   └── InsufficientBalanceException.java
│       │
│       ├── member3/             # Member 3: Toll & Lane Simulation (8 classes)
│       │   ├── TollCalculatable.java
│       │   ├── StandardTollCalculator.java
│       │   ├── LaneQueue.java
│       │   ├── TollBoothLane.java
│       │   ├── VehicleArrivalSimulator.java
│       │   ├── LaneSimulationManager.java
│       │   ├── VehiclePlateComparator.java
│       │   └── VehicleBlacklistedException.java
│       │
│       └── member4/             # Member 4: Analytics, Receipts & UI (8 classes)
│           ├── Receipt.java
│           ├── ReceiptGenerator.java
│           ├── TollAnalytics.java
│           ├── TransactionHistoryViewer.java
│           ├── TollAmountComparator.java
│           ├── FileStorageManager.java
│           ├── TollPlazaManager.java
│           └── MainApp.java
│
├── bin/                         # Compiled bytecode (.class files)
└── data/                        # Permanent Text File Storage
    ├── vehicles.txt             # Registered fleet data
    ├── wallets.txt              # Prepaid wallet accounts
    ├── transactions.txt         # Audit trail log
    ├── blacklist.txt            # Security blacklisted vehicle plates
    └── receipts/                # Individual printed toll receipt files (.txt)
        └── RCP-5001.txt
```

---

## 4-Member Team Allocation (8 Classes Each)

| Member | Module | Classes |
|:---|:---|:---|
| **Member 1** | Vehicle & Blacklist Management | `Vehicle`, `Car`, `Bike`, `Truck`, `BlacklistManager`, `VehicleRegistry`, `VehicleFileManager`, `InvalidVehicleDataException` |
| **Member 2** | Prepaid Wallet & Payment Processing | `Wallet`, `Transaction`, `PaymentProcessor`, `AutomatedTollDeductor`, `WalletManager`, `WalletFileManager`, `TransactionFileManager`, `InsufficientBalanceException` |
| **Member 3** | Toll Calculation & Multithreaded Simulation | `TollCalculatable`, `StandardTollCalculator`, `LaneQueue`, `TollBoothLane`, `VehicleArrivalSimulator`, `LaneSimulationManager`, `VehiclePlateComparator`, `VehicleBlacklistedException` |
| **Member 4** | Analytics, Receipts & Main Application | `Receipt`, `ReceiptGenerator`, `TollAnalytics`, `TransactionHistoryViewer`, `TollAmountComparator`, `FileStorageManager`, `TollPlazaManager`, `MainApp` |

*For a full breakdown of each class and its academic concepts, see [TEAM_DISTRIBUTION.md](file:///c:/Users/navde/OneDrive/Desktop/Toll%20Plaza/TEAM_DISTRIBUTION.md).*

---

## Core Java Concepts Implemented

- **OOP Principles**:
  - **Abstraction**: `Vehicle` (abstract class), `TollCalculatable` (interface), `PaymentProcessor` (interface).
  - **Encapsulation**: Private fields, robust getters/setters, defensive state handling.
  - **Inheritance & Polymorphism**: `Car`, `Bike`, `Truck` extend `Vehicle` with custom fee formulas; polymorphic strategy execution.
- **Method Overloading & Overriding**:
  - Overloading: `StandardTollCalculator.calculateToll(...)` (with peak surcharge & discount), `Wallet.recharge(amount, promoCode)`.
  - Overriding: Subclass fee calculation, `toString()`, `compareTo()`.
- **Custom Checked Exceptions**:
  - `InvalidVehicleDataException`: thrown on malformed vehicle inputs.
  - `InsufficientBalanceException`: thrown when wallet balance < toll fee.
  - `VehicleBlacklistedException`: triggered when a blacklisted vehicle enters a toll lane.
- **Java Collections Framework**:
  - `ArrayList`: dynamic ordered lists for fleet and lanes.
  - `LinkedList`: chronological transaction queue and regular lane FIFO queue.
  - `HashMap`: fast $O(1)$ lookup for vehicles and wallets by number plate.
  - `HashSet`: fast $O(1)$ blacklist membership verification.
  - `TreeSet`: sorted collection of issued toll receipts.
  - `PriorityQueue`: emergency and VIP vehicles (Ambulance, Fire, Police) automatically overtake regular traffic in toll queues.
- **Iterators & Traversal**:
  - `Iterator`: blacklist display and vehicle removal.
  - `ListIterator`: forward (`next()`) and reverse (`previous()`) audit log traversal.
- **Sorting & Ordering**:
  - `Comparable`: natural ordering for `Vehicle`, `Transaction`, `Receipt`.
  - `Comparator`: custom sorting with `VehiclePlateComparator` and `TollAmountComparator`.
- **Multithreading & Concurrency**:
  - `Thread` & `Runnable`: `TollBoothLane` and `VehicleArrivalSimulator` run concurrently as worker threads.
  - `wait()`, `notify()`, `notifyAll()`: producer-consumer synchronization in `LaneQueue`.
  - `synchronized`: thread-safe balance deductions and wallet recharges.
- **Stream API & Lambda Expressions**:
  - In `TollAnalytics`: `filter()`, `mapToDouble()`, `sum()`, `average()`, `max()`, `Collectors.groupingBy()`, `Collectors.counting()`, `Collectors.summingDouble()`.
- **File I/O Persistence**:
  - `BufferedReader`, `PrintWriter`, `FileWriter`, `FileReader` reading and writing human-readable `.txt` files.

---

## Compilation & Execution Instructions

### Option 1: Using Batch Scripts (Windows - Recommended)
1. Double-click `compile.bat` to compile all 32 classes.
2. Double-click `run.bat` to launch the interactive console application.

### Option 2: Command Line (Windows PowerShell / CMD)
```cmd
# Navigate to the project root directory
cd "c:\Users\navde\OneDrive\Desktop\Toll Plaza"

# 1. Compile all 32 classes into bin directory
javac -d bin src/tollplaza/member1/*.java src/tollplaza/member2/*.java src/tollplaza/member3/*.java src/tollplaza/member4/*.java

# 2. Run the application
java -cp bin tollplaza.member4.MainApp
```

### Option 3: Linux / macOS
```bash
chmod +x compile.sh run.sh
./compile.sh
./run.sh
```

---

## Sample Console Execution Flow

### 1. Main Menu
```text
******************************************************************************************
*              SMART HIGHWAY TOLL & AUTOMATED WALLET MANAGER                             *
*            BTech 3rd Sem Core Java Academic Project (Console Edition)                  *
*           Zero External Libraries | File I/O Storage | Multithreading                  *
******************************************************************************************

================================ MAIN MENU ===============================================
  1. Vehicle Registration & Fleet Management (Member 1)
  2. Prepaid FASTag Wallet Operations (Member 2)
  3. Manual Toll Booth Crossing & Receipt Generation (Member 3 & 4)
  4. Vehicle Blacklist Management (Member 1)
  5. Multi-Lane Concurrent Simulation [Multithreading] (Member 3)
  6. Toll Collection Analytics Dashboard [Stream API] (Member 4)
  7. Transaction Audit & Bidirectional History [Iterator] (Member 2 & 4)
  8. File Storage Status & Generated Receipts Inspection (Member 4)
  9. Seed / Refresh Academic Demo Data
  0. Save & Exit Program
==========================================================================================
```

### 2. Vehicle Registration & Fleet Inspection
```text
==========================================================================================
                                REGISTERED HIGHWAY FLEET                                  
==========================================================================================
 1. [CAR] Plate: DL01AB1234 | Owner: Rahul Sharma    | Type: CAR    | Reg: 2025-01-10 | Emergency: NO | Seats: 5 | Commercial: NO | Standard Toll: Rs. 100.00
 2. [CAR] Plate: MH02CD5678 | Owner: Priya Verma     | Type: CAR    | Reg: 2025-03-15 | Emergency: NO | Seats: 4 | Commercial: YES | Standard Toll: Rs. 130.00
 3. [CAR] Plate: KA03EF9012 | Owner: Emergency Ambulance | Type: CAR | Reg: 2025-02-20 | Emergency: YES | Seats: 6 | Commercial: NO | Standard Toll: Rs. 0.00
 4. [BIKE] Plate: PB65CD1122 | Owner: Simran Kaur     | Type: BIKE   | Reg: 2025-04-05 | Emergency: NO | Engine: 150 cc | Standard Toll: Rs. 50.00
 5. [BIKE] Plate: CH01GH3344 | Owner: Arjun Singh     | Type: BIKE   | Reg: 2025-05-12 | Emergency: NO | Engine: 650 cc | Standard Toll: Rs. 70.00
 6. [TRUCK] Plate: HR26IJ5566 | Owner: National Logistics | Type: TRUCK | Reg: 2024-11-01 | Emergency: NO | Axles: 4 | Weight: 18.5 tons | Standard Toll: Rs. 462.50
 7. [TRUCK] Plate: RJ14KL7788 | Owner: Express Cargo   | Type: TRUCK  | Reg: 2024-12-18 | Emergency: NO | Axles: 2 | Weight: 8.0 tons | Standard Toll: Rs. 200.00
------------------------------------------------------------------------------------------
Total Vehicles Registered: 7
```

### 3. Toll Receipt Generated via File I/O (`data/receipts/RCP-5001.txt`)
```text
************************************************************
          NATIONAL HIGHWAYS TOLL PLAZA AUTHORITY            
             ELECTRONIC TOLL COLLECTION RECEIPT             
************************************************************
 Receipt Number    : RCP-5001
 Date & Time       : 2026-11-18 20:13:45
 Lane ID           : BOOTH-MANUAL
 Vehicle Plate     : DL01AB1234
 Vehicle Category  : CAR
------------------------------------------------------------
 TOLL FEE DEDUCTED : Rs. 100.00
 REMAINING BALANCE : Rs. 950.00
------------------------------------------------------------
 Payment Status    : PAID VIA AUTOMATED FASTAG WALLET
 Wish you a safe and pleasant journey!
************************************************************
```

### 4. Multithreaded Lane Simulation with PriorityQueue
```text
==========================================================================================
          STARTING MULTITHREADED TOLL SIMULATION (3 LANES, 6 VEHICLES)           
==========================================================================================
[LANE-01 - FASTag Express] Lane operational on thread: Thread-LANE-01
[LANE-02 - Commercial Heavy] Lane operational on thread: Thread-LANE-02
[LANE-03 - Standard Multi-Axle] Lane operational on thread: Thread-LANE-03
[SIMULATOR] Vehicle arrival simulation initiated for 6 vehicles.
  -> [VEHICLE ARRIVED] KA03EF9012 approached the toll plaza gates.
  [LANE QUEUE] >>> EMERGENCY PRIORITY: KA03EF9012 jumped the queue!
[LANE-01] Processing vehicle: KA03EF9012 (CAR)...
  [LANE-01 SUCCESS] Toll Rs. 0.00 deducted for KA03EF9012. Wallet Balance: Rs. 0.00
  -> [VEHICLE ARRIVED] HR26IJ5566 approached the toll plaza gates.
[LANE-02] Processing vehicle: HR26IJ5566 (TRUCK)...
  [LANE-02 SUCCESS] Toll Rs. 462.50 deducted for HR26IJ5566. Wallet Balance: Rs. 1037.50
==========================================================================================
                         SIMULATION SUMMARY REPORT                                        
==========================================================================================
  Lane: LANE-01  (FASTag Express      ) | Vehicles:  3 | Revenue: Rs.   100.00
  Lane: LANE-02  (Commercial Heavy    ) | Vehicles:  2 | Revenue: Rs.   562.50
  Lane: LANE-03  (Standard Multi-Axle ) | Vehicles:  1 | Revenue: Rs.   130.00
------------------------------------------------------------------------------------------
  TOTAL PROCESSED: 6 vehicles | TOTAL REVENUE GENERATED: Rs. 792.50
==========================================================================================
```

### 5. Toll Collection Analytics Dashboard (Java Stream API)
```text
==========================================================================================
                     HIGHWAY TOLL ANALYTICS DASHBOARD (STREAM API)                        
==========================================================================================
  Total Toll Revenue Collected : Rs.    1155.00
  Total Wallet Recharges       : Rs.    1800.00
  Successful Toll Crossings    :              6
  Failed Transaction Attempts  :              1
  Average Toll per Vehicle     : Rs.     192.50
  Highest Single Toll Charged  : Rs.     462.50
------------------------------------------------------------------------------------------
  REVENUE BREAKDOWN BY VEHICLE CATEGORY (Collectors.groupingBy):
   - CAR      :   4 vehicles | Revenue: Rs.   330.00
   - BIKE     :   0 vehicles | Revenue: Rs.     0.00
   - TRUCK    :   2 vehicles | Revenue: Rs.   825.00
==========================================================================================
```

---

## File Storage Format Details

All data files are stored under the `data/` directory in standard delimiter-separated plain text:

- **`vehicles.txt`**:  
  `TYPE|PLATE|OWNER|REG_DATE|IS_EMERGENCY|EXTRA1|EXTRA2`  
  *(e.g., `CAR|DL01AB1234|Rahul Sharma|2025-01-10|false|5|false`)*
- **`wallets.txt`**:  
  `WALLET_ID|PLATE|BALANCE|ACTIVE`  
  *(e.g., `W-1001|DL01AB1234|950.00|true`)*
- **`transactions.txt`**:  
  `TXN_ID|WALLET_ID|PLATE|CATEGORY|AMOUNT|TYPE|STATUS|TIMESTAMP|REMARKS`  
  *(e.g., `TXN-REC-25928|W-1001|DL01AB1234|GENERAL|550.00|WALLET_RECHARGE|SUCCESS|2026-11-18 20:13:45|Recharged Rs. 500.0 + Bonus Rs. 50.0 [FASTAG10]`)*
- **`blacklist.txt`**:  
  `PLATE|REASON`  
  *(e.g., `DL09ZZ9999|Reported Stolen by Delhi Traffic Police`)*
- **`receipts/`**:  
  Houses individual `.txt` receipts generated per toll payment transaction.
