package com.studio.settlement.common.util;

import com.studio.settlement.bean.dto.BaseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CommonUtils {

    public static void initPageNum(BaseDTO dto) {
        if (null == dto.getPageNo()) {
            dto.setPageNo(1);
        }

        if (null == dto.getPageSize()) {
            dto.setPageSize(10);
        }
    }

}
