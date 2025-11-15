# 📚 Implementación de jQuery en el Catálogo

## 🎯 Funcionalidades Implementadas

Este catálogo utiliza **jQuery** extensivamente para proporcionar una experiencia de usuario dinámica e interactiva.

---

## ✨ 1. Búsqueda en Tiempo Real

**Método jQuery usado:** `.on('keyup')`, `.val()`, `.filter()`

```javascript
$('#buscar-producto').on('keyup', function() {
    aplicarFiltros();
});
```

**Funcionalidad:**
- Busca productos mientras escribes
- Filtra por nombre del producto
- Actualiza resultados instantáneamente sin recargar la página

**Cómo probarlo:**
- Escribe "Camaro" → Solo muestra el Camaro
- Escribe "Ford" → Solo muestra el Ford GT-40

---

## 💰 2. Filtro por Precio

**Método jQuery usado:** `.on('change')`, `.data()`, `.filter()`

```javascript
$('#filtro-precio').on('change', function() {
    aplicarFiltros();
});
```

**Categorías:**
- **Económicos:** S/ 15 - S/ 20
- **Medianos:** S/ 21 - S/ 30
- **Premium:** S/ 31+

**Cómo probarlo:**
- Selecciona "Premium" → Solo muestra el Ford GT-40 (S/ 35)
- Selecciona "Económicos" → Muestra productos de S/ 15-20

---

## 📊 3. Ordenamiento Dinámico

**Método jQuery usado:** `.sort()`, `.data()`, `localeCompare()`

```javascript
productosFiltrados = $(productosFiltrados).sort(function(a, b) {
    const precioA = parseFloat($(a).data('precio'));
    const precioB = parseFloat($(b).data('precio'));
    return precioA - precioB; // Menor a mayor
});
```

**Opciones:**
- Precio: Menor a Mayor
- Precio: Mayor a Menor
- Nombre: A-Z
- Nombre: Z-A

**Cómo probarlo:**
- Selecciona "Precio: Menor a Mayor" → Verás primero los de S/ 15
- Selecciona "Nombre: A-Z" → Orden alfabético

---

## 📄 4. Paginación

**Método jQuery usado:** `.slice()`, `.append()`, `.prop()`, `.empty()`

```javascript
const productosAPaginar = productosFiltrados.slice(inicio, fin);
$('#catalogo-productos').empty();
$(productosAPaginar).each(function() {
    $('#catalogo-productos').append($(this));
});
```

**Funcionalidad:**
- Muestra 3 productos por página
- Botones "Anterior" y "Siguiente"
- Indicador de página actual
- Los botones se deshabilitan automáticamente cuando no hay más páginas

**Cómo probarlo:**
- Haz clic en "Siguiente" → Muestra los siguientes 3 productos
- El botón "Anterior" se deshabilita en la página 1

---

## 🎨 5. Animaciones con jQuery

**Métodos jQuery usados:** `.fadeIn()`, `.fadeOut()`, `.animate()`, `.css()`

### a) Transiciones de productos
```javascript
$('.producto-item').fadeOut(300, function() {
    $(this).hide();
});
// Luego...
$(this).fadeIn(500);
```

### b) Scroll suave
```javascript
$('html, body').animate({
    scrollTop: $('#catalogo-productos').offset().top - 100
}, 400);
```

### c) Efecto hover
```javascript
$(document).on('mouseenter', '.producto-item', function() {
    $(this).find('img').css('transform', 'scale(1.05)');
});
```

---

## 🔄 6. Limpiar Filtros

**Método jQuery usado:** `.val()`, `.addClass()`, `.removeClass()`, `setTimeout()`

```javascript
$('#limpiar-filtros').on('click', function() {
    $('#buscar-producto').val('');
    $('#filtro-precio').val('todos');
    $('#ordenar-por').val('default');
    
    // Animación del botón
    $(this).addClass('btn-clicked');
    setTimeout(() => {
        $(this).removeClass('btn-clicked');
    }, 200);
    
    aplicarFiltros();
});
```

---

## 📊 7. Contador Dinámico de Resultados

**Método jQuery usado:** `.text()`, `.length`

```javascript
$('#contador-productos').text(totalProductos);
```

Muestra en tiempo real cuántos productos coinciden con los filtros aplicados.

---

## 🚫 8. Mensaje "Sin Resultados"

**Método jQuery usado:** `.fadeIn()`, `.fadeOut()`, `.hide()`, `.show()`

```javascript
if (totalProductos === 0) {
    $('#sin-resultados').fadeIn(300);
    $('#catalogo-productos').fadeOut(300);
}
```

Aparece automáticamente cuando no hay productos que coincidan con los filtros.

---

## 🎯 Métodos jQuery Utilizados (Resumen)

| Método | Uso en el Proyecto |
|--------|-------------------|
| `$(document).ready()` | Inicializar todo cuando carga la página |
| `.on()` | Eventos de click, change, keyup |
| `.val()` | Obtener/establecer valores de inputs |
| `.data()` | Leer atributos data-* de HTML |
| `.filter()` | Filtrar productos por condiciones |
| `.sort()` | Ordenar productos |
| `.slice()` | Paginación (cortar array) |
| `.each()` | Iterar sobre productos |
| `.append()` | Agregar productos al DOM |
| `.empty()` | Limpiar contenedor |
| `.fadeIn()` / `.fadeOut()` | Animaciones de aparición |
| `.animate()` | Scroll suave |
| `.css()` | Modificar estilos dinámicamente |
| `.text()` | Actualizar texto del contador |
| `.prop()` | Habilitar/deshabilitar botones |
| `.addClass()` / `.removeClass()` | Animaciones de clases |
| `.find()` | Buscar elementos hijos |
| `.offset()` | Obtener posición para scroll |

---

## 🎓 Para Demostrar a tu Profesor

### Demostración 1: Búsqueda en Tiempo Real
1. Abre el catálogo
2. Escribe "Ford" en el buscador
3. Muestra cómo filtra instantáneamente

### Demostración 2: Filtros Combinados
1. Selecciona "Económicos" en el filtro de precio
2. Ordena por "Precio: Mayor a Menor"
3. Muestra cómo se combinan ambos filtros

### Demostración 3: Paginación
1. Navega entre páginas con los botones
2. Muestra el scroll automático
3. Muestra cómo se deshabilitan los botones

### Demostración 4: Sin Resultados
1. Busca algo que no existe (ej: "Ferrari")
2. Muestra el mensaje de "sin resultados"
3. Haz clic en "Limpiar búsqueda"

### Demostración 5: Animaciones
1. Pasa el mouse sobre los productos (efecto hover)
2. Cambia de página (fadeIn/fadeOut)
3. Muestra el scroll suave

---

## 💡 Ventajas de Usar jQuery

✅ **Código más corto y legible** que JavaScript vanilla  
✅ **Compatibilidad** con navegadores antiguos  
✅ **Animaciones suaves** con métodos integrados  
✅ **Manipulación del DOM** simplificada  
✅ **Manejo de eventos** más fácil  

---

## 📝 Archivos Modificados

1. **catalogo.html** - Estructura HTML con controles y script jQuery
2. **styles.css** - Estilos para los nuevos elementos
3. **main.js** - Ya tenía jQuery, se mantiene para el carrito

---

**Desarrollado con jQuery 3.7.1**  
**Todas las funcionalidades son 100% funcionales y listas para demostrar** ✨
