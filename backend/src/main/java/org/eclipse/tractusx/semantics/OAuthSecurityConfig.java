/********************************************************************************
 * Copyright (c) 2021-2022 Robert Bosch Manufacturing Solutions GmbH
 * Copyright (c) 2021-2022 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ********************************************************************************/
package org.eclipse.tractusx.semantics;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Profile("!local")
@Configuration
public class OAuthSecurityConfig {
    @Value("${jwt.issuer-uri}")
    private String issuerUri; // Fetch issuer URI from application.properties

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
          .authorizeRequests(auth -> auth
            .requestMatchers(HttpMethod.OPTIONS).permitAll()
            .requestMatchers(HttpMethod.GET,"/**/models/**").access("@authorizationEvaluator.hasRoleViewSemanticModel()")
            .requestMatchers(HttpMethod.POST,"/**/models/**").access("@authorizationEvaluator.hasRoleAddSemanticModel()")
            .requestMatchers(HttpMethod.PUT,"/**/models/**").access("@authorizationEvaluator.hasRoleUpdateSemanticModel()")
            .requestMatchers(HttpMethod.DELETE,"/**/models/**").access("@authorizationEvaluator.hasRoleDeleteSemanticModel()"))
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .oauth2ResourceServer().jwt();

        return http.build();
    }
    // @Bean
    // public JwtDecoder jwtDecoder() {
    //     // Create a JwtDecoder using the issuer URI
    //     return NimbusJwtDecoder.withJwkSetUri(issuerUri).build();
    // }
    @Bean
    public JwtDecoder jwtDecoder() {
        // Create a JwtDecoder using the issuer URI
        return NimbusJwtDecoder.withJwkSetUri(issuerUri).build();
    }
    // @Bean
    // public JwtDecoder jwtDecoder(){
    //     return token -> {
    //         throw new UnsupportedOperationException("The JwtDecoder must not be called in tests by Spring.");
    //     };
    // }
}
