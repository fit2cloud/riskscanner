package io.riskscanner.base.mapper.ext;

import io.riskscanner.base.domain.ScanHistory;

public interface ExtScanHistoryMapper {

    Integer getScanId (String accountId);

    Integer updateByExampleSelective(ScanHistory record);

}