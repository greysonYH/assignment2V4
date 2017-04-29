package flymetomars.data;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by yli on 10/03/15.
 */
public interface DAO<T> {
    T load(Long id) throws SQLException;

    List<T> getAll();

    void save(T object) throws SQLException;

    void update(T object);

    void saveOrUpdate(T object);

    T delete(T object);

}
