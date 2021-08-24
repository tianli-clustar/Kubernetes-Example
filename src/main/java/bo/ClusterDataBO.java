package bo;

import vo.IstioVO;
import vo.ModulesVO;

import java.io.Serializable;
import java.util.List;

public class ClusterDataBO implements Serializable {
    private String name;
    private String namespace;
    private String chartName;
    private String chartVersion;
    private Integer partyId;
    private String registry;
    private String pullPolicy;
    private Boolean persistence;
    private IstioVO istio;
    private ModulesVO modules;
    private RollsiteBO rollsite;

    public String getName() {
        return name;
    }

    public ClusterDataBO setName(String name) {
        this.name = name;
        return this;
    }

    public String getNamespace() {
        return namespace;
    }

    public ClusterDataBO setNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public String getChartName() {
        return chartName;
    }

    public ClusterDataBO setChartName(String chartName) {
        this.chartName = chartName;
        return this;
    }

    public String getChartVersion() {
        return chartVersion;
    }

    public ClusterDataBO setChartVersion(String chartVersion) {
        this.chartVersion = chartVersion;
        return this;
    }

    public Integer getPartyId() {
        return partyId;
    }

    public ClusterDataBO setPartyId(Integer partyId) {
        this.partyId = partyId;
        return this;
    }

    public String getRegistry() {
        return registry;
    }

    public ClusterDataBO setRegistry(String registry) {
        this.registry = registry;
        return this;
    }

    public String getPullPolicy() {
        return pullPolicy;
    }

    public ClusterDataBO setPullPolicy(String pullPolicy) {
        this.pullPolicy = pullPolicy;
        return this;
    }

    public Boolean getPersistence() {
        return persistence;
    }

    public ClusterDataBO setPersistence(Boolean persistence) {
        this.persistence = persistence;
        return this;
    }

    public IstioVO getIstio() {
        return istio;
    }

    public ClusterDataBO setIstio(IstioVO istio) {
        this.istio = istio;
        return this;
    }

    public ModulesVO getModules() {
        return modules;
    }

    public ClusterDataBO setModules(ModulesVO modules) {
        this.modules = modules;
        return this;
    }

    public RollsiteBO getRollsite() {
        return rollsite;
    }

    public ClusterDataBO setRollsite(RollsiteBO rollsite) {
        this.rollsite = rollsite;
        return this;
    }

    @Override
    public String toString() {
        return  "name: " + name + '\n' +
                "namespace: " + namespace + '\n' +
                "chartName: " + chartName + '\n' +
                "chartVersion: " + chartVersion + '\n' +
                "partyId: " + partyId +'\n'+
                "registry: " + registry + '\n' +
                "pullPolicy: " + pullPolicy + '\n' +
                "persistence: " + persistence +'\n'+
                "istio:" + '\n'+istio +
                "modules:" + '\n'+modules +
                "rollsite:" + '\n'+rollsite;
    }
}
