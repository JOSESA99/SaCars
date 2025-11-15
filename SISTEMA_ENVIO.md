# Sistema de Selección de Zona de Envío + Checkout - SaCars

## 📦 Cómo Funciona

### Flujo del Usuario

#### Para "Agregar al Carrito":
1. **Usuario ve un producto** y hace clic en "Ver Producto"
2. Se abre el modal con los detalles del producto
3. **Usuario hace clic en "Agregar al Carrito"**
4. **Primera vez**: Se abre un modal para seleccionar la zona de envío
5. **Siguientes veces**: El producto se agrega directamente (la zona ya está guardada)

#### Para "Comprar Ahora":
1. **Usuario ve un producto** y hace clic en "Ver Producto"
2. Se abre el modal con los detalles del producto
3. **Usuario hace clic en "Comprar Ahora"**
4. **Primera vez**: Se abre un modal para seleccionar la zona de envío, luego va al checkout
5. **Siguientes veces**: Va directamente al checkout

### Zonas de Envío Disponibles

- **Banda de Shilcayo**: S/ 5.00
- **Tarapoto**: S/ 7.00
- **Morales**: S/ 10.00
- **Otros lugares de Perú**: Contacto por WhatsApp

## 🔧 Configuración Importante

### Número de WhatsApp

**DEBES CAMBIAR** el número de WhatsApp en el archivo `js/main.js`:

```javascript
const numeroWhatsApp = "51918341898"; // CAMBIAR POR TU NÚMERO
```

Reemplaza `51918341898` con tu número real en formato internacional (sin espacios ni guiones).

Ejemplo: Si tu número es 918341898, usa: `51918341898`

## 💾 Almacenamiento

El sistema guarda en `localStorage`:
- **zonaEnvio**: Nombre de la zona seleccionada
- **costoEnvio**: Costo de envío en soles
- **carrito**: Array con los productos agregados

## 📄 Archivos Creados/Modificados

### Archivos Nuevos:
1. **checkout.html** - Página de finalización de compra
2. **js/checkout.js** - Lógica específica del checkout

### Archivos Modificados:
3. **index.html** - Agregado modal de zona de envío
4. **catalogo.html** - Agregado modal de zona de envío
5. **carrito.html** - Actualizado para mostrar costo de envío
6. **js/main.js** - Lógica completa del sistema + botón "Comprar Ahora"
7. **css/styles.css** - Estilos del modal de envío + estilos del checkout

## 🛒 Página de Checkout

### Características:
- ✅ Muestra resumen del pedido con productos y totales
- ✅ Formulario de datos del cliente (nombre, teléfono, dirección, etc.)
- ✅ Opción para cambiar zona de envío
- ✅ Envío automático del pedido por WhatsApp
- ✅ Diseño responsive y consistente con SaCars

### Campos del Formulario:
- **Nombre Completo** (obligatorio)
- **Teléfono/WhatsApp** (obligatorio)
- **Email** (opcional)
- **Dirección de Entrega** (obligatorio)
- **Zona de Entrega** (solo lectura, se puede cambiar con botón)
- **Comentarios Adicionales** (opcional)

## 🎨 Características

- ✅ Selección de zona solo una vez por compra
- ✅ Costo de envío se suma automáticamente al total
- ✅ Opción de contactar por WhatsApp para otros lugares
- ✅ Diseño responsive (funciona en móviles)
- ✅ Notificaciones visuales al agregar productos

## 🔄 Para Resetear la Zona de Envío

Si quieres cambiar la zona de envío, el usuario debe:
1. Vaciar el carrito (botón "Vaciar Carrito")
2. Agregar productos nuevamente

O puedes agregar un botón para cambiar zona manualmente si lo necesitas.
