# Configuración para XAMPP - Sistema de Biblioteca Digital

## 📋 Requisitos Previos

- **XAMPP** instalado y funcionando
- **Java JDK 21** instalado
- **Maven 3.8+** instalado

## 🚀 Pasos para Configurar con XAMPP

### 1. Iniciar XAMPP

1. Abre el **Panel de Control de XAMPP**
2. Inicia el módulo **MySQL** (click en "Start")
3. Verifica que MySQL esté corriendo en el puerto **3306**

### 2. Crear las Bases de Datos

Tienes dos opciones para crear las bases de datos:

#### Opción A: Usando phpMyAdmin (Recomendado)

1. Abre tu navegador y ve a: **http://localhost/phpmyadmin**
2. Haz click en la pestaña **"SQL"**
3. Copia y pega el contenido del archivo `database-setup.sql`
4. Haz click en **"Continuar"** o **"Go"**

#### Opción B: Usando la línea de comandos de MySQL

1. Abre la terminal/comandos de Windows
2. Navega a la carpeta de MySQL de XAMPP (normalmente: `C:\xampp\mysql\bin`)
3. Ejecuta:
```bash
mysql -u root -p < ruta\completa\a\database-setup.sql
```
O conecta directamente:
```bash
mysql -u root
```
Luego ejecuta los comandos SQL del archivo `database-setup.sql`

### 3. Configurar la Contraseña de MySQL (si es necesario)

**Nota:** Por defecto, XAMPP MySQL viene con la contraseña vacía para el usuario `root`.

Si tu XAMPP tiene contraseña configurada:

1. Edita cada archivo `application.yml` de los servicios:
   - `user-management-service/src/main/resources/application.yml`
   - `book-catalog-service/src/main/resources/application.yml`
   - `loan-management-service/src/main/resources/application.yml`
   - `reports-service/src/main/resources/application.yml`
   - `notifications-service/src/main/resources/application.yml`

2. Cambia la línea:
```yaml
password: 
```
Por tu contraseña:
```yaml
password: tu_password_aqui
```

### 4. Verificar Conexión

Abre phpMyAdmin y verifica que se hayan creado estas 5 bases de datos:
- `library_users_db`
- `library_books_db`
- `library_loans_db`
- `library_reports_db`
- `library_notifications_db`

### 5. Compilar los Microservicios

Abre terminales separadas para cada servicio y ejecuta:

#### Terminal 1 - User Management Service
```bash
cd user-management-service
mvn clean install
mvn spring-boot:run
```

#### Terminal 2 - Book Catalog Service
```bash
cd book-catalog-service
mvn clean install
mvn spring-boot:run
```

#### Terminal 3 - Loan Management Service
```bash
cd loan-management-service
mvn clean install
mvn spring-boot:run
```

#### Terminal 4 - Reports Service
```bash
cd reports-service
mvn clean install
mvn spring-boot:run
```

#### Terminal 5 - Notifications Service
```bash
cd notifications-service
mvn clean install
mvn spring-boot:run
```

### 6. Verificar que Funcionan

Una vez que todos los servicios estén corriendo, verifica:

- **User Management:** http://localhost:8081/swagger-ui.html
- **Book Catalog:** http://localhost:8082/swagger-ui.html
- **Loan Management:** http://localhost:8083/swagger-ui.html
- **Reports:** http://localhost:8084/swagger-ui.html
- **Notifications:** http://localhost:8085/swagger-ui.html

## 🔧 Solución de Problemas

### Error: "Access denied for user 'root'@'localhost'"

**Solución:**
1. Abre phpMyAdmin: http://localhost/phpmyadmin
2. Ve a la pestaña "Usuarios"
3. Verifica la contraseña del usuario `root`
4. Actualiza el `application.yml` con la contraseña correcta

### Error: "Unknown database 'library_users_db'"

**Solución:**
1. Verifica que hayas ejecutado el script `database-setup.sql`
2. Comprueba en phpMyAdmin que las bases de datos existan

### Error: "Connection refused" o puerto ocupado

**Solución:**
1. Verifica que MySQL esté corriendo en XAMPP
2. Verifica que no haya otro MySQL corriendo en el puerto 3306
3. Revisa el firewall de Windows

### Error de compilación Maven

**Solución:**
```bash
mvn clean
mvn install -U
```

## 📝 Configuración de Conexión

Cada servicio está configurado para conectarse a XAMPP MySQL con:

```yaml
datasource:
  url: jdbc:mysql://localhost:3306/library_[servicio]_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
  username: root
  password:  # (vacío por defecto en XAMPP)
```

### Si necesitas cambiar la configuración:

Edita el archivo `application.yml` de cada servicio y modifica:
- **url:** Si MySQL está en otro puerto
- **username:** Si usas otro usuario
- **password:** Si tienes contraseña configurada

## ✅ Verificación Final

1. **MySQL corriendo en XAMPP** ✓
2. **5 bases de datos creadas** ✓
3. **5 servicios compilados y corriendo** ✓
4. **Swagger UI accesible en cada puerto** ✓

## 🎉 ¡Listo!

Tu sistema de microservicios ahora está funcionando con XAMPP MySQL.

**Próximo paso:** Prueba los endpoints desde Swagger UI o crea un usuario de prueba:

```bash
POST http://localhost:8081/api/users/register
{
  "name": "Usuario Prueba",
  "email": "test@example.com",
  "password": "password123",
  "phone": "123456789"
}
```




