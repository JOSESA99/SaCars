# ANÁLISIS DE REQUERIMIENTOS - SISTEMA SACARS

**Sistema de Ventas de Hot Wheels**  
**Fecha:** 31 de enero de 2026  
**Versión:** 1.0

---

## 1. REGISTRO DE USUARIOS

### Descripción del Proceso
Para el registro de usuarios, el sistema permite que cualquier visitante del sitio web pueda crear una cuenta para acceder a las funcionalidades de compra. El proceso de registro requiere los siguientes datos obligatorios: nombre completo (nombre y apellido separados), correo electrónico único, número de DNI (8 dígitos), contraseña segura, y opcionalmente número de teléfono y dirección. 

El usuario accede al formulario de registro a través de la opción "Registrarse" en el menú de autenticación. Una vez ingresados todos los datos requeridos, el sistema valida que el correo electrónico no esté previamente registrado en la base de datos. Si el correo es único, el sistema encripta la contraseña utilizando el algoritmo BCrypt para garantizar la seguridad, asigna automáticamente el rol de "cliente" al nuevo usuario, y almacena toda la información en la tabla `usuarios` de la base de datos.

Después de un registro exitoso, el sistema redirige automáticamente al usuario a la página de inicio de sesión con un mensaje de confirmación que indica que el registro fue exitoso. Si el correo electrónico ya existe en el sistema, se muestra un mensaje de error informando al usuario que ya existe una cuenta con ese email. El usuario queda activo por defecto en el sistema.

**Datos Registrados:**
- Nombre (obligatorio)
- Apellido (obligatorio)
- DNI (obligatorio, 8 caracteres)
- Email (obligatorio, único)
- Contraseña (obligatoria, encriptada)
- Teléfono (opcional)
- Dirección (opcional)
- Rol: Cliente (asignado automáticamente)
- Estado: Activo (por defecto)
- Fecha de registro (automática)

**Componentes Técnicos Involucrados:**
- Controller: `AuthController.java` - método `registrarCuentaUsuario()`
- Service: `UsuarioService.java`
- Model: `Usuario.java`
- Repository: `UsuarioRepository.java`
- Template: `/auth/registro.html`
- Tabla BD: `usuarios`

---

## 2. INICIO DE SESIÓN

### Descripción del Proceso
Para el inicio de sesión, el usuario accede al formulario de login ingresando su correo electrónico y contraseña previamente registrados. El sistema utiliza Spring Security para validar las credenciales del usuario de forma segura.

Cuando el usuario envía el formulario, el sistema busca en la base de datos un usuario activo con el email proporcionado. Si existe, compara la contraseña ingresada con la contraseña encriptada almacenada utilizando BCrypt. Si las credenciales son correctas, el sistema genera una sesión autenticada y redirige al usuario según su rol: si es administrador, lo dirige al panel de administración (`/admin/dashboard`); si es cliente, lo dirige al catálogo de productos (`/catalogo`).

Durante el proceso de autenticación, el sistema también carga los datos del usuario en memoria (nombre, apellido, email, rol, id) y los almacena en localStorage del navegador mediante JavaScript para mantener la sesión activa en el frontend. Si las credenciales son incorrectas o el usuario no existe, se muestra un mensaje de error informando que el email o la contraseña son incorrectos.

El sistema mantiene la sesión activa hasta que el usuario cierre sesión explícitamente o expire el tiempo de sesión configurado en Spring Security.

**Validaciones del Sistema:**
- Verificación de email existente en la base de datos
- Validación de contraseña encriptada mediante BCrypt
- Verificación de estado activo del usuario
- Control de roles para redirección apropiada

**Componentes Técnicos Involucrados:**
- Controller: `AuthController.java`
- Service: `CustomUserDetailsService.java`
- Security: `SecurityConfig.java`
- Template: `/auth/login.html`
- JavaScript: `auth-check.js`, `auth-modal-v2.js`
- Tabla BD: `usuarios`

---

## 3. GESTIÓN DE PERFIL DE USUARIO

### Descripción del Proceso
Para la gestión del perfil, el usuario autenticado puede visualizar y actualizar su información personal. El cliente accede a su perfil a través del menú de navegación donde encuentra la opción "Mi Perfil". 

Una vez en la página de perfil, el sistema carga automáticamente todos los datos actuales del usuario desde la base de datos mediante una petición al API REST (`/api/perfil/{idUsuario}`). El usuario puede visualizar su información completa incluyendo: nombre completo, DNI, correo electrónico, teléfono, dirección y rol asignado.

Para actualizar su información, el usuario modifica los campos deseados en el formulario de edición. Los campos editables son: nombre, apellido, teléfono, DNI y dirección. El campo de email también es editable pero el sistema valida que no coincida con el email de otro usuario existente. Cuando el usuario guarda los cambios, el sistema valida los datos ingresados y actualiza la información en la base de datos.

El sistema confirma la actualización exitosa mostrando un mensaje de éxito y recargando los datos actualizados en la vista. Si ocurre algún error durante la actualización, se muestra un mensaje descriptivo del problema. La contraseña no se puede modificar desde esta vista por razones de seguridad.

Además, en el perfil el usuario puede visualizar un resumen de sus pedidos realizados, incluyendo el estado actual de cada uno, la fecha de compra y el monto total.

**Datos Gestionables:**
- Visualización completa de información personal
- Edición de nombre y apellido
- Edición de teléfono
- Edición de DNI
- Edición de dirección
- Actualización de email (validando unicidad)
- Visualización de historial de pedidos

**Componentes Técnicos Involucrados:**
- Controller: `PerfilController.java` - endpoints `obtenerPerfil()` y `actualizarPerfil()`
- Service: `UsuarioService.java`
- Template: `perfil.html`
- JavaScript: `perfil.js`, `profile.js`
- Tabla BD: `usuarios`, `pedidos`

