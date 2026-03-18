# 🐴 Sistema de Gestión de Cabalgatas

Sistema web interno para gestionar cabalgatas, reservas, caballos y guías. Reemplaza WhatsApp + Google Calendar con un sistema centralizado.

## Stack Tecnológico

| Componente | Tecnología |
|---|---|
| Backend | Java 17 + Spring Boot 3.2.5 |
| Base de Datos | PostgreSQL 17 |
| Frontend | Thymeleaf + FullCalendar.js 6 |
| UI Framework | Bootstrap 5 |
| Arquitectura | MVC |

## Requisitos Previos

- **Java 17+** (JDK instalado)
- **Maven 3.8+**
- **PostgreSQL 17** con una base de datos creada

## Configuración de Base de Datos

1. Crear la base de datos en PostgreSQL:
```sql
CREATE DATABASE cabalgatas_db;
```

2. Verificar las credenciales en `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/cabalgatas_db
spring.datasource.username=postgres
spring.datasource.password=1234
```

Las tablas se crean automáticamente al iniciar la aplicación (`ddl-auto=update`).

## Cómo Correr el Proyecto

```bash
# Compilar
mvn compile

# Ejecutar
mvn spring-boot:run

# O compilar el JAR y ejecutar
mvn package -DskipTests
java -jar target/app-cabalgatas-1.0.0.jar
```

La aplicación estará disponible en: **http://localhost:8080/**

## Estructura del Proyecto

```
src/main/java/com/cabalgatas/
├── AppCabalgatasApplication.java    # Punto de entrada
├── controller/
│   ├── CabalgataController.java     # REST + Thymeleaf (calendario)
│   ├── ReservaController.java       # REST para reservas
│   ├── CaballoController.java       # REST + Thymeleaf (CRUD caballos)
│   └── GuiaController.java          # REST + Thymeleaf (CRUD guías)
├── dto/
│   ├── CabalgataDTO.java            # Crear/editar cabalgata
│   ├── CabalgataDetalleDTO.java     # Detalle con reservas/caballos/guías
│   ├── CalendarEventDTO.java        # Formato FullCalendar
│   └── ReservaDTO.java              # Crear/editar reserva
├── entity/
│   ├── Cabalgata.java               # Entidad principal
│   ├── Reserva.java                 # Reservas de clientes
│   ├── Caballo.java                 # Caballos disponibles
│   ├── Guia.java                    # Guías de cabalgatas
│   └── EstadoCabalgata.java         # Enum de estados
├── exception/
│   ├── ValidacionException.java     # Excepción de negocio
│   └── GlobalExceptionHandler.java  # Handler global de errores
├── repository/                      # Spring Data JPA
│   ├── CabalgataRepository.java
│   ├── ReservaRepository.java
│   ├── CaballoRepository.java
│   └── GuiaRepository.java
└── service/                         # Lógica de negocio
    ├── CabalgataService.java        # Validaciones de solapamiento
    ├── ReservaService.java          # Validación de cupo
    ├── CaballoService.java
    └── GuiaService.java

src/main/resources/
├── application.properties           # Configuración
└── templates/
    ├── calendar.html                # Página principal (FullCalendar)
    ├── caballos.html                # CRUD de caballos
    └── guias.html                   # CRUD de guías
```

## Endpoints API

### Cabalgatas
| Método | URL | Descripción |
|---|---|---|
| GET | `/api/cabalgatas?start=&end=` | Listar eventos para calendario |
| GET | `/api/cabalgatas/{id}` | Detalle de cabalgata |
| POST | `/api/cabalgatas` | Crear cabalgata |
| PUT | `/api/cabalgatas/{id}` | Actualizar cabalgata |
| DELETE | `/api/cabalgatas/{id}` | Eliminar cabalgata |
| POST | `/api/cabalgatas/{id}/asignar-caballo/{caballoId}` | Asignar caballo |
| DELETE | `/api/cabalgatas/{id}/remover-caballo/{caballoId}` | Remover caballo |
| POST | `/api/cabalgatas/{id}/asignar-guia/{guiaId}` | Asignar guía |
| DELETE | `/api/cabalgatas/{id}/remover-guia/{guiaId}` | Remover guía |

### Reservas
| Método | URL | Descripción |
|---|---|---|
| GET | `/api/reservas/cabalgata/{id}` | Listar reservas de cabalgata |
| POST | `/api/reservas` | Crear reserva |
| PUT | `/api/reservas/{id}` | Actualizar reserva |
| DELETE | `/api/reservas/{id}` | Eliminar reserva |

### Caballos / Guías
| Método | URL | Descripción |
|---|---|---|
| GET | `/api/caballos` | Listar todos |
| GET | `/api/caballos/disponibles` | Listar disponibles |
| POST | `/api/caballos` | Crear |
| PUT | `/api/caballos/{id}` | Actualizar |
| DELETE | `/api/caballos/{id}` | Eliminar |

*(Mismos endpoints para `/api/guias` con `/activos` en vez de `/disponibles`)*

## Validaciones Implementadas

### Cupo Máximo
- `SUM(reservas.cantidadPersonas) + nuevasPersonas <= cupoMaximo`
- Mensaje de error claro indicando cupo disponible

### Solapamiento Horario
- Fórmula: `(startA < endB) AND (endA > startB)` en la misma fecha
- Aplica a caballos y guías: no se pueden asignar a cabalgatas solapadas

### Otras Validaciones
- Caballo debe tener `disponible = true` para ser asignado
- Guía debe tener `activo = true` para ser asignado
- No duplicar asignaciones (mismo caballo/guía en la misma cabalgata)
- Hora de fin debe ser posterior a hora de inicio
- Al reducir cupo, no puede ser menor a personas ya reservadas

## Funcionalidades del Frontend

### Calendario (página principal)
- Vista mensual, semanal y diaria con FullCalendar
- Eventos coloreados por estado (verde=programada, azul=en curso, gris=finalizada, rojo=cancelada)
- Título del evento muestra cantidad de personas
- Click en fecha → crear nueva cabalgata
- Click en evento → ver detalle completo

### Modal de Detalle
- Estadísticas: fecha, horario, personas/cupo, estado
- **Pestaña Reservas**: crear, editar y eliminar reservas
- **Pestaña Caballos**: asignar y remover caballos con selector
- **Pestaña Guías**: asignar y remover guías con selector
- Alertas visuales cuando faltan caballos o guías

### Gestión de Caballos y Guías
- Tablas con CRUD completo
- Toggles de disponibilidad/activo
- Modales para crear y editar
