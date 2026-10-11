# Fuel Buddy

A succinct Android app for recording vehicle mileage.

> **Status:** Alpha

## Features

- Add, view, edit, and delete vehicles.
- Add, view, edit, and delete vehicle mileage records.
- Add, view, edit, and delete attachments (receipt photos) on vehicle mileage records.
- Average MPG displayed on vehicle details screen **(WIP)**.
- Price per gallon displayed on the vehicle mileage details screen.
- Vehicle mileage records listed newest-first, non-exported records visually marked.
- Export mileage records as CSV or JSON via system share utilities (Email, SMS, etc).

## Requirements

- Android 8.1 (API 27) or higher.

## Tech Stack

- Kotlin.
- Jetpack Compose.
- Room (SQLite) for local persistence.
- System camera for image capture.
- Android Photo Picker for selecting existing images.

## Architecture

This project was built as a chance to explore Jetpack Compose and MVVM Clean Architecture practices:

- **UI:** Compose screens backed by ViewModel state holders.
- **Domain:** use cases encapsulate business logic.
- **Data:** repository implementations over Room DAOs.

## How It Works

### Average MPG

Average MPG is computed from the vehicle's last 10 mileage records: total miles driven divided by total gallons pumped with non-full-tank records included as is.

### Attachments

Attachments can be added from the photo picker or captured with the camera. Files are stored in shared storage; they are intended to be accessible by other apps and **not deleted** when the app is uninstalled; however, these cases are untested as of yet.

### Exporting Mileage Records

From the vehicle details screen, tap the export button, then choose:

1. **Range:** latest (non-exported) records, or custom date range.
2. **Format:** CSV or JSON.

Export is then handed off to a system share activity, such as email or SMS. Once successful, each exported record is updated to display its exported status.

### Timestamps

Timestamps are stored as UTC ISO-8601 `Instant` values and converted to local dates for display.

## Contributing

Issues are welcome!

## License

Licensed under the [GNU General Public License v3.0](LICENSE).

## Author

Dylan Beebe ([officialBeebe](https://github.com/officialBeebe))
