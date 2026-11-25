# TEAM DISTRIBUTION & CLASS RESPONSIBILITY MATRIX

### Academic Project: Smart Highway Toll & Automated Wallet Manager
**Target Level**: 3rd Semester BTech (Core Java)  
**Total Team Members**: 4  
**Total Java Classes**: Exactly 32 Classes (8 Classes Per Member)  
**Architecture Principle**: 100% Core Java, Zero External Dependencies, 100% Console UI, Text File Storage (Replacing Traditional Database/JDBC).

---

## High-Level Distribution Summary

| Team Member | Module Name | Package | Allocated Classes | Key Java Concepts |
|:---|:---|:---|:---:|:---|
| **Member 1** | **Vehicle & Blacklist Management** | `tollplaza.member1` | **8 Classes** | Abstraction, Inheritance, Polymorphism, `HashSet`, `HashMap`, `ArrayList`, `Iterator`, File I/O, Custom Exception |
| **Member 2** | **Prepaid FASTag Wallet & Payments** | `tollplaza.member2` | **8 Classes** | Interfaces, Method Overloading, Thread Synchronization, `LinkedList`, `Comparable`, Append File I/O, Custom Exception |
| **Member 3** | **Toll Calculation & Lane Simulation** | `tollplaza.member3` | **8 Classes** | Multithreading (`Thread`, `Runnable`), `wait()`, `notify()`, `notifyAll()`, `PriorityQueue`, `Comparator`, Thread Lifecycle |
| **Member 4** | **Analytics, Receipts & Main Interface** | `tollplaza.member4` | **8 Classes** | Stream API & Lambdas, `TreeSet`, `ListIterator` (Bidirectional Traversal), `Comparator`, File Storage Management, Facade Pattern, Console UI |

---

## Detailed Member Breakdown

### Member 1: Vehicle & Blacklist Management Module
**Package**: `tollplaza.member1` (8 Classes)  
**Module Focus**: Highway fleet domain modeling, category-specific calculations, security blacklist validation, and vehicle file persistence.

1. **`Vehicle.java`** *(Abstract Base Class)*
   - **Role**: Base entity for all highway vehicles (`plateNumber`, `ownerName`, `vehicleType`, `registrationDate`, `isEmergency`).
   - **Concepts**: Encapsulation, Abstraction, implements `Comparable<Vehicle>` for natural plate sorting. Contains abstract `calculateStandardToll()`.

2. **`Car.java`** *(Concrete Subclass)*
   - **Role**: Models standard cars and commercial taxis (`seatingCapacity`, `isCommercial`).
   - **Concepts**: Inheritance (`extends Vehicle`), Method Overriding (`calculateStandardToll()`), super constructor delegation.

3. **`Bike.java`** *(Concrete Subclass)*
   - **Role**: Models motorcycles and superbikes (`engineCapacityCC`).
   - **Concepts**: Inheritance, Method Overriding with engine displacement pricing logic.

4. **`Truck.java`** *(Concrete Subclass)*
   - **Role**: Models commercial multi-axle freight vehicles (`axleCount`, `loadWeightTons`).
   - **Concepts**: Inheritance, Method Overriding with axle charge and overweight surcharge calculation.

5. **`BlacklistManager.java`** *(Manager Class)*
   - **Role**: Maintains security blacklist of stolen/violating vehicles.
   - **Concepts**: `HashSet<String>` for $O(1)$ fast containment checks, `Iterator<String>` traversal, File I/O saving/loading `data/blacklist.txt`.

6. **`VehicleRegistry.java`** *(Manager Class)*
   - **Role**: In-memory registry of all registered fleet vehicles.
   - **Concepts**: `HashMap<String, Vehicle>` for key-value plate lookups, `ArrayList<Vehicle>` for indexed iteration.

7. **`VehicleFileManager.java`** *(Persistence Class - Replaces VehicleDAO/DB)*
   - **Role**: Handles permanent text-file read/write operations for vehicle records (`data/vehicles.txt`).
   - **Concepts**: File I/O (`BufferedReader`, `PrintWriter`, `FileWriter`), string parsing with delimiter splitting.

8. **`InvalidVehicleDataException.java`** *(Custom Exception)*
   - **Role**: Checked custom exception thrown when vehicle registration data is missing, malformed, or invalid.
   - **Concepts**: Custom Exception Handling (`extends Exception`).

---

### Member 2: Prepaid Wallet & Payment Processing Module
**Package**: `tollplaza.member2` (8 Classes)  
**Module Focus**: FASTag wallet accounts, promotional recharges, automatic payment deductions, balance validation, and transaction persistence.

9. **`Wallet.java`** *(Entity Class)*
   - **Role**: Models a vehicle's prepaid FASTag wallet (`walletId`, `vehiclePlate`, `balance`, `active`).
   - **Concepts**: Encapsulation, Method Overloading (`recharge(double)` vs `recharge(double, String)`), Thread Synchronization (`synchronized` methods).

