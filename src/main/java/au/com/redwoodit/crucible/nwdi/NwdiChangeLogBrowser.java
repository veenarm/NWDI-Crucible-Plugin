package au.com.redwoodit.crucible.nwdi;

import com.atlassian.crucible.scm.ChangelogBrowser;
import com.atlassian.crucible.scm.DetailConstants;
import com.atlassian.crucible.scm.RevisionData;
import com.atlassian.crucible.scm.RevisionKey;
import com.cenqua.crucible.model.Principal;

import java.io.File;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NwdiChangeLogBrowser implements ChangelogBrowser {

    private NwdiContext context;
    private NwdiRepositoryConfiguration config;

    public NwdiChangeLogBrowser(NwdiRepositoryConfiguration config, NwdiContext context) {
        this.config = config;
        this.context = context;
    }

    @Override
    public ChangeSets listChanges(Principal principal, String path, String oldestCsid, boolean includeOldest, String newestCsid, boolean includeNewest, int maxChangesets) {
        return null;
    }

    public File getFile(String path) {
        return new File(config.getBasePath() + File.separator + path);
    }

    public RevisionKey currentKey(String path) {
        File f = getFile(path);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        return new RevisionKey(path, sdf.format(new Date(f.lastModified())));
    }

    public RevisionData getRevisionData(RevisionKey revisionKey) {
        if (revisionKey.equals(currentKey(revisionKey.getPath()))) {
            File f = getFile(revisionKey.getPath());

            RevisionData data = new RevisionData();
            data.setDetail(DetailConstants.COMMIT_DATE, new Date(f.lastModified()));
            data.setDetail(DetailConstants.FILE_TYPE, f.isDirectory() ? "dir" : "file");
            data.setDetail(DetailConstants.ADDED, true);
            data.setDetail(DetailConstants.DELETED, false);
            try {
                data.setDetail(DetailConstants.REVISION_LINK, f.toURL().toString());
            } catch (MalformedURLException e) {
            }
            return data;
        } else {
            throw new RuntimeException("Revision " + revisionKey.getRevision() + " of file " + revisionKey.getPath() + " is no longer available.");
        }
    }
}
