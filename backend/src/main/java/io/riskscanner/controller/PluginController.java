package io.riskscanner.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.riskscanner.base.domain.Plugin;
import io.riskscanner.commons.utils.PageUtils;
import io.riskscanner.commons.utils.Pager;
import io.riskscanner.controller.request.Plugin.PluginRequest;
import io.riskscanner.service.PluginService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "plugin")
public class PluginController {
    @Resource
    private PluginService pluginService;

    @RequestMapping(value = "all")
    public List<Plugin> getAllPlugin() {
        return pluginService.getAllPlugin();
    }

    @RequestMapping(value = "{pluginId}", method = RequestMethod.GET)
    public Object getCredential(@PathVariable String pluginId) {
        return pluginService.getCredential(pluginId);
    }

    @PostMapping("list/{goPage}/{pageSize}")
    public Pager<List<Plugin>> getPluginList(
            @PathVariable int goPage, @PathVariable int pageSize, @RequestBody PluginRequest request) {
        Page page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, pluginService.getPluginList(request));
    }
}
