package vo;

import java.io.Serializable;

public class IstioVO implements Serializable {
    private Boolean enabled;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "  " +
                "enabled: " + enabled + '\n';
    }
}
