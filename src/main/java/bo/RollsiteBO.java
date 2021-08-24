package bo;

import vo.PartyVO;

import java.io.Serializable;
import java.util.List;

public class RollsiteBO implements Serializable {
    private String type;
    private Integer nodePort;
    private List<PartyVO> partyList;

    public String getType() {
        return type;
    }

    public RollsiteBO setType(String type) {
        this.type = type;
        return this;
    }

    public Integer getNodePort() {
        return nodePort;
    }

    public RollsiteBO setNodePort(Integer nodePort) {
        this.nodePort = nodePort;
        return this;
    }

    public List<PartyVO> getPartyList() {
        return partyList;
    }

    public RollsiteBO setPartyList(List<PartyVO> partyList) {
        this.partyList = partyList;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("  type: " + type +'\n');
        stringBuilder.append("  nodePort: " + nodePort +'\n');
        stringBuilder.append("  partyList:" +'\n');
        for(PartyVO partyVO:partyList){
            stringBuilder.append(partyVO);
        }
        return stringBuilder.toString();
    }
}
