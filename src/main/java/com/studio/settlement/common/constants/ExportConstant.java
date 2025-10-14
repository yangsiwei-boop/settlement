package com.studio.settlement.common.constants;

import java.util.HashMap;
import java.util.Map;

public interface ExportConstant {

    /** 日报表导出模板 */
    Map<Long, String> DAILY_EXPORT_TEMPLATES = new HashMap<Long, String>(){
        {
            put(2L, "星沙厂日报表.xls");
            put(3L, "城西厂日报表.xls");
            put(4L, "城北厂日报表.xls");
            put(5L, "城南厂日报表.xls");
            put(6L, "城东厂日报表.xls");
        }
    };

}
