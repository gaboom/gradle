/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.api.publish.internal;

import org.gradle.api.UnknownDomainObjectException;
import org.gradle.api.internal.DefaultNamedDomainObjectSet;
import org.gradle.api.publish.Publication;
import org.gradle.api.publish.PublicationContainer;
import org.gradle.api.publish.UnknownPublicationException;
import org.gradle.internal.reflect.Instantiator;

public class DefaultPublicationContainer extends DefaultNamedDomainObjectSet<Publication> implements PublicationContainer {

    public DefaultPublicationContainer(Instantiator instantiator) {
        super(Publication.class, instantiator);
    }

    @Override
    protected UnknownDomainObjectException createNotFoundException(String name) {
        return new UnknownPublicationException(String.format("Configuration with name '%s' not found.", name));
    }
}
