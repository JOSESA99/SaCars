const API_URL = "http://localhost:8082/api";

$(document).ready(function () {
    console.log("AUTH JS CARGADO");

    // ---------------------------
    // CAMBIO DE TABS LOGIN / REGISTRO
    // ---------------------------
    $(".tab-button").click(function () {
        const tab = $(this).data("tab");
        console.log("Cambiando tab a:", tab);

        $(".tab-button").removeClass("active");
        $(this).addClass("active");

        $(".auth-form").removeClass("active");
        $("#" + tab).addClass("active");
    });

    // ---------------------------
    // LOGIN
    // ---------------------------
    $("#login-form").submit(function (e) {
        e.preventDefault();
        console.log("Interceptado login");

        const email = $("#login-email").val();
        const password = $("#login-password").val();

        console.log("Email:", email);
        console.log("Enviando login...");

        $.ajax({
            type: "POST",
            url: API_URL + "/auth/login",
            contentType: "application/json",
            data: JSON.stringify({ email, password }),

            success: function (response) {
                console.log("Respuesta del servidor:", response);

                if (response.success) {

                    const u = response.data.usuario;
                    console.log("Usuario:", u);

                    localStorage.setItem("token", response.data.token);

                    localStorage.setItem("usuario", JSON.stringify({
                        id: u.idUsuario,
                        nombre: u.nombre,
                        apellido: u.apellido,
                        correo: u.email,
                        telefono: u.telefono,
                        dni: u.dni,
                        rol: u.rol
                    }));

                    // Redirección automática según el rol
                    alert("Bienvenido " + u.nombre);
                    console.log("Rol del usuario:", u.rol);
                    
                    if (u.rol === 'administrador') {
                        console.log("Redirigiendo a admin dashboard...");
                        window.location.href = "/admin/dashboard";
                    } else if (u.rol === 'cliente') {
                        console.log("Redirigiendo a cliente dashboard...");
                        window.location.href = "/cliente/dashboard";
                    } else {
                        console.log("Redirigiendo a home...");
                        window.location.href = "/";
                    }

                } else {
                    $("#login-error").text(response.message);
                }
            },

            error: function (xhr, status, error) {
                console.error("Error en login:", xhr.responseText);
                console.error("Status:", status);
                console.error("Error:", error);
                
                let mensaje = "Error al conectar con el servidor.";
                if (xhr.responseJSON && xhr.responseJSON.message) {
                    mensaje = xhr.responseJSON.message;
                }
                $("#login-error").text(mensaje);
            },
        });
    });

    // ---------------------------
    // REGISTRO (con DNI)
    // ---------------------------
    $("#registro-form").submit(function (e) {
        e.preventDefault();
        console.log("Interceptado registro");

        const data = {
            nombre: $("#registro-nombre").val(),
            apellido: $("#registro-apellido").val(),
            dni: $("#registro-dni").val(),       // 👈 NUEVO
            email: $("#registro-email").val(),
            telefono: $("#registro-telefono").val(),
            password: $("#registro-password").val(),
        };

        const pass2 = $("#registro-password2").val();

        // Validación de DNI
        if (!data.dni || data.dni.length !== 8 || !/^\d+$/.test(data.dni)) {
            $("#registro-error").text("El DNI debe tener 8 dígitos numéricos.");
            return;
        }

        // Validación de contraseñas
        if (data.password !== pass2) {
            $("#registro-error").text("Las contraseñas no coinciden.");
            return;
        }

        $.ajax({
            type: "POST",
            url: API_URL + "/auth/registro",
            contentType: "application/json",
            data: JSON.stringify(data),

            success: function (response) {
                if (response.success) {
                    alert("¡Registro exitoso! Ahora inicia sesión.");

                    // Cambiar automáticamente al login
                    $('.tab-button[data-tab="login"]').click();

                    $("#registro-form")[0].reset();
                    $("#registro-error").text("");
                } else {
                    $("#registro-error").text(response.message);
                }
            },

            error: function () {
                $("#registro-error").text("Error al conectar con el servidor.");
            },
        });
    });
});
