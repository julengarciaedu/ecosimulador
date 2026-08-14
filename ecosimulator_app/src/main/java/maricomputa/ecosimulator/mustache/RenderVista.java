/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package maricomputa.ecosimulator.mustache;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import jakarta.servlet.http.HttpServletResponse;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Julen Profe
 */
public class RenderVista {

    public static void renderizarVista(HttpServletResponse response, String templateNameConContext, Object datos) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        // Buscamos la plantilla en la carpeta de recursos de la web
        String path = templateNameConContext;
        try {
            // Creamos una fábrica de Mustache
            MustacheFactory mf = new DefaultMustacheFactory();
            // Compilamos la plantilla desde el archivo
            Mustache mustache = mf.compile(new FileReader(path), templateNameConContext);
            // Ejecutamos la plantilla con los datos
            mustache.execute(response.getWriter(), datos);
        } catch (Exception e) {
            response.getWriter().println("Error: No se pudo renderizar la plantilla " + templateNameConContext);
            e.printStackTrace(response.getWriter());
        }
    }

}
