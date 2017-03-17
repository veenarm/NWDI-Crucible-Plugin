package au.com.redwoodit.crucible.nwdi;

import com.atlassian.crucible.scm.SCMRepository;
import com.atlassian.fisheye.plugin.configuration.ModuleConfigurationStore;
import com.atlassian.fisheye.plugins.scm.utils.ConfigurableSCMModule;
import com.atlassian.plugin.ModuleDescriptor;
import com.thoughtworks.xstream.XStream;

import java.io.UnsupportedEncodingException;
import java.util.*;

public class NwdiModule extends ConfigurableSCMModule<NwdiRepositoryConfiguration> {

    private ModuleDescriptor moduleDescriptor;
    private ModuleConfigurationStore store;
    private List<SCMRepository> repos = Collections.emptyList();

    public NwdiModule(ModuleConfigurationStore store) {
        super(store);
        this.store = store;
    }

    @Override
    protected SCMRepository createRepository(NwdiRepositoryConfiguration config) {
        NwdiContext context = new NwdiContext(config);
        return new NwdiRepository(config, context);
    }

    @Override
    public String getName() {
        return "NWDI";
    }

    public synchronized Collection<SCMRepository> getRepositories() {
        if (this.repos == null) {
            this.repos = this.loadConfiguration();
        }
        return this.repos;
    }

    public List<NwdiRepositoryConfiguration> getConfiguration() {
        byte[] configData = this.store.getConfiguration(this.moduleDescriptor);
        if (configData != null) {
            try {
                return (List) this.getXStream().fromXML(new String(configData, "UTF8"));
            } catch (Exception var3) {
                throw new RuntimeException("Error reading configuration:" + configData, var3);
            }
        } else {
            return new ArrayList();
        }
    }

    public void setConfiguration(List<NwdiRepositoryConfiguration> config) {
        try {
            this.store.putConfiguration(this.moduleDescriptor, this.getXStream().toXML(config).getBytes("UTF8"));
            this.repos = null;
        } catch (UnsupportedEncodingException var3) {
            throw new RuntimeException("UTF8 encoding not supported", var3);
        }
    }

    private List<SCMRepository> loadConfiguration() {
        ArrayList rs = new ArrayList();
        Iterator i = this.getConfiguration().iterator();

        while (i.hasNext()) {
            NwdiRepositoryConfiguration config = (NwdiRepositoryConfiguration) i.next();
            NwdiContext context = new NwdiContext(config);
            rs.add(new NwdiRepository(config, context));
        }

        return rs;
    }

    protected XStream getXStream() {
        XStream xstream = new XStream();
        xstream.setClassLoader(this.moduleDescriptor.getPlugin().getClassLoader());
        return xstream;
    }

    public void setModuleDescriptor(ModuleDescriptor moduleDescriptor) {
        this.moduleDescriptor = moduleDescriptor;
    }

    public ModuleDescriptor getModuleDescriptor() {
        return moduleDescriptor;
    }

}
