# REQUERIMIENTOS FUTUROS - SISTEMA SACARS

**Sistema de Ventas de Hot Wheels - Funcionalidades Pendientes de Implementación**  
**Fecha:** 31 de enero de 2026  
**Versión:** 1.0

---

## INTRODUCCIÓN

Este documento detalla los requerimientos funcionales que se implementarán en el sistema SaCars para completar el panel de administración y agregar funcionalidades avanzadas de gestión. Actualmente, el sistema cuenta con la estructura HTML del panel de administración, pero la funcionalidad backend y la interacción dinámica están pendientes de desarrollo.

---

## 1. DASHBOARD ADMINISTRATIVO CON MÉTRICAS EN TIEMPO REAL

### Descripción del Proceso
El dashboard administrativo será el centro de control principal para los administradores del sistema, proporcionando una vista completa y actualizada del estado del negocio. Cuando un administrador inicie sesión y acceda al panel (`/admin/dashboard`), el sistema cargará automáticamente estadísticas clave en tiempo real.

El dashboard presentará cuatro tarjetas de métricas principales que se actualizarán dinámicamente:

**Ingresos del Mes:**
El sistema calculará automáticamente el total de ventas del mes actual sumando todos los pedidos con estado COMPLETADO cuya fecha esté dentro del mes en curso. Además, comparará este valor con el mes anterior para calcular y mostrar el porcentaje de crecimiento o decrecimiento. El valor se mostrará formateado en soles (S/) con dos decimales. Por ejemplo: "S/ 15,450.00" con un indicador "+15% respecto al mes anterior" en color verde si es positivo o rojo si es negativo.

**Pedidos Pendientes:**
El sistema contará todos los pedidos que tengan estado "PENDIENTE" en la base de datos. Este número indicará cuántos pedidos requieren atención inmediata del administrador. Si el número supera un umbral configurable (por ejemplo, 10 pedidos), se mostrará un indicador visual de alerta (color rojo o animación pulsante). El administrador podrá hacer clic en esta tarjeta para ser redirigido directamente a la página de gestión de pedidos con un filtro pre-aplicado de "PENDIENTES".

**Clientes Registrados:**
El sistema contará el número total de usuarios con rol "cliente" que estén activos en la base de datos. Adicionalmente, calculará cuántos usuarios nuevos se registraron en los últimos 7 días para mostrar un indicador de crecimiento, por ejemplo: "+5 nuevos esta semana". Esta métrica ayuda a monitorear la adquisición de clientes.

**Productos en Stock:**
El sistema contará cuántos productos están activos y tienen stock disponible (stock > 0). También identificará productos con stock bajo (stock < 5) y mostrará una alerta si hay productos que requieren reposición urgente. El administrador podrá hacer clic para ver el detalle del inventario.

**Sección de Acciones Rápidas:**
El dashboard incluirá botones de acceso rápido a las funciones más utilizadas:
- Agregar nuevo producto
- Ver listado de pedidos
- Gestionar usuarios
- Generar reportes

**Tabla de Pedidos Recientes:**
En la parte inferior del dashboard, se mostrará una tabla con los últimos 10 pedidos realizados en el sistema, mostrando:
- ID del pedido
- Nombre del cliente
- Total pagado
- Estado actual (con color distintivo por cada estado)
- Fecha de creación
- Botones de acción: Ver detalle, Cambiar estado

La tabla permitirá al administrador obtener una vista rápida de la actividad reciente y tomar acciones inmediatas sobre pedidos que requieren atención.

**Actualización Automática:**
El sistema implementará actualización automática de las métricas cada 30 segundos mediante peticiones AJAX, permitiendo que el administrador vea cambios en tiempo real sin necesidad de recargar la página manualmente.

**Datos a Mostrar:**
- Total de ingresos del mes actual
- Comparativa porcentual con mes anterior
- Número de pedidos pendientes
- Total de clientes registrados
- Nuevos registros de la semana
- Total de productos con stock
- Alertas de stock bajo
- Últimos 10 pedidos con información completa

**Componentes Técnicos a Implementar:**
- Controller: `AdminDashboardController.java` con endpoint `/api/admin/dashboard/stats`
- Service: `DashboardService.java` para cálculos de métricas
- DTO: `DashboardStatsDTO.java` para encapsular estadísticas
- JavaScript: `dashboard-admin.js` para carga dinámica de datos
- Template: `admin/dashboard.html` (ya existe, agregar funcionalidad)
- Endpoints necesarios:
  - `GET /api/admin/dashboard/stats` - Obtener todas las métricas
  - `GET /api/admin/dashboard/recent-orders` - Últimos pedidos
  - `GET /api/admin/dashboard/alerts` - Alertas del sistema

---

## 2. GESTIÓN COMPLETA DE USUARIOS (CRUD)

### Descripción del Proceso
El sistema permitirá a los administradores gestionar todos los usuarios registrados en la plataforma mediante operaciones CRUD completas (Crear, Leer, Actualizar, Eliminar/Desactivar).

**Listar Usuarios:**
Cuando el administrador acceda a `/admin/usuarios`, el sistema mostrará una tabla completa con todos los usuarios del sistema, tanto clientes como administradores. La tabla incluirá las siguientes columnas:
- ID de usuario
- Nombre completo (nombre + apellido)
- DNI
- Correo electrónico
- Teléfono
- Rol (Cliente/Administrador) con etiqueta de color
- Estado (Activo/Inactivo) con indicador visual
- Fecha de registro
- Acciones (botones: Ver, Editar, Cambiar estado, Cambiar rol)

El sistema implementará funcionalidades de búsqueda y filtrado:
- **Búsqueda en tiempo real:** Campo de texto que filtrará usuarios por nombre, email o DNI mientras el administrador escribe
- **Filtro por rol:** Selector para mostrar solo clientes, solo administradores, o todos
- **Filtro por estado:** Selector para mostrar solo usuarios activos, inactivos, o todos
- **Ordenamiento:** Posibilidad de ordenar por cualquier columna (nombre, fecha de registro, etc.)
- **Paginación:** Mostrar 20 usuarios por página con controles de navegación

**Ver Detalle de Usuario:**
Al hacer clic en "Ver" de un usuario específico, el sistema abrirá un modal o página de detalle mostrando:
- Toda la información personal del usuario
- Historial de pedidos realizados (número, fecha, total, estado)
- Total gastado en la plataforma
- Fecha de último acceso
- Número de productos en carrito actual
- Estadísticas de comportamiento (productos favoritos, frecuencia de compra)

