
package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.LoanTypeDTO;
import co.com.pragma.api.dto.LoanTypeResponseDTO;
import co.com.pragma.model.loantype.LoanType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoanTypeMapper {
    LoanTypeResponseDTO toDto(LoanType loanType);
    LoanType toModel(LoanTypeDTO loanTypeDto);
}
