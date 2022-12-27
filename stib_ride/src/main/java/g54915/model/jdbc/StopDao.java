package g54915.model.jdbc;

import g54915.model.dto.StationDto;
import g54915.model.dto.StopDto;
import g54915.model.repository.Dao;
import g54915.model.util.exception.RepositoryException;
import javafx.util.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <code>StopDao</code> data access object for a database of stops. This class
 * is a singleton.
 */
public class StopDao implements Dao<Pair<Integer, Integer>, StopDto> {

    private Connection connection;

    private StopDao() throws RepositoryException {
        connection = DBManager.getInstance().getConnection();
    }

    /**
     * Returns the single instance of the <code>StopDao</code>.
     *
     * @return the single instance of the <code>StopDao</code>.
     */
    public static StopDao getInstance() throws RepositoryException {
        return StopDaoHolder.getInstance();
    }

    @Override
    public Pair<Integer, Integer> insert(StopDto item) throws RepositoryException {
        throw new UnsupportedOperationException("can't insert an element.");
    }

    @Override
    public void delete(Pair<Integer, Integer> key) throws RepositoryException {
        throw new UnsupportedOperationException("can't delete an element.");
    }

    @Override
    public void update(StopDto item) throws RepositoryException {
        throw new UnsupportedOperationException("can't update an element.");
    }

    @Override
    public List<StopDto> selectAll() throws RepositoryException {
        String sql = "SELECT id_line, id_station, id_order, name " +
                "FROM stops " +
                "JOIN lines line ON line.id = id_line " +
                "JOIN stations station ON station.id = id_station " +
                "ORDER BY id_line, id_order";
        List<StopDto> stops = new ArrayList<>();
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                StopDto dto = new StopDto(rs.getInt("id_line"), new StationDto(rs.getInt("id_station"), rs.getString("name")), rs.getInt("id_order"));
                stops.add(dto);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return stops;
    }

    public List<StopDto> selectSame(Integer key) throws RepositoryException {
        if (key == null) {
            throw new RepositoryException("Error with given objects");
        }
        String sql = "SELECT id_line, id_station, id_order, name " +
                "FROM stops " +
                "JOIN lines line ON line.id = id_line " +
                "JOIN stations station ON station.id = id_station " +
                "WHERE id_station = ? " +
                "ORDER BY id_line, id_order";
        List<StopDto> stops = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, key);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                StopDto dto = new StopDto(rs.getInt("id_line"),
                        new StationDto(rs.getInt("id_station"), rs.getString("name")),
                        rs.getInt("id_order"));
                stops.add(dto);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return stops;
    }

    public List<StopDto> selectAdj(List<StopDto> stops) throws RepositoryException {
        if (stops == null) {
            throw new RepositoryException("Error with given objects");
        }
        var sql = new StringBuilder("SELECT id_line, id_station, id_order, name "
                + "FROM stops JOIN lines line ON " +
                "line.id = id_line JOIN stations station ON station.id = id_station WHERE");
        List<Integer> preparedList = new ArrayList<>();
        stops.forEach(stop -> {
            if (stops.get(0) != stop) {
                sql.append(" OR ");
            }
            sql.append("(id_line = ? AND (id_order = ? OR id_order = ?))");
            preparedList.add(stop.getLine());
            preparedList.add(stop.getOrder() - 1);
            preparedList.add(stop.getOrder() + 1);
        });
        sql.append(" GROUP BY name");

        List<StopDto> prevNextStops = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < preparedList.size(); i++) {
                pstmt.setInt(i + 1, preparedList.get(i));
            }
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                StopDto dto = new StopDto(rs.getInt("id_line"),
                        new StationDto(rs.getInt("id_station"), rs.getString("name")),
                        rs.getInt("id_order"));
                prevNextStops.add(dto);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return prevNextStops;
    }

    @Override
    public StopDto select(Pair<Integer, Integer> key) throws RepositoryException {
        throw new UnsupportedOperationException("can't select an element.");
    }

    private static class StopDaoHolder {
        private static StopDao getInstance() throws RepositoryException {
            return new StopDao();
        }
    }
}
