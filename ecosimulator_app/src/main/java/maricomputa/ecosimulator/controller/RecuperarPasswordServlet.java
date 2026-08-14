// src/main/java/com/ecosimulator/servlet/RecuperarPasswordServlet.java
package maricomputa.ecosimulator.controller;

import maricomputa.ecosimulator.controler.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebServlet("/recuperar-password")
public class RecuperarPasswordServlet extends HttpServlet {
    
    private AuthService authService;
    
    @Override
    public void init() throws ServletException {
        authService = new AuthService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String token = request.getParameter("token");
        
        if (token != null && !token.isEmpty()) {
            // Mostrar formulario para nueva contraseña
            request.setAttribute("token", token);
            request.getRequestDispatcher("/WEB-INF/templates/recuperar-password.html")
                   .forward(request, response);
        } else {
            // Mostrar formulario para solicitar recuperación
            request.getRequestDispatcher("/WEB-INF/templates/solicitar-recuperacion.html")
                   .forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        try {
            if ("solicitar".equals(action)) {
                // Solicitar recuperación
                String email = request.getParameter("email");
                String baseUrl = request.getScheme() + "://" + request.getServerName() +
                                ":" + request.getServerPort() + request.getContextPath();
                
                String enlace = authService.generarEnlaceRecuperacion(email, baseUrl);
                
                if (enlace != null) {
                    // Aquí se enviaría el email con el enlace
                    // Por ahora, mostramos el enlace en la respuesta
                    request.setAttribute("mensaje", "Se ha enviado un enlace de recuperación a tu email");
                    request.setAttribute("enlace", enlace);
                } else {
                    request.setAttribute("error", "No se encontró un usuario con ese email");
                }
                
                request.getRequestDispatcher("/WEB-INF/templates/solicitar-recuperacion.html")
                       .forward(request, response);
                
            } else if ("restablecer".equals(action)) {
                // Restablecer contraseña
                String token = request.getParameter("token");
                String nuevaPassword = request.getParameter("nuevaPassword");
                String confirmPassword = request.getParameter("confirmPassword");
                
                if (!nuevaPassword.equals(confirmPassword)) {
                    request.setAttribute("error", "Las contraseñas no coinciden");
                    request.setAttribute("token", token);
                    request.getRequestDispatcher("/WEB-INF/templates/recuperar-password.html")
                           .forward(request, response);
                    return;
                }
                
                boolean success = authService.restablecerPasswordConToken(token, nuevaPassword);
                
                if (success) {
                    request.setAttribute("mensaje", "Contraseña actualizada exitosamente");
                    response.sendRedirect(request.getContextPath() + "/login");
                } else {
                    request.setAttribute("error", "El enlace ha expirado o no es válido");
                    request.setAttribute("token", token);
                    request.getRequestDispatcher("/WEB-INF/templates/recuperar-password.html")
                           .forward(request, response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error en el proceso: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/templates/error.html")
                   .forward(request, response);
        }
    }
}