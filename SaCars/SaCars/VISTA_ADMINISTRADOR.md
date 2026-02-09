## ADMINISTRADOR:
## RQ1.5: CRUD de Usuarios
El sistema permitirá a los administradores gestionar todos los usuarios registrados en la plataforma mediante operaciones 
CRUD completas (Crear, Leer, Actualizar, Eliminar/Desactivar). 
Editar usuario: El administrador podrá modificar la información de cualquier usuario haciendo clic en el botón "Editar". Se abrirá un formulario prellenado con los datos actuales del usuario donde se podrán modificar: 
- Nombre y apellido 
- DNI (validando que no coincida con otro usuario) 
- Email (validando unicidad) 
- Teléfono 
- Dirección 
- El administrador podrá activar o desactivar usuarios. Al desactivar un usuario: 
- No podrá iniciar sesión en el sistema - Sus pedidos pendientes permanecerán en el sistema - Su información se mantendrá en la base de datos - Podrá ser reactivado en cualquier momento Listar usuario: Cuando el administrador acceda a ese módulo de cliente, el sistema mostrará una tabla completa con todos los usuarios del sistema, tanto clientes como administradores. La tabla incluirá las siguientes columnas

## RQ1.4: Dashboard Administrativo 
El dashboard administrativo será el centro de control principal para los administradores del sistema, proporcionando una vista completa y actualizada del estado del negocio. Cuando un administrador inicie sesión y acceda al panel, el sistema cargará automáticamente estadísticas clave en tiempo real.
Clientes registrados: El sistema contará el número total de usuarios con rol "cliente" que estén activos en la base de datos. Adicionalmente, calculará cuántos usuarios nuevos se registraron en los últimos 7 días para mostrar un indicador de crecimiento, por ejemplo: "+5 nuevos esta semana". Esta métrica ayuda a monitorear la adquisición de clientes.
Pedidos pendientes: El sistema contará todos los pedidos que tengan estado "PENDIENTE" en la base de datos. Este número indicará cuántos pedidos requieren atención inmediata del administrador. Al ser un sistema pequeño de manera local y que se maneja en esta ciudad, el administrador es el quien va poder marcar el pedido como completado después de saber que fue entregado el pedido.
Ingresos por ventas: El sistema calculará automáticamente el total de ventas del mes actual sumando todos los pedidos con estado COMPLETADO cuya fecha esté dentro del mes en curso. Además, comparará este valor con el mes anterior para calcular y mostrar el porcentaje de crecimiento o decrecimiento. El valor se mostrará formateado en soles (S/) con dos decimales.
## RQ1.8: CRUD De productos
El sistema permitirá a los administradores gestionar el catálogo completo de productos mediante operaciones CRUD, proporcionando control total sobre el inventario de Hot Wheels.
Listar productos: El sistema mostrará una tabla con todos los productos del inventario, incluyendo productos activos e inactivos. La tabla mostrará: 
- ID del producto 
- Imagen miniatura 
- Nombre del producto 
- Descripción (truncada, 50 caracteres) 
- Precio actual 
- Stock disponible con indicador de alerta (rojo si stock < 5, amarillo si stock < 10) 
- Categoría 
- Estado (Activo/Inactivo)
 - Búsqueda por nombre o descripción en tiempo real 
- Filtro por categoría 
- Filtro por estado (Activo/Inactivo) 
- Filtro por stock (Con stock / Sin stock / Stock bajo)
Crear producto: El administrador accederá a un formulario para agregar productos nuevos con los siguientes campos:
*Información Básica: * 
- **Nombre: ** Campo de texto obligatorio, máx. 100 caracteres
 - **Descripción: ** Área de texto obligatoria para descripción detallada 
- **Precio: ** Campo numérico obligatorio, validación de valor positivo, formato decimal con 2 decimales 
- **Stock Inicial: ** Campo numérico obligatorio, entero positivo 
- **Categoría: ** Selector desplegable con categorías existentes (Deportivos, Clásicos, Tuners, Carreras, Fantasía, Otros) 
- **Imagen Principal: ** Campo de carga de archivo (validar formatos JPG, PNG, máx. 5MB)
Editar productos: 
El administrador podrá modificar cualquier producto existente. El sistema cargará un formulario idéntico al de creación, pero prellenado con los datos actuales del producto. Se podrán modificar todos los campos excepto el ID y la fecha de creación.
Desactivar productos: 
Cambia el estado del producto a inactivo. El producto desaparece del catálogo público, pero se mantiene en la base de datos. Los pedidos existentes con ese producto no se afectan.
## RQ1.9: Gestión de Stock:
Al no manejar diversidad de productos solo carritos será un modulo simple, se mostrará, total de
productos de catálogo, total de unidades disponibles en stock, habrá un botón de agregar stock en la
sección de inventario para poder manejar el subir cantidad de productos.
## RQ.1.7: CRUD de categoría
El sistema permitirá crear, editar y gestionar las categorías que organizan el catálogo de productos,
facilitando la navegación y búsqueda para los clientes, sigue el mismo flujo de crear un producto
## RQ1.8: Gestión de pedidos:
El sistema proporcionará a los administradores herramientas completas para gestionar todos los
pedidos del sistema, incluyendo cambio de estados, seguimiento y acciones específicas por pedido.
Listar pedidos:
El sistema mostrará una tabla con todos los pedidos registrados, mostrando:
- ID del pedido
- Número de factura asociado
- Nombre del cliente (con enlace a perfil del usuario)
- Fecha del pedido
- Productos incluidos (cantidad de ítems)
- Total pagado
- Zona de envío
El administrador se encargará de cambiar el estado del pedido, cuando el cliente hace el checkout por
la pagina web el pedido esta como pendiente, y al ser algo local, y entrega a domicilio rápido es el
administrador quien cambia el estado a completado.
## RQ1.9: Reportes
Al no ser un negocio muy grande, habrá reportes simples, como reporte de ventas, en la cual podrá
haber un seleccionable, de ventas de hoy, hace una semana, un mes, reporte de clientes registrados,
clientes que mas compraron, reporte de carritos que mas se venden, reporte de stock de carritos,
reporte de pedidos por estado y pedidos por zona, ingresos. 