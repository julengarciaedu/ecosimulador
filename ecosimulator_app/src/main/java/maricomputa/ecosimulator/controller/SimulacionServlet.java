// src/main/java/maricomputa/ecosimulator/controller/servlet/SimulacionServlet.java
package maricomputa.ecosimulator.controller;

import maricomputa.ecosimulator.modelo.dao.HabitatDao;
import maricomputa.ecosimulator.modelo.dao.SimulacionDao;
import maricomputa.ecosimulator.modelo.dao.EspecieDao;
import maricomputa.ecosimulator.modelo.entidad.Habitat;
import maricomputa.ecosimulator.modelo.entidad.Simulacion;
import maricomputa.ecosimulator.modelo.entidad.Especie;
import maricomputa.ecosimulator.mustache.RenderVista;
import maricomputa.ecosimulator.utils.SimulacionEngine;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@WebServlet("/simulacion/*")
public class SimulacionServlet extends HttpServlet {

    private SimulacionDao simulacionDAO;
    private HabitatDao habitatDAO;
    private EspecieDao especieDAO;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        simulacionDAO = new SimulacionDao();
        habitatDAO = new HabitatDao();
        especieDAO = new EspecieDao();
        System.out.println("🔄 SimulacionServlet INICIALIZADO");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String path = request.getPathInfo();
        Map<String, Object> datos = new HashMap<>();
        
        System.out.println("🔄 SimulacionServlet.doGet() - Path: " + path);
        
