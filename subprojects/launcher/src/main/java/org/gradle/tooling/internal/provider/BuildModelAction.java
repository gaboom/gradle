/*
 * Copyright 2011 the original author or authors.
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
package org.gradle.tooling.internal.provider;

import org.gradle.api.Action;
import org.gradle.api.internal.GradleInternal;
import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.initialization.*;
import org.gradle.tooling.provider.model.ToolingModelBuilder;
import org.gradle.tooling.provider.model.ToolingModelBuilderRegistry;

import java.io.Serializable;

public class BuildModelAction implements BuildAction<SerializedPayload>, Serializable {
    private final boolean runTasks;
    private final String modelName;
    private SerializedPayload model;

    public BuildModelAction(String modelName, boolean runTasks) {
        this.modelName = modelName;
        this.runTasks = runTasks;
    }

    public SerializedPayload run(BuildController buildController) {
        DefaultGradleLauncher launcher = (DefaultGradleLauncher) buildController.getLauncher();

        if (runTasks) {
            launcher.addListener(new TasksCompletionListener() {
                public void onTasksFinished(GradleInternal gradle) {
                    ToolingModelBuilder builder = getToolingModelBuilderRegistry(gradle).getBuilder(modelName);
                    Object result = builder.buildAll(modelName, gradle.getDefaultProject());
                    model = getPayloadSerializer(gradle).serialize(result);
                }
            });
            buildController.run();
        } else {
            launcher.addListener(new ModelConfigurationListener() {
                public void onConfigure(GradleInternal gradle) {
                    ensureAllProjectsEvaluated(gradle);
                    ToolingModelBuilder builder = getToolingModelBuilderRegistry(gradle).getBuilder(modelName);
                    Object result = builder.buildAll(modelName, gradle.getDefaultProject());
                    model = getPayloadSerializer(gradle).serialize(result);
                }
            });
            buildController.configure();
        }

        return model;
    }

    private ToolingModelBuilderRegistry getToolingModelBuilderRegistry(GradleInternal gradle) {
        return gradle.getDefaultProject().getServices().get(ToolingModelBuilderRegistry.class);
    }

    private PayloadSerializer getPayloadSerializer(GradleInternal gradle) {
        return gradle.getDefaultProject().getServices().get(PayloadSerializer.class);
    }

    private void ensureAllProjectsEvaluated(GradleInternal gradle) {
        gradle.getRootProject().allprojects((Action) new Action<ProjectInternal>() {
            public void execute(ProjectInternal projectInternal) {
                projectInternal.evaluate();
            }
        });
    }
}
