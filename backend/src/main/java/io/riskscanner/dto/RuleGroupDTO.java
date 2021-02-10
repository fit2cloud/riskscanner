package io.riskscanner.dto;

import io.riskscanner.base.domain.RuleGroup;

/**
 * @author maguohao
 */
public class RuleGroupDTO extends RuleGroup {

    private String pluginName;

    private String pluginIcon;

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getPluginIcon() {
        return pluginIcon;
    }

    public void setPluginIcon(String pluginIcon) {
        this.pluginIcon = pluginIcon;
    }
}
