
# 🛒 ShopNova — Supermarket Management System

**A role-based Java desktop application that digitalises supermarket operations — billing, inventory, loyalty points, and receipt generation — with zero external dependencies.**

<br/>

[![Course](https://img.shields.io/badge/Course-CS--212%20OOP-blue?style=flat-square)](/)
[![Class](https://img.shields.io/badge/Class-BESE--16B-purple?style=flat-square)](/)
[![University](https://img.shields.io/badge/University-NUST%20SEECS-red?style=flat-square)](/)
[![Team](https://img.shields.io/badge/Team-Mudassir_Azma%20%7C%20Khizer_Hayyat-orange?style=flat-square)](/)

</div>

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Problem It Solves](#-problem-it-solves)
- [Features](#-features)
- [OOP Concepts Demonstrated](#-oop-concepts-demonstrated)
- [System Architecture](#-system-architecture)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
- [How to Use](#-how-to-use)
- [Data Files](#-data-files)
- [Auto-Discount Engine](#-auto-discount-engine)
- [Loyalty Programme](#-loyalty-programme)
- [Receipt Generation](#-receipt-generation)
- [Screenshots](#-screenshots)
- [Future Improvements](#-future-improvements)
- [Team](#-team)

---

## 🌟 Overview

ShopNova is an **offline-capable Java Swing desktop application** built for small-to-medium supermarkets. It replaces manual, paper-based operations with a clean dual-role system — one interface for the **Admin**, one for the **Cashier** — backed by plain-text CSV files that require no database server or internet connection.

> Built as the Final Project for **CS-212: Object Oriented Programming** at NUST SEECS, ShopNova demonstrates all nine core OOP principles in a real-world retail context.

---

## 🔍 Problem It Solves

| Problem | How ShopNova Fixes It |
|---|---|
| 🥛 **Untracked product expiry** | Auto-discount engine flags and discounts near-expiry stock in real time |
| 🧾 **Slow, error-prone checkout** | Digital cart system with automatic change calculation and digital receipts |
| 👤 **No customer retention** | Loyalty points programme — earn on every purchase, redeem on the next |
| 🔐 **No access controls** | Role-based login separates Admin and Cashier capabilities completely |
| 📊 **Zero sales analytics** | Admin dashboard shows live inventory health and per-cashier sales totals |

---

## ✨ Features

### 👨‍💼 Admin Panel
- **Live Dashboard** — stat cards for Total Products, Total Cashiers, Low Stock count, and Expiring Soon count
- **Inventory Management** — full CRUD with colour-coded status badges (🟢 OK / 🟡 LOW STOCK / 🔴 EXPIRED)
- **Cashier Management** — add or remove cashier accounts, view cumulative sales per cashier
- **Smart Product Merging** — adding a duplicate product merges quantities instead of creating a copy

### 🧑‍💻 Cashier Panel
- **Customer Lookup** — auto-creates a profile on first visit, loads returning customers instantly
- **Cart System** — add products by ID, view line totals with auto-discounts applied, remove items
- **Loyalty Redemption** — redeem points at checkout (1 point = Rs 1 off)
- **Change Calculator** — enter cash paid, get change instantly
- **Receipt Printing** — PDF via Java2D or HTML fallback, zero third-party libraries

### 🔧 System-Wide
- **Atomic File Writes** — crash-safe persistence using `Files.move(..., ATOMIC_MOVE)`
- **Auto-Expiry Discounts** — tiered discounts applied automatically based on days to expiry
- **No external dependencies** — pure Java 17 standard library throughout

---

## 🧠 OOP Concepts Demonstrated

| # | Concept | Where Applied |
|---|---|---|
| 1 | **Abstraction** | `Person` and `User` — abstract base classes with abstract `toFile()` and `getRole()` |
| 2 | **Inheritance** | `Customer extends Person`, `Cashier extends User` |
| 3 | **Encapsulation** | `Inventory` — private product list, exposed only through controlled public methods |
| 4 | **Polymorphism** | `toFile()` behaves differently in `Cashier` vs `Customer`; `getStatus()` is state-dependent |
| 5 | **Interface — Priceable** | `Product implements Priceable`; `CartItem` delegates total calculation via interface |
| 6 | **Interface — Persistable** | `Inventory` and `CashierManager` implement `load()`/`save()` uniformly |
| 7 | **Composition** | `Cashier` owns its `ArrayList<CartItem>` — list lives and dies with the cashier |
| 8 | **Aggregation** | `CashierFrame` holds references to `Cashier` and `Inventory` created elsewhere |
| 9 | **Constructor Overloading** | `Customer(name)` for new customers vs `Customer(name, purchase, points)` for loading from file |

---

## 🏗 System Architecture

```
Main
 └── LoginFrame
      ├── AdminFrame
      │    ├── Inventory          (implements Persistable)
      │    └── CashierManager     (implements Persistable)
      │
      └── CashierFrame
           ├── Cashier
           │    └── ArrayList<CartItem>
           │         └── Product  (implements Priceable)
           ├── Inventory
           ├── CustomerManager
           │    └── ArrayList<Customer>
           └── ReceiptPrinter
```

### Class Hierarchy

```
Person (abstract)          User (abstract)
  └── Customer               └── Cashier

Persistable (interface)    Priceable (interface)
  ├── Inventory               └── Product
  └── CashierManager
```

---

## 🛠 Tech Stack

| Technology | Purpose |
|---|---|
| **Java 17** | Core application logic and OOP implementation |
| **Java Swing** (`javax.swing`) | Full custom GUI — frames, panels, tables, dialogs |
| **`java.time`** (`LocalDate`, `ChronoUnit`) | Expiry date parsing and days-remaining calculation |
| **`java.awt.print`** (`PrinterJob`) | Java2D receipt printing to PDF or physical printer |
| **`java.nio.file`** (`Files.move`, `ATOMIC_MOVE`) | Crash-safe atomic writes for customer data |
| **IntelliJ IDEA** | Development environment and build configuration |

---

## 📁 Project Structure

```
ShopNova/
│
├── src/
│   ├── Main.java                  # Entry point — launches LoginFrame on EDT
│   │
│   ├── model/
│   │   ├── Person.java            # Abstract base — name + toFile()
│   │   ├── User.java              # Abstract base — credentials + getRole()
│   │   ├── Customer.java          # Extends Person — points, purchase history
│   │   ├── Cashier.java           # Extends User — cart, totalSales
│   │   ├── Product.java           # Implements Priceable — expiry, discount logic
│   │   └── CartItem.java          # Holds Product reference + qty
│   │
│   ├── interfaces/
│   │   ├── Persistable.java       # load() + save() contract
│   │   └── Priceable.java         # getPrice() + total(qty) contract
│   │
│   ├── manager/
│   │   ├── Inventory.java         # Product store — implements Persistable
│   │   ├── CashierManager.java    # Cashier store — implements Persistable
│   │   └── CustomerManager.java   # Customer store — atomic file writes
│   │
│   ├── ui/
│   │   ├── LoginFrame.java        # Branding + role-selection + routing
│   │   ├── AdminFrame.java        # Dashboard, inventory, cashier management
│   │   └── CashierFrame.java      # Checkout, cart, loyalty, receipt
│   │
│   └── util/
│       └── ReceiptPrinter.java    # Java2D PDF printing + HTML fallback
│
├── data/
│   ├── inventory.txt              # id,name,qty,category,expirable,expiry,price
│   ├── cashiers.txt               # username,password,totalSales
│   └── customers.txt              # name,totalPurchase,points
│
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites

- **Java 17** or higher installed
- **IntelliJ IDEA** (recommended) or any Java IDE

### Clone the Repository

```bash
git clone https://github.com/your-username/ShopNova.git
cd ShopNova
```

### Run in IntelliJ IDEA

1. Open IntelliJ IDEA → **File → Open** → select the `ShopNova` folder
2. Wait for the project to index
3. Navigate to `src/Main.java`
4. Click the **▶ Run** button or press `Shift + F10`

### Run from Terminal

```bash
# Compile
javac -d out src/**/*.java

# Run
java -cp out Main
```

> **Note:** The `data/` folder must exist in the working directory. If it doesn't, create it — the app will populate the files on first run.

---

## 🖥 How to Use

### Admin Login
```
Username: admin
Password: admin123
```

### Cashier Login
Use any cashier account added by the Admin through the **Add Cashier** panel.

### Typical Admin Flow
```
Login (Admin) → Dashboard → Add Product → Add Cashier → Monitor Inventory
```

### Typical Cashier Checkout Flow
```
Login (Cashier) → Checkout → Enter Customer Name → Add Products to Cart
→ (Optional) Redeem Points → Enter Cash Paid → Confirm Sale → Print Receipt
```

---

## 💾 Data Files

All data is stored as plain-text CSV in the `data/` folder. No database required.

### `inventory.txt`
```
id,name,qty,category,expirable,expiry,price
1,Milk,50,Dairy,true,2026-05-01,120.0
2,Rice,200,Grains,false,,85.0
```

### `cashiers.txt`
```
username,password,totalSales
ali,pass123,15400.0
sara,mypass,8200.0
```

### `customers.txt`
```
name,totalPurchase,points
Ahmed,4500.0,45
Fatima,12000.0,120
```

---

## 🏷 Auto-Discount Engine

ShopNova automatically applies tiered discounts to perishable products based on how close they are to their expiry date:

```java
long days = ChronoUnit.DAYS.between(LocalDate.now(), expiry);

if (days < 7)  return 0.50;  // 50% off — expires within a week
if (days < 15) return 0.30;  // 30% off — expires within two weeks
if (days < 30) return 0.10;  // 10% off — expires within a month
return 0.0;                  // no discount
```

These discounts are displayed in the cart table side-by-side with the original price, so the cashier and customer can clearly see the saving.

---

## 🎁 Loyalty Programme

| Action | Effect |
|---|---|
| Spend **Rs 100** | Earn **1 loyalty point** |
| Redeem **1 point** | Get **Rs 1 off** the current bill |
| Points persist | Carried over across all future visits |

Points are earned on the net total after discounts and are saved immediately to `customers.txt` after every confirmed sale.

---

## 🧾 Receipt Generation

ShopNova generates receipts without any external PDF library:

1. **Primary** — `java.awt.print.PrinterJob` renders the receipt via Java2D and saves it as a PDF through the OS print-to-PDF service
2. **Fallback** — if no print service is detected, an HTML receipt is generated and saved locally, which can be opened in any browser

A sample HTML receipt includes:
- Customer name, date, cashier name
- Itemised list with original price, discount applied, and line total
- Subtotal, points redeemed, **TOTAL**
- Cash paid and **Change**
- Points earned this visit and new loyalty balance

---

## 📸 Screenshots

| Screen | Description |
|---|---|
| **Login Frame** | Split layout — branding panel left, login form right |
| **Admin Dashboard** | Four stat cards + condensed inventory table |
| **Inventory Management** | Full JTable with colour-coded status badges |
| **Add Product Form** | Dynamic form — expiry field appears only when 'Expirable?' is checked |
| **Cashier Dashboard** | Personalised sidebar with cumulative sales card |
| **Checkout Screen** | Cart + loyalty + payment all in one view |
| **Receipt Output** | HTML thermal-style receipt rendered in browser |

> Screenshots are located in the `/screenshots` folder of this repository.

---

## 🔮 Future Improvements

| Improvement | Priority | Why |
|---|---|---|
| **SQLite backend** | 🔴 High | Replace CSV files for concurrent multi-cashier writes and SQL reporting |
| **JUnit 5 test suite** | 🔴 High | Unit + integration tests to prevent regressions |
| **Hashed passwords** | 🔴 High | BCrypt/PBKDF2 instead of plain-text storage |
| **Sales analytics charts** | 🔴 High | JFreeChart revenue trends and cashier performance graphs |
| **Barcode scanner support** | 🟡 Medium | USB scanners as keyboard input — no manual ID entry |
| **Apache PDFBox receipts** | 🟡 Medium | Consistent PDF output across all OS configurations |
| **Automated expiry alerts** | 🟡 Medium | Background daemon notifies admin of items expiring within 7 days |
| **Multi-branch sync** | 🟡 Medium | Spring Boot REST API for multiple store locations |
| **Customer mobile portal** | 🟢 Low | Web/Android app for loyalty balance and purchase history |
| **Supervisor role** | 🟢 Low | Read-only dashboard access — third User subclass |

---

## 👥 Team

| Member | Role | Responsibilities |
|---|---|---|
| **Mudassir_Azam** | Backend / Cashier UI Lead | Model layer (`Person`, `User`, `Cashier`, `Customer`, `Product`, `CartItem`), `CashierManager`, `Inventory`, `CashierFrame`, data file schema design |
| **Khizer_Hayyat** | Admin UI / Utilities Lead | `AdminFrame`, `CustomerManager`, `LoginFrame`, `ReceiptPrinter`, UML design, final report writing |

**Shared:** Integration testing, edge-case validation, cross-layer bug fixing, report review.

---

<div align="center">

**CS-212: Object Oriented Programming — BESE-16B**

*National University of Sciences and Technology (NUST) — SEECS*

*Submitted: April 26, 2026*

<br/>

⭐ If you found this project useful, consider giving it a star!

</div>
