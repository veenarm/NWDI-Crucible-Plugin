package au.com.redwoodit.crucible.nwdi;

import com.atlassian.fisheye.plugins.scm.utils.SimpleConfiguration;

public class NwdiRepositoryConfiguration implements SimpleConfiguration {

    private String name;

    public String getServiceComponent() {
        return serviceComponent;
    }

    public void setServiceComponent(String serviceComponent) {
        this.serviceComponent = serviceComponent;
    }

    private String serviceComponent;
    private String basePath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }
}