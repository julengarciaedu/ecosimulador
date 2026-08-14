// src/main/java/com/ecosimulator/servlet/AdminServlet.java
package maricomputa.ecosimulator.controller;

import maricomputa.ecosimulator.modelo.dao.*;
import maricomputa.ecosimulator.modelo.entidad.*;
import maricomputa.ecosimulator.utils.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
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
        } else if (path.equals("/usuarios/activos")) {
            listarUsuariosFiltrado(request, response, true);
        } else if (path.equals("/usuarios/inactivos")) {
            listarUsuariosFiltrado(request, response, false);
        } else if (path.equals("/especies")) {
            listarEspecies(request, response);
        } else if (path.equals("/especies/protegidas")) {
            listarEspeciesFiltrado(request, response, "protegidas");
        } else if (path.equals("/especies/amenazadas")) {
            listarEspeciesFiltrado(request, response, "amenazadas");
        } else if (path.equals("/especies/invasoras")) {
            listarEspeciesFiltrado(request, response, "invasoras");
        } else if (path.equals("/habitats")) {
            listarHabitats(request, response);
        } else if (path.equals("/habitats/protegidos")) {
            listarHabitatsFiltrado(request, response, "protegidos");
        } else if (path.equals("/habitats/biodiversidad")) {
            listarHabitatsFiltrado(request, response, "biodiversidad");
        } else if (path.equals("/simulaciones")) {
            listarSimulaciones(request, response, null, "Todas las Simulaciones");
        } else if (path.equals("/simulaciones/activas")) {
            listarSimulaciones(request, response, "ejecutando", "Simulaciones en Ejecución");
        } else if (path.equals("/simulaciones/completadas")) {
            listarSimulaciones(request, response, "completada", "Simulaciones Completadas");
        } else if (path.equals("/simulaciones/fallidas")) {
            listarSimulaciones(request, response, "fallida", "Simulaciones Fallidas");
        } else if (path.equals("/usuario/nuevo")) {
            mostrarFormularioNuevoUsuario(request, response);
        } else if (path.equals("/especie/nueva")) {
            mostrarFormularioNuevaEspecie(request, response);
        } else if (path.equals("/habitat/nuevo")) {
            mostrarFormularioNuevoHabitat(request, response);
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
            if (path.equals("/usuario/crear")) {
                crearUsuario(request, response);
            } else if (path.equals("/especie/crear")) {
                crearEspecie(request, response);
            } else if (path.equals("/habitat/crear")) {
                crearHabitat(request, response);
            } else if (path.equals("/usuario/actualizar")) {
                actualizarUsuario(request, response);
            } else if (path.equals("/especie/actualizar")) {
                actualizarEspecie(request, response);
            } else if (path.equals("/habitat/actualizar")) {
                actualizarHabitat(request, response);
            } else if (path.equals("/usuario/eliminar")) {
                eliminarUsuario(request, response);
            } else if (path.equals("/especie/eliminar")) {
                eliminarEspecie(request, response);
            } else if (path.equals("/habitat/eliminar")) {
                eliminarHabitat(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

            Map<String, Object> datos = new HashMap();
            datos.put("totalUsuarios", usuarios.size());
            datos.put("totalEspecies", especies.size());
            datos.put("totalHabitats", habitats.size());
            datos.put("totalSimulaciones", simulaciones.size());

            long usuariosActivos = usuarios.stream().filter(Usuario::isActivo).count();
            datos.put("usuariosActivos", usuariosActivos);
            datos.put("usuariosInactivos", usuarios.size() - usuariosActivos);

            long especiesProtegidas = especies.stream().filter(Especie::isEsProtegida).count();
            long especiesInvasoras = especies.stream().filter(Especie::isEsInvasora).count();
            long especiesAmenazadas = especies.stream()
                    .filter(e -> e.getEstadoConservacion() != null
                            && (e.getEstadoConservacion().equals("CR")
                                || e.getEstadoConservacion().equals("EN")
                                || e.getEstadoConservacion().equals("VU")))
                    .count();
            datos.put("especiesProtegidas", especiesProtegidas);
            datos.put("especiesInvasoras", especiesInvasoras);
            datos.put("especiesAmenazadas", especiesAmenazadas);

            long habitantsProtegidos = habitats.stream()
                    .filter(h -> h.getEstatusProteccion() != null && !h.getEstatusProteccion().equals("sin_proteccion"))
                    .count();
            long habitantsAltaBiodiversidad = habitats.stream()
                    .filter(h -> h.getBiodiversidadIndex() != null && h.getBiodiversidadIndex().doubleValue() > 0.7)
                    .count();
            datos.put("habitantsProtegidos", habitantsProtegidos);
            datos.put("habitantsAltaBiodiversidad", habitantsAltaBiodiversidad);

            long simulacionesActivas = simulaciones.stream().filter(s -> "ejecutando".equals(s.getEstado())).count();
            long simulacionesCompletadas = simulaciones.stream().filter(s -> "completada".equals(s.getEstado())).count();
            long simulacionesFallidas = simulaciones.stream().filter(s -> "fallida".equals(s.getEstado())).count();
            datos.put("simulacionesActivas", simulacionesActivas);
            datos.put("simulacionesCompletadas", simulacionesCompletadas);
            datos.put("simulacionesFallidas", simulacionesFallidas);

            prepararContexto(datos, request);
            RenderVista.renderizarVista(response, getServletContext().getRealPath("templates/admin.html"), datos);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Usuario> usuarios = usuarioDAO.findAll();
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
            Map<String, Object> datos = new HashMap();
            datos.put("habitats", habitats);
            prepararContexto(datos, request);
            RenderVista.renderizarVista(response, getServletContext().getRealPath("templates/admin-habitats.html"), datos);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void listarUsuariosFiltrado(HttpServletRequest request, HttpServletResponse response, boolean activo)
            throws ServletException, IOException {
        try {
            List<Usuario> usuarios = usuarioDAO.findAll().stream()
                    .filter(u -> u.isActivo() == activo)
                    .collect(java.util.stream.Collectors.toList());
            Map<String, Object> datos = new HashMap();
            datos.put("usuarios", usuarios);
            prepararContexto(datos, request);
            RenderVista.renderizarVista(response, getServletContext().getRealPath("templates/admin-usuarios.html"), datos);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void listarEspeciesFiltrado(HttpServletRequest request, HttpServletResponse response, String filtro)
            throws ServletException, IOException {
        try {
            List<Especie> especies = especieDAO.findAll().stream()
                    .filter(e -> {
                        switch (filtro) {
                            case "protegidas": return e.isEsProtegida();
                            case "invasoras": return e.isEsInvasora();
                            case "amenazadas":
                                return e.getEstadoConservacion() != null
                                        && (e.getEstadoConservacion().equals("CR")
                                            || e.getEstadoConservacion().equals("EN")
                                            || e.getEstadoConservacion().equals("VU"));
                            default: return true;
                        }
                    })
                    .collect(java.util.stream.Collectors.toList());
            Map<String, Object> datos = new HashMap();
            datos.put("especies", especies);
            prepararContexto(datos, request);
            RenderVista.renderizarVista(response, getServletContext().getRealPath("templates/admin-especies.html"), datos);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void listarHabitatsFiltrado(HttpServletRequest request, HttpServletResponse response, String filtro)
            throws ServletException, IOException {
        try {
            List<Habitat> habitats = habitatDAO.findAll().stream()
                    .filter(h -> {
                        switch (filtro) {
                            case "protegidos":
                                return h.getEstatusProteccion() != null && !h.getEstatusProteccion().equals("sin_proteccion");
                            case "biodiversidad":
                                return h.getBiodiversidadIndex() != null && h.getBiodiversidadIndex().doubleValue() > 0.7;
                            default: return true;
                        }
                    })
                    .collect(java.util.stream.Collectors.toList());
            Map<String, Object> datos = new HashMap();
            datos.put("habitats", habitats);
            prepararContexto(datos, request);
            RenderVista.renderizarVista(response, getServletContext().getRealPath("templates/admin-habitats.html"), datos);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void listarSimulaciones(HttpServletRequest request, HttpServletResponse response, String estado, String titulo)
            throws ServletException, IOException {
        try {
            List<Simulacion> simulaciones = estado == null
                    ? simulacionDAO.findAll()
                    : simulacionDAO.findByEstado(estado);
            Map<String, Object> datos = new HashMap();
            datos.put("simulaciones", simulaciones);
            datos.put("titulo", titulo);
            prepararContexto(datos, request);
            RenderVista.renderizarVista(response, getServletContext().getRealPath("templates/admin-simulaciones.html"), datos);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void mostrarFormularioNuevoUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Map<String, Object> datos = new HashMap();
            prepararContexto(datos, request);
            RenderVista.renderizarVista(response, getServletContext().getRealPath("templates/admin-usuario-nuevo.html"), datos);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void mostrarFormularioNuevaEspecie(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Map<String, Object> datos = new HashMap();
            prepararContexto(datos, request);
            RenderVista.renderizarVista(response, getServletContext().getRealPath("templates/admin-especie-nueva.html"), datos);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void mostrarFormularioNuevoHabitat(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Map<String, Object> datos = new HashMap();
            prepararContexto(datos, request);
            RenderVista.renderizarVista(response, getServletContext().getRealPath("templates/admin-habitat-nuevo.html"), datos);
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
            Map<String, Object> datos = new HashMap();
            datos.put("habitat", habitat);
            prepararContexto(datos, request);
            RenderVista.renderizarVista(response, getServletContext().getRealPath("templates/admin-habitat-editar.html"), datos);
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
            Map<String, Object> datos = new HashMap();
            datos.put("usuario", usuario);
            prepararContexto(datos, request);
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
            Map<String, Object> datos = new HashMap();
            datos.put("especie", especie);
            prepararContexto(datos, request);
            RenderVista.renderizarVista(response, getServletContext().getRealPath("templates/admin-especie-editar.html"), datos);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void crearUsuario(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String rol = request.getParameter("rol");
        String avatar = request.getParameter("avatar");
        String bio = request.getParameter("bio");
        boolean activo = "on".equals(request.getParameter("activo"));

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setEmail(email);
        usuario.setPasswordHash(PasswordUtil.hashPassword(password));
        usuario.setRol(rol);
        usuario.setAvatar(avatar != null && !avatar.isEmpty() ? avatar : "avatares_default.png");
        usuario.setBio(bio);
        usuario.setActivo(activo);

        usuarioDAO.insert(usuario);

        response.sendRedirect(request.getContextPath() + "/admin/usuarios");
    }

    private void crearEspecie(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Especie especie = new Especie();
        volcarCamposEspecie(request, especie);
        Integer usuarioId = (Integer) request.getSession().getAttribute("usuarioId");
        especie.setCreadoPor(usuarioId);

        especieDAO.insert(especie);

        response.sendRedirect(request.getContextPath() + "/admin/especies");
    }

    private void crearHabitat(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Habitat habitat = new Habitat();
        volcarCamposHabitat(request, habitat);
        Integer usuarioId = (Integer) request.getSession().getAttribute("usuarioId");
        habitat.setCreadoPor(usuarioId);

        habitatDAO.insert(habitat);

        response.sendRedirect(request.getContextPath() + "/admin/habitats");
    }

    private void actualizarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        int id = Integer.parseInt(request.getParameter("id"));
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String rol = request.getParameter("rol");
        String avatar = request.getParameter("avatar");
        String bio = request.getParameter("bio");
        boolean activo = "on".equals(request.getParameter("activo"));

        Usuario usuario = usuarioDAO.findById(id);
        if (usuario != null) {
            usuario.setUsername(username);
            usuario.setEmail(email);
            usuario.setRol(rol);
            usuario.setAvatar(avatar);
            usuario.setBio(bio);
            usuario.setActivo(activo);
            usuarioDAO.update(usuario);
        }

        response.sendRedirect(request.getContextPath() + "/admin/usuarios");
    }

    private void actualizarEspecie(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        int id = Integer.parseInt(request.getParameter("id"));

        Especie especie = especieDAO.findById(id);
        if (especie != null) {
            volcarCamposEspecie(request, especie);
            especieDAO.update(especie);
        }

        response.sendRedirect(request.getContextPath() + "/admin/especies");
    }

    private void actualizarHabitat(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        int id = Integer.parseInt(request.getParameter("id"));

        Habitat habitat = habitatDAO.findById(id);
        if (habitat != null) {
            volcarCamposHabitat(request, habitat);
            habitatDAO.update(habitat);
        }

        response.sendRedirect(request.getContextPath() + "/admin/habitats");
    }

    private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        int id = Integer.parseInt(request.getParameter("id"));
        usuarioDAO.delete(id);
        response.sendRedirect(request.getContextPath() + "/admin/usuarios");
    }

    private void eliminarEspecie(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        int id = Integer.parseInt(request.getParameter("id"));
        especieDAO.delete(id);
        response.sendRedirect(request.getContextPath() + "/admin/especies");
    }

    private void eliminarHabitat(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        int id = Integer.parseInt(request.getParameter("id"));
        habitatDAO.delete(id);
        response.sendRedirect(request.getContextPath() + "/admin/habitats");
    }

    /**
     * Vuelca en el objeto Especie los campos que llegan del formulario
     * (usado tanto para crear como para actualizar).
     */
    private void volcarCamposEspecie(HttpServletRequest request, Especie especie) {
        especie.setNombreCientifico(request.getParameter("nombreCientifico"));
        especie.setNombreComun(request.getParameter("nombreComun"));
        especie.setReino(request.getParameter("reino"));
        especie.setFamilia(request.getParameter("familia"));
        especie.setHabitatPreferido(request.getParameter("habitatPreferido"));
        especie.setEstadoConservacion(request.getParameter("estadoConservacion"));
        especie.setDieta(request.getParameter("dieta"));
        especie.setDescripcion(request.getParameter("descripcion"));
        especie.setCaracteristicas(request.getParameter("caracteristicas"));
        especie.setLongevidadPromedio(parseIntOrNull(request.getParameter("longevidadPromedio")));
        especie.setTamanoPromedio(parseBigDecimalOrNull(request.getParameter("tamanoPromedio")));
        especie.setImpactoEcologico(parseBigDecimalOrNull(request.getParameter("impactoEcologico")));
        especie.setTasaReproduccion(parseBigDecimalOrNull(request.getParameter("tasaReproduccion")));
        especie.setTemperaturaOptima(parseBigDecimalOrNull(request.getParameter("temperaturaOptima")));
        especie.setHumedadOptima(parseBigDecimalOrNull(request.getParameter("humedadOptima")));
        especie.setEsEndemica("on".equals(request.getParameter("esEndemica")));
        especie.setEsInvasora("on".equals(request.getParameter("esInvasora")));
        especie.setEsProtegida("on".equals(request.getParameter("esProtegida")));
        especie.setImagenUrl(request.getParameter("imagenUrl"));
    }

    /**
     * Vuelca en el objeto Habitat los campos que llegan del formulario
     * (usado tanto para crear como para actualizar).
     */
    private void volcarCamposHabitat(HttpServletRequest request, Habitat habitat) {
        habitat.setNombre(request.getParameter("nombre"));
        habitat.setTipoBioma(request.getParameter("tipoBioma"));
        habitat.setPais(request.getParameter("pais"));
        habitat.setRegion(request.getParameter("region"));
        habitat.setLatitud(parseBigDecimalOrNull(request.getParameter("latitud")));
        habitat.setLongitud(parseBigDecimalOrNull(request.getParameter("longitud")));
        habitat.setAltitudMin(parseIntOrNull(request.getParameter("altitudMin")));
        habitat.setAltitudMax(parseIntOrNull(request.getParameter("altitudMax")));
        habitat.setAreaTotal(parseBigDecimalOrNull(request.getParameter("areaTotal")));
        habitat.setTemperaturaPromedio(parseBigDecimalOrNull(request.getParameter("temperaturaPromedio")));
        habitat.setPrecipitacionAnualPromedio(parseIntOrNull(request.getParameter("precipitacionAnual")));
        habitat.setBiodiversidadIndex(parseBigDecimalOrNull(request.getParameter("biodiversidadIndex")));
        habitat.setEstatusProteccion(request.getParameter("estatusProteccion"));
        habitat.setCalidadAgua(request.getParameter("calidadAgua"));
        habitat.setDescripcion(request.getParameter("descripcion"));
        habitat.setAmenazas(request.getParameter("amenazas"));
        habitat.setMedidasConservacion(request.getParameter("medidasConservacion"));
        habitat.setImagenPortada(request.getParameter("imagenPortada"));
    }

    private Integer parseIntOrNull(String valor) {
        if (valor == null || valor.trim().isEmpty()) return null;
        try {
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private BigDecimal parseBigDecimalOrNull(String valor) {
        if (valor == null || valor.trim().isEmpty()) return null;
        try {
            return new BigDecimal(valor.trim());
        } catch (NumberFormatException e) {
            return null;
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

    // Método para mostrar estadísticas del ecosistema
    private void mostrarEstadisticasEcosistema(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Map<String, Object> stats = ecosistemaService.getEstadisticasEcosistema();
            Map<String, Object> datos = new HashMap();
            datos.put("estadisticas", stats);
            RenderVista.renderizarVista(response, getServletContext().getRealPath("templates/admin-especies.html"), datos);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
