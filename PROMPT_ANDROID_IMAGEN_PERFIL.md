# 📱 Prompt para Cursor: Implementar Envío de Imagen de Perfil en Android

## Objetivo
Implementar la funcionalidad para que la aplicación Android envíe la imagen de perfil del usuario al registrarse en el backend. La imagen debe convertirse a Base64 y enviarse en el campo `profileImageBase64` del DTO de registro.

## Contexto
El backend ya está configurado para recibir imágenes en formato Base64 en el campo `profileImageBase64` del `UserRegistrationDTO`. El backend acepta:
- Data URI completo: `"data:image/png;base64,iVBORw0KGgo..."`
- Solo Base64: `"iVBORw0KGgo..."`

## Cambios Necesarios en Android

### 1. Actualizar el DTO de Registro de Usuario

**Archivo**: `app/src/main/java/com/library/app/data/remote/dto/UserDTOs.kt` (o similar)

```kotlin
data class UserRegistrationDTO(
    @SerializedName("name")
    val name: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("password")
    val password: String,
    
    @SerializedName("phone")
    val phone: String? = null,
    
    @SerializedName("profileImageBase64")
    val profileImageBase64: String? = null  // ← AGREGAR ESTE CAMPO
)
```

### 2. Crear Utilidad para Convertir Imagen a Base64

**Archivo**: `app/src/main/java/com/library/app/util/ImageUtils.kt` (crear nuevo archivo)

```kotlin
package com.library.app.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.content.Context
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.IOException

object ImageUtils {
    
    /**
     * Convierte un Bitmap a Base64 con formato data URI
     * @param bitmap La imagen a convertir
     * @param format Formato de imagen (Bitmap.CompressFormat.PNG o JPEG)
     * @param quality Calidad de compresión (0-100, solo para JPEG)
     * @return String Base64 con prefijo data URI
     */
    suspend fun bitmapToBase64(
        bitmap: Bitmap,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
        quality: Int = 90
    ): String = withContext(Dispatchers.IO) {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(format, quality, outputStream)
        val byteArray = outputStream.toByteArray()
        val base64 = android.util.Base64.encodeToString(byteArray, android.util.Base64.NO_WRAP)
        
        val mimeType = when (format) {
            Bitmap.CompressFormat.PNG -> "image/png"
            Bitmap.CompressFormat.JPEG -> "image/jpeg"
            Bitmap.CompressFormat.WEBP -> "image/webp"
            else -> "image/png"
        }
        
        "data:$mimeType;base64,$base64"
    }
    
    /**
     * Convierte un Bitmap a Base64 sin prefijo data URI (solo el string base64)
     * @param bitmap La imagen a convertir
     * @param format Formato de imagen
     * @param quality Calidad de compresión
     * @return String Base64 sin prefijo
     */
    suspend fun bitmapToBase64Only(
        bitmap: Bitmap,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
        quality: Int = 90
    ): String = withContext(Dispatchers.IO) {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(format, quality, outputStream)
        val byteArray = outputStream.toByteArray()
        android.util.Base64.encodeToString(byteArray, android.util.Base64.NO_WRAP)
    }
    
    /**
     * Carga un Bitmap desde un Uri (desde galería o cámara)
     * @param context Contexto de la aplicación
     * @param uri Uri de la imagen
     * @param maxWidth Ancho máximo deseado (para reducir tamaño)
     * @param maxHeight Alto máximo deseado (para reducir tamaño)
     * @return Bitmap o null si hay error
     */
    suspend fun loadBitmapFromUri(
        context: Context,
        uri: Uri,
        maxWidth: Int = 1024,
        maxHeight: Int = 1024
    ): Bitmap? = withContext(Dispatchers.IO) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream?.close()
            
            // Calcular escala para reducir tamaño
            options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)
            options.inJustDecodeBounds = false
            
            val inputStream2 = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream2, null, options)
            inputStream2?.close()
            
            bitmap
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
    
    /**
     * Carga un Bitmap desde un Uri usando MediaStore (más eficiente)
     */
    suspend fun loadBitmapFromUriMediaStore(
        context: Context,
        uri: Uri,
        maxWidth: Int = 1024,
        maxHeight: Int = 1024
    ): Bitmap? = withContext(Dispatchers.IO) {
        try {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context.contentResolver.query(uri, projection, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    val filePath = it.getString(columnIndex)
                    val options = BitmapFactory.Options().apply {
                        inJustDecodeBounds = true
                    }
                    BitmapFactory.decodeFile(filePath, options)
                    options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)
                    options.inJustDecodeBounds = false
                    BitmapFactory.decodeFile(filePath, options)
                } else {
                    null
                }
            } ?: loadBitmapFromUri(context, uri, maxWidth, maxHeight)
        } catch (e: Exception) {
            e.printStackTrace()
            loadBitmapFromUri(context, uri, maxWidth, maxHeight)
        }
    }
    
    /**
     * Calcula el factor de escala para reducir el tamaño de la imagen
     */
    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            
            while ((halfHeight / inSampleSize) >= reqHeight &&
                (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }
        
        return inSampleSize
    }
    
    /**
     * Redimensiona un Bitmap manteniendo la proporción
     */
    suspend fun resizeBitmap(
        bitmap: Bitmap,
        maxWidth: Int,
        maxHeight: Int
    ): Bitmap = withContext(Dispatchers.IO) {
        val width = bitmap.width
        val height = bitmap.height
        
        if (width <= maxWidth && height <= maxHeight) {
            return@withContext bitmap
        }
        
        val ratio = minOf(maxWidth.toFloat() / width, maxHeight.toFloat() / height)
        val newWidth = (width * ratio).toInt()
        val newHeight = (height * ratio).toInt()
        
        Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }
}
```