**Crear Usuario (Registrar desde Admin):**
El administrador podrá crear cuentas de usuario manualmente accediendo a un formulario con los siguientes campos:
- Nombre (obligatorio)
- Apellido (obligatorio)
- DNI (obligatorio, validación de 8 dígitos, único)
- Email (obligatorio, validación de formato, único)
- Contraseña (obligatoria, mínimo 6 caracteres)
- Confirmar contraseña
- Teléfono (opcional)
- Dirección (opcional)
- Rol (Cliente/Administrador) - Selector
- Estado inicial (Activo/Inactivo) - Por defecto Activo

El sistema validará que el email y DNI no existan previamente. Si el registro es exitoso, encriptará la contraseña con BCrypt y creará el usuario. Se enviará opcionalmente un correo de bienvenida al usuario creado con sus credenciales de acceso.

**Editar Usuario:**
El administrador podrá modificar la información de cualquier usuario haciendo clic en el botón "Editar". Se abrirá un formulario prellenado con los datos actuales del usuario donde se podrán modificar:
- Nombre y apellido
- DNI (validando que no coincida con otro usuario)
- Email (validando unicidad)
- Teléfono
- Dirección
- Estado (Activo/Inactivo)
- Rol (Cliente/Administrador)

**Importante:** No se podrá editar la contraseña desde esta vista por seguridad. Existirá una función separada de "Restablecer contraseña" que generará una contraseña temporal y la enviará por email.

El sistema validará que no pueda modificarse el rol del último administrador activo (debe existir al menos un administrador en el sistema).

**Cambiar Estado de Usuario:**
El administrador podrá activar o desactivar usuarios. Al desactivar un usuario:
- No podrá iniciar sesión en el sistema
- Sus pedidos pendientes permanecerán en el sistema
- Su información se mantendrá en la base de datos
- Podrá ser reactivado en cualquier momento

Esta es una alternativa segura a eliminar usuarios físicamente, manteniendo la integridad referencial de la base de datos.

**Cambiar Rol de Usuario:**
El administrador podrá promover un cliente a administrador o degradar un administrador a cliente mediante un botón específico. El sistema:
- Solicitará confirmación antes de cambiar el rol
- Validará que exista al menos un administrador activo después del cambio
- Registrará en logs quién realizó el cambio y cuándo

**Eliminar Usuario (Opcional/Avanzado):**
Opcionalmente, el sistema podría permitir la eliminación lógica de usuarios. Esta operación:
- Marcará el usuario como eliminado (campo `eliminado = true`)
- Ocultará el usuario de las listas normales
- Mantendrá los datos para integridad histórica
- Solo será reversible por un super-administrador

**Datos Gestionables:**
- Información personal completa de usuarios
- Roles y permisos
- Estados de cuenta (activo/inactivo)
- Creación manual de cuentas
- Modificación de datos existentes
- Historial de actividad del usuario

**Componentes Técnicos a Implementar:**
- Controller: `AdminUsuarioController.java` con endpoints REST
- Service: `AdminUsuarioService.java` para lógica de negocio
- DTO: `UsuarioAdminDTO.java` para transferencia de datos
- Template: `admin/usuarios.html` (crear)
- JavaScript: `usuarios-admin.js` para interacción
- Endpoints necesarios:
  - `GET /api/admin/usuarios` - Listar todos con filtros
  - `GET /api/admin/usuarios/{id}` - Detalle de usuario
  - `POST /api/admin/usuarios` - Crear nuevo usuario
  - `PUT /api/admin/usuarios/{id}` - Actualizar usuario
  - `PATCH /api/admin/usuarios/{id}/estado` - Cambiar estado
  - `PATCH /api/admin/usuarios/{id}/rol` - Cambiar rol
  - `DELETE /api/admin/usuarios/{id}` - Eliminar/desactivar
  - `POST /api/admin/usuarios/{id}/reset-password` - Restablecer contraseña

---

## 3. CRUD COMPLETO DE PRODUCTOS

### Descripción del Proceso
El sistema permitirá a los administradores gestionar el catálogo completo de productos mediante operaciones CRUD, proporcionando control total sobre el inventario de Hot Wheels.

**Listar Productos:**
En la ruta `/admin/productos`, el sistema mostrará una tabla con todos los productos del inventario, incluyendo productos activos e inactivos. La tabla mostrará:
- ID del producto
- Imagen miniatura
- Nombre del producto
- Descripción (truncada, 50 caracteres)
- Precio actual
- Stock disponible con indicador de alerta (rojo si stock < 5, amarillo si stock < 10)
- Categoría
- Estado (Activo/Inactivo)
- Destacado (Sí/No)
- Fecha de creación
- Acciones (Ver, Editar, Eliminar/Desactivar, Duplicar)

**Funcionalidades de filtrado y búsqueda:**
- Búsqueda por nombre o descripción en tiempo real
- Filtro por categoría
- Filtro por estado (Activo/Inactivo)
- Filtro por stock (Con stock / Sin stock / Stock bajo)
- Filtro por destacados
- Ordenamiento por nombre, precio, stock o fecha
- Paginación de 15 productos por página

**Ver Detalle de Producto:**
Al seleccionar "Ver" en un producto, el sistema mostrará una vista completa con:
- Toda la información del producto
- Imagen en tamaño completo
- Historial de ventas (cuántas unidades se han vendido)
- Productos relacionados o similares
- Estadísticas: visitas, agregados al carrito, conversión de venta

**Crear Nuevo Producto:**
El administrador accederá a un formulario para agregar productos nuevos con los siguientes campos:

*Información Básica:*
- **Nombre:** Campo de texto obligatorio, máx. 100 caracteres
- **Descripción:** Área de texto obligatoria para descripción detallada
- **Precio:** Campo numérico obligatorio, validación de valor positivo, formato decimal con 2 decimales
- **Stock Inicial:** Campo numérico obligatorio, entero positivo
- **SKU/Código:** Campo único para identificación interna (opcional, auto-generado si está vacío)

*Categorización:*
- **Categoría:** Selector desplegable con categorías existentes (Deportivos, Clásicos, Tuners, Carreras, Fantasía, Otros)
- **Tags/Etiquetas:** Campo de texto para palabras clave separadas por comas (opcional)

*Visualización:*
- **Imagen Principal:** Campo de carga de archivo (validar formatos JPG, PNG, WebP, máx. 5MB)
- **Imágenes Adicionales:** Opción de cargar hasta 4 imágenes secundarias
- **URL de imagen:** Alternativamente, campo de texto para URL de imagen externa

