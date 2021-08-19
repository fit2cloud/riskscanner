package io.riskscanner.commons.constants;

/**
 * @author maguohao
 */
public enum CommandEnum {
    custodian("custodian"), run("run"), dryrun("--dryrun"), schema("schema"), report("report"), version("version"), validate("validate"), nuclei("nuclei"), prowler("prowler");

    private String command;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    CommandEnum(String command) {
        this.command = command;
    }
}
