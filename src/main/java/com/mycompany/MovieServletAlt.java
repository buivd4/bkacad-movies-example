package com.mycompany;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Optional;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mycompany.exceptions.DatabaseActionException;
import com.mycompany.models.dao.MovieDAO;
import com.mycompany.models.dao.MovieDAOImpl;
import com.mycompany.models.entities.Movie;

public class MovieServletAlt extends HttpServlet{
    private MovieDAO movieDAO;
    static final String TABLE_NAME = "users";
    static final String DB_URL = "jdbc:mysql://localhost:3306/bkacad_pos?allowPublicKeyRetrieval=true&useSSL=false";
    static final String USER = "root";
    static final String PASS = "my-secret-pw";
    @Override
    public void init() {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            movieDAO = new MovieDAOImpl(conn);
            movieDAO.createTable();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khởi tạo MovieServlet", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            listMovies(req, resp);
        } else {
            String[] pathParts = pathInfo.split("/");
            if (pathParts.length == 2) {
                viewMovie(req, resp, Integer.parseInt(pathParts[1]));
            } else if (pathParts.length == 3 && pathParts[1].equals("edit")) {
                showEditForm(req, resp, Integer.parseInt(pathParts[2]));
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.equals("/add")) {
            addMovie(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.matches("/\\d+")) {
            int movieId = Integer.parseInt(pathInfo.substring(1));
            updateMovie(req, resp, movieId);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.matches("/\\d+")) {
            int movieId = Integer.parseInt(pathInfo.substring(1));
            deleteMovie(req, resp, movieId);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void listMovies(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Movie> movies = movieDAO.findAll();
        req.setAttribute("movies", movies);
        req.getRequestDispatcher("/movie/list.jsp").forward(req, resp);
    }

    private void viewMovie(HttpServletRequest req, HttpServletResponse resp, int movieId)
            throws ServletException, IOException {
        Optional<Movie> movie = movieDAO.findMovieById(movieId);
        req.setAttribute("movie", movie.orElse(null));
        req.getRequestDispatcher("/movie/view.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp, int movieId)
            throws ServletException, IOException {
        Optional<Movie> existingMovie = movieDAO.findMovieById(movieId);
        req.setAttribute("movie", existingMovie.orElse(null));
        req.getRequestDispatcher("/movie/edit.jsp").forward(req, resp);
    }

    private void addMovie(HttpServletRequest req, HttpServletResponse resp) throws IOException, DatabaseActionException {
        String title = req.getParameter("title");
        String genre = req.getParameter("genre");
        int yearOfRelease = Integer.parseInt(req.getParameter("yearOfRelease"));

        Movie newMovie = new Movie(0, title, genre, yearOfRelease);
        movieDAO.createMovie(newMovie);

        resp.sendRedirect(req.getContextPath() + "/movies");
    }

    private void updateMovie(HttpServletRequest req, HttpServletResponse resp, int movieId) throws IOException, DatabaseActionException {
        String newTitle = req.getParameter("title");
        String newGenre = req.getParameter("genre");
        int newYearOfRelease = Integer.parseInt(req.getParameter("yearOfRelease"));

        Optional<Movie> existingMovie = movieDAO.findMovieById(movieId);

        if (existingMovie.isPresent()) {
            Movie updatedMovie = new Movie(movieId, newTitle, newGenre, newYearOfRelease);
            movieDAO.updateMoviesTitle(movieId, newTitle);
            // Update other properties as needed

            resp.sendRedirect(req.getContextPath() + "/movies");
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void deleteMovie(HttpServletRequest req, HttpServletResponse resp, int movieId) throws IOException, DatabaseActionException {
        movieDAO.deleteMovie(movieId);
        resp.sendRedirect(req.getContextPath() + "/movies");
    }
}
