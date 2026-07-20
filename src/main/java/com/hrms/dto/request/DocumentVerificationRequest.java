package com.hrms.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentVerificationRequest {

    private Boolean verified;

    private String remarks;

}