*Configuración:*
- **Producto Destacado:** Checkbox para marcar si el producto aparecerá en secciones destacadas
- **Estado:** Selector (Activo/Inactivo) - Por defecto Activo
- **Permitir agotado:** Checkbox para indicar si se puede comprar con stock 0 (pre-orden)

*Especificaciones Adicionales (Opcional/Futuro):*
- Dimensiones del producto
- Peso
- Marca/Fabricante
- Año de lanzamiento
- Escala (1:64, etc.)

El formulario incluirá vista previa en tiempo real de cómo se verá el producto en el catálogo. Al enviar el formulario, el sistema validará todos los campos, procesará y subirá las imágenes al servidor, y creará el registro en la base de datos.

**Editar Producto:**
El administrador podrá modificar cualquier producto existente. El sistema cargará un formulario idéntico al de creación pero prellenado con los datos actuales del producto. Se podrán modificar todos los campos excepto el ID y la fecha de creación.

Al editar el precio, el sistema podría opcionalmente:
- Guardar un historial de cambios de precio
- Notificar sobre el impacto en pedidos pendientes
- Actualizar automáticamente productos en carritos de usuarios

Al modificar stock, el sistema:
- Validará que el nuevo stock no sea negativo
- Actualizará inmediatamente la disponibilidad en el catálogo
- Enviará alertas si el stock baja de un umbral crítico

**Gestión de Imágenes:**
Dentro de la edición, el administrador podrá:
- Reemplazar la imagen principal
- Agregar o eliminar imágenes secundarias
- Reorganizar el orden de las imágenes
- Optimizar imágenes automáticamente (redimensionar, comprimir)

**Eliminar/Desactivar Producto:**
El sistema ofrecerá dos opciones:
1. **Desactivar:** Cambia el estado del producto a inactivo. El producto desaparece del catálogo público pero se mantiene en la base de datos. Los pedidos existentes con ese producto no se afectan.
2. **Eliminar:** Eliminación física del producto (con confirmación múltiple). Esta operación solo se permitirá si:
   - El producto no tiene pedidos asociados
   - El producto no está en ningún carrito
   - No tiene referencias en facturas

Para mayor seguridad, se recomienda usar desactivación en lugar de eliminación.

**Duplicar Producto:**
Funcionalidad útil para crear variantes de productos similares. El sistema:
- Copiará todos los datos del producto seleccionado
- Agregará " - Copia" al nombre
- Generará un nuevo ID
- Permitirá al administrador editar los campos antes de guardar

**Gestión de Stock en Masa:**
Opción para actualizar el stock de múltiples productos simultáneamente:
- Seleccionar productos mediante checkboxes
- Opciones: Agregar cantidad, Reducir cantidad, Establecer cantidad fija
- Registrar movimiento de inventario con motivo (Recepción, Ajuste, Corrección, etc.)

**Importación/Exportación de Productos:**
- **Exportar:** Descargar catálogo completo en formato CSV o Excel
- **Importar:** Carga masiva de productos desde archivo CSV con validación de formato

**Datos Gestionables:**
- Información completa de productos
- Precios y stock
- Imágenes y multimedia
- Categorización y etiquetado
- Estados de publicación
- Configuración de destacados
- Historial de cambios

**Componentes Técnicos a Implementar:**
- Controller: `AdminProductoController.java`
- Service: `AdminProductoService.java`, `ImagenService.java`
- DTO: `ProductoAdminDTO.java`, `ProductoCreateDTO.java`, `ProductoUpdateDTO.java`
- Repository: Extensión de `ProductoRepository.java` con consultas admin
- Template: `admin/productos.html`, `admin/producto-form.html` (crear)
- JavaScript: `productos-admin.js`
- Endpoints necesarios:
  - `GET /api/admin/productos` - Listar con filtros y paginación
  - `GET /api/admin/productos/{id}` - Detalle de producto
  - `POST /api/admin/productos` - Crear producto
  - `PUT /api/admin/productos/{id}` - Actualizar producto
  - `PATCH /api/admin/productos/{id}/stock` - Actualizar solo stock
  - `PATCH /api/admin/productos/{id}/estado` - Cambiar estado
  - `DELETE /api/admin/productos/{id}` - Eliminar producto
  - `POST /api/admin/productos/{id}/duplicate` - Duplicar producto
  - `POST /api/admin/productos/import` - Importar desde CSV
  - `GET /api/admin/productos/export` - Exportar a CSV

---

## 4. GESTIÓN AVANZADA DE PEDIDOS

### Descripción del Proceso
El sistema proporcionará a los administradores herramientas completas para gestionar todos los pedidos del sistema, incluyendo cambio de estados, seguimiento y acciones específicas por pedido.

**Listar Todos los Pedidos:**
En `/admin/pedidos`, el sistema mostrará una tabla con todos los pedidos registrados, mostrando:
- ID del pedido
- Número de factura asociado
- Nombre del cliente (con enlace a perfil del usuario)
- Fecha del pedido
- Productos incluidos (cantidad de ítems)
- Total pagado
- Estado actual (con color distintivo)
- Método de pago
- Zona de envío
- Acciones (Ver detalle, Cambiar estado, Imprimir factura, Contactar cliente)

**Sistema de Estados de Pedidos:**
El sistema implementará un flujo de estados configurable:
1. **PENDIENTE** - Pedido recién creado, requiere procesamiento
2. **CONFIRMADO** - Pedido revisado y confirmado por admin
3. **PREPARANDO** - Productos siendo empaquetados
4. **EN_CAMINO** - Pedido enviado al cliente
5. **ENTREGADO** - Pedido recibido por el cliente
6. **CANCELADO** - Pedido cancelado por admin o cliente
7. **DEVUELTO** - Producto devuelto por el cliente

Cada cambio de estado será registrado con timestamp y usuario que realizó el cambio.

**Filtros y Búsqueda de Pedidos:**
- Búsqueda por ID de pedido o número de factura
- Búsqueda por nombre o email del cliente
- Filtro por estado (múltiple selección)
- Filtro por rango de fechas (desde - hasta)
- Filtro por rango de monto (mínimo - máximo)
- Filtro por método de pago
- Filtro por zona de envío
- Ordenamiento por fecha, total, estado

**Vista Detallada de Pedido:**
Al seleccionar un pedido, el sistema mostrará una vista completa con secciones organizadas:

*Información General:*
- ID de pedido y número de factura
- Estado actual con timeline visual de cambios de estado
- Fecha de creación y última actualización
- Método de pago utilizado

