// SISTEMA DE MODALES DE PRODUCTOS
$(document).ready(function() {
    // Variables del modal
    const modal = document.getElementById('modal-producto');
    const cerrarModal = document.querySelector('.modal-cerrar');
    const modalImg = document.getElementById('modal-img');
    const modalTitulo = document.getElementById('modal-titulo');
    const modalDescripcion = document.getElementById('modal-descripcion');
    const modalPrecio = document.getElementById('modal-precio');

    // Función para abrir modal con datos del producto
    function abrirModal(e) {
        const boton = e.target.closest('.btn-ver-producto');
        if (!boton) return;
        
        const imagen = boton.dataset.imagen;
        const titulo = boton.dataset.titulo;
        const descripcion = boton.dataset.descripcion;
        const precio = boton.dataset.precio;

        modalImg.src = imagen;
        modalTitulo.textContent = titulo;
        modalDescripcion.textContent = descripcion;
        modalPrecio.textContent = precio;
        
        modal.classList.add('modal-visible');
    }

    // Función para cerrar modal
    function cerrarElModal() {
        modal.classList.remove('modal-visible');
    }

    // Función para verificar autenticación antes de acciones
    function verificarYRedirigir() {
        const token = localStorage.getItem('token');
        if (!token) {
            window.location.href = '/auth/login';
            return false;
        }
        return true;
    }

    // DELEGACIÓN DE EVENTOS: Funciona con elementos dinámicos
    document.addEventListener('click', function(e) {
        if (e.target.closest('.btn-ver-producto')) {
            abrirModal(e);
        }
        
        // Comprar ahora - redirigir si no está logueado
        if (e.target.classList.contains('boton-comprar')) {
            if (!verificarYRedirigir()) return;
            // Aquí va la lógica de compra para usuarios logueados
            alert('Función de compra para usuarios logueados');
        }
        
        // Agregar al carrito - redirigir si no está logueado
        if (e.target.classList.contains('boton-agregar')) {
            if (!verificarYRedirigir()) return;
            // Aquí va la lógica del carrito para usuarios logueados
            alert('Producto agregado al carrito');
        }
    });

    // Event listeners para cerrar modal
    if (cerrarModal) {
        cerrarModal.addEventListener('click', cerrarElModal);
    }

    if (modal) {
        modal.addEventListener('click', function(e) {
            if (e.target === modal) {
                cerrarElModal();
            }
        });
    }
});