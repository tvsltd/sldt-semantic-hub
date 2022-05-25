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
package org.eclipse.tractusx.semantics.hub.bamm;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.eclipse.tractusx.semantics.hub.bamm.BammHelper;

import io.openmanufacturing.sds.aspectmodel.resolver.services.VersionedModel;
import io.vavr.control.Try;

public class BammHelperTest {
    @Test
    public void testBammLoaderWithValidModel() {
        final String modelString = "@prefix bamm: <urn:bamm:io.openmanufacturing:meta-model:1.0.0#> .\n @prefix bamm-c: <urn:bamm:io.openmanufacturing:characteristic:1.0.0#> .\n @prefix bamm-e: <urn:bamm:io.openmanufacturing:entity:1.0.0#> .\n @prefix unit: <urn:bamm:io.openmanufacturing:unit:1.0.0#> .\n @prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n @prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n @prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n @prefix : <urn:bamm:org.eclipse.tractusx:1.0.0#> .\n \n :Movement a bamm:Aspect;\n bamm:name \"Movement\";\n bamm:preferredName \"Movement\"@en;\n bamm:description \"Aspect for movement information\"@en;\n bamm:properties (:isMoving :speedLimitWarning :position);\n bamm:operations ().\n :isMoving a bamm:Property;\n bamm:name \"isMoving\";\n bamm:preferredName \"Moving\"@en;\n bamm:description \"Flag indicating whether the asset is currently moving\"@en;\n bamm:characteristic bamm-c:Boolean.\n :speedLimitWarning a bamm:Property;\n bamm:name \"speedLimitWarning\";\n bamm:preferredName \"Speed Limit Warning\"@en;\n bamm:description \"Indicates if the speed limit is adhered to.\"@en;\n bamm:characteristic :TrafficLight.\n :position a bamm:Property;\n bamm:name \"position\";\n bamm:preferredName \"Position\"@en;\n bamm:description \"Indicates a position\"@en;\n bamm:characteristic :SpatialPositionCharacteristic.\n :TrafficLight a bamm-c:Enumeration;\n bamm:name \"TrafficLight\";\n bamm:preferredName \"Warning Level\"@en;\n bamm:description \"Represents if speed of position change is within specification (green), within tolerance (yellow), or outside specification (red).\"@en;\n bamm:dataType xsd:string;\n bamm-c:values (\"green\" \"yellow\" \"red\").\n :SpatialPosition a bamm:Entity;\n bamm:name \"SpatialPosition\";\n bamm:preferredName \"Spatial Position\"@en;\n bamm:description \"Position in space, described along three axis, with the third axis optional, if all positions are in a plane.\"@en;\n bamm:properties (:x :y :z).\n :x a bamm:Property;\n bamm:name \"x\";\n bamm:preferredName \"x\"@en;\n bamm:description \"x coordinate in space\"@en;\n bamm:characteristic :Coordinate.\n :y a bamm:Property;\n bamm:name \"y\";\n bamm:preferredName \"y\"@en;\n bamm:description \"y coordinate in space\"@en;\n bamm:characteristic :Coordinate.\n :z a bamm:Property;\n bamm:name \"z\";\n bamm:preferredName \"z\"@en;\n bamm:description \"z coordinate in space\"@en;\n bamm:characteristic :Coordinate;\n bamm:optional \"true\"^^xsd:boolean.\n :Coordinate a bamm-c:Measurement;\n bamm:name \"Coordinate\";\n bamm:preferredName \"Coordinate\"@en;\n bamm:description \"Represents a coordinate along an axis in space.\"@en;\n bamm:dataType xsd:float;\n bamm-c:unit unit:metre.\n :SpatialPositionCharacteristic a bamm-c:SingleEntity;\n bamm:name \"SpatialPositionCharacteristic\";\n bamm:preferredName \"Spatial Position Characteristic\"@en;\n bamm:description \"Represents a single position in space with optional z coordinate.\"@en;\n bamm:dataType :SpatialPosition.\n";

        BammHelper bammHelper = new BammHelper();

        assertTrue(bammHelper.loadBammModel(modelString).isSuccess());
    }