*Información del Cliente:*
- Nombre completo y email (con botones de contacto rápido)
- Teléfono (con botón de llamar/WhatsApp)
- Historial de pedidos previos del cliente
- Calificación del cliente (si existe sistema de valoraciones)

*Detalles de Envío:*
- Dirección completa de entrega
- Zona y código postal
- Costo de envío
- Opción de editar dirección si el pedido no ha sido enviado

*Productos del Pedido:*
Tabla detallada con:
- Imagen miniatura del producto
- Nombre del producto (con enlace)
- Precio unitario al momento de la compra
- Cantidad
- Subtotal
- Disponibilidad actual de stock

Totales:
- Subtotal de productos
- Costo de envío
- Total pagado

*Historial de Acciones:*
Registro de todas las acciones realizadas sobre el pedido:
- Fecha y hora
- Acción realizada (creado, confirmado, enviado, etc.)
- Usuario que realizó la acción
- Observaciones o comentarios

**Cambio de Estado de Pedido:**
El administrador podrá cambiar el estado del pedido mediante:
- Selector desplegable de estados disponibles
- Campo obligatorio de observación/motivo del cambio
- Botón de confirmación

Al cambiar el estado:
- El sistema validará que el cambio de estado sea lógico (no permitir retrocesos inválidos)
- Registrará el cambio en el historial
- Enviará opcionalmente notificación por email al cliente
- Actualizará timestamps relevantes

**Cancelar Pedido:**
Funcionalidad especial para cancelar pedidos con proceso controlado:
1. Administrador selecciona "Cancelar pedido"
2. Sistema muestra modal solicitando motivo de cancelación
3. Opciones de reembolso (si aplica)
4. Confirmación de la acción
5. El sistema:
   - Cambia estado a CANCELADO
   - Devuelve el stock de los productos al inventario
   - Registra el motivo en el historial
   - Notifica al cliente
   - Marca la factura como anulada

**Imprimir/Descargar Documentos:**
Botones para generar e imprimir:
- Factura del pedido en formato PDF
- Etiqueta de envío con código de barras
- Packing slip (lista de empaque)
- Comprobante para el cliente

**Contactar Cliente:**
Botones de acción rápida para:
- Enviar email al cliente (abre formulario de email)
- Copiar teléfono al portapapeles
- Abrir WhatsApp Web con el número del cliente
- Ver perfil completo del cliente

**Agregar Observaciones/Notas:**
Campo de texto donde el administrador puede agregar notas internas sobre el pedido:
- Problemas detectados
- Solicitudes especiales del cliente
- Recordatorios para seguimiento
- Comunicaciones con el cliente

Las notas serán visibles solo para administradores y se registrarán con fecha y autor.

**Gestión de Devoluciones (Opcional/Avanzado):**
Si un cliente solicita devolución:
- Crear caso de devolución asociado al pedido
- Registrar motivo de la devolución
- Proceso de aprobación
- Devolución de dinero o emisión de crédito
- Actualización de stock cuando se reciba el producto

**Estadísticas por Pedido:**
- Tiempo de procesamiento (desde creación hasta entrega)
- Rentabilidad del pedido (considerando costos)
- Comparativa con pedidos similares

**Componentes Técnicos a Implementar:**
- Controller: `AdminPedidoController.java`
- Service: `AdminPedidoService.java`, `NotificacionService.java`
- DTO: `PedidoAdminDTO.java`, `CambioEstadoDTO.java`
- Model: Agregar campos de auditoría a `Pedido.java`
- Template: `admin/pedidos.html`, `admin/pedido-detalle.html` (crear)
- JavaScript: `pedidos-admin.js`
- Endpoints necesarios:
  - `GET /api/admin/pedidos` - Listar con filtros
  - `GET /api/admin/pedidos/{id}` - Detalle completo
  - `PATCH /api/admin/pedidos/{id}/estado` - Cambiar estado
  - `POST /api/admin/pedidos/{id}/cancelar` - Cancelar pedido
  - `POST /api/admin/pedidos/{id}/notas` - Agregar nota
  - `GET /api/admin/pedidos/{id}/historial` - Ver historial de cambios
  - `GET /api/admin/pedidos/{id}/factura-pdf` - Descargar factura
  - `POST /api/admin/pedidos/{id}/notificar` - Enviar notificación a cliente

---

## 5. SISTEMA DE REPORTES Y ESTADÍSTICAS

### Descripción del Proceso
El sistema generará reportes visuales e informes descargables para análisis del negocio, permitiendo a los administradores tomar decisiones basadas en datos reales.

**Página de Reportes:**
En `/admin/reportes`, el administrador encontrará un panel con múltiples secciones de reportes categorizados.

**1. Reportes de Ventas:**

*Reporte de Ventas por Período:*
El administrador podrá generar reportes de ventas seleccionando:
- Período: Hoy, Ayer, Últimos 7 días, Últimos 30 días, Este mes, Mes pasado, Rango personalizado
- Agrupación: Por día, Por semana, Por mes

El reporte mostrará:
- Gráfico de líneas o barras con evolución de ventas en el tiempo
- Total de ventas del período
- Número de pedidos realizados
- Ticket promedio (total ventas / número de pedidos)
- Comparativa con período anterior
- Productos más vendidos en ese período
- Horarios pico de ventas

*Reporte de Ventas por Producto:*
- Listado de productos con unidades vendidas
- Ingresos generados por cada producto
- Productos más rentables
- Productos con menos ventas (para identificar inventario de lento movimiento)
- Gráfico circular o de barras de participación de cada producto en ventas totales

*Reporte de Ventas por Categoría:*
- Ingresos por cada categoría
- Número de productos vendidos por categoría
- Categorías más populares
- Gráfico de distribución de ventas por categoría

**2. Reportes de Clientes:**

*Reporte de Clientes:*
- Total de clientes registrados
- Clientes activos (que han comprado)
- Clientes inactivos (registrados pero sin compras)
- Tasa de conversión (% de registrados que compraron)
- Nuevos clientes por período
- Gráfico de crecimiento de base de clientes

*Reporte de Mejores Clientes:*
- Top 10 clientes por monto gastado
- Top 10 clientes por frecuencia de compra
- Valor promedio de vida del cliente (CLV)
- Clientes recurrentes vs clientes de una sola compra

**3. Reportes de Inventario:**

*Reporte de Stock:*
- Productos con stock crítico (< 5 unidades)
- Productos sin stock
- Productos con stock alto (posible sobrestock)
- Valor total del inventario (suma de precio × stock)
- Alertas de reposición necesaria

