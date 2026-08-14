// src/main/java/com/ecosimulator/servlet/LoginServlet.java
package maricomputa.ecosimulator.controller;

import maricomputa.ecosimulator.modelo.dao.UsuarioDao;
import maricomputa.ecosimulator.modelo.entidad.Usuario;
import maricomputa.ecosimulator.utils.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import maricomputa.ecosimulator.modelo.entidad.ErrorM;
import maricomputa.ecosimulator.mustache.RenderVista;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    private UsuarioDao usuarioDAO;
    
    @Override
    public void init() throws ServletException {
        usuarioDAO = new UsuarioDao();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Mostrar página de login
        RenderVista.renderizarVista(response, getServletContext().getRealPath("templates/login.html"), "");
        //request.getRequestDispatcher("templates/login.html").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Map<String, Object> datos = new HashMap<>();
        
        try {
            // Buscar usuario por username
            Usuario usuario = usuarioDAO.findByUsername(username);
            
            if (usuario != null && usuario.isActivo()) {
                // Verificar contraseña
                if (usuario.verifyPassword(password)) {
                    // Login exitoso
                    HttpSession session = request.getSession();
                    session.setAttribute("usuarioId", usuario.getId());
                    session.setAttribute("username", usuario.getUsername());
                    session.setAttribute("email", usuario.getEmail());
                    session.setAttribute("rol", usuario.getRol());
                    session.setAttribute("avatar", usuario.getAvatar());
                    
                    // Actualizar último login
                    usuario.setUltimoLogin(new Timestamp(System.currentTimeMillis()));
                    usuarioDAO.update(usuario);
                    
                    // Redirigir según rol
                    String redirectUrl = usuario.isAdmin() ? "/admin/dashboard" : "/dashboard";
                    response.sendRedirect(request.getContextPath() + redirectUrl);
                    //RenderVista.renderizarVista(response, getServletContext().getRealPath(redirectUrl), "");
                    return;
                }
            }
            
            // Login fallido
            //request.setAttribute("error", "Usuario o contraseña incorrectos");
            ErrorM error = new ErrorM("Usuario o contraseña incorrectos");
            RenderVista.renderizarVista(response, getServletContext().getRealPath("templates/login.html"), error);
            //RenderVista.renderizarVista(response, getServletContext().getRealPath("templates/login.html"), datos.put("error", "Usuario o contraseña incorrectos"));
            //request.getRequestDispatcher("templates/login.html")
                   //.forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            //request.setAttribute("error", "Error en el sistema: " + e.getMessage());
            //request.getRequestDispatcher("templates/login.html")
                   //.forward(request, response);
            RenderVista.renderizarVista(response, getServletContext().getRealPath("templates/login.html"), new ErrorM("Error en el sistema: " + e.getMessage()));      
        }
    }
    
    // En cada Servlet, agregar estos métodos helper para Mustache
    private void prepararContexto(Map<String, Object> context, HttpServletRequest request) {
        HttpSession session = request.getSession();
        context.put("username", session.getAttribute("username"));
        context.put("rol", session.getAttribute("rol"));
        context.put("isAdmin", "admin".equals(session.getAttribute("rol")));
        context.put("isEcoUsuario", "ecousuario".equals(session.getAttribute("rol")));
    }
    
    
}

    