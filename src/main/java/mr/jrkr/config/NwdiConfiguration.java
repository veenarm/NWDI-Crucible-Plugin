package mr.jrkr.config;

import com.atlassian.fisheye.plugins.scm.utils.SimpleConfiguration;

public class NwdiConfiguration implements SimpleConfiguration {

    private String name;
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