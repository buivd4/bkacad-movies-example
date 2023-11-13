package com.mycompany.models.dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mycompany.exceptions.DatabaseActionException;
import com.mycompany.models.entities.Movie;

public class MovieDAOImpl implements MovieDAO {

    private final Connection connection;

    public MovieDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createTable() throws DatabaseActionException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS MOVIES (" +
                    "id INTEGER AUTO_INCREMENT PRIMARY KEY," +
                    "title VARCHAR(255)," +
                    "genre VARCHAR(255)," +
                    "yearOfRelease INTEGER)");
        } catch (SQLException e) {
            throw new DatabaseActionException();
        }
    }

    @Override
    public void deleteTable() throws DatabaseActionException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS MOVIES");
        } catch (SQLException e) {
            throw new DatabaseActionException();
        }
    }

    @Override
    public void createMovie(Movie movie) throws DatabaseActionException {
        String sql = "INSERT INTO MOVIES (title, genre, yearOfRelease) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, movie.getTitle());
            preparedStatement.setString(2, movie.getGenre());
            preparedStatement.setInt(3, movie.getYearOfRelease());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseActionException();
        }
    }

    @Override
    public void deleteMovie(int id) throws DatabaseActionException {
        String sql = "DELETE FROM MOVIES WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseActionException();
        }
    }

    @Override
    public void updateMoviesTitle(int id, String newTitle) throws DatabaseActionException {
        String sql = "UPDATE MOVIES SET title=? WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newTitle);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseActionException();
        }
    }

    @Override
    public Optional<Movie> findMovieById(int id) throws DatabaseActionException {
        String sql = "SELECT * FROM MOVIES WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToMovie(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseActionException();
        }
        return Optional.empty();
    }

    @Override
    public List<Movie> findAll() throws DatabaseActionException {
        String sql = "SELECT * FROM MOVIES ";
        List<Movie> rt = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    rt.add(new Movie(resultSet.getInt("id"), resultSet.getString("title"),resultSet.getString("genre"), resultSet.getInt("yearOfRelease")));
                }
            }
            return rt;
        } catch (SQLException e) {
            throw new DatabaseActionException();
        }
    }

    private Movie mapResultSetToMovie(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String title = resultSet.getString("title");
        String genre = resultSet.getString("genre");
        int yearOfRelease = resultSet.getInt("yearOfRelease");
        return new Movie(id, title, genre, yearOfRelease);
    }
}