---

## 4. CATÁLOGO DE PRODUCTOS

### Descripción del Proceso
El catálogo de productos permite a todos los usuarios (autenticados o no) visualizar el inventario completo de autos Hot Wheels disponibles para la venta. Los usuarios acceden al catálogo a través del menú principal seleccionando la opción "Catálogo".

El sistema muestra todos los productos activos en formato de cuadrícula (grid), donde cada producto se presenta con una imagen, nombre descriptivo, descripción breve y precio. Los productos se cargan desde la base de datos y se muestran inicialmente en su orden por defecto.

El catálogo cuenta con un sistema de filtrado y búsqueda avanzado que permite a los usuarios encontrar productos específicos de manera eficiente. El sistema incluye:

**Búsqueda por texto:** El usuario puede escribir palabras clave en un campo de búsqueda y el sistema filtra en tiempo real los productos cuyo nombre coincida con el texto ingresado, mostrando solo los resultados relevantes.

**Filtro por rango de precio:** Los usuarios pueden filtrar productos por categorías de precio predefinidas:
- Económicos: S/ 15.00 - S/ 20.00
- Medianos: S/ 21.00 - S/ 30.00
- Premium: S/ 31.00 en adelante
- Todos los precios (sin filtro)

**Ordenamiento:** El sistema permite ordenar los productos según diferentes criterios:
- Por defecto (orden de inserción en BD)
- Precio: Menor a Mayor
- Precio: Mayor a Menor
- Nombre: A-Z (alfabéticamente ascendente)
- Nombre: Z-A (alfabéticamente descendente)

**Paginación:** Para mejorar el rendimiento y la experiencia de usuario, el catálogo muestra 6 productos por página con controles de navegación (botones anterior y siguiente) que permiten recorrer todas las páginas de resultados. El sistema indica claramente la página actual y el total de páginas disponibles.

Los usuarios pueden combinar múltiples filtros simultáneamente. Por ejemplo, pueden buscar "Camaro", filtrar por precio "Económico" y ordenar por "Nombre A-Z" al mismo tiempo. El sistema aplica todos los filtros en conjunto y actualiza los resultados automáticamente.

Cada tarjeta de producto incluye un botón "Ver Producto" que permite al usuario acceder al detalle completo del producto para obtener más información antes de agregarlo al carrito. El sistema también muestra un contador dinámico que indica cuántos productos coinciden con los filtros aplicados.

**Funcionalidades del Catálogo:**
- Visualización de productos en grid responsivo
- Búsqueda en tiempo real por nombre
- Filtrado por rangos de precio
- Ordenamiento múltiple (precio, nombre)
- Paginación de resultados (6 productos por página)
- Contador de productos filtrados
- Botón para limpiar todos los filtros
- Vista previa con imagen, nombre, descripción y precio
- Botón de acceso al detalle del producto

**Componentes Técnicos Involucrados:**
- Controller: `HomeController.java`
- Repository: `ProductoRepository.java`
- Template: `catalogo.html`
- JavaScript: `catalogo-filtros.js`
- CSS: `styles.css`
- Tabla BD: `productos`

---

## 5. DETALLE DE PRODUCTO Y MODAL

### Descripción del Proceso
El sistema permite a los usuarios visualizar información detallada de cada producto antes de agregarlo al carrito de compras. Cuando un usuario hace clic en el botón "Ver Producto" de cualquier artículo del catálogo, el sistema abre un modal (ventana emergente) que muestra toda la información disponible del producto seleccionado.

El modal se carga dinámicamente con los siguientes datos del producto: imagen de alta calidad, nombre completo del modelo, descripción detallada con características y especificaciones, precio unitario y disponibilidad en stock. El modal se posiciona en el centro de la pantalla sobre una capa oscura semi-transparente que bloquea la interacción con el resto de la página.

Dentro del modal, el usuario puede realizar las siguientes acciones:

**Selección de cantidad:** El usuario puede especificar cuántas unidades desea agregar al carrito mediante un selector numérico. El sistema valida que la cantidad seleccionada no exceda el stock disponible del producto.

**Agregar al carrito:** Una vez seleccionada la cantidad deseada, el usuario puede hacer clic en el botón "Agregar al carrito". El sistema valida si el usuario ha iniciado sesión; si no lo ha hecho, muestra un mensaje solicitando autenticación. Si está autenticado, el producto se agrega al carrito almacenado en localStorage del navegador con los siguientes datos: ID del producto, nombre, imagen, precio unitario, cantidad seleccionada y un subtotal calculado (precio × cantidad).

Si el producto ya existe en el carrito, el sistema actualiza la cantidad sumando las unidades adicionales en lugar de crear una entrada duplicada. El sistema actualiza inmediatamente el contador visual del carrito que aparece en el encabezado de la página, mostrando el número total de artículos diferentes agregados.

El usuario puede cerrar el modal en cualquier momento haciendo clic en el botón "X" ubicado en la esquina superior derecha, haciendo clic fuera del área del modal, o presionando la tecla "Escape" del teclado. Al cerrar el modal, el usuario regresa a la vista del catálogo sin perder los productos agregados al carrito.

**Información Mostrada en el Modal:**
- Imagen del producto en tamaño grande
- Nombre completo del modelo
- Descripción detallada del producto
- Precio unitario en soles (S/)
- Stock disponible
- Selector de cantidad
- Subtotal calculado dinámicamente
- Botón para agregar al carrito
- Botón de cerrar

**Componentes Técnicos Involucrados:**
- JavaScript: `modal.js`, `main.js`
- Template: `catalogo.html`
- CSS: `styles.css`
- Storage: localStorage del navegador

---

## 6. CARRITO DE COMPRAS