    @Test
    public void testBammLoaderWithNotSelfContainedModel() {
        final String modelString = "@prefix bamm: <urn:bamm:io.openmanufacturing:meta-model:1.0.0#> .\n @prefix bamm-c: <urn:bamm:io.openmanufacturing:characteristic:1.0.0#> .\n @prefix bamm-e: <urn:bamm:io.openmanufacturing:entity:1.0.0#> .\n @prefix unit: <urn:bamm:io.openmanufacturing:unit:1.0.0#> .\n @prefix blub: <urn:bamm:org.eclipse.tractusx.extension:1.0.0#> .\n @prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n @prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n @prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n @prefix : <urn:bamm:org.eclipse.tractusx:1.0.0#> .\n \n :Movement a bamm:Aspect;\n bamm:name \"Movement\";\n bamm:preferredName \"Movement\"@en;\n bamm:description \"Aspect for movement information\"@en;\n bamm:properties (:position);\n bamm:operations ().\n :position a bamm:Property;\n bamm:name \"position\";\n bamm:preferredName \"Position\"@en;\n bamm:description \"Indicates a position\"@en;\n bamm:characteristic :SpatialPositionCharacteristic.\n :SpatialPositionCharacteristic a bamm-c:SingleEntity;\n bamm:name \"SpatialPositionCharacteristic\";\n bamm:preferredName \"Spatial Position Characteristic\"@en;\n bamm:description \"Represents a single position in space with optional z coordinate.\"@en;\n bamm:dataType blub:SpatialPosition.\n";

        BammHelper bammHelper = new BammHelper();

        assertTrue(bammHelper.loadBammModel(modelString).isFailure());
    }

    @Test
    public void testBammValidationWithValidModel() {
        final String modelString = "@prefix bamm: <urn:bamm:io.openmanufacturing:meta-model:1.0.0#> .\n @prefix bamm-c: <urn:bamm:io.openmanufacturing:characteristic:1.0.0#> .\n @prefix bamm-e: <urn:bamm:io.openmanufacturing:entity:1.0.0#> .\n @prefix unit: <urn:bamm:io.openmanufacturing:unit:1.0.0#> .\n @prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n @prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n @prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n @prefix : <urn:bamm:org.eclipse.tractusx:1.0.0#> .\n \n :Movement a bamm:Aspect;\n bamm:name \"Movement\";\n bamm:preferredName \"Movement\"@en;\n bamm:description \"Aspect for movement information\"@en;\n bamm:properties (:isMoving :speedLimitWarning :position);\n bamm:operations ().\n :isMoving a bamm:Property;\n bamm:name \"isMoving\";\n bamm:preferredName \"Moving\"@en;\n bamm:description \"Flag indicating whether the asset is currently moving\"@en;\n bamm:characteristic bamm-c:Boolean.\n :speedLimitWarning a bamm:Property;\n bamm:name \"speedLimitWarning\";\n bamm:preferredName \"Speed Limit Warning\"@en;\n bamm:description \"Indicates if the speed limit is adhered to.\"@en;\n bamm:characteristic :TrafficLight.\n :position a bamm:Property;\n bamm:name \"position\";\n bamm:preferredName \"Position\"@en;\n bamm:description \"Indicates a position\"@en;\n bamm:characteristic :SpatialPositionCharacteristic.\n :TrafficLight a bamm-c:Enumeration;\n bamm:name \"TrafficLight\";\n bamm:preferredName \"Warning Level\"@en;\n bamm:description \"Represents if speed of position change is within specification (green), within tolerance (yellow), or outside specification (red).\"@en;\n bamm:dataType xsd:string;\n bamm-c:values (\"green\" \"yellow\" \"red\").\n :SpatialPosition a bamm:Entity;\n bamm:name \"SpatialPosition\";\n bamm:preferredName \"Spatial Position\"@en;\n bamm:description \"Position in space, described along three axis, with the third axis optional, if all positions are in a plane.\"@en;\n bamm:properties (:x :y :z).\n :x a bamm:Property;\n bamm:name \"x\";\n bamm:preferredName \"x\"@en;\n bamm:description \"x coordinate in space\"@en;\n bamm:characteristic :Coordinate.\n :y a bamm:Property;\n bamm:name \"y\";\n bamm:preferredName \"y\"@en;\n bamm:description \"y coordinate in space\"@en;\n bamm:characteristic :Coordinate.\n :z a bamm:Property;\n bamm:name \"z\";\n bamm:preferredName \"z\"@en;\n bamm:description \"z coordinate in space\"@en;\n bamm:characteristic :Coordinate;\n bamm:optional \"true\"^^xsd:boolean.\n :Coordinate a bamm-c:Measurement;\n bamm:name \"Coordinate\";\n bamm:preferredName \"Coordinate\"@en;\n bamm:description \"Represents a coordinate along an axis in space.\"@en;\n bamm:dataType xsd:float;\n bamm-c:unit unit:metre.\n :SpatialPositionCharacteristic a bamm-c:SingleEntity;\n bamm:name \"SpatialPositionCharacteristic\";\n bamm:preferredName \"Spatial Position Characteristic\"@en;\n bamm:description \"Represents a single position in space with optional z coordinate.\"@en;\n bamm:dataType :SpatialPosition.\n";

        BammHelper bammHelper = new BammHelper();

        Try<VersionedModel> model = bammHelper.loadBammModel(modelString);

        assertTrue(bammHelper.validateModel(model).conforms());
    }

