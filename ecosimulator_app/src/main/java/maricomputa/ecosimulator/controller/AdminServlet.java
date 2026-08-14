// src/main/java/com/ecosimulator/servlet/AdminServlet.java
package maricomputa.ecosimulator.controller;

import maricomputa.ecosimulator.modelo.dao.*;
import maricomputa.ecosimulator.modelo.entidad.*;
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
import maricomputa.ecosimulator.controler.service.EcosistemaService;
import maricomputa.ecosimulator.mustache.RenderVista;

@WebServlet("/admin/*")
public class AdminServlet extends HttpServlet {

    private UsuarioDao usuarioDAO;
    private EspecieDao especieDAO;
    private HabitatDao habitatDAO;
    private SimulacionDao simulacionDAO;
    private EcosistemaService ecosistemaService;

    @Override
    public void init() throws ServletException {
        usuarioDAO = new UsuarioDao();
        especieDAO = new EspecieDao();
        habitatDAO = new HabitatDao();
        simulacionDAO = new SimulacionDao();
        ecosistemaService = new EcosistemaService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo();

        if (path == null || path.equals("/") || path.equals("/dashboard")) {
            mostrarDashboard(request, response);
        } else if (path.equals("/usuarios")) {
            listarUsuarios(request, response);
        } else if (path.equals("/especies")) {
            listarEspecies(request, response);
        } else if (path.equals("/habitats")) {
            listarHabitats(request, response);
        } else if (path.startsWith("/usuario/editar/")) {
            editarUsuario(request, response);
        } else if (path.startsWith("/especie/editar/")) {
            editarEspecie(request, response);
        } else if (path.startsWith("/habitat/editar/")) {
            editarHabitat(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo();

        if (path == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            if (path.equals("/usuario/actualizar")) {
                actualizarUsuario(request, response);
            } else if (path.equals("/especie/actualizar")) {
                actualizarEspecie(request, response);
            } else if (path.equals("/habitat/actualizar")) {
                actualizarHabitat(request, response);
            } else if (path.equals("/usuario/eliminar")) {
                eliminarUsuario(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
//            request.setAttribute("error", "Error al procesar la operación: " + e.getMessage());
//            response.sendRedirect(request.getContextPath() + "/admin");
            RenderVista.renderizarVista(response, getServletContext().getRealPath("templates/admin.html"), new ErrorM("Error al procesar la operación: " + e.getMessage()));

        }
    }

    private void mostrarDashboard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Estadísticas
            List<Usuario> usuarios = usuarioDAO.findAll();
            List<Especie> especies = especieDAO.findAll();
            List<Habitat> habitats = habitatDAO.findAll();
            List<Simulacion> simulaciones = simulacionDAO.findAll();

//            request.setAttribute("totalUsuarios", usuarios.size());
//            request.setAttribute("totalEspecies", especies.size());
//            request.setAttribute("totalHabitats", habitats.size());
//            request.setAttribute("totalSimulaciones", simulaciones.size());
            Map<String, Object> datos = new HashMap();
            datos.put("totalUsuarios", usuarios.size());
            datos.put("totalEspecies", especies.size());
            datos.put("totalHabitats", habitats.size());
            datos.put("totalSimulaciones", simulaciones.size());
            prepararContexto(datos, request);
            //getServletContext().getRealPath("admin/consoladmin.html")
            RenderVista.renderizarVista(response, getServletContext().getRealPath("templates/admin.html"), datos);

//            request.getRequestDispatcher(getServletContext().getRealPath("template/admin.html"))
//                    .forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Usuario> usuarios = usuarioDAO.findAll();
//            request.setAttribute("usuarios", usuarios);
//            request.getRequestDispatcher("templates/admin-usuarios.html")
//                    .forward(request, response);
            Map<String, Object> datos = new HashMap();
            datos.put("usuarios", usuarios);
            prepararContexto(datos, request);
            RenderVista.renderizarVista(response, getServletContext().getRealPath("templates/admin-usuarios.html"), datos);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void listarEspecies(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Especie> especies = especieDAO.findAll();
//            request.setAttribute("especies", especies);
//            request.getRequestDispatcher("templates/admin-especies.html")
//                    .forward(request, response);
            Map<String, Object> datos = new HashMap();
            datos.put("especies", especies);
            prepararContexto(datos, request);
            RenderVista.renderizarVista(response, getServletContext().getRealPath("templates/admin-especies.html"), datos);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void listarHabitats(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Habitat> habitats = habitatDAO.findAll();
//            request.setAttribute("habitats", habitats);
//            request.getRequestDispatcher("templates/admin-habitats.html")
//                    .forward(request, response);
            Map<String, Object> datos = new HashMap();
            datos.put("habitats", habitats);
            prepararContexto(datos, request);
            RenderVista.renderizarVista(response, getServletContext().getRealPath("templates/admin-habitats.html"), datos);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void editarHabitat(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String[] parts = request.getPathInfo().split("/");
            int id = Integer.parseInt(parts[parts.length - 1]);
            Habitat habitat = habitatDAO.findById(id);
//            request.setAttribute("habitat", habitat);
//            request.getRequestDispatcher("templates/admin-habitat-editar.html")
//                    .forward(request, response);
            //prepararContexto(datos, request);
            RenderVista.renderizarVista(response, getServletContext().getRealPath("templates/admin-habitat-editar.html"), habitat);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void editarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String[] parts = request.getPathInfo().split("/");
            int id = Integer.parseInt(parts[parts.length - 1]);
            Usuario usuario = usuarioDAO.findById(id);
//            request.setAttribute("usuario", usuario);
//            request.getRequestDispatcher("templates/admin-usuario-editar.html")
//                    .forward(request, response);
            Map<String, Object> datos = new HashMap();
            datos.put("usuario", usuario);            
            RenderVista.renderizarVista(response, getServletContext().getRealPath("templates/admin-usuario-editar.html"), datos);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void editarEspecie(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String[] parts = request.getPathInfo().split("/");
            int id = Integer.parseInt(parts[parts.length - 1]);
            Especie especie = especieDAO.findById(id);
//            request.setAttribute("especie", especie);
//            request.getRequestDispatcher("templates/admin-especie-editar.html")
//                    .forward(request, response);
            RenderVista.renderizarVista(response, getServletContext().getRealPath("templates/admin-especie-editar.html"), especie);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void actualizarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        int id = Integer.parseInt(request.getParameter("id"));
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String rol = request.getParameter("rol");
        boolean activo = "on".equals(request.getParameter("activo"));

        Usuario usuario = usuarioDAO.findById(id);
        if (usuario != null) {
            usuario.setUsername(username);
            usuario.setEmail(email);
            usuario.setRol(rol);
            usuario.setActivo(activo);
            usuarioDAO.update(usuario);
        }

        response.sendRedirect(request.getContextPath() + "/admin-usuarios.hmtl");
    }

    private void actualizarEspecie(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        int id = Integer.parseInt(request.getParameter("id"));
        String nombreCientifico = request.getParameter("nombreCientifico");
        String nombreComun = request.getParameter("nombreComun");
        String estadoConservacion = request.getParameter("estadoConservacion");
        String dieta = request.getParameter("dieta");

        Especie especie = especieDAO.findById(id);
        if (especie != null) {
            especie.setNombreCientifico(nombreCientifico);
            especie.setNombreComun(nombreComun);
            especie.setEstadoConservacion(estadoConservacion);
            especie.setDieta(dieta);
            especieDAO.update(especie);
        }

        response.sendRedirect(request.getContextPath() + "/admin-especies.html");
    }

    private void actualizarHabitat(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // Similar a actualizar especie
        response.sendRedirect(request.getContextPath() + "/admin-habitats.html");
    }

    private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        int id = Integer.parseInt(request.getParameter("id"));
        usuarioDAO.delete(id);
        response.sendRedirect(request.getContextPath() + "/admin-usuarios.html");
    }

    // En cada Servlet, agregar estos métodos helper para Mustache
    private void prepararContexto(Map<String, Object> context, HttpServletRequest request) {
        HttpSession session = request.getSession();
        context.put("username", session.getAttribute("username"));
        context.put("rol", session.getAttribute("rol"));
        context.put("isAdmin", "admin".equals(session.getAttribute("rol")));
        context.put("isEcoUsuario", "ecousuario".equals(session.getAttribute("rol")));
    }

    // Método para mostrar estadísticas del ecosistema
    private void mostrarEstadisticasEcosistema(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Map<String, Object> stats = ecosistemaService.getEstadisticasEcosistema();
//            request.setAttribute("estadisticas", stats);
//            request.getRequestDispatcher("templates/admin-estadisticas.html")
//                    .forward(request, response);
            Map<String, Object> datos = new HashMap();
            datos.put("estadisticas", stats);
            RenderVista.renderizarVista(response, getServletContext().getRealPath("templates/admin-especies.html"), datos);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