### Descripción del Proceso
El carrito de compras permite a los usuarios gestionar los productos que desean adquirir antes de proceder al checkout. Los usuarios acceden al carrito haciendo clic en el icono del carrito ubicado en el encabezado del sitio, que muestra un contador con el número de productos agregados.

El sistema carga el carrito desde localStorage del navegador, donde se almacenan todos los productos agregados durante la sesión del usuario. La página del carrito muestra una lista detallada de todos los productos seleccionados, incluyendo para cada uno: imagen del producto, nombre, precio unitario, cantidad seleccionada, y subtotal (precio × cantidad).

El usuario puede realizar las siguientes operaciones en el carrito:

**Modificar cantidad:** Cada producto tiene controles de incremento (+) y decremento (-) que permiten ajustar la cantidad deseada. El sistema actualiza automáticamente el subtotal del producto y el total general del carrito cuando se modifica la cantidad. El sistema no permite cantidades menores a 1.

**Eliminar producto:** Cada producto incluye un botón de eliminar (ícono de papelera o cruz) que permite remover completamente el artículo del carrito. El sistema solicita confirmación antes de eliminar para evitar eliminaciones accidentales.

**Selección de zona de envío:** El sistema presenta un selector desplegable donde el usuario debe elegir la zona de entrega entre las siguientes opciones:
- Banda de Shilcayo: Costo de envío S/ 5.00
- Tarapoto: Costo de envío S/ 5.00
- Morales: Costo de envío S/ 10.00
- Otros: Costo de envío S/ 5.00

Cuando el usuario selecciona una zona, el sistema calcula automáticamente el costo de envío y lo agrega al total de la compra. El sistema muestra claramente el desglose de costos: subtotal de productos + costo de envío = total a pagar.

**Resumen de compra:** El sistema muestra un panel lateral o inferior con el resumen financiero que incluye:
- Subtotal (suma de todos los productos)
- Costo de envío según zona seleccionada
- Total a pagar

**Proceder al checkout:** Una vez que el usuario ha revisado su carrito y seleccionado la zona de envío, puede hacer clic en el botón "Proceder al pago" o "Finalizar compra". El sistema valida que:
1. El carrito no esté vacío
2. Se haya seleccionado una zona de envío
3. El usuario esté autenticado

Si todas las validaciones son exitosas, el sistema guarda la zona y el costo de envío en localStorage y redirige al usuario a la página de checkout. Si el usuario no está autenticado, se muestra un mensaje solicitando iniciar sesión.

**Vaciar carrito:** El sistema también proporciona un botón para vaciar completamente el carrito, eliminando todos los productos de una sola vez. Esta acción solicita confirmación del usuario.

Si el carrito está vacío, el sistema muestra un mensaje informativo indicando que no hay productos y proporciona un enlace para regresar al catálogo.

**Funcionalidades del Carrito:**
- Visualización de todos los productos agregados
- Modificación de cantidad de cada producto
- Eliminación individual de productos
- Cálculo automático de subtotales
- Selección de zona de envío
- Cálculo automático de costos de envío
- Visualización de total a pagar
- Validación de usuario autenticado
- Botón para vaciar carrito completo
- Redirección a checkout
- Persistencia en localStorage

**Componentes Técnicos Involucrados:**
- Template: `carrito.html`
- JavaScript: `main.js`
- CSS: `styles.css`
- Storage: localStorage (carrito, zonaEnvio, costoEnvio)

---

## 7. PROCESO DE CHECKOUT

### Descripción del Proceso
El checkout es el proceso final donde el usuario confirma su compra y proporciona los datos necesarios para completar la transacción. Los usuarios acceden al checkout desde el carrito de compras después de hacer clic en "Proceder al pago".

El sistema carga automáticamente tres tipos de información:

**Datos del usuario:** El sistema recupera y precarga automáticamente la información del usuario autenticado desde el backend, incluyendo: nombre completo, DNI, teléfono y correo electrónico. Estos campos aparecen prellenados en el formulario de checkout, aunque el usuario puede modificarlos si es necesario. Si el backend no responde, el sistema utiliza los datos almacenados en localStorage como respaldo.

**Productos del carrito:** El sistema muestra un resumen de todos los productos que el usuario va a comprar, incluyendo imagen, nombre, cantidad y precio de cada producto. Esta información se obtiene desde localStorage.

**Información de envío precargada:** El sistema carga automáticamente la zona de envío seleccionada previamente en el carrito y el costo asociado.

El usuario debe completar el formulario de checkout proporcionando la siguiente información:

**Datos personales de facturación:**
- Nombre completo (precargado, editable)
- DNI (precargado, editable)
- Teléfono (precargado, editable)
- Email (precargado, editable)

**Dirección de envío:**
- Dirección completa (calle, número, referencia)
- Zona/Ciudad (selector desplegable con opciones: Banda de Shilcayo, Tarapoto, Morales, Otros)
- Código postal (se asigna automáticamente según la zona seleccionada)

**Método de pago:**
El usuario selecciona el método de pago deseado mediante un selector. Actualmente el sistema solo soporta:
- Contra entrega (pago en efectivo al recibir el producto)

El sistema valida que todos los campos obligatorios estén completados antes de permitir finalizar la compra. Cuando el usuario hace clic en "Confirmar Compra", el sistema realiza las siguientes operaciones en orden:

1. **Validación de datos:** Verifica que todos los campos requeridos estén completos y correctos.

2. **Preparación de datos:** El sistema construye un objeto DTO (Data Transfer Object) con toda la información necesaria:
   - ID del usuario autenticado
   - Lista de productos con ID, cantidad y precio unitario
   - Dirección completa de envío
   - Ciudad/zona de envío
   - Código postal
   - Método de pago
   - Total calculado (subtotal + envío)

