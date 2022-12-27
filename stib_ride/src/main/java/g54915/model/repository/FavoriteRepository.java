package g54915.model.repository;

import g54915.model.dto.FavoriteDto;
import g54915.model.jdbc.FavoriteDao;
import g54915.model.util.exception.RepositoryException;

import java.util.List;

public class FavoriteRepository implements Repository<String, FavoriteDto> {

    private final FavoriteDao dao;

    public FavoriteRepository() throws RepositoryException {
        dao = FavoriteDao.getInstance();
    }

    public FavoriteRepository(FavoriteDao dao) {
        this.dao = dao;
    }

    @Override
    public String add(FavoriteDto item) throws RepositoryException {
        if (item.getKey().equals("")) {
            throw new RepositoryException("the name can't be empty");
        }
        return dao.insert(item);
    }

    @Override
    public void update(FavoriteDto item) throws RepositoryException {
        if (item.getKey().equals("")) {
            throw new RepositoryException("the name can't be empty");
        }
        dao.update(item);
    }

    @Override
    public void remove(String key) throws RepositoryException {
        dao.delete(key);
    }

    @Override
    public List<FavoriteDto> getAll() throws RepositoryException {
        return dao.selectAll();
    }

    @Override
    public FavoriteDto get(String key) throws RepositoryException {
        return dao.select(key);
    }
}
