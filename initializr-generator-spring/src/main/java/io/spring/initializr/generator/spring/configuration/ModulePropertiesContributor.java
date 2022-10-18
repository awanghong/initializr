/*
 * Copyright 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.initializr.generator.spring.configuration;

import java.io.IOException;
import java.nio.file.Path;

import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.contributor.ProjectContributor;
import io.spring.initializr.generator.project.contributor.SingleResourceProjectContributor;
import io.spring.initializr.generator.spring.util.MavenModuleUtil;

/**
 * A {@link ProjectContributor} that creates module-specific directories when a
 * module-related project is detected.
 *
 * @author wang hong
 */
public class ModulePropertiesContributor extends SingleResourceProjectContributor {

	private final ProjectDescription description;

	public ModulePropertiesContributor(ProjectDescription description) {
		super("src/main/resources/application.properties", "classpath:configuration/application.properties");
		this.description = description;
	}

	@Override
	public void contribute(Path projectRoot) throws IOException {
		projectRoot = MavenModuleUtil.obtainMavenModulePath(this.description.getArchitecture(),
				this.description.getName(), projectRoot);
		super.contribute(projectRoot);
	}

}
