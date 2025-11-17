const API_URL = 'http://localhost:8082/api';

$(document).ready(function() {

    // Cambiar entre pestañas
    $('.tab-button').click(function() {
        const tab = $(this).data('tab');

        $('.tab-button').removeClass('active');
        $(this).addClass('active');

        $('.auth-form').removeClass('active');
        $('#' + tab).addClass('active');
    });

    // ---------------- LOGIN ----------------
    $('#login-form').submit(function(e) {
        e.preventDefault();

        const email = $('#login-email').val();
        const contrasena = $('#login-password').val();

        if (!email || !contrasena) {
            mostrarError('#login-error', 'Por favor completa todos los campos');
            return;
        }

        $.ajax({
            type: 'POST',
            url: API_URL + '/auth/login',
            contentType: 'application/json',
            data: JSON.stringify({
                email: email,
                contrasena: contrasena
            }),
            success: function(response) {
                if (response.success) {
                    localStorage.setItem('token', response.data.token);
                    localStorage.setItem('usuario', JSON.stringify(response.data.usuario));
                    localStorage.setItem('rol', response.data.usuario.rol);

                    alert('Bienvenido ' + response.data.usuario.nombre);
                    window.location.href = 'index.html';
                } else {
                    mostrarError('#login-error', response.message || 'Error en login');
                }
            },
            error: function(xhr) {
                console.error('Error:', xhr);
                mostrarError('#login-error', 'Error al conectar con el servidor.');
            }
        });
    });

    // ---------------- REGISTRO ----------------
    $('#registro-form').submit(function(e) {
        e.preventDefault();

        const nombre = $('#registro-nombre').val();
        const apellido = $('#registro-apellido').val();
        const email = $('#registro-email').val();
        const telefono = $('#registro-telefono').val();
        const contrasena = $('#registro-password').val();
        const contrasena2 = $('#registro-password2').val();

        if (!nombre || !email || !telefono || !contrasena || !contrasena2) {
            mostrarError('#registro-error', 'Por favor completa todos los campos');
            return;
        }

        if (contrasena !== contrasena2) {
            mostrarError('#registro-error', 'Las contraseñas no coinciden');
            return;
        }

        if (contrasena.length < 6) {
            mostrarError('#registro-error', 'La contraseña debe tener al menos 6 caracteres');
            return;
        }

        $.ajax({
            type: 'POST',
            url: API_URL + '/auth/registro',
            contentType: 'application/json',
            data: JSON.stringify({
                nombre: nombre,
                apellido: apellido,
                email: email,
                telefono: telefono,
                contrasena: contrasena
            }),
            success: function(response) {
                if (response.success) {
                    alert('¡Registro exitoso! Ahora inicia sesión.');
                    $('.tab-button[data-tab="login"]').click();
                    $('#registro-form')[0].reset();
                } else {
                    mostrarError('#registro-error', response.message || 'Error en registro');
                }
            },
            error: function() {
                mostrarError('#registro-error', 'Error al conectar con el servidor');
            }
        });
    });

});


function mostrarError(selector, mensaje) {
    const errorEl = $(selector);
    errorEl.text(mensaje).addClass('show');
    setTimeout(() => {
        errorEl.removeClass('show');
    }, 5000);
}
