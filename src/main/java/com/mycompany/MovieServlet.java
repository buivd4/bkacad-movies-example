package com.mycompany;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mycompany.models.dao.MovieDAO;
import com.mycompany.models.dao.MovieDAOImpl;
import com.mycompany.models.entities.Movie;

public class MovieServlet extends HttpServlet{
    private MovieDAO movieDAO;
    static final String TABLE_NAME = "users";
    static final String DB_URL = "jdbc:mysql://localhost:3306/bkacad_pos?allowPublicKeyRetrieval=true&useSSL=false";
    static final String USER = "root";
    static final String PASS = "my-secret-pw";

    @Override
    public void init() {
        try {
            // Initialize the MovieDAOImpl with a Connection obtained from your DataSource
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            movieDAO = new MovieDAOImpl(conn);
            movieDAO.createTable(); // Ensure the table exists
        } catch (Exception e) {
            throw new RuntimeException("Error initializing MovieServlet", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Movie> movies = movieDAO.findAll();
        req.setAttribute("movies", movies);
    
        // Forward the request to the JSP
        RequestDispatcher dispatcher = req.getRequestDispatcher("/movie/list.jsp");
        try {
            dispatcher.forward(req, resp);
        } catch (ServletException | IOException e) {
            throw new RuntimeException("Error forwarding to movies.jsp", e);
        }
    
    }

}
