UTN – FRP
Programación III – Trabajo práctico final

El objetivo del trabajo práctico es comprobar que el alumno está en condiciones de desarrollar una aplicación web, desde la interfaz de usuario (frontend), la lógica de negocio (backend) y el diseño de los datos (base de datos).
Con este propósito, se presentarán dos opciones en las que cada equipo (con un máximo de dos alumnos por cada equipo) pueda desarrollar la aplicación, usando de manera exclusiva: Vaadin Flow o Vaadin Hilla, Spring Boot y PostgreSQL.
Opción de Base:
Se requiere que el alumno pueda desarrollar, con la tecnología propuesta, la aplicación Terceros. El sistema hace referencia a la administración de pagos a proveedores (terceros) por parte de una facultad.
Los pasos a desarrollar y el orden son los siguientes:
·	Restaurar (restore) la base de datos (estructura + datos)
·	Crear un proyecto Vaadin 25+
·	Agregar dependencias
·	Construir una interfaz para Crear o Actualizar datos de la Facultad.
·	Construir una interfaz CRUD para el manejo de Proveedores (Terceros)
·	Construir una interfaz CRUD para el manejo de Facturas. Tener en cuenta que una Factura posee 1 Items que la conforma; y además, al registrar una factura, la misma está vinculada a un proveedor o tercero.
·	Construir una interfaz CRUD para el manejo de Pagos. Tener en cuenta que al resgistrar un pago, el mismo se realiza sobre un proveedor o tercero.
Opción avanzada:
Se requiere que se cree una tabla User, donde se registren los datos de usuario y password. En cuanto a la interfaz, modificar el sistema para que el mismo proporcione control de acceso. No se requiere una interfaz CRUD para la gestión de usuarios, la misma se determinará por base. 
