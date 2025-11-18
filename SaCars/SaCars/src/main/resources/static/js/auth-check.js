// auth-check.js - Controla qué se muestra según autenticación
$(document).ready(function() {
    verificarAutenticacion();
});

function verificarAutenticacion() {
    const token = localStorage.getItem('token');
    const usuario = JSON.parse(localStorage.getItem('usuario') || '{}');
    
    if (token && usuario) {
        // USUARIO LOGUEADO - Mostrar funcionalidades completas
        $('#usuario-conectado').show();
        $('#nombre-usuario').text(usuario.nombre);
        $('.btn-login-header').hide();
        $('#btn-logout').show();
        
        // Habilitar botones de compra
        $('.boton-comprar, .boton-agregar').prop('disabled', false);
        
        // Actualizar contador del carrito
        actualizarContadorCarrito();
        
    } else {
        // USUARIO NO LOGUEADO - Limitar funcionalidades
        $('#usuario-conectado').hide();
        $('.btn-login-header').show();
        $('#btn-logout').hide();
        
        // Deshabilitar botones de compra y mostrar tooltip
        $('.boton-comprar, .boton-agregar').prop('disabled', true)
            .attr('title', 'Inicia sesión para comprar')
            .css('opacity', '0.6');
    }
}

function actualizarContadorCarrito() {
    const carrito = JSON.parse(localStorage.getItem('carrito') || '[]');
    const contador = carrito.reduce((total, item) => total + item.cantidad, 0);
    
    // Crear o actualizar contador
    let $contador = $('.contador-carrito');
    if ($contador.length === 0) {
        $('.carrito-icono').append('<span class="contador-carrito">0</span>');
        $contador = $('.contador-carrito');
    }
    
    $contador.text(contador).toggle(contador > 0);
}

// Logout
$('#btn-logout').click(function(e) {
    e.preventDefault();
    localStorage.removeItem('token');
    localStorage.removeItem('usuario');
    localStorage.removeItem('carrito');
    window.location.href = '/';
});