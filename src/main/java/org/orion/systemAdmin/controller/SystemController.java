package org.orion.systemAdmin.controller;

import org.orion.common.basic.SearchResult;
import org.orion.systemAdmin.entity.SystemConfig;
import org.orion.systemAdmin.service.SysConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class SystemController {

    @Resource
    private SysConfigService sysConfigService;

    @RequestMapping("/web/System/Configuration")
    public String initSystemCongifPage() {
        return "systemadmin/system_config";
    }

    @RequestMapping("/web/System/Configuration/search")
    @ResponseBody
    public SearchResult searchSystemConfig(String page) {
        SearchResult searchResult = new SearchResult();
        List<SystemConfig> sysConfigList = sysConfigService.searchAllSystemConfigs(Integer.parseInt(page), 5);
        searchResult.setPage(Integer.parseInt(page));
        searchResult.setTotalPages(sysConfigService.getSystemConfigTotalPages(5));
        searchResult.setResult(sysConfigList);
        return searchResult;
    }

    @RequestMapping("/web/System/Configuration/modify")
    @ResponseBody
    public SystemConfig initModify(String configKey) {
        return sysConfigService.getSystemConfig(configKey);
    }
}
