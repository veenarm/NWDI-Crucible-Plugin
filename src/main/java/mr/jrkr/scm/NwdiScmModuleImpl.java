package mr.jrkr.scm;

import com.atlassian.crucible.scm.SCMModule;
import com.atlassian.crucible.scm.SCMRepository;
import com.atlassian.fisheye.plugin.configuration.ModuleConfigurationStore;
import com.atlassian.fisheye.plugins.scm.utils.Configurable;
import com.atlassian.plugin.ModuleDescriptor;
import com.thoughtworks.xstream.XStream;
import mr.jrkr.config.NwdiConfiguration;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class NwdiScmModuleImpl implements SCMModule, Configurable<List<NwdiConfiguration>> {

    private ModuleDescriptor moduleDescriptor;
    private List<SCMRepository> repos = Collections.emptyList();
    private ModuleConfigurationStore store;

    public NwdiScmModuleImpl(ModuleConfigurationStore store) {
        this.store = store;
    }

    public List<NwdiConfiguration> getConfiguration() {
        byte[] configData = store.getConfiguration(moduleDescriptor);
        if (configData != null) {
            try {
                return (List<NwdiConfiguration>) getXStream().fromXML(new String(configData, "UTF8"));
            } catch (Exception e) {
                throw new RuntimeException("Error reading configuration:" + configData, e);
            }
        }
        return new ArrayList<>();
    }

    public void setConfiguration(List<NwdiConfiguration> config) {
        try {
            store.putConfiguration(moduleDescriptor, getXStream().toXML(config).getBytes("UTF8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF8 encoding not supported", e);
        }
    }

    private XStream getXStream() {
        XStream xstream = new XStream();
        xstream.setClassLoader(moduleDescriptor.getPlugin().getClassLoader());
        return xstream;
    }

    public String getName() {
        return "Nwdi SCM (or DTR as they call it)";
    }


    public synchronized Collection<SCMRepository> getRepositories() {
        if (repos == null) {
            repos = new ArrayList<SCMRepository>();
            for (NwdiConfiguration config : getConfiguration()) {
                repos.add(new NwdiScmRepositoryImpl(config));
            }
        }
        return repos;
    }

    public void setModuleDescriptor(ModuleDescriptor moduleDescriptor) {
        this.moduleDescriptor = moduleDescriptor;
    }

    public ModuleDescriptor getModuleDescriptor() {
        return moduleDescriptor;
    }
}
