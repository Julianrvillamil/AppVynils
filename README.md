# AppVynills

AppVynills es una aplicación de Android que permite a los usuarios explorar y ver detalles de álbumes, incluidas pistas, géneros, comentarios e intérpretes. La aplicación está construida utilizando el patrón de arquitectura **MVVM (Model-View-ViewModel)**, junto con **Jetpack Components**, **Volley** y **Retrofit** para operaciones de red, y **LiveData** para actualizaciones reactivas de la interfaz de usuario.

## Tabla de Contenidos
- [Introducción](#introducción)
  - [Prerrequisitos](#prerrequisitos)
  - [Configuración del Proyecto](#configuración-del-proyecto)
  - [Clonación del Repositorio](#clonación-del-repositorio)
- [Estructura del Proyecto](#estructura-del-proyecto)
  - [Descripción de los Módulos](#descripción-de-los-módulos)
  - [Diagrama de Arquitectura](#diagrama-de-arquitectura)
- [Uso](#uso)
  - [Ejecutar la Aplicación en un Emulador](#ejecutar-la-aplicación-en-un-emulador)
  - [Ejecutar la Aplicación en un Dispositivo Físico](#ejecutar-la-aplicación-en-un-dispositivo-físico)
- [Generación del APK](#generación-del-apk)


---

## Introducción

### Prerrequisitos

- **Android Studio Arctic Fox o posterior**: Se recomienda la última versión estable.
- **Java 11+**: El proyecto requiere Java 11 o superior.
- **SDK de Android**: Asegúrate de que el SDK de Android esté instalado.
- **Emulador de Android** (opcional): Un dispositivo virtual para ejecutar la aplicación localmente en caso de no tener un dispositivo físico disponible.

### Configuración del Proyecto

1. **Descargar e instalar Android Studio**: Si aún no está instalado, puede descargarlo desde [aquí](https://developer.android.com/studio).
2. **Instalar el Kit de Desarrollo de Java (JDK)**: Asegúrese de que Java 11 o superior esté instalado. Puedes verificar ejecutando:

   ```bash
   java -version

### Clonación del repositorio

1. Abra una terminal y navega al directorio donde deseas guardar el proyecto.
2. Clona el repositorio del proyecto:

    ```bash
     git clone https://github.com/yourusername/appvynills.git
     cd appvynills
    
3. Abrir el proyecto en Android Studio: Abra Android Studio, selecciona Open Project y navega hasta el directorio appvynills clonado.
4. Sincroniza el proyecto con Gradle: Android Studio pedirá sincronizar las dependencias. Si no lo hace, dirijase a
     - File > Sync Project with Gradle Files



## Estructura del proyecto

El proyecto sigue una arquitectura MVVM que divide responsabilidades en diferentes capas para facilitar el mantenimiento y las pruebas.

### Descripción de los Módulos

- MainActivity: Contiene el BottomNavigationView y gestiona la navegación entre los fragmentos principales (Home, Dashboard, Notifications).
- Fragments:
  -   HomeFragment: Muestra una lista de álbumes utilizando un RecyclerView.
  -   AlbumDetailFragment: Muestra los detalles del álbum cuando se selecciona uno en HomeFragment.
- ViewModels:
- Repositorio:
- Modelos:
- Adaptadores:
- Networking:

### Diagrama de Arquitectura

A continuación se muestra un diagrama de la arquitectura simplificada del proyecto para referencia:

[Diagrama de componentes](https://github.com/user-attachments/assets/4849a457-7030-4e27-9b82-f357f40c95fe)

## Uso

### Ejecutar la Aplicación en un Emulador

1. Abrir el Administrador de AVD: En Android Studio, ve a Tools > AVD Manager.
2. Crear un Dispositivo Virtual: Selecciona un modelo de dispositivo (por ejemplo, Pixel 5).
3. Seleccionar Imagen del Sistema: Elige una imagen del sistema (se recomienda Android 10 o superior).
4. Ejecutar el Emulador: Una vez configurado, inicia el emulador y presiona Run en Android Studio para instalar y ejecutar la aplicación.
   
### Ejecutar la Aplicación en un Dispositivo Físico

1. Habilitar Depuración USB: Ve a Ajustes > Opciones de desarrollador > Depuración USB en tu dispositivo.
2. Conectar el Dispositivo: Conecta el dispositivo a tu computadora.
3. Ejecutar la Aplicación: En Android Studio, selecciona tu dispositivo en el menú desplegable y presiona Run (parte superior derecha en Android Studio).

## Generación del APK

1. Construir el APK: En Android Studio, ve a Build > Build Bundle(s) / APK(s) > Build APK(s).
2. Después de que se complete la construcción, encuentra el archivo APK en:
  - Versión de depuración: app/build/outputs/apk/debug/app-debug.apk
  - Versión de lanzamiento: app/build/outputs/apk/release/app-release.apk (requiere firma).
3. Instalar APK: Transfiere el APK a tu dispositivo Android e instálalo directamente.

