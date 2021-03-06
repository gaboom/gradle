/*
 * Copyright 2013 the original author or authors.
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

package org.gradle.nativecode.base.tasks
import org.gradle.api.Incubating
import org.gradle.nativecode.base.internal.LinkerSpec
import org.gradle.nativecode.base.internal.SharedLibraryLinkerSpec

/**
 * Links a binary shared library from object files and imported libraries.
 */
@Incubating
class LinkSharedLibrary extends AbstractLinkTask {
    @Override
    protected LinkerSpec createLinkerSpec() {
        return new Spec()
    }

    private static class Spec implements SharedLibraryLinkerSpec {
        Iterable<File> libs;
        Iterable<File> source;
        File outputFile;
        File tempDir;
        Iterable<String> args = new ArrayList<String>();
        String installName;

        public String getInstallName() {
            return installName == null ? getOutputFile().getName() : installName;
        }
    }
}