*Reporte de Movimientos de Inventario:*
- Entradas y salidas de stock por período
- Productos más rotados
- Productos de lento movimiento
- Análisis de rotación de inventario

**4. Reportes Financieros:**

*Reporte de Ingresos:*
- Ingresos brutos totales por período
- Desglose de ingresos por método de pago
- Ingresos por zona geográfica
- Costos de envío recaudados
- Gráfico de evolución de ingresos

*Reporte de Facturación:*
- Total de facturas emitidas
- Valor promedio de factura
- Facturas anuladas
- Estado de facturación (emitidas, pendientes, pagadas)

**5. Reportes de Pedidos:**

*Reporte de Estado de Pedidos:*
- Distribución de pedidos por estado
- Tiempo promedio de procesamiento
- Pedidos pendientes por antigüedad
- Tasa de cancelación
- Pedidos completados vs totales

*Reporte de Zonas de Envío:*
- Pedidos por zona
- Ingresos por zona
- Costos de envío por zona
- Zonas más rentables

**Funcionalidades de los Reportes:**

*Personalización:*
- Selección de rango de fechas personalizado
- Filtros múltiples (por producto, categoría, cliente, zona)
- Comparación con períodos anteriores
- Opciones de agrupación de datos

*Visualización:*
- Gráficos interactivos (barras, líneas, circular, área)
- Tablas detalladas con datos
- Tarjetas de métricas clave (KPIs)
- Indicadores de crecimiento/decrecimiento con colores

*Exportación:*
- Descargar reportes en formato PDF con gráficos
- Exportar datos a Excel/CSV para análisis externo
- Imprimir reportes directamente
- Programar reportes automáticos por email (opcional)

*Actualización en Tiempo Real:*
- Botón de refrescar datos
- Actualización automática cada X minutos
- Indicador de última actualización

**Métricas Clave (KPIs) del Dashboard de Reportes:**
- Ventas totales del período
- Crecimiento porcentual vs período anterior
- Número de transacciones
- Ticket promedio
- Tasa de conversión
- Productos en inventario
- Valor del inventario
- Clientes nuevos
- Tasa de retención de clientes

**Reportes Pre-configurados:**
Botones de acceso rápido a reportes comunes:
- Reporte Diario (resumen de actividad del día)
- Reporte Semanal
- Reporte Mensual
- Reporte de Cierre de Mes
- Reporte Anual

**Componentes Técnicos a Implementar:**
- Controller: `ReporteController.java`
- Service: `ReporteService.java`, `EstadisticaService.java`
- DTO: `ReporteVentasDTO.java`, `ReporteInventarioDTO.java`, etc.
- Libraries: Chart.js o similar para gráficos, Apache POI para Excel
- Template: `admin/reportes.html` (crear)
- JavaScript: `reportes-admin.js`, librerías de gráficos
- Endpoints necesarios:
  - `GET /api/admin/reportes/ventas` - Reporte de ventas
  - `GET /api/admin/reportes/productos` - Reporte de productos
  - `GET /api/admin/reportes/clientes` - Reporte de clientes
  - `GET /api/admin/reportes/inventario` - Reporte de inventario
  - `GET /api/admin/reportes/financiero` - Reporte financiero
  - `POST /api/admin/reportes/export-pdf` - Exportar a PDF
  - `POST /api/admin/reportes/export-excel` - Exportar a Excel

---

## 6. GESTIÓN SIMPLE DE STOCK (Ajustado para Negocio Local)

### Descripción del Proceso
El sistema implementará un módulo **simple y práctico** para el control básico del inventario de Hot Wheels, orientado a un negocio local sin necesidad de complejidades empresariales. El enfoque es **facilidad de uso** y **funcionalidad esencial**.

> **Nota:** Este módulo está diseñado específicamente para un negocio local que vende productos físicos (autos Hot Wheels) con entrega a domicilio. No requiere sistemas complejos de gestión de inventario multinivel.

**Vista Rápida de Stock:**
En el propio **Dashboard Principal** del administrador, se mostrará una sección con:
- Total de productos en catálogo
- Total de unidades disponibles en stock
- **Alertas visuales destacadas:**
  - 🔴 Productos SIN STOCK (0 unidades) - Requieren compra urgente
  - 🟡 Productos con STOCK BAJO (menos de 3 unidades) - Planificar reposición pronto

**Sección de Inventario (Vista Completa):**
En `/admin/inventario`, tabla simple con todos los productos mostrando:
- Imagen miniatura del producto
- Nombre del producto
- Precio actual
- **Stock actual** (número destacado con color según estado)
- Estado visual: ✅ OK (verde) / ⚠️ Bajo (amarillo) / ❌ Agotado (rojo)
- Botón de acción rápida: **"+ Agregar Stock"**

**Agregar Stock (Funcionalidad Principal):**
Cuando el administrador compra más carritos y necesita actualizarlos en el sistema:

1. Click en botón **"+ Agregar Stock"** de un producto
2. Se abre un modal simple:
   - Producto seleccionado (nombre e imagen)
   - Stock actual mostrado
   - Campo: **"¿Cuántas unidades compraste?"** (número a sumar)
   - Campo opcional: **"Nota"** (ej: "Compré en Toy Store, factura #123")
   - Botón: **"Agregar"**

3. Al confirmar:
   - El stock se incrementa automáticamente
   - Se guarda un registro simple en el historial
   - Se actualiza la vista inmediatamente
   - Si había alerta de stock bajo, desaparece

**Opción de Agregar Stock Masivo:**
Cuando el administrador compra varios productos a la vez:
- Botón: **"Agregar Stock a Varios Productos"**
- Modal con lista de productos (checkboxes)
- Campo: "Cantidad a agregar a CADA producto seleccionado"
- Campo opcional: "Nota general" (ej: "Restock mensual enero 2026")
- El sistema suma la cantidad a todos los seleccionados

**Descuento Automático al Vender:**
El sistema hará el descuento de stock **automáticamente** cuando:
- Se complete un pedido (estado COMPLETADO/ENTREGADO)
- Por cada producto en el pedido, se resta la cantidad vendida
- **No requiere acción manual del administrador**

**Historial Simple de Movimientos:**
Tabla básica mostrando los últimos movimientos de stock:
- Fecha y hora
- Producto
- Acción: **"+ Agregó 5 unidades"** o **"- Vendió 2 unidades (Pedido #15)"**
- Nota (si la hay)
- Stock resultante