10. **`Transaction.java`** *(Entity Class)*
    - **Role**: Represents an individual financial audit record (`transactionId`, `walletId`, `vehiclePlate`, `amount`, `type`, `status`, `timestamp`).
    - **Concepts**: Implements `Comparable<Transaction>` (chronological ordering), Wrapper Classes, DateTime formatting.

11. **`PaymentProcessor.java`** *(Interface)*
    - **Role**: Contract defining toll deduction and balance validation specifications.
    - **Concepts**: Abstraction, Interface design.

12. **`AutomatedTollDeductor.java`** *(Service Class)*
    - **Role**: Concrete implementation of `PaymentProcessor` for automated RFID FASTag deductions.
    - **Concepts**: Interface implementation, Polymorphism, atomic transaction ID generation, validation logic.

13. **`WalletManager.java`** *(Manager Class)*
    - **Role**: Manages active wallets and in-memory transaction audit logs.
    - **Concepts**: `HashMap<String, Wallet>` for wallet lookup by plate, `LinkedList<Transaction>` for sequential transaction history.

14. **`WalletFileManager.java`** *(Persistence Class - Replaces WalletDAO/DB)*
    - **Role**: Permanently saves and loads wallet states to/from `data/wallets.txt`.
    - **Concepts**: File I/O (`BufferedReader`, `PrintWriter`, `FileWriter`).

15. **`TransactionFileManager.java`** *(Persistence Class - Replaces TransactionDAO/DB)*
    - **Role**: Appends completed transactions to the persistent audit log file `data/transactions.txt`.
    - **Concepts**: File appending (`FileWriter(file, true)`), `BufferedReader` recovery.

16. **`InsufficientBalanceException.java`** *(Custom Exception)*
    - **Role**: Custom checked exception thrown when a wallet has insufficient balance for toll deduction.
    - **Concepts**: Custom Exception Handling, records wallet ID, current balance, and shortage amount.

---

### Member 3: Toll Calculation, Lane Simulation & Multithreading Module
**Package**: `tollplaza.member3` (8 Classes)  
**Module Focus**: Dynamic toll pricing, thread-safe lane queues with wait/notify, concurrent toll booth workers, and traffic simulation.

17. **`TollCalculatable.java`** *(Interface)*
    - **Role**: Interface defining standard and overloaded toll fee calculation contracts.
    - **Concepts**: Abstraction, Method Overloading in interfaces.

18. **`StandardTollCalculator.java`** *(Service Class)*
    - **Role**: Calculates toll fees by vehicle type, with exemptions for emergency vehicles and surge pricing.
    - **Concepts**: Polymorphism, Method Overriding, Method Overloading (peak-hour surge and promotional discounts).

19. **`LaneQueue.java`** *(Shared Synchronized Resource)*
    - **Role**: Producer-consumer vehicle queue buffer connecting arrival traffic to toll booth lanes.
    - **Concepts**: Inter-thread Communication (`wait()`, `notify()`, `notifyAll()`), Thread Synchronization (`synchronized`), `PriorityQueue` (Emergency vehicles jump queue), `LinkedList` (FIFO queue).

20. **`TollBoothLane.java`** *(Worker Thread Class)*
    - **Role**: Simulates a physical automated toll lane booth continuously processing vehicles.
    - **Concepts**: Multithreading (`implements Runnable`), Thread lifecycle, barrier cycle delay (`Thread.sleep()`), coordination with WalletManager & BlacklistManager.

21. **`VehicleArrivalSimulator.java`** *(Producer Thread Class)*
    - **Role**: Generates simulated incoming traffic approaching plaza gates at random intervals.
    - **Concepts**: Multithreading (`implements Runnable`), random arrival delays (`Thread.sleep()`).

22. **`LaneSimulationManager.java`** *(Controller Class)*
    - **Role**: Coordinates concurrent simulation across multiple lanes and arrival simulator.
    - **Concepts**: Thread orchestration, Thread lifecycle (`start()`, `join()`, `getState()`).

23. **`VehiclePlateComparator.java`** *(Utility Class)*
    - **Role**: Custom comparator sorting vehicles by emergency priority first, vehicle type second, plate third.
    - **Concepts**: `Comparator<Vehicle>` interface, custom multi-level sorting logic.

24. **`VehicleBlacklistedException.java`** *(Custom Exception)*
    - **Role**: Custom checked exception triggered when a blacklisted vehicle enters a toll booth.
    - **Concepts**: Custom Exception Handling, security alarm triggering.

---

### Member 4: Toll Analytics, Receipt Generation & Main System Interface Module
**Package**: `tollplaza.member4` (8 Classes)  
**Module Focus**: Stream API analytics, receipt text-file generation, bidirectional list iteration, storage orchestration, and console user interface.

25. **`Receipt.java`** *(Entity Class)*
    - **Role**: Represents an official printed highway toll receipt ticket (`receiptNumber`, `laneId`, `vehiclePlate`, `tollAmount`, `remainingBalance`, `issueTime`).
    - **Concepts**: Encapsulation, `Comparable<Receipt>` interface.

