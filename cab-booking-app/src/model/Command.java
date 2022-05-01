package model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Command {

    public static final String SPACE = " ";
    private String commandName;
    private List<String> params;

    public String getCommand() {
        return commandName;
    }

    public void setCommand(String commandName) {
        this.commandName = commandName;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public Command(String input) {
        List<String> tokensList = Arrays.stream(input.trim().split(SPACE))
                .map(String::trim)
                .filter(token -> token.length() > 0)
                .collect(Collectors.toList());
        setCommand(tokensList.get(0).toLowerCase());
        tokensList.remove(0);
        setParams(tokensList);
    }

}
