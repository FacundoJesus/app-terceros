# UTN – FRP

## Programación III – Trabajo Práctico Final

### Objetivo

El objetivo del trabajo práctico es comprobar que el alumno está en condiciones de desarrollar una aplicación web completa, abarcando:

* Interfaz de usuario (**Frontend**)
* Lógica de negocio (**Backend**)
* Diseño y gestión de datos (**Base de Datos**)

Para ello, cada equipo (máximo **dos alumnos**) deberá desarrollar la aplicación utilizando exclusivamente las siguientes tecnologías:

* **Vaadin Flow** o **Vaadin Hilla**
* **Spring Boot**
* **PostgreSQL**

---

# Opción Base: Sistema de Gestión de Terceros

El sistema tiene como finalidad la administración de pagos a proveedores (terceros) por parte de una facultad.

## Requisitos

### 1. Restauración de la Base de Datos

* Restaurar la base de datos proporcionada (estructura y datos).

### 2. Creación del Proyecto

* Crear un proyecto utilizando **Vaadin 25+**.
* Configurar las dependencias necesarias.

### 3. Gestión de Facultad

Desarrollar una interfaz que permita:

* Crear datos de la facultad.
* Actualizar datos de la facultad.

### 4. Gestión de Proveedores (Terceros)

Desarrollar una interfaz **CRUD** para:

* Crear proveedores.
* Consultar proveedores.
* Modificar proveedores.
* Eliminar proveedores.

### 5. Gestión de Facturas

Desarrollar una interfaz **CRUD** para el manejo de facturas.

Consideraciones:

* Una factura pertenece a un proveedor (tercero).
* Una factura está compuesta por uno o más ítems.
* Debe permitirse registrar, consultar, modificar y eliminar facturas.

### 6. Gestión de Pagos

Desarrollar una interfaz **CRUD** para el manejo de pagos.

Consideraciones:

* Un pago se realiza sobre un proveedor (tercero).
* Debe permitirse registrar, consultar, modificar y eliminar pagos.

---

# Opción Avanzada

Implementar control de acceso al sistema.

## Requisitos

### Gestión de Usuarios

Crear una tabla denominada `User` que almacene:

* Usuario
* Contraseña

### Seguridad

Modificar la aplicación para que:

* Solicite autenticación al ingresar.
* Permita el acceso únicamente a usuarios registrados.
* Proteja las funcionalidades del sistema mediante control de acceso.

### Consideraciones

* No se requiere una interfaz CRUD para la administración de usuarios.
* Los usuarios serán gestionados directamente desde la base de datos.

---

# Tecnologías Utilizadas

* Java
* Spring Boot
* Vaadin Flow / Vaadin Hilla
* PostgreSQL
* Maven

---

# Entidades Principales

## Facultad

Información institucional de la facultad.

## Proveedor (Tercero)

Representa a las personas o empresas proveedoras de bienes o servicios.

## Factura

Documento emitido por un proveedor.

### Relación

* Un proveedor puede tener muchas facturas.
* Una factura posee uno o más ítems.

## Ítem de Factura

Detalle de productos o servicios incluidos en una factura.

## Pago

Registro de pagos realizados a un proveedor.

### Relación

* Un proveedor puede tener múltiples pagos.

## Usuario (Opción Avanzada)

Entidad utilizada para la autenticación y autorización del sistema.

---

# Resultado Esperado

Al finalizar el trabajo práctico, el sistema deberá permitir:

* Administrar los datos de la facultad.
* Gestionar proveedores.
* Gestionar facturas y sus ítems.
* Gestionar pagos.
* (Opcional avanzado) Gestionar el acceso mediante autenticación de usuarios.

Todo ello utilizando exclusivamente Vaadin, Spring Boot y PostgreSQL.