26. **`ReceiptGenerator.java`** *(Service & File I/O Class)*
    - **Role**: Formats and writes official ASCII toll receipts to disk in `data/receipts/RECEIPT_*.txt`.
    - **Concepts**: `TreeSet<Receipt>` (maintains natural sorted order of receipts), File I/O (`BufferedWriter`, `PrintWriter`).

27. **`TollAnalytics.java`** *(Analytics Service Class)*
    - **Role**: Generates statistical revenue and vehicle crossing analytics.
    - **Concepts**: Java 8+ Stream API (`filter()`, `mapToDouble()`, `sum()`, `average()`, `max()`), `Collectors.groupingBy()`, `Collectors.counting()`, `Collectors.summingDouble()`, Lambda Expressions.

28. **`TransactionHistoryViewer.java`** *(Audit Traversal Class)*
    - **Role**: Demonstrates forward and backward navigation through transaction audit logs.
    - **Concepts**: `Iterator<Transaction>`, `ListIterator<Transaction>` (`hasNext()`, `next()`, `hasPrevious()`, `previous()`).

29. **`TollAmountComparator.java`** *(Utility Class)*
    - **Role**: Custom comparator sorting transactions by monetary amount in ascending or descending order.
    - **Concepts**: `Comparator<Transaction>` interface.

30. **`FileStorageManager.java`** *(Infrastructure Class - Replaces DBConnection)*
    - **Role**: Verifies directory integrity, creates text files on initialization, and displays physical file health.
    - **Concepts**: File system management (`File`, `mkdirs`, `createNewFile`, `exists`).

31. **`TollPlazaManager.java`** *(Facade Orchestrator Class)*
    - **Role**: Facade pattern integrating subsystems from all 4 members into a single cohesive API.
    - **Concepts**: Facade Pattern, High Cohesion, Dependency Composition, System-wide Load & Save.

32. **`MainApp.java`** *(Console UI & Entry Point)*
    - **Role**: 100% interactive console-driven user interface with ASCII banners, sub-menus, and input validation.
    - **Concepts**: Console UI, `Scanner`, defensive input parsing, end-to-end integration.

---

## Academic Rubric Mapping Checklist

| Required Academic Concept | Implementing Classes |
|:---|:---|
| **OOP: Abstraction & Interfaces** | `Vehicle`, `TollCalculatable`, `PaymentProcessor` |
| **OOP: Inheritance & Polymorphism** | `Car`, `Bike`, `Truck` extending `Vehicle`; `AutomatedTollDeductor` implementing `PaymentProcessor`; `StandardTollCalculator` implementing `TollCalculatable` |
| **Method Overloading** | `StandardTollCalculator.calculateToll(...)`, `Wallet.recharge(...)` |
| **Method Overriding** | Subclass toll calculations in `Car`, `Bike`, `Truck`; `toString()`; `compareTo()` |
| **Custom Checked Exceptions** | `InvalidVehicleDataException`, `InsufficientBalanceException`, `VehicleBlacklistedException` |
| **Collections: ArrayList** | `VehicleRegistry`, `LaneSimulationManager`, `MainApp` |
| **Collections: LinkedList** | `WalletManager` (transactions log), `LaneQueue` (FIFO regular vehicle line) |
| **Collections: HashMap** | `VehicleRegistry` (plate $\to$ Vehicle), `WalletManager` (plate $\to$ Wallet), `BlacklistManager` |
| **Collections: HashSet** | `BlacklistManager` (unique set of blacklisted number plates for $O(1)$ lookup) |
| **Collections: TreeSet** | `ReceiptGenerator` (sorted receipt records) |
| **Collections: PriorityQueue** | `LaneQueue` (emergency/VIP vehicles prioritized over normal vehicles) |
| **Iterator & ListIterator** | `BlacklistManager`, `TransactionHistoryViewer` (forward `next()` & backward `previous()`) |
| **Comparable & Comparator** | `Vehicle`, `Transaction`, `Receipt` implement `Comparable`; `VehiclePlateComparator`, `TollAmountComparator` implement `Comparator` |
| **Multithreading** | `TollBoothLane` (Runnable), `VehicleArrivalSimulator` (Runnable), `Thread` creation |
| **Thread Synchronization** | `synchronized` methods in `Wallet`, `LaneQueue`, `VehicleRegistry`, `WalletManager` |
| **wait(), notify(), notifyAll()** | `LaneQueue.enqueueVehicle()` (`notifyAll()`), `LaneQueue.dequeueVehicle()` (`wait()`, `notifyAll()`) |
| **File I/O Storage (No DB)** | `VehicleFileManager`, `WalletFileManager`, `TransactionFileManager`, `ReceiptGenerator`, `FileStorageManager` |
| **Stream API & Lambdas** | `TollAnalytics` (`filter`, `mapToDouble`, `Collectors.groupingBy`, `Collectors.counting`, `Collectors.summingDouble`, `average`, `max`) |
