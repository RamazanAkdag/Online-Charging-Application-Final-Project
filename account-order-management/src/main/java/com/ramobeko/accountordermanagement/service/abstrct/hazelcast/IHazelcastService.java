package com.ramobeko.accountordermanagement.service.abstrct.hazelcast;

import com.ramobeko.accountordermanagement.service.abstrct.IWriteService;

public interface IHazelcastService<T> extends IWriteService<T> {
    void delete(Long id);
}
