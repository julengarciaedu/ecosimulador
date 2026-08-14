// Ejemplo de integración con resultados de simulación
function actualizarEcosistema(resultados) {
    const score = resultados.puntuacionSostenibilidad;
    
    // Actualizar estado visual
    if (window.ecoState) {
        window.ecoState.updateState(null, score);
    }
    
    // Actualizar elementos específicos
    document.querySelectorAll('[data-simulation-score]').forEach(el => {
        const score = parseInt(el.dataset.simulationScore);
        // Actualizar contenido según puntuación
    });
    
    // Actualizar badges
    document.querySelectorAll('.eco-badge').forEach(el => {
        // Cambiar clase según estado
    });
}