Filtros simples:
- Ver movimientos de un producto específico
- Ver solo agregados o solo ventas
- Filtro por fecha (últimos 7 días, 30 días, personalizado)

**NO incluye** (complejidades innecesarias para negocio local):
- ❌ Tipos complejos de movimientos (merma, transferencia, devoluciones)
- ❌ Auditorías detalladas ni conteos físicos digitalizados
- ❌ Proyecciones predictivas ni análisis de rotación
- ❌ Configuración de stock mínimo/máximo por producto
- ❌ Lead times ni puntos de reorden automatizados
- ❌ Proveedores ni órdenes de compra

**Alertas Automáticas Simples:**
El sistema mostrará notificaciones cuando:
- Un producto llegue a **0 unidades**: "¡Agotado! Necesitas comprar más del Camaro 67"
- Un producto tenga **menos de 3 unidades**: "Stock bajo del Driftsta, considera reponerlo"

Las alertas aparecen en el dashboard y opcionalmente se pueden enviar por email al administrador.

**Ajuste Manual de Stock (Para Correcciones):**
Si el administrador detecta que el stock en el sistema no coincide con lo que tiene físicamente:
- Botón: **"Ajustar Stock"** 
- Campo: "Stock real que tienes físicamente"
- El sistema compara con el stock del sistema
- Muestra la diferencia: "El sistema tiene 8, tú tienes 6. Faltarían 2."
- Campo: "Motivo del ajuste" (ej: "Se rompió una unidad", "Error de conteo anterior")
- Al confirmar, actualiza al valor correcto y registra el ajuste en historial

**Búsqueda Rápida:**
- Campo de búsqueda para encontrar productos rápidamente por nombre
- Ver solo productos sin stock
- Ver solo productos con stock bajo

**Componentes Técnicos a Implementar:**
- Model: `MovimientoStock.java` (nueva entidad **simple**)
- Controller: `StockController.java`
- Service: `StockService.java`
- Repository: `MovimientoStockRepository.java`
- DTO: `AgregarStockDTO.java`, `AjustarStockDTO.java`
- Template: `admin/inventario.html` (crear simple y limpia)
- JavaScript: `stock-admin.js`
- Tabla BD: `movimientos_stock` (estructura simple):
  ```sql
  CREATE TABLE movimientos_stock (
    id_movimiento BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    tipo ENUM('AGREGAR', 'VENTA', 'AJUSTE'),
    cantidad INT NOT NULL,
    stock_anterior INT NOT NULL,
    stock_nuevo INT NOT NULL,
    nota VARCHAR(255),
    id_pedido INT NULL,
    usuario_admin VARCHAR(100),
    fecha_movimiento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto),
    FOREIGN KEY (id_pedido) REFERENCES pedidos(id_pedido)
  );
  ```

**Endpoints necesarios (API REST simple):**
- `GET /api/admin/stock/productos` - Lista de productos con stock
- `POST /api/admin/stock/agregar` - Agregar stock a un producto
- `POST /api/admin/stock/ajustar` - Ajustar stock (corrección)
- `GET /api/admin/stock/historial` - Historial de movimientos
- `GET /api/admin/stock/alertas` - Productos con stock bajo o agotados

---

## 7. GESTIÓN DE CATEGORÍAS DE PRODUCTOS

### Descripción del Proceso
El sistema permitirá crear, editar y gestionar las categorías que organizan el catálogo de productos, facilitando la navegación y búsqueda para los clientes.

**Listar Categorías:**
En `/admin/categorias`, se mostrará una tabla con todas las categorías existentes:
- ID de categoría
- Nombre de la categoría
- Descripción
- Imagen/Icono asociado (si existe)
- Número de productos en esa categoría
- Estado (Activa/Inactiva)
- Orden de visualización (para ordenar en el catálogo público)
- Acciones (Editar, Eliminar/Desactivar, Subir, Bajar)

**Crear Nueva Categoría:**
Formulario para agregar categorías con campos:
- **Nombre:** Texto obligatorio, único (ej: "Deportivos", "Clásicos")
- **Descripción:** Texto opcional para describir la categoría
- **Slug:** URL amigable (auto-generado desde el nombre, ej: "deportivos")
- **Imagen/Icono:** Opcional, para mostrar en el catálogo
- **Orden:** Número para definir la posición de visualización
- **Estado:** Activa por defecto
- **Meta descripción:** Para SEO (opcional)

**Editar Categoría:**
Modificar cualquier campo de una categoría existente. El sistema validará:
- Que el nombre no esté duplicado
- Que el slug sea único
- Al cambiar el nombre, actualizar slug automáticamente (opcional)

**Eliminar/Desactivar Categoría:**
- **Desactivar:** La categoría no aparece en el catálogo público pero se mantiene
- **Eliminar:** Solo si no tiene productos asociados. Si tiene productos:
  - Opción 1: Reasignar todos los productos a otra categoría
  - Opción 2: No permitir eliminación hasta reasignar manualmente

**Ordenar Categorías:**
Interfaz drag-and-drop o botones arriba/abajo para reordenar la visualización de categorías en el catálogo público.

**Asignación de Productos a Categorías:**
- Vista de productos por categoría
- Opción de cambiar la categoría de un producto desde aquí
- Mover productos en lote entre categorías

**Componentes Técnicos a Implementar:**
- Model: `Categoria.java` (ya existe, posiblemente extender)
- Controller: `AdminCategoriaController.java`
- Service: `CategoriaService.java`
- Template: `admin/categorias.html` (crear)
- Endpoints necesarios:
  - `GET /api/admin/categorias` - Listar todas
  - `POST /api/admin/categorias` - Crear categoría
  - `PUT /api/admin/categorias/{id}` - Actualizar
  - `DELETE /api/admin/categorias/{id}` - Eliminar
  - `PATCH /api/admin/categorias/{id}/estado` - Cambiar estado
  - `PUT /api/admin/categorias/reordenar` - Cambiar orden

---

## 8. GESTIÓN DE MENSAJES DE CONTACTO

### Descripción del Proceso
El sistema proporcionará un módulo para que los administradores gestionen y respondan los mensajes recibidos a través del formulario de contacto del sitio web.

**Bandeja de Mensajes:**
En `/admin/mensajes`, se mostrará una lista de todos los mensajes de contacto recibidos:
- ID del mensaje
- Estado (Nuevo/Leído/Respondido/Cerrado) con indicador visual
- Nombre del remitente
- Email del remitente
- Asunto del mensaje
- Fecha de envío
- Prioridad (si se implementa)
- Acciones (Ver, Responder, Marcar como leído, Eliminar)

