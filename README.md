# Disney-AlkemyChallenge

Desarrollar una API para explorar el mundo de Disney, la cual permitirá conocer y modificar los personajes que lo componen y entender en qué películas estos participaron. El proyecto incluye tanto el backend (Spring Boot) como el frontend (React + TypeScript).

## 🚀 Tecnologías Utilizadas

### Backend
- **Java 17**
- **Spring Boot 3.x**
- **Spring Security** con JWT
- **Spring Data JPA**
- **PostgreSQL**
- **MapStruct** para mapeo de objetos
- **Swagger/OpenAPI** para documentación
- **Maven**

### Frontend
- **React 19**
- **TypeScript**
- **Vite** como bundler
- **Tailwind CSS** para estilos
- **shadcn/ui** para componentes
- **React Router DOM** para navegación
- **Axios** para peticiones HTTP
- **JWT Decode** para manejo de tokens

## 📋 Requerimientos Técnicos

### 1. Modelado de Base de Datos

#### Personaje

- **Imagen**
- **Nombre**
- **Edad**
- **Peso**
- **Historia**
- **Películas o series asociadas**

#### Película o Serie

- **Imagen**
- **Título**
- **Fecha de creación**
- **Calificación** (del 1 al 5)
- **Personajes asociados**

#### Género

- **Nombre**
- **Imagen**
- **Películas o series asociadas**

### 2. Autenticación de Usuarios

El sistema implementa autenticación JWT con refresh tokens para mayor seguridad:

**Endpoints de Autenticación:**

- `POST /auth/register` - Registro de usuarios
- `POST /auth/login` - Inicio de sesión
- `POST /auth/refresh` - Renovación de token de acceso
- `POST /auth/logout` - Cierre de sesión

**Características de Seguridad:**
- Tokens JWT con expiración de 15 minutos
- Refresh tokens almacenados en cookies httpOnly
- Renovación automática de tokens en el frontend
- Roles de usuario (USER, ADMIN)

### 3. Listado de Personajes

El listado deberá mostrar:

- **Imagen**
- **Nombre**

**Endpoint:**

- `/characters`

### 4. Creación, Edición y Eliminación de Personajes (CRUD)

Deberán existir las operaciones básicas de creación, edición y eliminación de personajes.

### 5. Detalle de Personaje

En el detalle deberán listarse todos los atributos del personaje, así como sus películas o series relacionadas.

### 6. Búsqueda de Personajes

Deberá permitir buscar por nombre, y filtrar por edad, peso o películas/series en las que participó. Para especificar el término de búsqueda o filtros, se deberán enviar como parámetros de query:

- `GET /characters?name=nombre`
- `GET /characters?age=edad`
- `GET /characters?movies=idMovie`

### 7. Listado de Películas

Deberá mostrar solamente los campos:

- **Imagen**
- **Título**
- **Fecha de creación**

**Endpoint:**

- `GET /movies`

### 8. Detalle de Película / Serie con sus personajes

Devolverá todos los campos de la película o serie junto a los personajes asociados a la misma.

### 9. Creación, Edición y Eliminación de Película / Serie

Deberán existir las operaciones básicas de creación, edición y eliminación de películas o series.

### 10. Búsqueda de Películas o Series

Deberá permitir buscar por título, y filtrar por género. Además, deberá permitir ordenar los resultados por fecha de creación de forma ascendente o descendente.

**Endpoints de Búsqueda, Filtro u Ordenación:**

- `GET /movies?name=nombre`
- `GET /movies?genre=idGenero`
- `GET /movies?order=ASC|DESC`

### 11. Envío de Emails

Al registrarse en el sitio, el usuario deberá recibir un email de bienvenida. Es recomendable la utilización de algún servicio de terceros como SendGrid.

## Documentación

Es deseable documentar los endpoints utilizando alguna herramienta como Postman o Swagger.

## Tests

De forma opcional, se podrán agregar tests de los diferentes endpoints de la APP, verificando posibles escenarios de error:

- Campos faltantes o con un formato inválido en BODY de las peticiones
- Acceso a recursos inexistentes en endpoints de detalle

## 🏗️ Arquitectura del Proyecto

### Backend
```
backend/
├── src/main/java/com/alkemy/disney_AlkemyChallenge/
│   ├── Config/           # Configuraciones (CORS, OpenAPI, etc.)
│   ├── Controller/       # Controladores REST
│   ├── DTO/             # Objetos de transferencia de datos
│   ├── Entity/          # Entidades JPA
│   ├── Enum/            # Enumeraciones
│   ├── Exception/       # Excepciones personalizadas
│   ├── Handler/         # Manejadores globales de excepciones
│   ├── Mapper/          # Mappers de MapStruct
│   ├── Repository/      # Repositorios JPA
│   ├── Security/        # Configuración de seguridad
│   ├── Service/         # Lógica de negocio
│   └── Validation/      # Validaciones personalizadas
```