        try {
            // Redirigir según la URL
            if (path == null || path.equals("/")) {
                // Listar simulaciones (redirigir a dashboard)
                response.sendRedirect(request.getContextPath() + "/dashboard");
                return;
                
            } else if (path.equals("/nueva")) {
                // Mostrar formulario para nueva simulación
                mostrarFormularioNueva(request, response);
                return;
                
            } else if (path.startsWith("/ver/")) {
                // Ver detalle de una simulación
                verSimulacion(request, response);
                return;
                
            } else if (path.startsWith("/resultado/")) {
                // Ver resultados de una simulación
                verResultado(request, response);
                return;
                
            } else if (path.equals("/publicas")) {
                // Listar simulaciones públicas
                listarPublicas(request, response);
                return;
            }
            
            // Si no coincide con ninguna ruta, error 404
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            
        } catch (Exception e) {
            e.printStackTrace();
            RenderVista.renderizarVista(response, 
                getServletContext().getRealPath("templates/error.html"), 
                Map.of("error", "Error al cargar la simulación: " + e.getMessage()));
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String path = request.getPathInfo();
        
        System.out.println("🔄 SimulacionServlet.doPost() - Path: " + path);
        
        try {
            if (path == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            
            if (path.equals("/crear")) {
                crearSimulacion(request, response);
            } else if (path.equals("/ejecutar")) {
                ejecutarSimulacion(request, response);
            } else if (path.equals("/eliminar")) {
                eliminarSimulacion(request, response);
            } else if (path.equals("/compartir")) {
                compartirSimulacion(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error: " + e.getMessage());
        }
    }
    
    // ============================================
    // MÉTODOS PRIVADOS
    // ============================================
    
    /**
     * Muestra el formulario para crear una nueva simulación
     */
    private void mostrarFormularioNueva(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        Map<String, Object> datos = new HashMap<>();
        
        try {
            // Obtener hábitats y especies disponibles
            List<Habitat> habitats = habitatDAO.findAll();
            List<Especie> especies = especieDAO.findAll();
            
            datos.put("habitats", habitats);
            datos.put("especies", especies);
            
            // Datos del usuario
            prepararContexto(datos, request);
            
            RenderVista.renderizarVista(response, 
                getServletContext().getRealPath("templates/simulacion-nueva.html"), 
                datos);
                
        } catch (Exception e) {
            e.printStackTrace();
            RenderVista.renderizarVista(response, 
                getServletContext().getRealPath("templates/error.html"), 
                Map.of("error", "Error al cargar el formulario: " + e.getMessage()));
        }
    }
    
    /**
     * Crea una nueva simulación
     */
    private void crearSimulacion(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        Integer usuarioId = (Integer) request.getSession().getAttribute("usuarioId");
        
        // Recoger datos del formulario
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        String habitatIdParam = request.getParameter("habitatId");
        String duracionParam = request.getParameter("duracion");
        String temperaturaParam = request.getParameter("temperatura");
        String precipitacionParam = request.getParameter("precipitacion");
        
        // Validar datos obligatorios
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        
        if (habitatIdParam == null || habitatIdParam.isEmpty()) {
            throw new IllegalArgumentException("Debes seleccionar un hábitat");
        }
        
        Integer habitatId = Integer.parseInt(habitatIdParam);
        int duracion = duracionParam != null ? Integer.parseInt(duracionParam) : 50;
        double temperatura = temperaturaParam != null ? Double.parseDouble(temperaturaParam) : 15.0;
        double precipitacion = precipitacionParam != null ? Double.parseDouble(precipitacionParam) : 1000.0;
        
        // Crear objeto Simulacion
        Simulacion simulacion = new Simulacion();
        simulacion.setUuid(UUID.randomUUID().toString());
        simulacion.setNombre(nombre);
        simulacion.setDescripcion(descripcion);
        simulacion.setUsuarioId(usuarioId);
        simulacion.setHabitatId(habitatId);
        simulacion.setDuracionSimulada(duracion);
        simulacion.setEstado("pendiente");
        simulacion.setProgreso(0);
        simulacion.setModeloIa("deepseek-chat");
        simulacion.setRiesgoEstimado("medio");
        simulacion.setEsPublica(false);
        simulacion.setNotasUsuario("");
        
        // Guardar parámetros como JSON
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("duracionSimulada", duracion);
        parametros.put("temperaturaInicial", temperatura);
        parametros.put("precipitacionInicial", precipitacion);
        
        // Recoger especies seleccionadas
        String[] especiesSeleccionadas = request.getParameterValues("especies");
        if (especiesSeleccionadas != null && especiesSeleccionadas.length > 0) {
            parametros.put("especiesIds", especiesSeleccionadas);
        }
        
        // Convertir parámetros a JSON real (la columna es de tipo JSON en MySQL,
        // Map.toString() no es JSON válido y la inserción fallaba)
        simulacion.setParametrosConfiguracion(objectMapper.writeValueAsString(parametros));
        
        // Guardar en base de datos
        simulacionDAO.insert(simulacion);
        
        System.out.println("✅ Simulación creada: " + nombre + " (ID: " + simulacion.getId() + ")");
        
        // Redirigir a la página de la simulación
        response.sendRedirect(request.getContextPath() + "/simulacion/ver/" + simulacion.getId());
    }
    
    /**
     * Muestra el detalle de una simulación
     */
    private void verSimulacion(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String[] parts = request.getPathInfo().split("/");
        int id = Integer.parseInt(parts[parts.length - 1]);
        
        Map<String, Object> datos = new HashMap<>();
        
        try {
            Simulacion simulacion = simulacionDAO.findById(id);
            if (simulacion == null) {
                throw new IllegalArgumentException("Simulación no encontrada");
            }
            
            // Verificar permisos
            Integer usuarioId = (Integer) request.getSession().getAttribute("usuarioId");
            String rol = (String) request.getSession().getAttribute("rol");
            
            if (!simulacion.isEsPublica() && simulacion.getUsuarioId() != usuarioId && !"admin".equals(rol)) {
                throw new SecurityException("No tienes permiso para ver esta simulación");
            }
            
            datos.put("simulacion", simulacion);
            
            // Obtener hábitat si existe
            if (simulacion.getHabitatId() != null) {
                Habitat habitat = habitatDAO.findById(simulacion.getHabitatId());
                datos.put("habitat", habitat);
            }
            
            // Datos del usuario
            prepararContexto(datos, request);
            
            // Determinar estado del ecosistema (para el CSS dinámico)
            if (simulacion.getPuntuacionSostenibilidad() != null) {
                int score = simulacion.getPuntuacionSostenibilidad();
                String estado;
                if (score >= 80) estado = "healthy";
                else if (score >= 60) estado = "balanced";
                else if (score >= 40) estado = "vulnerable";
                else if (score >= 20) estado = "danger";
                else estado = "critical";
                datos.put("estadoEcosistema", estado);
            }
            
            RenderVista.renderizarVista(response, 
                getServletContext().getRealPath("templates/simulacion-ver.html"), 
                datos);
                
        } catch (Exception e) {
            e.printStackTrace();
            RenderVista.renderizarVista(response, 
                getServletContext().getRealPath("templates/error.html"), 
                Map.of("error", "Error al cargar la simulación: " + e.getMessage()));
        }
    }
    
    /**
     * Muestra los resultados de una simulación
     */
    private void verResultado(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String[] parts = request.getPathInfo().split("/");
        int id = Integer.parseInt(parts[parts.length - 1]);
        
        Map<String, Object> datos = new HashMap<>();
        
        try {
            Simulacion simulacion = simulacionDAO.findById(id);
            if (simulacion == null || !"completada".equals(simulacion.getEstado())) {
                throw new IllegalArgumentException("Resultados no disponibles");
            }
            
            datos.put("simulacion", simulacion);
            prepararContexto(datos, request);
            
            RenderVista.renderizarVista(response, 
                getServletContext().getRealPath("templates/simulacion-resultado.html"), 
                datos);
                
        } catch (Exception e) {
            e.printStackTrace();
            RenderVista.renderizarVista(response, 
                getServletContext().getRealPath("templates/error.html"), 
                Map.of("error", "Error al cargar resultados: " + e.getMessage()));
        }
    }
    
    /**
     * Lista simulaciones públicas
     */
    private void listarPublicas(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        Map<String, Object> datos = new HashMap<>();
        
        try {
            List<Simulacion> simulaciones = simulacionDAO.findPublicas();
            datos.put("simulaciones", simulaciones);
            prepararContexto(datos, request);
            
            RenderVista.renderizarVista(response, 
                getServletContext().getRealPath("templates/simulacion-publicas.html"), 
                datos);
                
        } catch (Exception e) {
            e.printStackTrace();
            RenderVista.renderizarVista(response, 
                getServletContext().getRealPath("templates/error.html"), 
                Map.of("error", "Error al cargar simulaciones públicas: " + e.getMessage()));
        }
    }
    
    /**
     * Ejecuta una simulación (en hilo separado), usando el motor real
     * de simulación (SimulacionEngine) sobre el hábitat y las especies
     * elegidas al crearla.
     */
    private void ejecutarSimulacion(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        int id = Integer.parseInt(request.getParameter("id"));

        System.out.println("🚀 Ejecutando simulación ID: " + id);

        // Actualizar estado a "ejecutando"
        simulacionDAO.updateEstado(id, "ejecutando");
        simulacionDAO.updateProgreso(id, 10);

        // Ejecutar en hilo separado (simulación)
        new Thread(() -> {
            try {
                Simulacion simulacion = simulacionDAO.findById(id);
                if (simulacion == null) {
                    throw new IllegalStateException("Simulación no encontrada");
                }

                Thread.sleep(300);
                simulacionDAO.updateProgreso(id, 30);

                Habitat habitat = simulacion.getHabitatId() != null
                        ? habitatDAO.findById(simulacion.getHabitatId()) : null;

                Map<String, Object> parametros = objectMapper.readValue(
                        simulacion.getParametrosConfiguracion(), Map.class);
                List<Especie> especies = obtenerEspeciesParaSimulacion(parametros);

                Thread.sleep(300);
                simulacionDAO.updateProgreso(id, 60);

                SimulacionEngine engine = new SimulacionEngine();
                Map<String, Object> resultados = engine.ejecutar(especies, habitat, parametros);
                Map<String, Object> metricas = engine.calcularMetricas(resultados);
                int sostenibilidad = engine.calcularSostenibilidad(resultados);
                int biodiversidad = engine.calcularBiodiversidad(resultados);
                String riesgo = engine.estimarRiesgo(resultados);

                simulacionDAO.updateProgreso(id, 90);

                simulacionDAO.completarSimulacion(
                    id,
                    objectMapper.writeValueAsString(resultados),
                    objectMapper.writeValueAsString(metricas),
                    sostenibilidad,
                    biodiversidad,
                    riesgo
                );

                System.out.println("✅ Simulación ID " + id + " completada");

            } catch (Exception e) {
                e.printStackTrace();
                try {
                    simulacionDAO.updateEstado(id, "fallida");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("Simulación iniciada");
    }

    /**
     * Resuelve las especies sobre las que correr el motor: las elegidas al
     * crear la simulación (parámetro "especiesIds") si las hay, o todas
     * las especies disponibles en caso contrario.
     */
    private List<Especie> obtenerEspeciesParaSimulacion(Map<String, Object> parametros) throws Exception {
        Object especiesIds = parametros.get("especiesIds");
        if (especiesIds instanceof List) {
            List<Especie> seleccionadas = new ArrayList<>();
            for (Object idObj : (List<?>) especiesIds) {
                Especie especie = especieDAO.findById(Integer.parseInt(String.valueOf(idObj)));
                if (especie != null) {
                    seleccionadas.add(especie);
                }
            }
            if (!seleccionadas.isEmpty()) {
                return seleccionadas;
            }
        }
        return especieDAO.findAll();
    }
    
    /**
     * Elimina una simulación
     */
    private void eliminarSimulacion(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        int id = Integer.parseInt(request.getParameter("id"));
        Integer usuarioId = (Integer) request.getSession().getAttribute("usuarioId");
        String rol = (String) request.getSession().getAttribute("rol");
        
        Simulacion simulacion = simulacionDAO.findById(id);
        if (simulacion == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        // Verificar permisos (admin o propietario)
        if (!"admin".equals(rol) && simulacion.getUsuarioId() != usuarioId) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        simulacionDAO.delete(id);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("Simulación eliminada");
    }
    
    /**
     * Comparte una simulación (hacerla pública)
     */
    private void compartirSimulacion(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        int id = Integer.parseInt(request.getParameter("id"));
        boolean esPublica = "true".equals(request.getParameter("esPublica"));
        
        Simulacion simulacion = simulacionDAO.findById(id);
        if (simulacion == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        simulacion.setEsPublica(esPublica);
        simulacionDAO.update(simulacion);
        
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("Estado de compartir actualizado");
    }
    
    /**
     * Prepara el contexto para Mustache
     */
    private void prepararContexto(Map<String, Object> context, HttpServletRequest request) {
        HttpSession session = request.getSession();
        context.put("username", session.getAttribute("username"));
        context.put("rol", session.getAttribute("rol"));
        context.put("isAdmin", "admin".equals(session.getAttribute("rol")));
        context.put("isEcoUsuario", "ecousuario".equals(session.getAttribute("rol")));
    }
}