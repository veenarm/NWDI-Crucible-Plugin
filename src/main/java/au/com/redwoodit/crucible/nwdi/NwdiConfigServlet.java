package au.com.redwoodit.crucible.nwdi;

import com.atlassian.crucible.spi.FisheyePluginUtilities;
import com.atlassian.fisheye.plugin.web.helpers.VelocityHelper;
import com.atlassian.fisheye.plugins.scm.utils.SimpleConfigurationServlet;
import com.atlassian.plugin.PluginAccessor;

public class NwdiConfigServlet extends SimpleConfigurationServlet<NwdiRepositoryConfiguration> {
    public NwdiConfigServlet(PluginAccessor pluginAccessor, FisheyePluginUtilities fisheyePluginUtilities, VelocityHelper velocityHelper) {
        super(pluginAccessor, fisheyePluginUtilities, velocityHelper);
    }

    protected NwdiRepositoryConfiguration defaultConfig() {
        return new NwdiRepositoryConfiguration();
    }

    protected String getProviderPluginModuleKey() {
        return "au.com.redwoodit.nwdi-crucible-plugin:scmprovider";
    }

    protected String getTemplatePackage() {
        return "/nwdi-templates";
    }
}