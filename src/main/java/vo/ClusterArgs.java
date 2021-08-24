package vo;

import java.io.Serializable;

public class ClusterArgs implements Serializable {
    private String name;
    private String namespace;
    private String chart_name;
    private String chart_version;
    private Boolean cover;
    private byte[] data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getChart_name() {
        return chart_name;
    }

    public void setChart_name(String chart_name) {
        this.chart_name = chart_name;
    }

    public String getChart_version() {
        return chart_version;
    }

    public void setChart_version(String chart_version) {
        this.chart_version = chart_version;
    }

    public Boolean getCover() {
        return cover;
    }

    public void setCover(Boolean cover) {
        this.cover = cover;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
