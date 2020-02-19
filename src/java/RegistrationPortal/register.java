package RegistrationPortal;

import com.sun.org.apache.bcel.internal.generic.F2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;

/**
 *
 * @author Adarsh
 */
@MultipartConfig            //annotation which denotes that this servlet contains audio-video or any image file
public class register extends HttpServlet {

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
           
            String userName = request.getParameter("user_Name");
            String email = request.getParameter("user_email");
            String password = request.getParameter("user_password");
            Part imgpart = request.getPart("image");
            String filename = imgpart.getSubmittedFileName();
           // out.println(filename);
                       
            
            
            //connection with database using jdbc...............
            try {   
                Thread.sleep(3000);
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                Connection con = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=RegistrationPortal;user=sa;password=password@123");
            
            //query........
            
            String q = "insert into UserDetails(Name,Email,Password,ImageName) values(?,?,?,?)";
         
            con.prepareStatement(q);
            PreparedStatement pstmt = con.prepareStatement(q);
            pstmt.setString(1,userName);
            pstmt.setString(2,email);
            pstmt.setString(3,password);
            pstmt.setString(4,filename);
            
            pstmt.executeUpdate();
            
            //Upload...
            InputStream is = imgpart.getInputStream();
            byte []data = new byte[is.available()];
            
            is.read(data);
            String path = request.getRealPath("/")+"image"+File.separator+filename;
            //out.println(path);
            
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(data);
            fos.close();
            //Upload End
            
            out.println("Done");
            } 
            catch (Exception ex) {
                ex.printStackTrace();
                out.println("Error");                       
            }
            
           
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
