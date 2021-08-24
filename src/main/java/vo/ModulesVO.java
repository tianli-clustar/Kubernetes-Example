package vo;

import java.io.Serializable;
import java.util.List;

public class ModulesVO implements Serializable {
    private List<String> noname;

    public List<String> getNoname() {
        return noname;
    }

    public ModulesVO setNoname(List<String> noname) {
        this.noname = noname;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder=new StringBuilder();
        for(String s:noname){
            stringBuilder.append("  - ");
            stringBuilder.append(s);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
