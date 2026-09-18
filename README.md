# 🏢 Sistema de Gestión de Terceros

![Spring Boot](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)
![Vaadin](https://img.shields.io/badge/Vaadin-00B4F0?style=for-the-badge&logo=vaadin&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)

> **🚀 Proyecto Desplegado:** [Acceder a la aplicación (Render)](https://app-terceros.onrender.com/login)

---

## 🎓 UTN – FRP: Programación III – Trabajo Práctico Final

El objetivo de este proyecto es demostrar la capacidad de desarrollar una aplicación web completa, integrando diferentes capas de software:

* 🎨 **Interfaz de usuario (Frontend):** Construida íntegramente con componentes de **Vaadin Hilla** y React.
* ⚙️ **Lógica de negocio (Backend):** Estructurada mediante el framework **Spring Boot**.
* 🗄️ **Diseño y gestión de datos:** Persistencia relacional manejada con **PostgreSQL**.

---

## 🎯 Objetivo Principal

El sistema tiene como finalidad la **administración de pagos a proveedores (terceros)** por parte de una institución o facultad.

### Requisitos Funcionales Implementados

1. **Gestión de Facultad 🏛️**
   * Crear y actualizar datos institucionales.
2. **Gestión de Proveedores (Terceros) 👥**
   * Operaciones completas (CRUD): Crear, Consultar, Modificar y Eliminar proveedores.
3. **Gestión de Facturas 🧾**
   * Registro y manejo de facturas.
   * Relaciones: Una factura pertenece a un proveedor y está compuesta por uno o más ítems.
4. **Gestión de Pagos 💳**
   * Seguimiento y registro de los pagos realizados a un proveedor.

---

## 🔒 Opción Avanzada: Seguridad y Control de Acceso

Se ha implementado la **Opción Avanzada** solicitada por la cátedra, agregando control de acceso al sistema:

* **Autenticación requerida:** El sistema exige credenciales al iniciar. Solo usuarios autorizados y registrados en la tabla `User` pueden ingresar.
* **Rutas protegidas:** Todas las vistas y endpoints CRUD del sistema están securizados.

---

## 🏗️ Entidades Principales

* **Facultad:** Información institucional general.
* **Proveedor (Tercero):** Personas o empresas proveedoras de bienes/servicios.
* **Factura:** Documento emitido por un proveedor (contiene múltiples Ítems).
* **Ítem de Factura:** Detalle de productos/servicios.
* **Pago:** Registro de los pagos realizados a proveedores.
* **Usuario:** Entidad utilizada para la autenticación en el sistema.

---

## 🛠️ Ejecución Local

Para correr este proyecto en tu entorno local:

1. **Restaurar base de datos:** Asegurate de tener una instancia de PostgreSQL corriendo con la base de datos necesaria.
2. **Variables de Entorno:** Configura las variables para la conexión (`PGHOST`, `PGPORT`, `PGDATABASE`, `PGUSER`, `PGPASSWORD`) o configuralo en `app/src/main/resources/application.properties`.
3. **Ejecutar el proyecto:** En la terminal, dentro de la carpeta `app/`, ejecutá:

```bash
mvn clean package -DskipTests
mvn spring-boot:run
```
