/*
 * Copyright 2012-2019 the original author or authors.
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

package io.spring.initializr.generator.spring.build.maven;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

import io.spring.initializr.generator.buildsystem.BillOfMaterials;
import io.spring.initializr.generator.buildsystem.BuildWriter;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildWriter;
import io.spring.initializr.generator.io.IndentingWriter;
import io.spring.initializr.generator.io.IndentingWriterFactory;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.contributor.ProjectContributor;
import io.spring.initializr.generator.spring.util.MavenModuleUtil;
import io.spring.initializr.generator.version.VersionReference;
import io.spring.initializr.metadata.InitializrMetadata;

/**
 * {@link ProjectContributor} to contribute the files for a {@link MavenBuild}.
 *
 * @author Andy Wilkinson
 * @author Stephane Nicoll
 */
public class MavenBuildProjectContributor implements BuildWriter, ProjectContributor {

	private final MavenBuild build;

	private final IndentingWriterFactory indentingWriterFactory;

	private final MavenBuildWriter buildWriter;

	private final ProjectDescription description;

	private final InitializrMetadata metadata;

	public MavenBuildProjectContributor(ProjectDescription description, InitializrMetadata metadata, MavenBuild build,
			IndentingWriterFactory indentingWriterFactory) {
		this.description = description;
		this.metadata = metadata;
		this.build = build;
		this.indentingWriterFactory = indentingWriterFactory;
		this.buildWriter = new MavenBuildWriter();
	}

	@Override
	public void contribute(Path projectRoot) throws IOException {
		String architecture = obtainArchitecture();
		if (MavenModuleUtil.NONE_ARCHITECTURE.equals(architecture)) {
			singleModulePomCreateAndFill(projectRoot);
		}
		else {
			multiModulePomCreateAndFill(projectRoot);
		}
	}

	private void multiModulePomCreateAndFill(Path projectRoot) throws IOException {
		String api = this.description.getName() + MavenModuleUtil.API;
		createModule(api, projectRoot);
		String common = this.description.getName() + MavenModuleUtil.COMMON;
		createModule(common, projectRoot);
		String core = this.description.getName() + MavenModuleUtil.CORE;
		createModule(core, projectRoot, api, common);
		String web = this.description.getName() + MavenModuleUtil.WEB;
		createModule(web, projectRoot, core);
		createMainModule(projectRoot, api, common, core, web);
	}

	private void createMainModule(Path projectRoot, String... dependModule) throws IOException {
		Path pomFile = Files.createFile(projectRoot.resolve("pom.xml"));
		writeMainBuild(Files.newBufferedWriter(pomFile), dependModule);
	}

	public void writeMainBuild(Writer out, String... dependModule) throws IOException {
		try (IndentingWriter writer = this.indentingWriterFactory.createIndentingWriter("maven", out)) {
			for (String depend : dependModule) {
				this.build.modules().module(depend);
				this.build.boms().add(depend, BillOfMaterials.withCoordinates(this.description.getPackageName(), depend)
						.version(VersionReference.ofValue("0.0.1-SNAPSHOT")));
			}
			this.buildWriter.writeTo(writer, this.build);
		}
	}

	private void singleModulePomCreateAndFill(Path projectRoot) throws IOException {
		Path pomFile = Files.createFile(projectRoot.resolve("pom.xml"));
		writeBuild(Files.newBufferedWriter(pomFile));
	}

	private String obtainArchitecture() {
		String architecture = this.description.getArchitecture();
		String aDefault = this.metadata.getArchitectures().getDefault().getId();
		architecture = (architecture != null) ? architecture : aDefault;
		architecture = (architecture != null) ? architecture : MavenModuleUtil.MVC_ARCHITECTURE;
		return architecture;
	}

	@Override
	public void writeBuild(Writer out) throws IOException {
		try (IndentingWriter writer = this.indentingWriterFactory.createIndentingWriter("maven", out)) {
			this.buildWriter.writeTo(writer, this.build);
		}
	}

	private void createModule(String moduleName, Path projectRoot, String... dependModule) throws IOException {
		Path changelogDirectory = projectRoot.resolve(moduleName);
		Files.createDirectories(changelogDirectory);
		Path output = projectRoot.resolve(moduleName + "/pom.xml");
		if (!Files.exists(output)) {
			Files.createDirectories(output.getParent());
			Files.createFile(output);
		}
		writeMultiModuleBuild(moduleName, Files.newBufferedWriter(output), dependModule);
	}

	public void writeMultiModuleBuild(String moduleName, Writer out, String... dependModule) throws IOException {
		try (IndentingWriter writer = this.indentingWriterFactory.createIndentingWriter("maven", out)) {
			MavenBuild newMavenBuild = this.build.modules().obtainModuleBuild(moduleName);
			newMavenBuild.settings().artifact(moduleName);
			newMavenBuild.settings().version(this.build.getSettings().getVersion());
			newMavenBuild.settings().parent(this.build.getSettings().getGroup(), this.build.getSettings().getArtifact(),
					this.build.getSettings().getVersion(), "../pom.xml");
			if (dependModule != null) {
				for (String singleModule : dependModule) {
					Dependency original = Dependency.withCoordinates(this.build.getSettings().getGroup(), singleModule)
							.build();
					newMavenBuild.dependencies().add(singleModule, Dependency.from(original));
				}
			}
			this.buildWriter.writeTo(writer, newMavenBuild);
		}
	}

}
