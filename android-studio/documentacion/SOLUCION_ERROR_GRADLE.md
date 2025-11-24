# 🔧 Solución Error Gradle - "Could not find method implementation()"

## ❌ Error

```
Could not find method implementation() for arguments [com.squareup.retrofit2:retrofit:2.9.0]
```

## ✅ Solución

Este error ocurre porque falta el plugin de Android o la configuración del proyecto está incorrecta.

### Paso 1: Verificar estructura del proyecto

Tu proyecto Android debe tener esta estructura:

```
android-studio/
├── app/
│   ├── build.gradle          ← Este archivo
│   └── src/
├── build.gradle              ← Archivo raíz (build.gradle.root)
├── settings.gradle
├── gradle.properties
└── gradle/
    └── wrapper/
        └── gradle-wrapper.properties
```

### Paso 2: Archivo build.gradle raíz

Crea o verifica `build.gradle` en la raíz del proyecto:

```gradle
buildscript {
    ext.kotlin_version = '1.9.22'
    ext.hilt_version = '2.48'
    
    repositories {
        google()
        mavenCentral()
    }
    
    dependencies {
        classpath 'com.android.tools.build:gradle:8.2.0'
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
        classpath "com.google.dagger:hilt-android-gradle-plugin:$hilt_version"
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
```

### Paso 3: Archivo app/build.gradle

El archivo `app/build.gradle` debe empezar con:

```gradle
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'kotlin-kapt'
    id 'dagger.hilt.android.plugin'
}

android {
    // ... configuración
}

dependencies {
    // Aquí van las dependencias con implementation
}
```

### Paso 4: Verificar settings.gradle

```gradle
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Library Digital"
include ':app'
```

### Paso 5: Sincronizar

1. **En Android Studio:**
   - File → Sync Project with Gradle Files
   - O: Click en el icono de elefante (Gradle Sync)

2. **Desde terminal:**
   ```bash
   ./gradlew --refresh-dependencies
   ```

### Paso 6: Si persiste el error

1. **Invalidar caché:**
   - File → Invalidate Caches / Restart
   - Marcar todas las opciones
   - Click "Invalidate and Restart"

2. **Limpiar proyecto:**
   ```bash
   ./gradlew clean
   ```

3. **Verificar versión de Gradle:**
   - `gradle/wrapper/gradle-wrapper.properties` debe tener:
   ```properties
   distributionUrl=https\://services.gradle.org/distributions/gradle-8.2-bin.zip
   ```

## 📝 Nota Importante

Si estás usando **build.gradle.kts** (Kotlin DSL), la sintaxis es diferente:

```kotlin
dependencies {
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
}
```

Si estás usando **build.gradle** (Groovy), la sintaxis es:

```gradle
dependencies {
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
}
```

**No mezcles ambas sintaxis en el mismo archivo.**

