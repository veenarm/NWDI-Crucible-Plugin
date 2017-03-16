package mr.jrkr.scm;

import com.atlassian.crucible.scm.DetailConstants;
import com.atlassian.crucible.scm.RevisionData;
import com.atlassian.crucible.scm.RevisionKey;
import com.atlassian.crucible.scm.SCMRepository;
import com.cenqua.crucible.model.Principal;
import mr.jrkr.config.NwdiConfiguration;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NwdiScmRepositoryImpl implements SCMRepository {

    private final NwdiConfiguration config;

    public NwdiScmRepositoryImpl(NwdiConfiguration config) {
        this.config = config;
    }

    public boolean isAvailable(Principal principal) {
        return true;
    }

    public String getName() {
        return config.getName();
    }

    public String getDescription() {
        return getName() + " file system repo at: " + config.getBasePath();
    }

    public String getStateDescription() {
        return "Available";
    }

    public RevisionData getRevisionData(Principal principal, RevisionKey revisionKey) {
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

    public void streamContents(Principal principal, RevisionKey revisionKey, OutputStream outputStream) throws IOException {
        if (revisionKey.equals(currentKey(revisionKey.getPath()))) {
            InputStream is = new FileInputStream(getFile(revisionKey.getPath()));
            try {
                IOUtils.copy(is, outputStream);
            } finally {
                IOUtils.closeQuietly(is);
            }
        } else {
            throw new RuntimeException("Revision " + revisionKey.getRevision() + " of file " + revisionKey.getPath() + " is no longer available.");
        }
    }

    public RevisionKey getDiffRevisionKey(Principal principal, RevisionKey revisionKey) {
        // diffs are not supported in this example
        return null;
    }


    /**
     * Returns a {@link RevisionKey} instance for the specified file. Because we
     * do not support versioning, the revision string will be set to the file's
     * last modification date.
     *
     * @param path
     * @return
     */
    private RevisionKey currentKey(String path) {
        File f = getFile(path);
        return new RevisionKey(path, createDateFormat().format(new Date(f.lastModified())));
    }

    /**
     * Takes the name of a file in the repository and returns a file handle to the
     * file on disk.
     *
     * @param path
     * @return
     */
    private File getFile(String path) {
        return new File(config.getBasePath() + File.separator + path);
    }

    private DateFormat createDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    }
}