### Frontend
```
frontend/
├── src/
│   ├── components/      # Componentes React
│   │   ├── admin/       # Componentes de administración
│   │   └── ui/          # Componentes de shadcn/ui
│   ├── context/         # Contextos de React
│   ├── pages/           # Páginas de la aplicación
│   │   └── admin/       # Páginas de administración
│   ├── services/        # Servicios de API
│   ├── types/           # Tipos TypeScript
│   └── lib/             # Utilidades
```

## 🔧 Configuración y Ejecución

### Backend

1. **Requisitos:**
   - Java 17 o superior
   - Maven
   - PostgreSQL

2. **Configuración de Base de Datos:**
   ```properties
   # application.properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/disney_db
   spring.datasource.username=tu_usuario
   spring.datasource.password=tu_password
   ```

3. **Ejecución:**
   ```bash
   cd backend
   mvn spring-boot:run
   ```

4. **Documentación API:**
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - OpenAPI: http://localhost:8080/v3/api-docs

### Frontend

1. **Requisitos:**
   - Node.js 18 o superior
   - pnpm (recomendado) o npm

2. **Instalación:**
   ```bash
   cd frontend
   pnpm install
   ```

3. **Ejecución:**
   ```bash
   pnpm dev
   ```

4. **Acceso:**
   - Aplicación: http://localhost:5173

## 🔐 Características de Seguridad

### Autenticación JWT
- **Access Token:** Expira en 15 minutos
- **Refresh Token:** Almacenado en cookie httpOnly
- **Renovación automática:** El frontend renueva automáticamente los tokens expirados
- **Logout seguro:** Invalida refresh tokens al cerrar sesión

### Autorización
- **Roles:** USER y ADMIN
- **Endpoints protegidos:** Requieren autenticación y roles específicos
- **Validación de permisos:** Los usuarios solo pueden acceder a sus propios recursos

## 📱 Funcionalidades del Frontend

### Páginas Públicas
- **Home:** Página principal con contenido destacado
- **Login:** Inicio de sesión
- **Register:** Registro de usuarios
- **Movies:** Listado de películas/series
- **Characters:** Listado de personajes
- **Movie Detail:** Detalle de película/serie
- **Character Detail:** Detalle de personaje

### Páginas de Administración (ADMIN)
- **Admin Dashboard:** Panel principal de administración
- **Admin Movies:** Gestión de películas/series
- **Admin Characters:** Gestión de personajes
- **Admin Genres:** Gestión de géneros
- **Admin Users:** Gestión de usuarios
- **Admin Movie Characters:** Asociación de personajes con películas

### Características
- **Responsive Design:** Adaptable a diferentes dispositivos
- **Navegación intuitiva:** Menú de navegación y breadcrumbs
- **Formularios validados:** Validación en tiempo real
- **Notificaciones:** Feedback visual para acciones del usuario
- **Carga de imágenes:** Soporte para subida de archivos

## 🧪 Testing

### Backend
El proyecto incluye tests unitarios para:
- **Controladores:** Pruebas de endpoints
- **Servicios:** Pruebas de lógica de negocio
- **Validaciones:** Pruebas de casos de error

### Ejecutar Tests
```bash
cd backend
mvn test
```

## 📦 Despliegue

### Docker
El proyecto incluye configuración Docker para facilitar el desarrollo:

```bash
# Base de datos PostgreSQL (para desarrollo)
cd backend
docker-compose up -d

## 🔄 Cambios Principales

### Backend
- ✅ **Refresh Token:** Implementado sistema de renovación automática
- ✅ **Mappers:** Reemplazados Converters por MapStruct
- ✅ **Endpoints optimizados:** Eliminados filtros innecesarios, agregados endpoints funcionales
- ✅ **Seguridad mejorada:** JWT con expiración y renovación automática
- ✅ **Documentación:** Swagger/OpenAPI completo

### Frontend
- ✅ **React 19:** Versión más reciente con mejoras de rendimiento
- ✅ **TypeScript:** Tipado completo para mejor desarrollo
- ✅ **Tailwind CSS:** Framework de estilos moderno
- ✅ **shadcn/ui:** Componentes de UI profesionales
- ✅ **Context API:** Gestión de estado global
- ✅ **Interceptores Axios:** Manejo automático de tokens
- ✅ **Responsive Design:** Adaptable a todos los dispositivos
