package au.com.redwoodit.crucible.nwdi;

import org.apache.commons.lang.Validate;

public class NwdiContext {

    private String name;

    public NwdiContext(NwdiRepositoryConfiguration config) {
        Validate.notNull(config, "NWDI config cannot be null");
        this.name = config.getName();
    }

    public String getName() {
        return this.name;
    }
}
