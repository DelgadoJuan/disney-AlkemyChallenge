# Disney-AlkemyChallenge

Desarrollar una API para explorar el mundo de Disney, la cual permitirá conocer y modificar los personajes que lo componen y entender en qué películas estos participaron.

## Requerimientos Técnicos

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

Para realizar peticiones a los endpoints subsiguientes, el usuario deberá contar con un token que obtendrá al autenticarse. Deberán desarrollarse los endpoints de registro y login, que permitan obtener el token.

**Endpoints de Autenticación:**

- `/auth/login`
- `/auth/register`

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
