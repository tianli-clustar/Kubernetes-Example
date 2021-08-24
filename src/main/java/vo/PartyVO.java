package vo;

import java.io.Serializable;

public class PartyVO implements Serializable {
    private Integer partyId;
    private String partyIp;
    private Integer partyPort;

    public Integer getPartyId() {
        return partyId;
    }

    public PartyVO setPartyId(Integer partyId) {
        this.partyId = partyId;
        return this;
    }

    public String getPartyIp() {
        return partyIp;
    }

    public PartyVO setPartyIp(String partyIp) {
        this.partyIp = partyIp;
        return this;
    }

    public Integer getPartyPort() {
        return partyPort;
    }

    public PartyVO setPartyPort(Integer partyPort) {
        this.partyPort = partyPort;
        return this;
    }

}
