# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Simple Shopping is a fully offline Android shopping list app. Users create lists organized by section headers (Produce, Dairy, etc.), toggle between Create mode (add/edit items) and Shopping mode (cross items off), and can mark items as recurring so they persist across trips.

## Build Commands

```bash
# JAVA_HOME and Android SDK tools (adb, emulator) are on PATH via ~/.bashrc
./gradlew assembleDebug    # Build debug APK
./gradlew assembleRelease  # Build release APK
./gradlew clean            # Clean build
./gradlew installDebug     # Install on connected device/emulator
```

## Emulator & Device Commands

`adb` and `emulator` are on PATH (configured in `~/.bashrc`). `JAVA_HOME` is also set there.

```bash
# Launch emulator (AVD name: Medium_Phone_API_36.1)
emulator -avd Medium_Phone_API_36.1 &

# Install and launch app
./gradlew installDebug
adb shell am start -n com.simpleshopping/.MainActivity

# Restart app (force-stop then launch)
adb shell am force-stop com.simpleshopping
adb shell am start -n com.simpleshopping/.MainActivity

# View logcat (filtered to app)
adb logcat -d --pid=$(adb shell pidof -s com.simpleshopping)

# Clear app data (wipes database — triggers prepopulation on next launch)
adb shell pm clear com.simpleshopping
```

## Workflow

After every feature addition or bug fix, **always build and install** the updated APK on the emulator so the user can immediately test:

```bash
./gradlew installDebug
```

## Tech Stack

- **Language:** Kotlin 2.0.21 with JVM target 17
- **Min SDK:** 26 (Android 8.0) / **Target SDK:** 35 (Android 15)
- **Build:** Gradle 8.11.1 with Kotlin DSL (.kts), AGP 8.7.3, KSP 2.0.21-1.0.28
- **UI:** View Binding, Material Design 3, CoordinatorLayout + RecyclerView + FAB
- **Persistence:** Room 2.8.4 (4 tables: sections, items, item_history, trip_snapshot)
- **Architecture:** MVVM — ViewModel + StateFlow, single Activity, no fragments for navigation
- **Theme:** Custom attribute system (`attrs.xml`) with notepad theme overlay. Handwritten font (Patrick Hand).

## Architecture

- Single-module project (`app/`), package: `com.simpleshopping`
- **MainActivity** — hosts RecyclerView, toolbar with mode toggle, FAB for adding items
- **ShoppingListViewModel** — combines section+item Room Flows into flat list, manages app mode (CREATE/SHOPPING) and sort mode (MANUAL/STORE_ROUTE)
- **Room database** (`ShoppingDatabase`) — singleton with prepopulated default sections
- **ShoppingRepository** — wraps 4 DAOs (SectionDao, ItemDao, ItemHistoryDao, TripSnapshotDao)
- **ShoppingListAdapter** — multi-view-type ListAdapter using `ListItem` sealed class (SectionHeader | ShoppingItem)
- **ThemeManager** — SharedPreferences-backed theme switcher, applied before `setContentView()`
- **NotepadItemDecoration** — draws ruled blue lines + red margin on RecyclerView

### Key Packages

- `data/` — Room entities, DAOs, database, repository
- `adapter/` — ListItem sealed class, ShoppingListAdapter
- `theme/` — ThemeManager
- `dialog/` — AddItemDialogFragment (with autocomplete), AddSectionDialogFragment

### Database Schema

- `sections` — id, name, sort_order, is_default
- `items` — id, section_id (FK), name, is_checked, is_recurring, sort_order, check_position
- `item_history` — id, name, section_id (FK), usage_count, last_check_position (for autocomplete + store route)
- `trip_snapshot` — id, item_name, section_id (FK), was_recurring (for "repeat last trip")

### Theme System

Custom attributes in `attrs.xml` (notepadBackground, notepadLineColor, notepadTextColor, etc.) allow new themes by adding a `<style>` block in `themes.xml`. Currently ships with `Theme.SimpleShopping.Notepad`.
