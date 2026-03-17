# List & Shop

The simple shopping list. Build your list, hit the store, done.

A fully offline Android shopping list app focused on speed and simplicity. No account, no internet, no ads, no tracking — your list lives on your phone and nowhere else.

## Features

- **Two modes** — *Create* mode to build your list, *Shopping* mode to cross items off as you go
- **Sections** — organize items into categories (Produce, Dairy, etc.) that you can reorder by dragging
- **Recurring items** — star items to keep them on your list trip after trip
- **Quantities** — tap an item to add more; long-press the quantity badge to subtract
- **Drag reorder** — drag handles on both items and sections for manual ordering
- **Context menus** — long-press any item to Edit, Move to another section, or Delete
- **Smart store route** — learns the order you walk the aisles and sorts your list to match
- **Repeat last trip** — restore your previous list with one tap
- **"I got it!" zone** — checked items slide into a separate section to keep the active list clean
- **Collapsible sections** — tap a section header in Shopping mode to collapse it
- **Autocomplete** — item name suggestions based on your history, with smart section assignment
- **Interactive tutorial** — a 9-step walkthrough on first launch (replayable from the menu)
- **Notepad theme** — handwritten font, ruled lines, and a yellow legal-pad aesthetic

## Tech Stack

- **Language:** Kotlin 2.0
- **Min SDK:** 26 (Android 8.0) / **Target SDK:** 35 (Android 15)
- **Architecture:** MVVM — ViewModel + StateFlow, single Activity
- **Persistence:** Room (offline-only, no network permissions)
- **UI:** Material Design 3, RecyclerView, View Binding

## Building

```bash
./gradlew assembleDebug    # debug APK
./gradlew assembleRelease  # release APK (requires signing config in local.properties)
./gradlew bundleRelease    # release AAB for Play Store
./gradlew installDebug     # build and install on connected device/emulator
```

## License

Source code is licensed under the [MIT License](LICENSE).

The [Patrick Hand](https://fonts.google.com/specimen/Patrick+Hand) font is licensed under the [SIL Open Font License](NOTICES).