3. **Envío al backend:** El sistema envía una petición POST al endpoint `/api/checkout` con todos los datos del pedido en formato JSON.

4. **Procesamiento en el servidor:** El backend realiza las siguientes operaciones en una transacción:
   - Valida la existencia del usuario
   - Verifica el stock disponible de cada producto
   - Crea un registro de pedido en la tabla `pedidos` con estado "PENDIENTE"
   - Crea registros de detalle para cada producto en la tabla `detalle_pedido`
   - Calcula el subtotal y total del pedido
   - Genera un número de factura único y secuencial (formato: B001-000001)
   - Crea un registro de factura en la tabla `factura` con todos los datos fiscales
   - Reduce el stock de cada producto comprado
   - Si todo es exitoso, retorna el ID del pedido, ID de factura y número de factura

5. **Confirmación:** Si la transacción es exitosa, el sistema:
   - Muestra un mensaje de confirmación con el número de pedido y factura
   - Limpia completamente el carrito (localStorage)
   - Limpia los datos de envío guardados
   - Redirige al usuario a una página de confirmación o a su perfil donde puede ver el detalle del pedido

Si ocurre algún error durante el proceso (falta de stock, error de conexión, datos inválidos), el sistema muestra un mensaje de error específico y permite al usuario corregir la información sin perder los datos ingresados.

**Datos del Pedido Generado:**
- ID del pedido (auto-generado)
- ID del usuario
- Fecha y hora del pedido (automática)
- Estado: PENDIENTE (por defecto)
- Dirección de envío completa
- Ciudad de envío
- Código postal
- Total de la compra
- Método de pago
- Lista de productos con cantidad y precio

**Datos de la Factura Generada:**
- ID de factura (auto-generado)
- Número de factura único (B001-000001, secuencial)
- ID del pedido asociado
- Fecha de emisión (automática)
- Nombre del cliente
- DNI del cliente
- Subtotal (suma de productos)
- Total (subtotal + envío)
- Datos de la empresa (SaCars, RUC, dirección)

**Componentes Técnicos Involucrados:**
- Controller: `CheckoutController.java` - endpoint `finalizarCompra()`
- Service: `PedidoService.java`, `UsuarioService.java`, `FacturaService.java`
- DTO: `CheckoutRequestDTO.java`, `CheckoutItemDTO.java`
- Model: `Pedido.java`, `DetallePedido.java`, `Factura.java`
- Repository: `PedidoRepository.java`, `DetallePedidoRepository.java`, `FacturaRepository.java`
- Template: `checkout.html`
- JavaScript: `checkout.js`
- Tablas BD: `pedidos`, `detalle_pedido`, `factura`, `productos`

---

## 8. GESTIÓN DE PEDIDOS DEL USUARIO

### Descripción del Proceso
El sistema permite a los usuarios autenticados consultar el historial completo de sus pedidos realizados. Los clientes pueden acceder a esta funcionalidad desde su panel de perfil o desde un enlace específico en el menú de usuario.

Cuando el usuario accede a la sección de pedidos, el sistema realiza una consulta al backend solicitando todos los pedidos asociados al ID del usuario autenticado mediante el endpoint `/api/pedidos/usuario/{idUsuario}`. El sistema recupera todos los registros de la tabla `pedidos` donde el campo `id_usuario` coincida con el usuario actual.

El sistema muestra una lista completa de todos los pedidos con la siguiente información:

**Lista de pedidos:**
Para cada pedido se muestra:
- Número de pedido (ID)
- Fecha y hora del pedido
- Estado actual (PENDIENTE, COMPLETADO, ENVIADO, CANCELADO)
- Total pagado
- Dirección de envío
- Zona de entrega
- Botón para ver detalle completo

Los pedidos se muestran ordenados por fecha, mostrando primero los más recientes. Cada pedido incluye un indicador visual del estado mediante colores o iconos distintivos:
- PENDIENTE: Amarillo/Naranja
- COMPLETADO: Verde
- ENVIADO: Azul
- CANCELADO: Rojo

**Detalle de pedido individual:**
Cuando el usuario hace clic en "Ver detalle" de un pedido específico, el sistema realiza una petición al endpoint `/api/pedidos/{id}` para obtener información completa del pedido, incluyendo todos los productos asociados.

El detalle del pedido muestra:
- Información general del pedido (número, fecha, estado, total)
- Dirección completa de envío
- Zona y código postal
- Método de pago utilizado
- Lista detallada de productos comprados:
  - Nombre del producto
  - Cantidad
  - Precio unitario
  - Subtotal por producto
- Subtotal de productos
- Costo de envío
- Total pagado

El usuario puede imprimir o descargar el comprobante del pedido. También puede regresar a la lista de pedidos en cualquier momento.

Si el usuario no tiene pedidos registrados, el sistema muestra un mensaje informativo indicando que aún no ha realizado compras y proporciona un enlace directo al catálogo de productos.

**Información Disponible en Pedidos:**
- Historial completo de compras del usuario
- Estado actual de cada pedido
- Fecha de realización
- Monto total pagado
- Dirección de envío utilizada
- Desglose de productos por pedido
- Cantidades y precios individuales
- Acceso a factura asociada

**Componentes Técnicos Involucrados:**
- Controller: `PedidoController.java` - endpoints `listar()` y `detalle()`
- Repository: `PedidoRepository.java`
- Model: `Pedido.java`, `DetallePedido.java`
- Template: Secciones en `perfil.html` o `cliente/dashboard.html`
- JavaScript: `perfil.js`
- Tablas BD: `pedidos`, `detalle_pedido`, `productos`

---

## 9. SISTEMA DE FACTURACIÓN

### Descripción del Proceso
El sistema de facturación genera automáticamente comprobantes electrónicos para cada pedido completado. Este proceso se ejecuta de forma transparente para el usuario como parte del checkout, sin requerir intervención manual.

