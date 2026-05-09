package com.srtp.agent.common.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PageRequest extends BaseRequest {
    private long current = 1;
    private long pageSize = 10;
    private String sortField;
    private String sortOrder;
}
