package com.ramobeko.accountordermanagement.service.abstrct.oracle;

import com.ramobeko.accountordermanagement.model.dto.request.SubscriberRequest;
import com.ramobeko.accountordermanagement.model.dto.request.SubscriberUpdateRequest;
import com.ramobeko.accountordermanagement.model.shared.OracleSubscriber;

import java.util.List;


public interface IOracleSubscriberService extends IOracleService<OracleSubscriber,SubscriberRequest> {
    public void update(SubscriberUpdateRequest request);

    public OracleSubscriber createSubscriber(Long id, SubscriberRequest request);
}