Cuando un pedido es confirmado y procesado exitosamente, el sistema ejecuta inmediatamente el servicio de facturación que realiza las siguientes operaciones:

**Generación del número de factura:**
El sistema consulta el último número de factura emitido en la base de datos y genera el siguiente número secuencial siguiendo el formato: B001-000001, B001-000002, etc. El formato se compone de:
- Serie: B001 (fija, boletas de venta)
- Correlativo: 6 dígitos con ceros a la izquierda (auto-incremental)

Esta numeración es única y no se repite, garantizando la trazabilidad de cada transacción.

**Captura de datos de facturación:**
El sistema recopila todos los datos necesarios para la factura:

*Datos del pedido asociado:*
- ID del pedido completado
- Fecha y hora de emisión (timestamp actual)
- Productos adquiridos con cantidades y precios
- Subtotal de productos
- Costo de envío
- Total final

*Datos del cliente:*
- Nombre completo (extraído del pedido)
- DNI del cliente (para identificación fiscal)

*Datos de la empresa:*
- Nombre comercial: SaCars
- RUC: 45529645621
- Dirección: Tarapoto - Perú

**Cálculo de montos:**
El sistema calcula:
- Subtotal: Suma del precio × cantidad de todos los productos
- Total: Subtotal + costo de envío

**Almacenamiento:**
Toda la información de la factura se almacena en la tabla `factura` de la base de datos, creando un registro permanente vinculado al pedido mediante el campo `id_pedido`. Esta relación permite recuperar la factura cuando sea necesario.

**Entrega al usuario:**
Una vez generada la factura, el sistema:
1. Retorna el número de factura al frontend en la respuesta del checkout
2. Muestra el número de factura en la página de confirmación
3. Permite al usuario visualizar o descargar la factura desde su perfil
4. Asocia la factura permanentemente con el pedido para consultas futuras

El usuario puede acceder a sus facturas en cualquier momento desde su perfil, donde puede visualizar todas las facturas emitidas, consultar los detalles de cada una, e imprimir o descargar el documento.

Si ocurre algún error durante la generación de la factura, el sistema registra el error en logs y permite reintentar la generación sin afectar el pedido ya creado.

**Datos de la Factura:**
- Número de factura único y secuencial
- ID del pedido asociado
- Fecha de emisión
- Nombre del cliente
- DNI del cliente
- Subtotal de productos
- Total incluyendo envío
- Datos fiscales de la empresa
- Desglose de productos (a través de detalle_pedido)

**Normativa:**
El sistema de facturación está preparado para cumplir con los requisitos de SUNAT para comprobantes electrónicos, aunque actualmente genera facturas internas del sistema.

**Componentes Técnicos Involucrados:**
- Service: `FacturaService.java`
- Model: `Factura.java`
- Repository: `FacturaRepository.java`
- Controller: `CheckoutController.java`
- Tabla BD: `factura`

---

## 10. PANEL DE ADMINISTRACIÓN

### Descripción del Proceso
El panel de administración es un área restringida del sistema que permite a los usuarios con rol de "administrador" gestionar el contenido y configuración de la plataforma de ventas. El acceso está protegido mediante autenticación y validación de roles implementada en Spring Security.

Cuando un usuario administrador inicia sesión correctamente, el sistema verifica su rol y lo redirige automáticamente al panel de administración (`/admin/dashboard`). Los usuarios con rol de cliente no pueden acceder a esta área; si intentan acceder directamente a las URLs de administración, el sistema los redirige a la página de acceso denegado o al catálogo.

El panel de administración proporciona un conjunto completo de herramientas para la gestión del negocio:

**Dashboard principal:**
Muestra métricas y estadísticas del negocio en tiempo real:
- Número total de pedidos realizados
- Ingresos totales generados
- Cantidad de usuarios registrados
- Productos en stock
- Pedidos pendientes de procesamiento
- Gráficos de ventas por período
- Productos más vendidos

**Gestión de productos:**
Permite administrar el inventario completo:
- **Listar productos:** Visualización de todos los productos con filtros y búsqueda
- **Agregar producto:** Formulario para crear nuevos productos con los campos:
  - Nombre del producto
  - Descripción detallada
  - Precio
  - Stock inicial
  - URL de imagen
  - Categoría
  - Estado (activo/inactivo)
  - Marcador de producto destacado
- **Editar producto:** Modificación de cualquier campo de productos existentes
- **Eliminar/Desactivar producto:** Opción para desactivar productos sin borrarlos físicamente
- **Control de stock:** Actualización de cantidades disponibles
- **Gestión de imágenes:** Carga y asociación de imágenes de productos

**Gestión de pedidos:**
Control completo sobre los pedidos de clientes:
- Visualización de todos los pedidos del sistema
- Filtrado por estado (Pendiente, Completado, Enviado, Cancelado)
- Filtrado por fecha
- Filtrado por cliente
- **Detalle de pedido:** Ver información completa de cada pedido
- **Cambio de estado:** Actualizar el estado del pedido según su progreso:
  - PENDIENTE → PROCESANDO
  - PROCESANDO → ENVIADO
  - ENVIADO → COMPLETADO
  - Opción de CANCELADO en cualquier momento
- **Información de envío:** Visualización de direcciones y zonas de entrega
- **Información de facturación:** Acceso a datos de factura asociada

**Gestión de usuarios:**
Administración de cuentas de usuario:
- Lista completa de usuarios registrados
- Visualización de información personal
- Cambio de roles (cliente ↔ administrador)
- Activación/desactivación de cuentas
- Búsqueda de usuarios por email o nombre
- Historial de compras por usuario

**Gestión de categorías:**
Organización del catálogo:
- Crear nuevas categorías de productos
- Editar nombres y descripciones de categorías
- Asignar productos a categorías
- Desactivar categorías

