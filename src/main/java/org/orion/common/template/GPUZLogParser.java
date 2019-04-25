package org.orion.common.template;

import org.orion.common.document.text.BaseTextParser;
import org.orion.common.document.text.Text;

import java.util.*;

public class GPUZLogParser extends BaseTextParser {
    public GPUZLogParser(String path) {
        super(path);
    }

    @Override
    public Map<String, Integer> fieldMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("date", 0);
        map.put("coreClock", 1);
        map.put("memoryClock", 2);
        map.put("GpuTemperature", 3);
        map.put("dedicatedMemUsage", 4);
        map.put("load", 5);
        map.put("memoryControllerLoad", 6);
        map.put("videoEngineLoad", 7);
        map.put("busInterfaceLoad", 8);
        map.put("perfCapReason", 9);
        map.put("vddc", 10);
        map.put("CpuTemperature", 11);
        map.put("SystemMemUsage", 12);
        return map;
    }

    @Override
    public Map<Integer, List<String>> separate(Text text) {
        Map<Integer, List<String>> map = new HashMap<>();
        if (text != null) {
            List<String> lines = text.getContent();
            if (lines != null && lines.size() > 0) {
                for(int i = 0; i < lines.size(); i++) {
                    List<String> singleRow = new ArrayList<>();
                    String[] params = lines.get(i).split(",");
                    Arrays.stream(params).forEach((arg) -> {
                        singleRow.add(arg.trim());
                    });
                    map.put(i, singleRow);
                }
            }
        }
        return map;
    }

    @Override
    public void exclude(List<String> content) {
        content.remove(0);
    }
}
