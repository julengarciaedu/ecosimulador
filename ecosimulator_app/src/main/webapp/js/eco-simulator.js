// ============================================
// eco-simulator.js - Control de Estados
// ============================================

class EcoStateManager {
    constructor() {
        this.states = {
            healthy: {
                bodyClass: 'healthy',
                emoji: '🌳',
                message: 'Ecosistema Saludable',
                color: '#2d6a4f'
            },
            balanced: {
                bodyClass: 'balanced',
                emoji: '🌿',
                message: 'Ecosistema en Equilibrio',
                color: '#40916c'
            },
            vulnerable: {
                bodyClass: 'vulnerable',
                emoji: '⚠️',
                message: 'Ecosistema Vulnerable',
                color: '#d4a373'
            },
            danger: {
                bodyClass: 'danger',
                emoji: '🔥',
                message: 'Ecosistema en Peligro',
                color: '#b53b3b'
            },
            critical: {
                bodyClass: 'critical',
                emoji: '🚨',
                message: '¡Ecosistema Crítico!',
                color: '#7a1a1a'
            }
        };
        
        this.currentState = null;
        this.init();
    }
    
    init() {
        // Escuchar cambios en la simulación
        this.setupStateListener();
    }
    
    setupStateListener() {
        // Observar cambios en el estado de la simulación
        const stateObserver = new MutationObserver((mutations) => {
            mutations.forEach((mutation) => {
                if (mutation.attributeName === 'data-ecosystem-state') {
                    this.updateUI();
                }
            });
        });
        
        // Observar el body para cambios de estado
        stateObserver.observe(document.body, {
            attributes: true,
            attributeFilter: ['data-ecosystem-state']
        });
    }
    
    updateState(stateKey, score) {
        // Determinar estado basado en puntuación
        let newState = this.determineState(score);
        
        // Actualizar atributo data en body
        document.body.setAttribute('data-ecosystem-state', newState);
        this.currentState = newState;
        
        // Actualizar elementos visuales
        this.updateHeaderEmoji(newState);
        this.updateFavicon(newState);
        this.updateStatusMessages(newState);
        
        // Mostrar notificación
        this.showStateNotification(newState);
    }
    
    determineState(score) {
        if (score >= 80) return 'healthy';
        if (score >= 60) return 'balanced';
        if (score >= 40) return 'vulnerable';
        if (score >= 20) return 'danger';
        return 'critical';
    }
    
    updateHeaderEmoji(state) {
        const header = document.querySelector('header');
        if (!header) return;
        
        // Remover emojis existentes
        const existingEmoji = header.querySelector('.eco-emoji');
        if (existingEmoji) existingEmoji.remove();
        
        // Agregar nuevo emoji
        const emoji = document.createElement('span');
        emoji.className = 'eco-emoji';
        emoji.textContent = this.states[state].emoji;
        emoji.style.cssText = `
            font-size: 4rem;
            display: block;
            margin: var(--space-sm) auto;
            animation: float 4s ease-in-out infinite;
            opacity: 0.8;
        `;
        
        header.insertBefore(emoji, header.firstChild);
    }
    
    updateFavicon(state) {
        // Cambiar favicon según estado
        const canvas = document.createElement('canvas');
        canvas.width = 32;
        canvas.height = 32;
        const ctx = canvas.getContext('2d');
        
        // Fondo
        ctx.fillStyle = this.states[state].color;
        ctx.beginPath();
        ctx.arc(16, 16, 14, 0, Math.PI * 2);
        ctx.fill();
        
        // Emoji
        ctx.fillStyle = 'white';
        ctx.font = '20px sans-serif';
        ctx.textAlign = 'center';
        ctx.textBaseline = 'middle';
        ctx.fillText(this.states[state].emoji, 16, 16);
        
        const favicon = document.querySelector('link[rel="icon"]') || 
                       document.createElement('link');
        favicon.rel = 'icon';
        favicon.href = canvas.toDataURL('image/x-icon');
        document.head.appendChild(favicon);
    }
    
    updateStatusMessages(state) {
        // Actualizar mensajes en la UI
        const statusElements = document.querySelectorAll('.eco-status');
        statusElements.forEach(element => {
            element.textContent = this.states[state].message;
            element.style.color = this.states[state].color;
            element.style.fontWeight = 'bold';
        });
    }
    
    showStateNotification(state) {
        // Mostrar notificación del cambio de estado
        const notification = document.createElement('div');
        notification.className = 'notification';
        notification.innerHTML = `
            <strong>${this.states[state].emoji} ${this.states[state].message}</strong>
            <p style="margin-top: var(--space-xs); font-size: 0.9rem; opacity: 0.8;">
                El ecosistema ha cambiado a estado <strong>${state}</strong>
            </p>
        `;
        notification.style.borderLeftColor = this.states[state].color;
        
        document.body.appendChild(notification);
        
        // Auto-eliminar después de 5 segundos
        setTimeout(() => {
            notification.style.opacity = '0';
            notification.style.transform = 'translateY(20px)';
            setTimeout(() => notification.remove(), 500);
        }, 5000);
    }
}

// ============================================
// Inicializar gestor de estados
// ============================================
document.addEventListener('DOMContentLoaded', () => {
    window.ecoState = new EcoStateManager();
    
    // Ejemplo: Simular cambio de estado según resultado de simulación
    // Esto se integraría con el resultado de la simulación
    const simulationResults = document.querySelector('[data-simulation-score]');
    if (simulationResults) {
        const score = parseInt(simulationResults.dataset.simulationScore);
        window.ecoState.updateState(null, score);
    }
});

// ============================================
// Función global para actualizar estado
// ============================================
function updateEcoState(score) {
    if (window.ecoState) {
        window.ecoState.updateState(null, score);
    }
}