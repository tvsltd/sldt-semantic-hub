###############################################################
# Copyright (c) 2021-2022 T-Systems International GmbH
# Copyright (c) 2021-2022 Contributors to the Eclipse Foundation
#
# See the NOTICE file(s) distributed with this work for additional
# information regarding copyright ownership.
#
# This program and the accompanying materials are made available under the
# terms of the Apache License, Version 2.0 which is available at
# https://www.apache.org/licenses/LICENSE-2.0.
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
# WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
# License for the specific language governing permissions and limitations
# under the License.
#
# SPDX-License-Identifier: Apache-2.0
###############################################################

# Docker buildfile to containerize the semantics layer
FROM maven:3-eclipse-temurin-17 AS builder
COPY . /build
WORKDIR /build
RUN mvn package -DskipTests

FROM eclipse-temurin:17-jre

RUN groupadd -g 101 spring \
    && useradd -u 100 -g spring -s /bin/false spring \
    && mkdir -p /service \
    && chown spring:spring /service

USER 100:101

WORKDIR /service

COPY --from=builder /build/backend/target/semantic-hub*.jar app.jar

COPY backend/Dockerfile *legal /legal/

COPY LICENSE NOTICE.md DEPENDENCIES SECURITY.md /legal/

ENV JAVA_TOOL_OPTIONS "-Xms512m -Xmx2048m"
EXPOSE 4242

ENTRYPOINT [ "java","-jar", "/service/app.jar" ]
