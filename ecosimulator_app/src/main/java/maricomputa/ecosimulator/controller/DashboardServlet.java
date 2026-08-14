// src/main/java/com/ecosimulator/servlet/DashboardServlet.java
package maricomputa.ecosimulator.controller;

import maricomputa.ecosimulator.modelo.dao.SimulacionDao;
import maricomputa.ecosimulator.modelo.entidad.Simulacion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import maricomputa.ecosimulator.modelo.entidad.ErrorM;
import maricomputa.ecosimulator.mustache.RenderVista;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    
    private SimulacionDao simulacionDAO;
    
    @Override
    public void init() throws ServletException {
        simulacionDAO = new SimulacionDao();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        Integer usuarioId = (Integer) request.getSession().getAttribute("usuarioId");
        Map<String, Object> datos = new HashMap();
        
        try {
            // Obtener simulaciones del usuario
            List<Simulacion> simulaciones = simulacionDAO.findByUsuario(usuarioId);
            datos.put("simulaciones", simulaciones);

            // Estadísticas
            int total = simulaciones.size();
            int completadas = (int) simulaciones.stream()
                .filter(s -> "completada".equals(s.getEstado()))
                .count();
            int ejecutando = (int) simulaciones.stream()
                .filter(s -> "ejecutando".equals(s.getEstado()))
                .count();
            
//            request.setAttribute("totalSimulaciones", total);
//            request.setAttribute("simulacionesCompletadas", completadas);
//            request.setAttribute("simulacionesActivas", ejecutando);
            datos.put("totalSimulaciones", total);
            datos.put("simulacionesCompletadas", completadas);
            datos.put("simulacionesActivas", ejecutando);
            
            //Datos del usuario
            prepararContexto(datos, request);

        } catch (Exception e) {
            e.printStackTrace();
            RenderVista.renderizarVista(response, getServletContext().getRealPath("templates/admin.html"), new ErrorM("Error al cargar el Dashboard: " + e.getMessage()));
            return;
        }

        RenderVista.renderizarVista(response, getServletContext().getRealPath("templates/dashboard.html"), datos);
    }

    // En cada Servlet, agregar estos métodos helper para Mustache
    private void prepararContexto(Map<String, Object> context, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        context.put("username", username);
        context.put("usernameInitial", username != null && !username.isEmpty()
                ? String.valueOf(Character.toUpperCase(username.charAt(0))) : "?");
        context.put("email", session.getAttribute("email"));
        context.put("rol", session.getAttribute("rol"));
        context.put("isAdmin", "admin".equals(session.getAttribute("rol")));
        context.put("isEcoUsuario", "ecousuario".equals(session.getAttribute("rol")));
    }
}