**Mensajes de contacto:**
Gestión de consultas recibidas:
- Visualización de mensajes del formulario de contacto
- Marcado de mensajes como leídos/no leídos
- Filtrado por estado
- Respuesta o eliminación de mensajes

**Reportes y estadísticas:**
Generación de reportes para análisis del negocio:
- Ventas por período (diario, semanal, mensual)
- Productos más vendidos
- Clientes frecuentes
- Análisis de ingresos
- Reportes de stock bajo

Todas las operaciones de administración están protegidas con validaciones de seguridad y permisos. El sistema registra logs de todas las acciones administrativas para auditoría.

La interfaz de administración está diseñada para ser intuitiva, con formularios de validación, mensajes de confirmación para acciones críticas (como eliminar productos), y feedback visual inmediato de las operaciones realizadas.

**Funcionalidades del Panel Admin:**
- Gestión completa de productos (CRUD)
- Gestión de pedidos y estados
- Gestión de usuarios y roles
- Gestión de categorías
- Visualización de mensajes de contacto
- Dashboard con métricas de negocio
- Reportes y estadísticas
- Control de inventario
- Gestión de facturas

**Componentes Técnicos Involucrados:**
- Controllers: Diversos controllers con endpoints `/api/admin/*`
- Security: `SecurityConfig.java` con restricción de rutas por rol
- Template: `admin/dashboard.html` y vistas relacionadas
- JavaScript: Scripts específicos de admin
- Models y Repositories: Todos los del sistema
- Tablas BD: Acceso a todas las tablas del sistema

---

## 11. FORMULARIO DE CONTACTO

### Descripción del Proceso
El formulario de contacto permite a los visitantes del sitio web (autenticados o no) enviar consultas, sugerencias o reportar problemas directamente al equipo de SaCars. Los usuarios acceden al formulario a través del menú principal seleccionando la opción "Contacto".

La página de contacto presenta un formulario simple y amigable donde el usuario debe proporcionar la siguiente información:

**Campos del formulario:**
- **Nombre completo:** Identificación del remitente (obligatorio)
- **Correo electrónico:** Para recibir respuesta (obligatorio, validado formato email)
- **Asunto:** Breve título que describe el tema de la consulta (obligatorio, máx. 200 caracteres)
- **Mensaje:** Descripción detallada de la consulta, sugerencia o problema (obligatorio, área de texto libre)

El usuario completa todos los campos y hace clic en el botón "Enviar mensaje". El sistema realiza las siguientes validaciones antes de procesar:
- Verifica que todos los campos obligatorios estén completos
- Valida el formato correcto del email
- Verifica que el mensaje tenga un mínimo de caracteres
- Sanitiza los datos para prevenir inyecciones XSS

Una vez validados los datos, el sistema:
1. Crea un registro en la tabla `contactos` de la base de datos con toda la información
2. Marca automáticamente el mensaje como "no leído" (leido = 0)
3. Registra la fecha y hora actual del contacto
4. Genera un ID único para el mensaje

El sistema muestra inmediatamente un mensaje de confirmación al usuario indicando que su consulta ha sido recibida exitosamente y que será atendida a la brevedad posible. El formulario se limpia automáticamente, permitiendo enviar otra consulta si es necesario.

**Gestión en el panel de administración:**
Los administradores pueden visualizar todos los mensajes de contacto desde su panel de administración. El sistema les permite:
- Ver listado completo de mensajes recibidos
- Filtrar por estado (leído/no leído)
- Ordenar por fecha
- Ver detalle completo de cada mensaje
- Marcar mensajes como leídos después de procesarlos
- Eliminar mensajes antiguos o spam
- Visualizar el email del remitente para responder directamente

Los mensajes no leídos se destacan visualmente (negrita o color diferente) para llamar la atención del administrador sobre consultas pendientes.

**Datos del Mensaje de Contacto:**
- ID único del mensaje (auto-generado)
- Nombre del remitente
- Email del remitente
- Asunto del mensaje
- Contenido del mensaje
- Fecha y hora de envío (automática)
- Estado: leído/no leído (booleano)

**Funcionalidades:**
- Envío de mensajes sin necesidad de estar autenticado
- Validación de datos en cliente y servidor
- Confirmación de envío exitoso
- Almacenamiento permanente en base de datos
- Panel de gestión para administradores
- Marcado de mensajes procesados
- Filtrado y búsqueda de mensajes

**Componentes Técnicos Involucrados:**
- Model: `Contacto.java` (entidad)
- Repository: `ContactoRepository.java`
- Controller: Controller específico para contacto
- Template: `contacto.html`
- Tabla BD: `contactos`

---

## 12. SISTEMA DE AUTENTICACIÓN Y SEGURIDAD

### Descripción del Proceso
El sistema implementa un robusto mecanismo de autenticación y seguridad basado en Spring Security para proteger los datos de los usuarios y controlar el acceso a las diferentes áreas de la aplicación.

**Encriptación de contraseñas:**
Todas las contraseñas de usuarios se almacenan encriptadas utilizando el algoritmo BCrypt con factor de trabajo configurable (por defecto 10 rondas). Cuando un usuario se registra, su contraseña se encripta antes de ser almacenada en la base de datos. Durante el inicio de sesión, la contraseña ingresada se compara con el hash almacenado sin necesidad de desencriptarla, garantizando que las contraseñas nunca se manejan en texto plano.

**Gestión de sesiones:**
El sistema mantiene sesiones autenticadas para usuarios que han iniciado sesión correctamente. Spring Security crea una sesión HTTP que se mantiene activa durante un período configurable. Los datos del usuario autenticado se almacenan en el contexto de seguridad (SecurityContext) y también en localStorage del navegador para persistencia en el frontend.

