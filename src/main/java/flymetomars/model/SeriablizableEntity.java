package flymetomars.model;

/**
 * Created by yli on 10/03/15.
 */
public abstract class SeriablizableEntity {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