### 3. Actualizar el ViewModel o Repository para Incluir la Imagen

**Archivo**: `app/src/main/java/com/library/app/data/repository/UserRepository.kt` (o similar)

```kotlin
// En el método de registro, agregar parámetro opcional para la imagen
suspend fun registerUser(
    name: String,
    email: String,
    password: String,
    phone: String? = null,
    profileImageUri: Uri? = null  // ← AGREGAR ESTE PARÁMETRO
): Result<UserResponse> = withContext(Dispatchers.IO) {
    try {
        // Convertir imagen a Base64 si está presente
        val profileImageBase64 = profileImageUri?.let { uri ->
            val bitmap = ImageUtils.loadBitmapFromUriMediaStore(context, uri)
                ?: return@withContext Result.failure(Exception("Error al cargar la imagen"))
            
            // Redimensionar si es muy grande (opcional, para reducir tamaño)
            val resizedBitmap = ImageUtils.resizeBitmap(bitmap, 800, 800)
            
            // Convertir a Base64
            ImageUtils.bitmapToBase64(resizedBitmap, Bitmap.CompressFormat.JPEG, 85)
        }
        
        val registrationDTO = UserRegistrationDTO(
            name = name,
            email = email,
            password = password,
            phone = phone,
            profileImageBase64 = profileImageBase64  // ← INCLUIR AQUÍ
        )
        
        val response = userApiService.register(registrationDTO)
        
        if (response.isSuccessful && response.body() != null) {
            Result.success(response.body()!!)
        } else {
            Result.failure(Exception(response.message() ?: "Error desconocido"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
```

### 4. Actualizar la UI para Seleccionar y Enviar Imagen

**Archivo**: `app/src/main/java/com/library/app/ui/register/RegisterViewModel.kt` (o similar)

```kotlin
class RegisterViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri.asStateFlow()
    
    fun selectImage(uri: Uri) {
        _selectedImageUri.value = uri
    }
    
    fun clearImage() {
        _selectedImageUri.value = null
    }
    
    suspend fun register(
        name: String,
        email: String,
        password: String,
        phone: String? = null
    ): Result<UserResponse> {
        return userRepository.registerUser(
            name = name,
            email = email,
            password = password,
            phone = phone,
            profileImageUri = _selectedImageUri.value  // ← PASAR LA URI AQUÍ
        )
    }
}
```

**Archivo**: `app/src/main/java/com/library/app/ui/register/RegisterFragment.kt` (o similar)

