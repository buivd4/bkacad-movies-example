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

public class MovieServlet extends HttpServlet{
    private MovieDAO movieDAO;
    static final String TABLE_NAME = "users";
    static final String DB_URL = "jdbc:mysql://localhost:3306/bkacad_pos?allowPublicKeyRetrieval=true&useSSL=false";
    static final String USER = "root";
    static final String PASS = "my-secret-pw";
    @Override
    public void init() {
        try {
            // Khởi tạo MovieDAOImpl với Connection từ cơ sở dữ liệu
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            movieDAO = new MovieDAOImpl(conn);
            movieDAO.createTable(); // Đảm bảo bảng tồn tại
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khởi tạo MovieServlet", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getPathInfo();
        if (action==null){
            action="/list";
        }
        try {
            switch (action) {
                case "/view":
                    viewMovie(req, resp);
                    break;
                case "/add":
                    showAddForm(req, resp);
                    break;
                case "/edit":
                    showEditForm(req, resp);
                    break;
                case "/delete":
                    deleteMovie(req, resp);
                    break;
                case "/list":
                default:
                    listMovies(req, resp);
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi xử lý yêu cầu", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getPathInfo();
        try {
            switch (action) {
                case "/add":
                    addMovie(req, resp);
                    break;
                case "/edit":
                    updateMovie(req, resp);
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi xử lý yêu cầu POST", e);
        }
    }

    private void addMovie(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException, DatabaseActionException {
        String title = req.getParameter("title");
        String genre = req.getParameter("genre");
        int yearOfRelease = Integer.parseInt(req.getParameter("yearOfRelease"));

        Movie newMovie = new Movie(0, title, genre, yearOfRelease);
        movieDAO.createMovie(newMovie);

        resp.sendRedirect("list");
    }

    private void updateMovie(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException, DatabaseActionException {
        int id = Integer.parseInt(req.getParameter("id"));
        String newTitle = req.getParameter("title");

        Optional<Movie> existingMovie = movieDAO.findMovieById(id);

        if (existingMovie.isPresent()) {
            movieDAO.updateMoviesTitle(id, newTitle);
            resp.sendRedirect("list");
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // Xử lý yêu cầu xem tất cả các bộ phim
    private void listMovies(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Movie> movies = movieDAO.findAll();
        req.setAttribute("movies", movies);

        RequestDispatcher dispatcher = req.getRequestDispatcher("/page/list.jsp");
        dispatcher.forward(req, resp);
    }

    // Xử lý yêu cầu xem một bộ phim theo ID
    private void viewMovie(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Optional<Movie> movie = movieDAO.findMovieById(id);

        req.setAttribute("movie", movie.orElse(null));

        RequestDispatcher dispatcher = req.getRequestDispatcher("/page/view.jsp");
        dispatcher.forward(req, resp);
    }

    // Hiển thị form thêm mới bộ phim
    private void showAddForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/page/add.jsp");
        dispatcher.forward(req, resp);
    }

    // Hiển thị form sửa thông tin bộ phim
    private void showEditForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Optional<Movie> existingMovie = movieDAO.findMovieById(id);

        req.setAttribute("movie", existingMovie.orElse(null));

        RequestDispatcher dispatcher = req.getRequestDispatcher("/page/edit.jsp");
        dispatcher.forward(req, resp);
    }

    // Xử lý yêu cầu xoá bộ phim
    private void deleteMovie(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, DatabaseActionException {
        int id = Integer.parseInt(req.getParameter("id"));
        movieDAO.deleteMovie(id);

        resp.sendRedirect("list");
    }
}
