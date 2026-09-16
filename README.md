# Blackout Esports — Backend de jugadores

## Estructura

```
blackout-back-sett-backend/
  docker-compose.yml     # Levanta todos los microservicios + sus BDs
  blackout-back-sett/        # Microservicio 1: jugadores de Valorant y sus configs
```

Iremos agregando una carpeta por cada microservicio nuevo (catalog-service, orders-service, etc.),
cada uno con su propio Dockerfile y bloque en el `docker-compose.yml`.

## Requisitos

- Docker Desktop instalado y corriendo
- (Opcional, si quieres correr el servicio SIN Docker) Java 21 y Maven

## Levantar todo con Docker (recomendado)

Desde la carpeta `blackout-back-sett-backend/`:

```bash
docker compose up --build
```

Esto:
1. Levanta una base de datos Postgres para `blackout-back-sett` (puerto `5433` en tu máquina)
2. Construye la imagen del `blackout-back-sett` con Maven dentro del contenedor
3. Levanta el `blackout-back-sett` en `http://localhost:8081`

Para bajarlo:

```bash
docker compose down
```

Para bajarlo y borrar también los datos de la BD:

```bash
docker compose down -v
```

## Probar la API

Con todo levantado, abre en el navegador:

```
http://localhost:8081/swagger-ui.html
```

Pulsa **Authorize** e ingresa un Access Token válido antes de probar los endpoints.

## Autenticación y permisos

Este servicio acepta Access Tokens JWT emitidos por los dos proveedores del proyecto:

- **Microsoft Entra ID:** los roles de aplicación `Admin` y `Staff` pueden leer, crear, editar y eliminar.
- **AWS Cognito:** los fans autenticados pueden leer. Aunque un token de Cognito contenga una etiqueta `Admin`, el backend siempre lo trata como `Fan`.

Todos los `GET` exigen un token válido. `POST`, `PUT` y `DELETE` exigen un Access Token de Entra con rol `Admin` o `Staff`.
El servidor comprueba firma, emisor, vencimiento y sujeto. Para Entra también comprueba la audiencia y el scope
`access_as_user`; para Cognito comprueba `token_use=access` y el `client_id`. Un token de Microsoft Graph
(`User.Read` o audiencia de Graph) no sirve para llamar esta API.

Copia `.env.example` a `.env` y completa:

| Variable | Valor esperado |
|---|---|
| `ENTRA_ISSUER` | `https://login.microsoftonline.com/<tenant-id>/v2.0` |
| `ENTRA_AUDIENCE` | El valor exacto de `aud` emitido para la API de Blackout |
| `ENTRA_SCOPE` | `access_as_user` |
| `COGNITO_ISSUER` | `https://cognito-idp.<region>.amazonaws.com/<user-pool-id>` |
| `COGNITO_CLIENT_ID` | ID del cliente web de Cognito |

El frontend administrativo debe solicitar `api://<id-aplicacion-api>/access_as_user`, además de los scopes de Graph
que use por separado. Los roles `Admin` y `Staff` deben estar definidos como roles de aplicación y asignados a los usuarios.

### Endpoints principales

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/players` | Lista todos los jugadores (de todos los juegos) |
| GET | `/api/players/{id}` | Obtiene un jugador por su id (slug, ej: `prinzcl`) |
| GET | `/api/games/{game}/players` | Lista jugadores de un juego (ej: `/api/games/valorant/players`) |
| GET | `/api/games/{game}/players/search?query=` | Busca por handle, equipo o rol |
| GET | `/api/games/{game}/players/featured?count=4` | Jugadores actualizados recientemente (para la home) |
| POST | `/api/players` | Crea un jugador |
| PUT | `/api/players/{id}` | Actualiza un jugador |
| DELETE | `/api/players/{id}` | Elimina un jugador |

Al levantar por primera vez, `data.sql` inserta 3 jugadores de ejemplo (incluyendo **Prinzcl**, el mismo que ya está hardcodeado en `data/players.ts` del frontend) para que los datos calcen al conectar todo.

### Ejemplo de body para POST /api/players

```json
{
  "handle": "Demon1",
  "realName": "Max Mazanov",
  "team": "NRG",
  "countryCode": "US",
  "role": "Duelista",
  "game": "valorant",
  "settings": {
    "dpi": 800,
    "inGameSens": 0.35,
    "windowsSens": 6,
    "hz": 360,
    "resolution": "1920x1080",
    "aspectRatio": "16:9 Nativa"
  },
  "crosshair": {
    "code": "0;P;c;1;o;1",
    "color": "#00FF00",
    "size": 4,
    "thickness": 4,
    "gap": 2,
    "outline": true,
    "opacity": 1,
    "centerDot": false,
    "outerLines": false
  },
  "gear": {
    "mouse": "Logitech G Pro X Superlight",
    "mousepad": "Glide XL",
    "keyboard": "Wooting 60HE",
    "monitor": "Vantage 24.5\" 360Hz",
    "headset": "HyperX Cloud II"
  }
}
```

El `id` es opcional: si no lo envías, se genera automáticamente como slug a partir del `handle` (ej: `"Demon1"` → `"demon1"`). El `edpi` tampoco se envía — el backend lo calcula solo (`dpi * inGameSens`).

## Levantar SIN Docker (alternativa)

1. Instala Postgres localmente y crea una BD llamada `players_db`
2. Entra a `blackout-back-sett/`
3. Corre: `mvn spring-boot:run`
   - Si tu Postgres no usa usuario/clave `postgres`/`postgres` o no corre en el puerto 5433,
     ajusta las variables de entorno `DB_HOST`, `DB_PORT`, `DB_USER`, `DB_PASSWORD` antes de correrlo.

## Conectar desde el frontend (React + Vite)

El CORS admite por defecto `http://localhost:5173`, `http://localhost:3000` y `http://localhost:3001`.
Cada petición debe enviar `Authorization: Bearer <access_token>`.

## Pruebas

Dentro de `blackout-back-sett/`, ejecuta `mvn clean verify`. Las pruebas usan H2 y tokens RSA locales;
no llaman a Entra ni a Cognito. Cubren CRUD, permisos, firma, emisor, audiencia, scope, vencimiento,
tipo de token Cognito, cliente Cognito, CORS y Swagger.
