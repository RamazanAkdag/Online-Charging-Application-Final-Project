package com.ramobeko.accountordermanagement.service.abstrct.oracle;


import com.ramobeko.accountordermanagement.model.dto.IDTO;
import com.ramobeko.accountordermanagement.model.dto.RegisterRequest;
import com.ramobeko.accountordermanagement.service.abstrct.ICrudService;

public interface IOracleService<T, DTO extends IDTO> extends ICrudService<T, DTO> {
}
