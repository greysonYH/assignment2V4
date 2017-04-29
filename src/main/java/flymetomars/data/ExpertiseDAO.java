package flymetomars.data;

import flymetomars.model.Expertise;
import flymetomars.model.Person;

import java.util.Set;

/**
 * @author Yuan-Fang Li
 * @version $Id: $
 */
public interface ExpertiseDAO extends DAO<Expertise> {
    Set<Expertise> getExpertiseByPerson(Person person);
}
