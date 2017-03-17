package au.com.redwoodit.crucible.nwdi;

import com.atlassian.crucible.scm.*;
import com.atlassian.crucible.spi.services.NotFoundException;
import com.cenqua.crucible.model.Principal;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;

public class NwdiRepository implements SCMRepository, HasDirectoryBrowser, HasChangelogBrowser {

    private final NwdiContext context;
    private final NwdiRepositoryConfiguration config;
    private final NwdiChangeLogBrowser changelogBrowser;
    private final NwdiDirectoryBrowser directoryBrowser;

    public NwdiRepository(NwdiRepositoryConfiguration config, NwdiContext context) {
        this.context = context;
        this.config = config;
        this.changelogBrowser = new NwdiChangeLogBrowser(config, context);
        this.directoryBrowser = new NwdiDirectoryBrowser(config);
    }

    @Override
    public ChangelogBrowser getChangelogBrowser() {
        return this.changelogBrowser;
    }

    @Override
    public DirectoryBrowser getDirectoryBrowser() {
        return this.directoryBrowser;
    }

    @Override
    public boolean isAvailable(Principal principal) {
        return true;
    }

    @Override
    public String getName() {
        return this.context.getName();
    }

    @Override
    public String getDescription() {
        return this.getName() + " (File System at " + this.config.getServiceComponent() + ")";
    }

    @Override
    public String getStateDescription() {
        return "Available";
    }

    @Override
    public RevisionData getRevisionData(Principal principal, RevisionKey revisionKey) throws NotFoundException {
        return this.changelogBrowser.getRevisionData(revisionKey);
    }

    @Override
    public void streamContents(Principal principal, RevisionKey revisionKey, OutputStream outputStream) throws IOException, NotFoundException {
        if (this.directoryBrowser.isFile(revisionKey)) {
            FileInputStream is = new FileInputStream(this.directoryBrowser.getFile(revisionKey.getPath()));

            try {
                IOUtils.copy(is, outputStream);
            } finally {
                IOUtils.closeQuietly(is);
            }
        } else {
            throw new RuntimeException("Revision " + revisionKey.getRevision() + " of file " + revisionKey.getPath() + " is no longer available.");
        }

    }

    public FileHistory getFileHistory(Principal principal, String path) {
        return new FileHistory(Collections.singletonList(new RevisionKey(path, "current")));
    }


    @Override
    public RevisionKey getDiffRevisionKey(Principal principal, RevisionKey revisionKey) throws NotFoundException {
        return null;
    }

}