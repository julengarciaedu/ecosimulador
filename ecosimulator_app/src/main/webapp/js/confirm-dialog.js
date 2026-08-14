// Sustituye a window.confirm() por un <dialog> con el mismo estilo que el resto
// de la aplicación (los diálogos nativos del navegador no se pueden personalizar).
(function () {
    'use strict';

    var dialogEl = null;

    function crearDialogo() {
        var dialog = document.createElement('dialog');
        dialog.className = 'confirm-dialog';
        dialog.innerHTML =
            '<div class="confirm-dialog-icon"></div>' +
            '<p class="confirm-dialog-mensaje"></p>' +
            '<div class="confirm-dialog-actions">' +
            '<button type="button" class="btn-secondary confirm-dialog-cancelar"></button>' +
            '<button type="button" class="confirm-dialog-aceptar"></button>' +
            '</div>';
        document.body.appendChild(dialog);
        return dialog;
    }

    /**
     * Muestra un diálogo de confirmación estilizado y devuelve una Promise<boolean>.
     * opciones: { tipo: 'danger'|'info', textoAceptar, textoCancelar }
     */
    window.confirmarAccion = function (mensaje, opciones) {
        opciones = opciones || {};
        var tipo = opciones.tipo || 'danger';
        var textoAceptar = opciones.textoAceptar || (tipo === 'danger' ? '🗑️ Eliminar' : '✅ Aceptar');
        var textoCancelar = opciones.textoCancelar || 'Cancelar';

        if (!dialogEl) {
            dialogEl = crearDialogo();
        }

        var icono = dialogEl.querySelector('.confirm-dialog-icon');
        var texto = dialogEl.querySelector('.confirm-dialog-mensaje');
        var btnAceptar = dialogEl.querySelector('.confirm-dialog-aceptar');
        var btnCancelar = dialogEl.querySelector('.confirm-dialog-cancelar');

        dialogEl.classList.toggle('confirm-dialog-danger', tipo === 'danger');
        icono.textContent = tipo === 'danger' ? '⚠️' : 'ℹ️';
        texto.textContent = mensaje;
        btnAceptar.textContent = textoAceptar;
        btnAceptar.className = (tipo === 'danger' ? 'btn-danger' : 'btn-primary') + ' confirm-dialog-aceptar';
        btnCancelar.textContent = textoCancelar;

        return new Promise(function (resolve) {
            function limpiar() {
                btnAceptar.removeEventListener('click', onAceptar);
                btnCancelar.removeEventListener('click', onCancelar);
                dialogEl.removeEventListener('cancel', onCancelarTeclado);
            }
            function onAceptar() {
                limpiar();
                dialogEl.close();
                resolve(true);
            }
            function onCancelar() {
                limpiar();
                dialogEl.close();
                resolve(false);
            }
            function onCancelarTeclado() {
                limpiar();
                resolve(false);
            }
            btnAceptar.addEventListener('click', onAceptar);
            btnCancelar.addEventListener('click', onCancelar);
            dialogEl.addEventListener('cancel', onCancelarTeclado, { once: true });
            dialogEl.showModal();
        });
    };

    /**
     * Para usar como onsubmit="return confirmarEnvioFormulario(event, 'mensaje')"
     * en formularios de eliminación: intercepta el envío, muestra el diálogo y,
     * si se confirma, envía el formulario original.
     */
    window.confirmarEnvioFormulario = function (event, mensaje, opciones) {
        event.preventDefault();
        var form = event.target;
        window.confirmarAccion(mensaje, opciones).then(function (confirmado) {
            if (confirmado) {
                form.submit();
            }
        });
        return false;
    };
})();