**Control de acceso basado en roles:**
El sistema implementa dos roles principales:
- **Cliente:** Acceso a catálogo, carrito, checkout, perfil personal y pedidos propios
- **Administrador:** Acceso completo a todas las funcionalidades de cliente más el panel de administración

Spring Security utiliza anotaciones y configuración para restringir el acceso a endpoints específicos:
- Rutas públicas: `/`, `/catalogo`, `/contacto`, `/auth/**`, `/api/productos/**`
- Rutas de cliente: `/perfil`, `/carrito`, `/checkout`, `/api/pedidos/**`, `/api/perfil/**`
- Rutas de administrador: `/admin/**`, `/api/admin/**`

Si un usuario no autenticado intenta acceder a rutas protegidas, el sistema lo redirige automáticamente a la página de login. Si un usuario autenticado intenta acceder a rutas para las que no tiene permisos (por ejemplo, un cliente intentando acceder al panel admin), el sistema lo redirige a una página de acceso denegado.

**Validación de sesión en el frontend:**
El sistema incluye scripts JavaScript que verifican constantemente el estado de autenticación del usuario:
- Comprueba la existencia de datos de usuario en localStorage
- Valida la vigencia de la sesión antes de realizar operaciones sensibles
- Muestra u oculta elementos de la interfaz según el rol del usuario
- Actualiza el menú de navegación mostrando el nombre del usuario
- Proporciona botón de cierre de sesión cuando está autenticado

**Cierre de sesión:**
Cuando el usuario hace clic en "Cerrar sesión", el sistema:
1. Invalida la sesión HTTP en el servidor
2. Limpia todos los datos del usuario en localStorage
3. Limpia el carrito de compras
4. Redirige al usuario a la página de inicio
5. Actualiza la interfaz para mostrar opciones de usuario no autenticado

**Protección contra ataques:**
El sistema implementa medidas de seguridad adicionales:
- **CSRF Protection:** Tokens anti-falsificación en formularios
- **XSS Protection:** Sanitización de entradas de usuario
- **SQL Injection:** Uso de consultas preparadas (JPA/Hibernate)
- **Session Fixation:** Regeneración de sesiones después del login
- **Fuerza bruta:** Límites de intentos de login (configurable)

**Recuperación de contraseña:**
Aunque no está completamente implementado en la versión actual, el sistema tiene la estructura para agregar funcionalidad de recuperación de contraseña mediante email.

**Persistencia de sesión:**
El sistema permite configurar "Recordarme" para mantener sesiones activas por períodos extendidos, almacenando tokens seguros en cookies.

**Componentes Técnicos Involucrados:**
- Security: `SecurityConfig.java`
- Service: `CustomUserDetailsService.java`, `UsuarioService.java`
- JavaScript: `auth-check.js`, `auth-modal-v2.js`, `auth-admin.js`, `auth-client.js`
- Model: `Usuario.java`
- Filters y interceptors de Spring Security
- BCrypt para encriptación de contraseñas

---

## 13. GESTIÓN DE STOCK Y DISPONIBILIDAD

### Descripción del Proceso
El sistema mantiene un control automático del inventario de productos para evitar sobreventa y garantizar que solo se puedan comprar productos con stock disponible.

Cada producto en la base de datos tiene un campo `stock` que indica el número de unidades disponibles para la venta. Este valor se actualiza automáticamente cada vez que:

**Al completar una compra:**
Cuando un pedido es procesado exitosamente durante el checkout, el sistema ejecuta automáticamente las siguientes operaciones para cada producto del carrito:
1. Recupera el stock actual del producto desde la base de datos
2. Resta la cantidad comprada del stock disponible
3. Actualiza el registro del producto con el nuevo valor de stock
4. Si el stock llega a 0, el producto se marca visualmente como "Agotado"

**Validaciones de stock:**
Antes de confirmar cualquier compra, el sistema valida que:
- Todos los productos en el carrito tengan stock disponible
- La cantidad solicitada no exceda el stock actual
- Los productos no hayan sido desactivados

Si algún producto no tiene stock suficiente, el sistema:
- Muestra un mensaje específico indicando qué producto no tiene stock
- Indica el stock disponible actual
- Permite al usuario ajustar la cantidad o eliminar el producto del carrito
- Previene el proceso de checkout hasta resolver el problema de stock

**Visualización en el catálogo:**
Los productos con bajo stock o sin stock se muestran con indicadores visuales:
- Stock bajo (menos de 5 unidades): Etiqueta de advertencia
- Sin stock (0 unidades): Etiqueta "Agotado", botón de compra deshabilitado

**Panel de administración:**
Los administradores pueden:
- Ver el stock actual de todos los productos
- Recibir alertas de productos con stock bajo
- Actualizar manualmente el stock cuando llega nueva mercancía
- Ver historial de movimientos de stock
- Generar reportes de productos más vendidos
- Identificar productos que requieren reposición

**Transacciones atómicas:**
Las actualizaciones de stock se realizan dentro de transacciones de base de datos, garantizando que si una operación falla (por ejemplo, fallo al crear el pedido), el stock no se decremente y permanezca consistente.

**Prevención de condiciones de carrera:**
El sistema utiliza bloqueos de base de datos para prevenir que múltiples usuarios compren simultáneamente las últimas unidades disponibles de un producto.

**Componentes Técnicos Involucrados:**
- Service: `PedidoService.java`, `ProductoService.java`
- Model: `Producto.java`
- Repository: `ProductoRepository.java`
- Tabla BD: `productos` (campo `stock`)

---

## 14. CÁLCULO AUTOMÁTICO DE TOTALES

### Descripción del Proceso
El sistema implementa un mecanismo automatizado para calcular todos los montos relacionados con las compras en diferentes etapas del proceso.

