# Login-Permissions
Permissions in Android Studio

Camera Permission
Purpose: Allows an app to access the device's camera to capture photos and videos.
Permission:
Manifest.permission.CAMERA: Required to use the camera hardware. Without this permission, the app cannot take photos or record videos.
Usage:
Check if the permission is granted before accessing the camera.
If not granted, request the permission from the user.
Handle the user's response appropriately (either grant or deny).

Microphone Permission
Purpose: Allows an app to access the device's microphone to record audio.
Permission:
Manifest.permission.RECORD_AUDIO: Required to capture audio through the device's microphone. Without this permission, the app cannot record sound.
Usage:
Check if the permission is granted before accessing the microphone.
If not granted, request the permission from the user.
Handle the user's response appropriately (either grant or deny).
Calendar (Diary) Permissions
Purpose: Allows an app to read and write calendar events.

Permissions:
Manifest.permission.READ_CALENDAR: Required to read calendar events. Without this permission, the app cannot access the user's calendar events.
Manifest.permission.WRITE_CALENDAR: Required to create or modify calendar events. Without this permission, the app cannot add or edit events in the user's calendar.
Usage:
Check if the permissions are granted before accessing the calendar.
If not granted, request the permissions from the user.
Handle the user's response appropriately (either grant or deny).
