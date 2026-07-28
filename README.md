# Courier Tracking System

## Overview

A pure Java Swing desktop application for managing courier parcels. This academic mini-project demonstrates core OOP concepts, file-based persistence, and a layered architecture.

## Package Structure

- `com.courier.model`
  - `Courier.java` — parcel domain model with encapsulation.
  - `CourierStatus.java` — enum for parcel status with color coding.
  - `Priority.java` — enum for delivery priority.
- `com.courier.service`
  - `CourierService.java` — business logic and domain rules.
- `com.courier.persistence`
  - `CourierRepository.java` — abstraction for persistence.
  - `FileCourierRepository.java` — CSV-based file persistence.
- `com.courier.util`
  - `InputValidator.java` — reusable validation helpers.
  - `IdGenerator.java` — sequential tracking ID generator.
- `com.courier.ui`
  - `Main.java` — application entry point.
  - `MainFrame.java` — main window with tabbed navigation.
  - `BasePanel.java`, `BaseDialog.java` — abstract UI base classes.
  - `CourierTableModel.java` — table model for parcel records.
  - `StatusProgressPanel.java` — visual status progression component.
- `com.courier.ui.panels`
  - `DashboardPanel.java` — stats dashboard.
  - `BookingPanel.java` — parcel booking form.
  - `TrackingPanel.java` — parcel lookup and status update.
  - `AllRecordsPanel.java` — search, filter, sort, edit, delete records.
  - `EditDialog.java` — dialog for editing receiver/address/weight.

## How to Compile and Run

From the project root:

```powershell
cd c:\Users\DELL\Desktop\project
javac -d out src\**\*.java
java -cp out com.courier.Main
```

## Persistence

- Uses `data/couriers.csv` in the project directory.
- Loads all records on startup.
- If the file does not exist, demo data is preloaded.
- Saves on every change: booking, editing, status update, deletion.

## OOP Concepts Demonstrated

- Encapsulation: `Courier` fields are private; public getters/setters control access.
- Abstraction: `CourierRepository` interface separates persistence from `CourierService`.
- Inheritance / Polymorphism: `BasePanel` and `BaseDialog` are extended by concrete UI classes.
- Enums over magic strings: `CourierStatus` and `Priority` are enums.
- Exception handling: input and persistence errors are caught and displayed with dialogs.
- Collections: `ArrayList` is used in-memory for courier records, with filtering and sorting via streams.

## Notes

- The UI uses Nimbus LAF if available.
- The application is pure Java; no HTML/CSS/JavaScript or external frameworks are used.
- The service layer can be tested independently from the Swing UI.
