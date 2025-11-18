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
        
        // Actualizar contador del carrito
        actualizarContadorCarrito();
        
    } else {
        // USUARIO NO LOGUEADO - Solo mostrar UI
        $('#usuario-conectado').hide();
        $('.btn-login-header').show();
        $('#btn-logout').hide();
        
        // NO DESHABILITAR BOTONES - Se manejará en los eventos
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