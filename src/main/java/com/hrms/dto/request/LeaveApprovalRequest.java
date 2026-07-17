package com.hrms.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeaveApprovalRequest {

    @Size(max = 1000)
    private String remarks;

}