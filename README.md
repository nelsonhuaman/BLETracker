# BLEtracker

**BLEtracker** es una aplicación móvil Android desarrollada con Kotlin y Jetpack Compose que permite detectar y registrar dispositivos cercanos a través de Bluetooth Low Energy (BLE). Está diseñada para facilitar la trazabilidad de contactos cercanos, como en casos de rastreo de contagios, y puede adaptarse a diferentes escenarios donde se requiera interacción mediante BLE.

---

## 🛰️ Características

- Escaneo de dispositivos BLE cercanos.
- Transmisión como dispositivo emisor (Advertiser).
- Registro de UUID de dispositivos cercanos.
- Almacenamiento local con Room Database.
- Integración con Supabase como backend para sincronización de datos.
- Uso de coroutines para operaciones asíncronas.
- Implementación de servicios en segundo plano (background).
- Arquitectura basada en MVVM
- Uso de Retrofit para conexión a Supabase API.

### Estructura general:
- `ui/`: Composables y pantallas de interfaz de usuario.
- `viewmodel/`: Manejo de estado de cada pantalla utilizando `ViewModel` y `StateFlow`.
- `domain/`: Modelos de negocio puros y lógica de dominio.
- `data/`: Repositorios, modelos DTO y configuración de Room y Supabase.

## Integrantes
- Huaman Apaza, Nelson Aluyis 100%
- Arteaga Peña, Carlos Fabián 90%
- Cahuana Aguilar, Josué Mathías Miguel 90%
