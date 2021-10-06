package com.irka.dizaynifo.fileservice.client;

import com.irka.infrastructure.constant.MicroserviceConstants;
import com.irka.infrastructure.rest.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = MicroserviceConstants.USER_SERVICE)
public interface UserClient {
    @GetMapping("/currentUser")
    BaseResponse<String> getCurrentUser();
}
