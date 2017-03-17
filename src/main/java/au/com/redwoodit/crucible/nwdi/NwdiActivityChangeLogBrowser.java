package au.com.redwoodit.crucible.nwdi;

import com.atlassian.crucible.scm.ChangelogBrowser;
import com.cenqua.crucible.model.Principal;

public class NwdiActivityChangeLogBrowser implements ChangelogBrowser {

    private NwdiContext context;
    private NwdiRepositoryConfiguration config;

    public NwdiActivityChangeLogBrowser(NwdiRepositoryConfiguration config, NwdiContext context) {
        this.config = config;
        this.context = context;
    }

    @Override
    public ChangeSets listChanges(Principal principal, String path, String oldestCsid, boolean includeOldest, String newestCsid, boolean includeNewest, int maxChangesets) {
        return null;
    }
}