**Cálculo en el carrito:**
Cuando el usuario agrega productos al carrito o modifica cantidades:
- **Subtotal por producto:** Se calcula multiplicando precio unitario × cantidad
- **Subtotal general:** Suma de todos los subtotales de productos
- **Costo de envío:** Varía según la zona seleccionada (S/ 5.00 o S/ 10.00)
- **Total a pagar:** Subtotal general + costo de envío

Todos estos cálculos se actualizan en tiempo real mediante JavaScript cada vez que el usuario realiza un cambio. Los valores se muestran formateados con dos decimales y el símbolo de moneda (S/).

**Cálculo en checkout:**
Durante el proceso de checkout, el sistema recalcula todos los montos en el backend para garantizar que no hayan sido manipulados en el cliente:
1. Recibe los productos con cantidades del frontend
2. Consulta los precios actuales en la base de datos (no confía en los precios del frontend)
3. Multiplica cada precio × cantidad
4. Suma todos los subtotales
5. Agrega el costo de envío según la zona
6. Calcula el total final

Este doble cálculo (frontend y backend) garantiza la integridad de los montos cobrados.

**Cálculo en pedidos y facturas:**
Al crear el pedido y factura, el sistema almacena:
- Precio unitario de cada producto al momento de la compra (puede diferir del precio actual)
- Subtotal de cada ítem (precio × cantidad)
- Subtotal general del pedido
- Costo de envío
- Total facturado

Estos valores quedan registrados permanentemente, permitiendo que incluso si los precios de productos cambian en el futuro, los pedidos históricos mantengan los precios con los que se facturaron.

**Manejo de decimales:**
Todos los cálculos monetarios utilizan el tipo `BigDecimal` de Java para evitar errores de redondeo típicos de tipos flotantes. Los valores se redondean a 2 decimales siguiendo reglas estándar (HALF_UP).

**Componentes Técnicos Involucrados:**
- JavaScript: `main.js`, `checkout.js` para cálculos en cliente
- Service: `PedidoService.java` para cálculos en servidor
- Model: Uso de `BigDecimal` en todas las entidades relacionadas con dinero
- Tablas BD: Campos `precio`, `subtotal`, `total` en múltiples tablas

---

## RESUMEN DE ENTIDADES Y RELACIONES

### Modelo de Datos Principal

**Usuarios** (`usuarios`)
- Almacena información de clientes y administradores
- Relación 1:N con Pedidos
- Roles: cliente, administrador

**Productos** (`productos`)
- Catálogo de autos Hot Wheels
- Relación N:1 con Categorías
- Relación 1:N con Carrito
- Relación 1:N con Detalle Pedido

**Categorías** (`categorias`)
- Clasificación de productos
- Relación 1:N con Productos

**Carrito** (`carrito`)
- Items temporales del carrito de compras
- Relación N:1 con Usuarios
- Relación N:1 con Productos

**Pedidos** (`pedidos`)
- Registro de órdenes de compra
- Relación N:1 con Usuarios
- Relación 1:N con Detalle Pedido
- Relación 1:1 con Factura

**Detalle Pedido** (`detalle_pedido`)
- Productos específicos de cada pedido
- Relación N:1 con Pedidos
- Relación N:1 con Productos

**Factura** (`factura`)
- Comprobantes de venta
- Relación 1:1 con Pedidos

**Contactos** (`contactos`)
- Mensajes del formulario de contacto
- Entidad independiente

---

## TECNOLOGÍAS UTILIZADAS

### Backend
- **Java 17** - Lenguaje de programación
- **Spring Boot 3.x** - Framework principal
- **Spring Security** - Autenticación y autorización
- **Spring Data JPA** - Persistencia de datos
- **Hibernate** - ORM (Object-Relational Mapping)
- **BCrypt** - Encriptación de contraseñas
- **Lombok** - Reducción de código boilerplate

### Frontend
- **HTML5** - Estructura de páginas
- **CSS3** - Estilos y diseño responsivo
- **JavaScript (ES6+)** - Lógica del cliente
- **jQuery 3.x** - Manipulación del DOM y AJAX
- **Thymeleaf** - Motor de plantillas (si aplica)

### Base de Datos
- **MySQL / MariaDB 10.4** - Sistema de gestión de base de datos
- Motor InnoDB
- Codificación UTF-8 (utf8mb4)

### Herramientas de Desarrollo
- **Maven** - Gestión de dependencias y construcción
- **Git** - Control de versiones
- **XAMPP** - Entorno de desarrollo local

### APIs y Servicios
- **REST API** - Comunicación frontend-backend
- **JSON** - Formato de intercambio de datos
- **AJAX** - Peticiones asíncronas

---

## CONCLUSIONES

El sistema SaCars es una aplicación web completa de comercio electrónico especializada en la venta de autos Hot Wheels. Implementa todos los procesos fundamentales de un e-commerce moderno:

✅ **Gestión de usuarios** con registro, autenticación y perfiles  
✅ **Catálogo de productos** con búsqueda, filtrado y ordenamiento avanzado  
✅ **Carrito de compras** con gestión de cantidades y cálculo de envíos  
✅ **Proceso de checkout** completo con validaciones  
✅ **Sistema de pedidos** con seguimiento y historial  
✅ **Facturación automática** con numeración secuencial  
✅ **Panel de administración** para gestión del negocio  
✅ **Control de inventario** con actualización automática de stock  
✅ **Seguridad robusta** con encriptación y control de acceso por roles  
✅ **Formulario de contacto** para atención al cliente  

El sistema está construido con arquitectura de tres capas (presentación, lógica de negocio, datos) siguiendo buenas prácticas de desarrollo y patrones de diseño establecidos.

---

**Documento generado:** 31 de enero de 2026  
**Sistema:** SaCars - Venta de Hot Wheels  
**Versión del documento:** 1.0
