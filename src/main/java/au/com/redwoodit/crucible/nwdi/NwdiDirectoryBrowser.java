package au.com.redwoodit.crucible.nwdi;

import com.atlassian.crucible.scm.*;
import com.atlassian.crucible.spi.services.NotFoundException;
import com.cenqua.crucible.model.Principal;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class NwdiDirectoryBrowser implements DirectoryBrowser {

    private final NwdiRepositoryConfiguration config;

    public NwdiDirectoryBrowser(NwdiRepositoryConfiguration config) {
        this.config = config;
    }

    @Override
    public List<FileSummary> listFiles(Principal principal, String path) throws NotFoundException {
        List<FileSummary> files = new ArrayList<FileSummary>();
        for (String p : list(path, true)) {
            files.add(new FileSummary(currentKey(p)));
        }
        return files;
    }

    @Override
    public List<DirectorySummary> listDirectories(Principal principal, String path) throws NotFoundException {
        List<DirectorySummary> files = new ArrayList<DirectorySummary>();
        for (String p : list(path, false)) {
            files.add(new DirectorySummary(p));
        }
        return files;
    }

    @Override
    public FileHistory getFileHistory(Principal principal, String path, String pegRevision) throws NotFoundException {
        return new FileHistory(Collections.singletonList(currentKey(path)));
    }

    private List<String> list(String path, boolean returnFiles) {
        File parent = getFile(path);
        List<String> files = new ArrayList<String>();
        if (parent.isDirectory()) {
            File[] children = parent.listFiles();
            // this may be null if we can't read the directory, for instance.
            if (children != null) {
                for (File f : children) {
                    if (f.isFile() && returnFiles || f.isDirectory() && !returnFiles) {
                        files.add(getPath(f));
                    }
                }
            }
        }
        return files;
    }

    /**
     * @return the path for a given File relative to the base configured for this
     * repository -- the path doesn't include the base component.
     */
    private String getPath(File file) {
        String s = file.getAbsolutePath();
        if (!s.startsWith(config.getBasePath())) {
            throw new RuntimeException("Invalid file with path " + s + " is not under base " + config.getBasePath());
        }
        return s.substring(config.getBasePath().length() + 1);
    }

    /**
     * Takes the name of a file in the repository and returns a file handle to the
     * file on disk.
     *
     * @param path
     * @return
     */
    public File getFile(String path) {
        return new File(config.getBasePath() + File.separator + path);
    }


    public boolean isFile(RevisionKey key) {
        return key.equals(currentKey(key.getPath()));
    }

    /**
     * Returns a {@link RevisionKey} instance for the specified file. Because we
     * do not support versioning, the revision string will be set to the file's
     * last modification date.
     *
     * @param path
     * @return
     */
    public RevisionKey currentKey(String path) {
        File f = getFile(path);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        return new RevisionKey(path, sdf.format(new Date(f.lastModified())));
    }

}