    @Test
    public void testBammValidationWithInvalidModel() {
        final String modelString = "@prefix bamm: <urn:bamm:io.openmanufacturing:meta-model:1.0.0#> .\n @prefix bamm-c: <urn:bamm:io.openmanufacturing:characteristic:1.0.0#> .\n @prefix bamm-e: <urn:bamm:io.openmanufacturing:entity:1.0.0#> .\n @prefix unit: <urn:bamm:io.openmanufacturing:unit:1.0.0#> .\n @prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n @prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n @prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n @prefix : <urn:bamm:org.eclipse.tractusx:1.0.0#> .\n \n :Movement a bamm:Aspect;\n bamm:name \"Movement\";\n bamm:preferredName \"Movement\"@en;\n bamm:description \"Aspect for movement information\"@en;\n bamm:property (:isMoving :speedLimitWarning :position);\n bamm:operations ().\n :isMoving a bamm:Property;\n bamm:name \"isMoving\";\n bamm:preferredName \"Moving\"@en;\n bamm:description \"Flag indicating whether the asset is currently moving\"@en;\n bamm:characteristic bamm-c:Boolean.\n :speedLimitWarning a bamm:Property;\n bamm:name \"speedLimitWarning\";\n bamm:preferredName \"Speed Limit Warning\"@en;\n bamm:description \"Indicates if the speed limit is adhered to.\"@en;\n bamm:characteristic :TrafficLight.\n :position a bamm:Property;\n bamm:name \"position\";\n bamm:preferredName \"Position\"@en;\n bamm:description \"Indicates a position\"@en;\n bamm:characteristic :SpatialPositionCharacteristic.\n :TrafficLight a bamm-c:Enumeration;\n bamm:name \"TrafficLight\";\n bamm:preferredName \"Warning Level\"@en;\n bamm:description \"Represents if speed of position change is within specification (green), within tolerance (yellow), or outside specification (red).\"@en;\n bamm:dataType xsd:string;\n bamm-c:values (\"green\" \"yellow\" \"red\").\n :SpatialPosition a bamm:Entity;\n bamm:name \"SpatialPosition\";\n bamm:preferredName \"Spatial Position\"@en;\n bamm:description \"Position in space, described along three axis, with the third axis optional, if all positions are in a plane.\"@en;\n bamm:properties (:x :y :z).\n :x a bamm:Property;\n bamm:name \"x\";\n bamm:preferredName \"x\"@en;\n bamm:description \"x coordinate in space\"@en;\n bamm:characteristic :Coordinate.\n :y a bamm:Property;\n bamm:name \"y\";\n bamm:preferredName \"y\"@en;\n bamm:description \"y coordinate in space\"@en;\n bamm:characteristic :Coordinate.\n :z a bamm:Property;\n bamm:name \"z\";\n bamm:preferredName \"z\"@en;\n bamm:description \"z coordinate in space\"@en;\n bamm:characteristic :Coordinate;\n bamm:optional \"true\"^^xsd:boolean.\n :Coordinate a bamm-c:Measurement;\n bamm:name \"Coordinate\";\n bamm:preferredName \"Coordinate\"@en;\n bamm:description \"Represents a coordinate along an axis in space.\"@en;\n bamm:dataType xsd:float;\n bamm-c:unit unit:metre.\n :SpatialPositionCharacteristic a bamm-c:SingleEntity;\n bamm:name \"SpatialPositionCharacteristic\";\n bamm:preferredName \"Spatial Position Characteristic\"@en;\n bamm:description \"Represents a single position in space with optional z coordinate.\"@en;\n bamm:dataType :SpatialPosition.\n";

        BammHelper bammHelper = new BammHelper();

        Try<VersionedModel> model = bammHelper.loadBammModel(modelString);

        assertFalse(bammHelper.validateModel(model).conforms());
    }
}
