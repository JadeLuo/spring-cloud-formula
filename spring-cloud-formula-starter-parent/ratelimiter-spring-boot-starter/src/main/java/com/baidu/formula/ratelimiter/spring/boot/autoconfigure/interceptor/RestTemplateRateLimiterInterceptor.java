/*
 * Copyright (c) 2018 Baidu, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baidu.formula.ratelimiter.spring.boot.autoconfigure.interceptor;

import com.baidu.formula.ratelimiter.spring.boot.autoconfigure.util.FormulaConfigUtils;
import com.baidu.formula.ratelimiter.spring.boot.autoconfigure.util.SystemTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * Created by luoguangming on 2019/5/23.
 * Interceptor for RestTemplate
 */
public class RestTemplateRateLimiterInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RestTemplateRateLimiterInterceptor.class);

    private String serviceName;

    public RestTemplateRateLimiterInterceptor(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        // add system tags from env
        FormulaConfigUtils.getSystemTags().forEach((k, v) -> {
            request.getHeaders().add(k, v);
        });
        // add service name from input
        request.getHeaders().add(SystemTag.SERVICE_NAME, serviceName);
        logger.debug("RestTemplate: insert {} into httpHeader: {}", SystemTag.SERVICE_NAME, serviceName);
        return execution.execute(request, body);
    }
}