Los mensajes nuevos se destacarán visualmente (negrita, color distintivo) para llamar la atención.

**Filtros y Búsqueda:**
- Filtro por estado del mensaje
- Búsqueda por nombre, email o asunto
- Filtro por rango de fechas
- Ordenar por fecha (más recientes primero)

**Ver Detalle del Mensaje:**
Al hacer clic en un mensaje, se mostrará:
- Información completa del remitente (nombre, email, teléfono si proporcionó)
- Asunto completo
- Contenido del mensaje
- Fecha y hora de envío
- Historial de respuestas (si ha habido intercambio)

El sistema marcará automáticamente el mensaje como "leído" cuando se abra.

**Responder Mensaje:**
El administrador podrá responder directamente desde el sistema:
- Se abrirá un formulario de respuesta con el email del cliente prellenado
- Campo de asunto (incluye "Re: " + asunto original)
- Editor de texto para escribir la respuesta
- Botón "Enviar respuesta"

Al enviar:
- Se enviará un email al cliente con la respuesta
- Se registrará la respuesta en el historial del mensaje
- El estado cambia a "Respondido"

**Marcar Estados:**
Botones de acción rápida para:
- Marcar como leído/no leído
- Marcar como respondido
- Cerrar mensaje (marcar como resuelto)

**Eliminar Mensajes:**
Opción de eliminar mensajes antiguos o spam con confirmación.

**Estadísticas de Mensajes:**
Métricas visibles en el panel:
- Mensajes pendientes de respuesta
- Mensajes recibidos hoy/esta semana
- Tiempo promedio de respuesta
- Tasa de respuesta

**Componentes Técnicos a Implementar:**
- Model: `Contacto.java` (ya existe, posiblemente agregar campos)
- Controller: `AdminContactoController.java`
- Service: `ContactoService.java`, `EmailService.java` (para respuestas)
- Template: `admin/mensajes.html` (crear)
- Endpoints necesarios:
  - `GET /api/admin/contactos` - Listar mensajes
  - `GET /api/admin/contactos/{id}` - Ver detalle
  - `PATCH /api/admin/contactos/{id}/estado` - Cambiar estado
  - `POST /api/admin/contactos/{id}/responder` - Enviar respuesta
  - `DELETE /api/admin/contactos/{id}` - Eliminar mensaje

---

## 9. CONFIGURACIÓN DEL SISTEMA

### Descripción del Proceso
Panel de configuración para ajustar parámetros generales del sistema y personalizar la experiencia de la tienda.

**Sección de Configuración General:**
En `/admin/configuracion`, el administrador podrá configurar:

**Información de la Empresa:**
- Nombre del negocio (SaCars)
- RUC/NIT
- Dirección física
- Teléfono de contacto
- Email de contacto
- Horarios de atención
- Redes sociales (URLs)

**Configuración de Facturación:**
- Serie de facturas (B001, F001, etc.)
- Siguiente número de factura
- Incluir IGV/impuestos
- Porcentaje de impuesto
- Mensajes personalizados en facturas

**Configuración de Envíos:**
- Zonas de envío disponibles con costos
- Agregar/editar/eliminar zonas
- Tiempo estimado de entrega por zona
- Política de envíos (texto descriptivo)

**Configuración de Stock:**
- Umbral de stock bajo (número de unidades)
- Umbral de stock crítico
- Permitir compras sin stock (pre-orden)
- Reserva automática de stock al agregar al carrito

**Configuración de Notificaciones:**
- Activar/desactivar notificaciones por email
- Email para alertas de administrador
- Plantillas de emails (bienvenida, confirmación de pedido, etc.)
- Configuración SMTP (servidor de correo)

**Configuración de Seguridad:**
- Tiempo de expiración de sesión
- Intentos de login permitidos
- Activar autenticación de dos factores (2FA)
- Política de contraseñas (longitud mínima, complejidad)

**Métodos de Pago:**
- Activar/desactivar métodos de pago disponibles
- Configuración de pasarelas de pago (si se integran)
- Instrucciones de pago para cada método

**Configuración de SEO:**
- Meta descripción del sitio
- Keywords
- Título del sitio
- Configuración de URLs amigables

**Respaldos y Mantenimiento:**
- Botón para generar respaldo de base de datos
- Programar respaldos automáticos
- Modo mantenimiento (cerrar sitio temporalmente)

**Componentes Técnicos a Implementar:**
- Model: `Configuracion.java` (nueva entidad para almacenar configuraciones)
- Controller: `ConfiguracionController.java`
- Service: `ConfiguracionService.java`
- Template: `admin/configuracion.html` (crear)
- Endpoints necesarios:
  - `GET /api/admin/configuracion` - Obtener configuración actual
  - `PUT /api/admin/configuracion` - Actualizar configuración

---

## 10. LOGS Y AUDITORÍA DEL SISTEMA

### Descripción del Proceso
Sistema de registro de actividades y auditoría para rastrear todas las acciones importantes realizadas en el sistema.

**Registro de Actividades:**
El sistema registrará automáticamente:
- Logins y logouts de usuarios (especialmente administradores)
- Creación, modificación y eliminación de productos
- Cambios en pedidos (estados, modificaciones)
- Cambios en usuarios (creación, cambio de rol, desactivación)
- Movimientos de inventario
- Cambios en configuración del sistema
- Accesos a información sensible

*Datos del Log:*
- Timestamp (fecha y hora exacta)
- Usuario que realizó la acción
- Tipo de acción (CREATE, UPDATE, DELETE, LOGIN, etc.)
- Entidad afectada (Producto, Usuario, Pedido, etc.)
- ID de la entidad
- Valores anteriores (antes del cambio)
- Valores nuevos (después del cambio)
- IP del usuario
- Dispositivo/navegador

**Visualización de Logs:**
En `/admin/logs`, tabla con todos los registros:
- Filtros por tipo de acción, usuario, fecha, entidad
- Búsqueda por ID de entidad
- Ordenamiento por fecha
- Exportación de logs a archivo

**Alertas de Seguridad:**
Notificaciones automáticas para:
- Múltiples intentos fallidos de login
- Accesos desde IPs desconocidas
- Cambios críticos (cambio de rol a administrador, eliminación masiva, etc.)
- Accesos fuera de horario habitual

**Componentes Técnicos a Implementar:**
- Model: `AuditoriaLog.java` (nueva entidad)
- Service: `AuditoriaService.java`
- Repository: `AuditoriaLogRepository.java`
- Interceptor: Para capturar acciones automáticamente
- Template: `admin/logs.html` (crear)
- Tabla BD: `auditoria_logs` (crear)

