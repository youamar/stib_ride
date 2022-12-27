package g54915.model.repository;

import g54915.model.jdbc.StopDao;
import g54915.model.dto.StopDto;
import g54915.model.util.exception.RepositoryException;

import java.util.List;
import javafx.util.Pair;

public class StopRepository implements Repository<Pair<Integer, Integer>, StopDto> {

    private final StopDao dao;

    public StopRepository() throws RepositoryException {
        dao = StopDao.getInstance();
    }

    @Override
    public void update(StopDto item) throws RepositoryException {
        dao.update(item);
    }

    @Override
    public Pair<Integer, Integer> add(StopDto item) throws RepositoryException {
        return dao.insert(item);
    }

    @Override
    public void remove(Pair<Integer, Integer> key) throws RepositoryException {
        dao.delete(key);
    }

    @Override
    public List<StopDto> getAll() throws RepositoryException {
        return dao.selectAll();
    }

    @Override
    public StopDto get(Pair<Integer, Integer> key) throws RepositoryException {
        return dao.select(key);
    }

    public List<StopDto> getSame(Integer key) throws RepositoryException {
        return dao.selectSame(key);
    }

    public List<StopDto> getAdj(List<StopDto> stops) throws RepositoryException {
        return dao.selectAdj(stops);
    }

}
