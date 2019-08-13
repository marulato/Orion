package org.orion.systemAdmin.controller;

import org.orion.common.audit.AuditTrail;
import org.orion.common.basic.AppContext;
import org.orion.common.basic.Response;
import org.orion.common.basic.SearchParam;
import org.orion.common.basic.SearchResult;
import org.orion.common.dao.crud.CrudManager;
import org.orion.common.mastercode.ErrorCode;
import org.orion.common.miscutil.Encrtption;
import org.orion.common.validation.Validation;
import org.orion.systemAdmin.entity.AppConsts;
import org.orion.systemAdmin.entity.SystemConfig;
import org.orion.systemAdmin.service.SysConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SystemController {

    @Resource
    private SysConfigService sysConfigService;
    @Resource
    private CrudManager crudManager;
    private final Logger logger = LoggerFactory.getLogger(SystemController.class);

    @RequestMapping("/web/System/Configuration")
    public String initSystemCongifPage() {
        return "systemadmin/system_config";
    }

    @RequestMapping("/web/System/Configuration/search")
    @ResponseBody
    public SearchResult searchSystemConfig(String page, String pageSize, SystemConfig systemConfig) {
        SearchParam<SystemConfig> searchParam = new SearchParam(page, pageSize, systemConfig);
        SearchResult searchResult = null;
        searchParam.orderByAsc(SystemConfig.COL_CONFIG_KEY);
        try {
            List<SystemConfig> sysConfigList = sysConfigService.search(searchParam);
            searchResult = SearchResult.success(sysConfigList, sysConfigService.getSystemConfigTotalPages(searchParam));
        } catch (Exception e) {
            searchResult = SearchResult.error();
            logger.error("Searching System configs encountered an exception", e);
        }
        return searchResult;
    }

    @RequestMapping("/web/System/Configuration/modify")
    @ResponseBody
    public SystemConfig initModifySystemConfig(String configKey) {
        return sysConfigService.getSystemConfig(configKey);
    }

    @RequestMapping("/web/System/Configuration/validate")
    @ResponseBody
    public Response validateSystemConfig(SystemConfig systemConfig) {
        List<ErrorCode> errors = Validation.doValidate(systemConfig);
        Response response = new Response();
        if (!errors.isEmpty()) {
            response.setCode(AppConsts.VALIDATION_ERROR);
            response.setErrors(errors);
        } else {
            response.setCode(AppConsts.VALIDATION_PASSED);
        }
        return response;
    }

    @RequestMapping("/web/System/Configuration/doModify")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Response doModifySystemConfig(SystemConfig systemConfig, String token, HttpServletRequest request) {
        Response response = new Response();
        if (!Encrtption.validateToken(token)) {
            response.setCode("000");
            return response;
        }
        try {
            AppContext context = AppContext.getAppContext(request);
            SystemConfig Sysconfigbefore = sysConfigService.getSystemConfig(systemConfig.getConfigKey());
            AuditTrail<SystemConfig> before = new AuditTrail<>(Sysconfigbefore, AppConsts.AUDIT_BEFOR_MODIFY, context);
            crudManager.createAudit(before);
            sysConfigService.updateSystemConfig(systemConfig);
            AuditTrail<SystemConfig> after = new AuditTrail<>(systemConfig, AppConsts.AUDIT_AFTER_MODIFY, context);
            crudManager.createAudit(after);
            response.setCode(AppConsts.RESPONSE_SUCCESS);
        } catch (Exception e) {
            logger.error("Modifying a configuration encountered an exception", e);
            response.setCode(AppConsts.RESPONSE_ERROR);
            throw e;
        }

        return response;
    }

    public Response prepareSystemInfo(HttpServletRequest request) {
        Response response = new Response();
        Map<String, String> info = new HashMap<>();
        try {
            ServletContext servletContext = request.getServletContext();
            String serverInfo = servletContext.getServerInfo();
            String osInfo = System.getProperty("os.name");
            info.put("server_info", serverInfo);
            response.setObject(info);
            Runtime runtime = Runtime.getRuntime();
            if (osInfo.contains("Windows")) {
                Process process = runtime.exec("systeminfo");
                InputStreamReader reader = new InputStreamReader(process.getInputStream(), "GBK");
                LineNumberReader input = new LineNumberReader(reader);
                String line = "";
                while ((line = input.readLine()) != null) {
                    if (line.contains("OS 名称")) {
                        String[] details = line.split(":");
                        info.put("os_info", details[1].trim());
                        break;
                    }
                }
            } else if (osInfo.contains("Linux")) {
                Process process = runtime.exec("cat/etc/issue");
                InputStreamReader reader = new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8);
                LineNumberReader input = new LineNumberReader(reader);
                String line = "";
                while ((line = input.readLine()) != null) {
                    info.put("os_info", line);
                    break;
                }
            }
            String mysqlVer = crudManager.execute("SELECT VERSION();");
            info.put("mysql_ver", mysqlVer);
            info.put("jvm_vendor", System.getProperty("java.vm.vendor"));
            info.put("jvm_name", System.getProperty("java.vm.name "));
            info.put("jvm_version", System.getProperty("java.vm.version"));
            info.put("jre_vendor", System.getProperty("java.vendor"));
            info.put("jre_version", System.getProperty("java.version"));

        } catch (Exception e) {
            info.clear();
        }
        return response;
    }
}
