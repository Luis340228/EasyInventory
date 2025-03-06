1. Nombre del Proyecto
EasyInventory - Aplicación para la gestión de inventarios en tiendas de abarrotes.

2. Descripción General
EasyInventory es una aplicación móvil desarrollada en Kotlin con Jetpack Compose que permite a los usuarios gestionar el inventario de una tienda de abarrotes. Entre sus principales características se encuentran:
  * Registro y gestión de productos con información detallada.
  * Administración de proveedores, categorías y marcas.
  * Registro de ventas y cálculo automático del inventario.
  * Listado de productos próximos a caducar y caducados.
  * Notificaciones automáticas sobre productos a punto de vencer.

3. Tecnologías Utilizadas
Lenguaje: Kotlin
Interfaz: Jetpack Compose
Base de Datos: Firebase Firestore
Notificaciones: Firebase Cloud Messaging (FCM)
Patrón de Arquitectura: MVVM

4. Estructura del Proyecto

/EasyInventory
│── app/src/main/java/com/tricode/easyinventory/
│   ├── ui.theme/                 # Configuración de tema
│   ├── screens/                  # Pantallas principales
│   │   ├── HomeScreen.kt         # Menú principal
│   │   ├── ProductListScreen.kt  # Lista de productos
│   │   ├── AddEditProductScreen.kt # Formulario de productos
│   │   ├── SalesScreen.kt        # Registro de ventas
│   │   ├── ProviderListScreen.kt # Gestión de proveedores
│   │   ├── ExpiringProductsScreen.kt # Productos por caducar
│   │   ├── ExpiredProductsScreen.kt  # Productos caducados
│   │   ├── TopSellingProductsScreen.kt # Productos más vendidos
│   ├── viewmodel/                # Lógica de negocio (MVVM)
│   │   ├── InventoryViewModel.kt
│   ├── notifications/             # Configuración de FCM
│   │   ├── MyFirebaseMessagingService.kt
│   ├── MainActivity.kt            # Punto de entrada de la app
│── res/                           # Recursos de la aplicación (icons, imágenes, layouts)
│── AndroidManifest.xml            # Configuración y permisos

5. Instalación y Configuración
  * Requisitos Previos
    * Tener Android Studio instalado.
    * Contar con una cuenta en Firebase y configurar un proyecto.
    * Agregar el archivo google-services.json en la carpeta /app.

Pasos para ejecutar el proyecto
1.- Clonar el repositorio:

git clone https://github.com/Luis340228/EasyInventory.git
cd EasyInventory

2.- Abrir el proyecto en Android Studio.
3.- Sincronizar dependencias (Gradle sync).
4.- Ejecutar la app en un emulador o dispositivo físico.

6. Integración con Firebase
Se utilizaron dos servicios principales de Firebase:
* Firestore: Para almacenar y gestionar los datos del inventario.
* FCM (Firebase Cloud Messaging): Para enviar notificaciones automáticas sobre productos próximos a caducar y caducados.

7. Notificaciones Automáticas
Los usuarios se suscriben automáticamente a los temas expiring_products y expired_products.
Se programó en Firebase Console el envío de alertas diarias sobre productos en estas condiciones.
También se envían notificaciones locales al abrir la app si se detectan productos próximos a caducar o caducados.

8. Funcionalidades Implementadas
  * Gestión de productos con proveedores, categorías y marcas.
  * Registro y control de ventas con actualización automática del inventario.
  * Notificaciones automáticas sobre productos por caducar y caducados.
  * Notificaciones automáticas sobre productos por caducar y caducados. Listado de los productos más vendidos con estadísticas.
  * Notificaciones automáticas sobre productos por caducar y caducados. Diseño intuitivo con Jetpack Compose.

9. Licencia y Créditos
Este proyecto fue desarrollado por Tricode como parte de un sistema de gestión de inventario en Android.