---

## 11. NOTIFICACIONES Y COMUNICACIONES

### Descripción del Proceso
Sistema de notificaciones para mantener informados a clientes y administradores sobre eventos importantes.

**Notificaciones por Email:**
El sistema enviará emails automáticos en los siguientes casos:

*Para Clientes:*
- Email de bienvenida al registrarse
- Confirmación de pedido realizado
- Actualización de estado del pedido
- Pedido enviado con información de seguimiento (si aplica)
- Pedido entregado
- Pedido cancelado
- Respuesta a mensaje de contacto

*Para Administradores:*
- Alerta de nuevo pedido recibido
- Alerta de stock bajo
- Alerta de producto agotado
- Nuevo mensaje de contacto recibido
- Resumen diario/semanal de ventas

**Plantillas de Email:**
Crear y personalizar plantillas HTML para cada tipo de notificación:
- Diseño consistente con la marca
- Información relevante según el tipo de notificación
- Botones de acción (Ver pedido, Contactar soporte, etc.)
- Footer con información de contacto y políticas

**Notificaciones en el Sistema:**
Centro de notificaciones dentro del panel de admin:
- Campana de notificaciones en el header
- Listado de notificaciones no leídas
- Marcar como leída
- Enlace directo a la acción relacionada

**Configuración de Notificaciones:**
Permitir a administradores configurar qué notificaciones recibir:
- Seleccionar tipos de notificaciones activas
- Frecuencia de resúmenes (diario, semanal)
- Canales de notificación (email, en sistema)

**Componentes Técnicos a Implementar:**
- Service: `NotificacionService.java`, `EmailService.java`
- Model: `Notificacion.java` (nueva entidad)
- Template: Plantillas de emails en `resources/templates/email/`
- Library: JavaMailSender para envío de emails
- Tabla BD: `notificaciones` (crear)

---

## 12. SISTEMA DE RESPALDO Y RECUPERACIÓN

### Descripción del Proceso
Implementar funcionalidad para crear respaldos de la base de datos y recuperar datos en caso de emergencia.

**Generación de Respaldos:**
- Botón manual para generar respaldo completo de la BD
- Programar respaldos automáticos (diarios, semanales)
- Almacenamiento de respaldos en carpeta segura del servidor
- Opción de descargar respaldo

**Restauración:**
- Listar respaldos disponibles con fecha
- Seleccionar respaldo y restaurar (con confirmación múltiple)
- Validar integridad del respaldo antes de restaurar

**Exportación de Datos:**
- Exportar datos específicos (productos, usuarios, pedidos)
- Formatos: SQL, CSV, JSON

**Componentes Técnicos a Implementar:**
- Service: `RespaldoService.java`
- Controller: `RespaldoController.java`
- Uso de herramientas del sistema operativo (mysqldump)
- Template: Sección en `admin/configuracion.html`

---

## RESUMEN DE TABLAS NUEVAS A CREAR

1. **movimientos_inventario**
   - id_movimiento (PK)
   - id_producto (FK)
   - tipo_movimiento (ENUM)
   - cantidad
   - stock_anterior
   - stock_resultante
   - usuario_id (FK)
   - fecha_movimiento
   - motivo
   - documento_referencia

2. **auditoria_logs**
   - id_log (PK)
   - usuario_id (FK)
   - accion
   - entidad
   - entidad_id
   - valores_anteriores (JSON/TEXT)
   - valores_nuevos (JSON/TEXT)
   - ip_address
   - user_agent
   - timestamp

3. **notificaciones**
   - id_notificacion (PK)
   - usuario_id (FK)
   - tipo
   - titulo
   - mensaje
   - leida (boolean)
   - url_accion
   - fecha_creacion

4. **configuracion_sistema**
   - id_config (PK)
   - clave (unique)
   - valor (TEXT)
   - tipo_dato
   - descripcion
   - fecha_modificacion

---

## PRIORIZACIÓN DE IMPLEMENTACIÓN

**Fase 1 - Alta Prioridad (Funcionalidades Core):**
1. Dashboard Administrativo con Métricas
2. CRUD Completo de Productos
3. Gestión Avanzada de Pedidos
4. Gestión de Stock y Movimientos

**Fase 2 - Media Prioridad (Gestión Completa):**
5. Gestión de Usuarios
6. Sistema de Reportes Básicos
7. Gestión de Categorías
8. Gestión de Mensajes de Contacto

**Fase 3 - Baja Prioridad (Mejoras y Optimización):**
9. Configuración del Sistema
10. Logs y Auditoría
11. Notificaciones Avanzadas
12. Sistema de Respaldo

---

## ESTIMACIÓN DE TIEMPO DE DESARROLLO

**Fase 1:** 3-4 semanas
**Fase 2:** 2-3 semanas
**Fase 3:** 2-3 semanas

**Total estimado:** 7-10 semanas de desarrollo

---

## TECNOLOGÍAS Y HERRAMIENTAS ADICIONALES REQUERIDAS

**Backend:**
- Spring Boot (ya en uso)
- Spring Security (ya en uso)
- Spring Data JPA (ya en uso)
- JavaMailSender para emails
- Apache POI para exportación Excel
- iText o Flying Saucer para PDF

**Frontend:**
- Chart.js o ApexCharts para gráficos
- jQuery (ya en uso)
- DataTables para tablas avanzadas
- SweetAlert2 para modals y alertas
- Moment.js para manejo de fechas

**Base de Datos:**
- MySQL/MariaDB (ya en uso)
- Posiblemente crear índices adicionales para optimización

---

## CONCLUSIONES

Este documento detalla las funcionalidades pendientes de implementación que convertirán el panel de administración de SaCars en una herramienta completa y profesional para la gestión del negocio. La implementación de estos requerimientos proporcionará a los administradores control total sobre:

✅ Operaciones diarias (pedidos, stock, productos)  
✅ Análisis del negocio (reportes, estadísticas, KPIs)  
✅ Gestión de clientes y comunicación  
✅ Configuración y personalización del sistema  
✅ Seguridad y auditoría de operaciones  

Con estas funcionalidades implementadas, SaCars tendrá un sistema de e-commerce completo y robusto que permitirá escalar el negocio de manera eficiente.

---

**Documento generado:** 31 de enero de 2026  
**Sistema:** SaCars - Requerimientos Futuros  
**Versión del documento:** 1.0  
**Estado:** Pendiente de implementación
