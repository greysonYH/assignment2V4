package flymetomars.data;

import flymetomars.model.Mission;
import flymetomars.model.Person;

import java.util.List;
import java.util.Set;

/**
 * Created by yli on 10/03/15.
 */
public interface MissionDAO extends DAO<Mission>{
    Set<Mission> getMissionsByCreator(Person person);

    Mission getMissionsByCreatorAndName(Person person, String name);

    Set<Mission> getAllMissions();
}