```kotlin
class RegisterFragment : Fragment() {
    
    private val viewModel: RegisterViewModel by viewModels()
    private var selectedImageUri: Uri? = null
    
    // Activity Result Launcher para seleccionar imagen
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            viewModel.selectImage(it)
            // Mostrar preview de la imagen
            binding.imagePreview.setImageURI(it)
            binding.imagePreview.visibility = View.VISIBLE
        }
    }
    
    private fun setupImageSelection() {
        binding.btnSelectImage.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }
        
        binding.btnRemoveImage.setOnClickListener {
            selectedImageUri = null
            viewModel.clearImage()
            binding.imagePreview.visibility = View.GONE
        }
    }
    
    private fun registerUser() {
        val name = binding.etName.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val phone = binding.etPhone.text.toString().takeIf { it.isNotBlank() }
        
        viewModelScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            
            val result = viewModel.register(name, email, password, phone)
            
            binding.progressBar.visibility = View.GONE
            
            result.onSuccess { user ->
                // Navegar a siguiente pantalla o mostrar éxito
                Toast.makeText(context, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show()
            }.onFailure { error ->
                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
```

### 5. Layout XML para Seleccionar Imagen (opcional)

**Archivo**: `app/src/main/res/layout/fragment_register.xml` (agregar estos elementos)

```xml
<!-- Botón para seleccionar imagen -->
<Button
    android:id="@+id/btnSelectImage"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Seleccionar Foto de Perfil"
    android:layout_marginTop="16dp" />

<!-- Preview de la imagen seleccionada -->
<ImageView
    android:id="@+id/imagePreview"
    android:layout_width="120dp"
    android:layout_height="120dp"
    android:layout_marginTop="16dp"
    android:scaleType="centerCrop"
    android:visibility="gone"
    android:background="@drawable/ic_profile_placeholder" />

<!-- Botón para remover imagen -->
<Button
    android:id="@+id/btnRemoveImage"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Remover"
    android:layout_marginTop="8dp"
    android:visibility="gone" />
```

## Checklist de Implementación

- [ ] Agregar campo `profileImageBase64` al `UserRegistrationDTO` en Android
- [ ] Crear archivo `ImageUtils.kt` con funciones de conversión
- [ ] Actualizar `UserRepository` para aceptar `Uri` de imagen y convertir a Base64
- [ ] Actualizar `RegisterViewModel` para manejar la selección de imagen
- [ ] Actualizar `RegisterFragment` con botón para seleccionar imagen
- [ ] Agregar `ActivityResultLauncher` para seleccionar imagen desde galería
- [ ] Agregar preview de imagen seleccionada en la UI
- [ ] Probar registro con imagen
- [ ] Probar registro sin imagen (debe funcionar igual)
- [ ] Verificar que la imagen se guarda correctamente en el backend

## Notas Importantes

1. **Tamaño de Imagen**: Las imágenes se redimensionan a máximo 800x800px y se comprimen a calidad 85% para reducir el tamaño del Base64.

2. **Formato**: Se usa JPEG con calidad 85% por defecto para balancear calidad y tamaño. Puedes cambiar a PNG si prefieres mejor calidad.

3. **Permisos**: Asegúrate de tener los permisos necesarios en `AndroidManifest.xml`:
   ```xml
   <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
   <uses-permission android:name="android.permission.READ_MEDIA_IMAGES" /> <!-- Para Android 13+ -->
   ```

4. **Manejo de Errores**: El código incluye manejo de errores, pero puedes mejorarlo según tus necesidades.

5. **Testing**: Prueba con diferentes tamaños de imagen y formatos (PNG, JPEG, WEBP).

## Ejemplo de Uso Completo

```kotlin
// En el Fragment o Activity
private fun registerWithImage() {
    viewModelScope.launch {
        val imageUri = selectedImageUri // Uri de la imagen seleccionada
        
        val result = userRepository.registerUser(
            name = "Juan Pérez",
            email = "juan@example.com",
            password = "password123",
            phone = "+56912345678",
            profileImageUri = imageUri  // Puede ser null si no hay imagen
        )
        
        result.onSuccess { user ->
            Log.d("Register", "Usuario registrado: ${user.name}")
            Log.d("Register", "Tiene imagen: ${user.profileImageBase64 != null}")
        }
    }
}
```

## Verificación en Backend

Después de implementar, verifica en los logs del backend que aparezca:
```
=== REGISTRO DE USUARIO - DATOS RECIBIDOS ===
profileImageBase64 presente: SÍ
profileImageBase64 longitud: [número] caracteres
```

Si aparece "NO", revisa que el campo se esté enviando correctamente desde Android.

