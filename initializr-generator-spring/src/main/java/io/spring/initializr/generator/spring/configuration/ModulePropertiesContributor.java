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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.spring.initializr.generator.io.IndentingWriter;
import io.spring.initializr.generator.io.IndentingWriterFactory;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.PropertiesContainer;
import io.spring.initializr.generator.project.contributor.ProjectContributor;
import io.spring.initializr.generator.spring.code.PropertiesCustomizer;
import io.spring.initializr.generator.spring.util.LambdaSafe;
import io.spring.initializr.generator.spring.util.MavenModuleUtil;

import org.springframework.beans.factory.ObjectProvider;

/**
 * A {@link ProjectContributor} that creates module-specific directories when a
 * module-related project is detected.
 *
 * @author wang hong
 */
public class ModulePropertiesContributor implements ProjectContributor {

	private final ProjectDescription description;

	private final ObjectProvider<PropertiesCustomizer<?>> propertiesCustomizers;

	private final IndentingWriterFactory indentingWriterFactory;

	private final String EQUAL = "=";

	private final String relativePath = "src/main/resources/application.properties";

	public ModulePropertiesContributor(ProjectDescription description,
			ObjectProvider<PropertiesCustomizer<?>> propertiesCustomizers,
			IndentingWriterFactory indentingWriterFactory) {
		this.description = description;
		this.propertiesCustomizers = propertiesCustomizers;
		this.indentingWriterFactory = indentingWriterFactory;
	}

	@Override
	public void contribute(Path projectRoot) throws IOException {
		projectRoot = MavenModuleUtil.obtainMavenModulePath(this.description.getArchitecture(),
				this.description.getName(), projectRoot);
		Path output = projectRoot.resolve(this.relativePath);
		if (!Files.exists(output)) {
			Files.createDirectories(output.getParent());
			Files.createFile(output);
		}
		PropertiesContainer propertiesContainer = new PropertiesContainer();
		customizeProperties(propertiesContainer);
		writeTo(propertiesContainer, output);
	}

	@SuppressWarnings("unchecked")
	private void customizeProperties(PropertiesContainer propertiesContainer) {
		List<PropertiesCustomizer<?>> customizers = this.propertiesCustomizers.orderedStream()
				.collect(Collectors.toList());
		LambdaSafe.callbacks(PropertiesCustomizer.class, customizers, propertiesContainer)
				.invoke((customizer) -> customizer.customize(propertiesContainer));
	}

	private void writeTo(PropertiesContainer propertiesContainer, Path output) throws IOException {
		if (propertiesContainer.isEmpty()) {
			return;
		}
		Map<String, String> properties = propertiesContainer.values();
		try (IndentingWriter writer = this.indentingWriterFactory.createIndentingWriter("java",
				Files.newBufferedWriter(output))) {
			properties.entrySet().forEach((entry) -> writer.println(entry.getKey() + this.EQUAL + entry.getValue()));
		}
	}

}
