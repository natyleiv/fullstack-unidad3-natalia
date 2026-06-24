# Sistema de Microservicios - Completeria Vincalia

Sistema de gestion para una completeria chilena, desarrollado con arquitectura de microservicios utilizando **Spring Boot 4.0.6**, **Java 25** y **Spring Cloud 2025.1.1**.

---

## Contexto del Proyecto

El sistema resuelve la gestion integral de una PYME del rubro gastronomico (completeria), abarcando desde el catalogo de productos, gestion de clientes, pedidos, pagos, entregas a domicilio, cupones de descuento, inventario de ingredientes y proveedores. Cada dominio de negocio esta implementado como un microservicio independiente con su propia base de datos.

---

## Integrantes del Equipo

| Nombre | Correo | Microservicios asignados |
|--------|--------|--------------------------|
| Vicente Gonzalez | vicente.gonzalez@duocuc.cl | clientes-service, catalogo-service |
| Natalia Leiva | natalia.leiva@duocuc.cl | pedidos-service, delivery-service, proveedores-service |
| Carolina Cabello | car.cabellop@duocuc.cl | cupones-service, pagos-service, inventario-service |

---

## Arquitectura del Sistema

### Tecnologias

| Componente | Tecnologia |
|-----------|-----------|
| Lenguaje | Java 25 |
| Framework | Spring Boot 4.0.6 |
| Cloud | Spring Cloud 2025.1.1 |
| Discovery | Netflix Eureka |
| Gateway | Spring Cloud Gateway (WebFlux) |
| Config | Spring Cloud Config Server |
| Comunicacion | OpenFeign |
| Base de Datos | MySQL 8.0 (una por servicio) |
| Migraciones | Flyway |
| ORM | JPA / Hibernate |
| Documentacion | SpringDoc OpenAPI 3.0.2 |
| Contenedores | Docker / Docker Compose |

### Microservicios

| Servicio | Descripcion | Puerto | Base de Datos |
|----------|------------|--------|---------------|
| eureka-server | Descubrimiento de servicios | 8761 | - |
| config-server | Configuracion centralizada | 8888 | - |
| api-gateway | Puerta de enlace y enrutador | 8082 | - |
| clientes-service | Gestion de clientes | dinamico | db_clientes |
| catalogo-service | Catalogo de productos | dinamico | db_catalogo |
| cupones-service | Cupones de descuento | dinamico | db_cupones |
| pedidos-service | Gestion de pedidos | dinamico | db_pedidos |
| pagos-service | Procesamiento de pagos | dinamico | db_pagos |
| delivery-service | Gestion de entregas | dinamico | db_delivery |
| inventario-service | Ingredientes e inventario | dinamico | db_inventario |
| proveedores-service | Gestion de proveedores | dinamico | db_proveedores |

### Comunicacion entre servicios (Feign Clients)

```
pedidos-service     --> clientes-service    (valida cliente activo)
pagos-service       --> pedidos-service     (valida pedido existente)
delivery-service    --> pedidos-service     (obtiene datos del pedido)
clientes-service    --> cupones-service     (valida y aplica cupones)
catalogo-service    --> cupones-service     (obtiene descuento)
inventario-service  --> catalogo-service    (obtiene datos del producto)
```

---

## Rutas del API Gateway

Todas las peticiones se realizan a traves del API Gateway en el puerto **8082**.

| Ruta | Servicio destino |
|------|-----------------|
| `/api/clientes/**` | clientes-service |
| `/api/productos/**` | catalogo-service |
| `/api/cupones/**` | cupones-service |
| `/api/pedidos/**` | pedidos-service |
| `/api/pagos/**` | pagos-service |
| `/api/entregas/**` | delivery-service |
| `/api/ingredientes/**` | inventario-service |
| `/api/proveedores/**` | proveedores-service |

---

## Documentacion Swagger

La documentacion OpenAPI esta centralizada en el API Gateway.

- **Swagger UI:** [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)

Desde la lista desplegable de Swagger UI se puede acceder a la documentacion de cada microservicio:

| Servicio | URL de docs |
|----------|------------|
| Catalogo | `/v3/api-docs/catalogo` |
| Clientes | `/v3/api-docs/clientes` |
| Pedidos | `/v3/api-docs/pedidos` |
| Delivery | `/v3/api-docs/delivery` |
| Cupones | `/v3/api-docs/cupones` |
| Inventario | `/v3/api-docs/inventario` |
| Pagos | `/v3/api-docs/pagos` |
| Proveedores | `/v3/api-docs/proveedores` |

---

## Guia de Despliegue

### Opcion 1: Docker (Recomendado)

**Requisitos:** Docker Desktop instalado y en ejecucion.

```bash
# Desde la carpeta FullStackUnidad3/
docker compose up --build -d
```

Esto levanta automaticamente:
- MySQL con las 8 bases de datos creadas via `init-db/`
- Eureka Server, Config Server
- Los 8 microservicios de negocio
- API Gateway

**Verificar que todo funciona:**

```bash
# Ver estado de contenedores
docker compose ps

# Probar un endpoint
curl http://localhost:8082/api/productos

# Ver servicios registrados en Eureka
# Abrir en navegador: http://localhost:8761
```

**Detener el sistema:**

```bash
docker compose down
```

### Opcion 2: Local / Hibrido (Sin Docker)

**Requisitos:**
- Java 25 instalado
- MySQL activo en el puerto 3306 (por ejemplo con XAMPP)
- Maven 3.9+

**Paso 1:** Crear las bases de datos manualmente en MySQL:

```sql
CREATE DATABASE IF NOT EXISTS db_clientes;
CREATE DATABASE IF NOT EXISTS db_catalogo;
CREATE DATABASE IF NOT EXISTS db_pedidos;
CREATE DATABASE IF NOT EXISTS db_delivery;
CREATE DATABASE IF NOT EXISTS db_cupones;
CREATE DATABASE IF NOT EXISTS db_inventario;
CREATE DATABASE IF NOT EXISTS db_pagos;
CREATE DATABASE IF NOT EXISTS db_proveedores;
```

**Paso 2:** Levantar los servicios en este orden:

```bash
# 1. Eureka Server
cd eureka-server && mvn spring-boot:run

# 2. Config Server (en otra terminal)
cd config-server && mvn spring-boot:run

# 3. Microservicios de negocio (cada uno en su terminal)
cd clientes-service && mvn spring-boot:run
cd catalogo-service && mvn spring-boot:run
cd cupones-service && mvn spring-boot:run
cd pedidos-service && mvn spring-boot:run
cd pagos-service && mvn spring-boot:run
cd delivery-service && mvn spring-boot:run
cd inventario-service && mvn spring-boot:run
cd proveedores-service && mvn spring-boot:run

# 4. API Gateway (al final)
cd api-gateway && mvn spring-boot:run
```

**Verificar:** Abrir [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)

---

## Pruebas Unitarias

Cada microservicio de negocio cuenta con pruebas unitarias en las capas de **Service** y **Controller**.

Para ejecutar los tests de un servicio:

```bash
cd <nombre-service>
mvn test
```

---

## Estructura del Repositorio

```
FullStackUnidad3/
  docker-compose.yml
  init-db/
  eureka-server/
  config-server/
  api-gateway/
  clientes-service/
  catalogo-service/
  cupones-service/
  pedidos-service/
  pagos-service/
  delivery-service/
  inventario-service/
  proveedores-service/
```
