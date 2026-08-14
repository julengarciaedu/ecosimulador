/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package maricomputa.ecosimulator.controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.sql.Timestamp;
import maricomputa.ecosimulator.modelo.dao.UsuarioDao;
import maricomputa.ecosimulator.modelo.entidad.ErrorM;
import maricomputa.ecosimulator.modelo.entidad.Usuario;
import maricomputa.ecosimulator.mustache.RenderVista;
import maricomputa.ecosimulator.utils.PasswordUtil;

/**
 *
 * @author Julen Profe
 */
@WebServlet("/pruebaservlet")
public class PruebaServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            // Buscar usuario por username
            UsuarioDao usuarioDAO = new UsuarioDao();
            Usuario usuario = null;
            usuario = usuarioDAO.findByUsername("admin");
            usuario.setPasswordHash(PasswordUtil.hashPassword("4dm1n2o2s31s"));
            usuarioDAO.update(usuario);
            usuario = usuarioDAO.findByUsername("ecologo1");
            usuario.setPasswordHash(PasswordUtil.hashPassword("eco123"));
            usuarioDAO.update(usuario);
            usuario = usuarioDAO.findByUsername("ecologo2");
            usuario.setPasswordHash(PasswordUtil.hashPassword("d0X"));
            usuarioDAO.update(usuario);
            //-----
            usuario = usuarioDAO.findByUsername("admin");
            System.out.println("Usuario: "+usuario.getUsername()+" | Password: "+ usuario.verifyPassword("4dm1n2o2s31s"));
            usuario = usuarioDAO.findByUsername("ecologo1");
            System.out.println("Usuario: "+usuario.getUsername()+" | Password: "+ usuario.verifyPassword("eco123"));
            usuario = usuarioDAO.findByUsername("ecologo2");
            System.out.println("Usuario: "+usuario.getUsername()+" | Password: "+ usuario.verifyPassword("d0X"));
            System.out.println("Usuario: "+usuario.getUsername()+" | Password: "+ usuario.verifyPassword("1234"));
              
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            //request.setAttribute("error", "Error en el sistema: " + e.getMessage());
            //request.getRequestDispatcher("templates/login.html")
                   //.forward(request, response);
            RenderVista.renderizarVista(response, getServletContext().getRealPath("templates/login.html"), new ErrorM("Error en el sistema: " + e.getMessage()));      
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
