/*
 * Copyright 2012-2022 the original author or authors.
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

package io.spring.initializr.metadata;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A {@link ServiceCapability} listing the available democodes defined as a
 * {@link ServiceCapabilityType#HIERARCHICAL_MULTI_SELECT} capability.
 *
 * @author Wang Hong
 */
public class DemosCapability extends ServiceCapability<List<DemoMeta>> {

	final List<DemoMeta> content = new ArrayList<>();

	@JsonIgnore
	private final Map<String, DemoMeta> indexedDemos = new LinkedHashMap<>();

	public DemosCapability() {
		super("democodes", ServiceCapabilityType.HIERARCHICAL_MULTI_SELECT, "使用示例", "使用示例代码");
	}

	@Override
	public List<DemoMeta> getContent() {
		return this.content;
	}

	@Override
	public void merge(List<DemoMeta> otherContent) {
		otherContent.forEach((group) -> {
			if (this.content.stream()
					.noneMatch((it) -> group.getName() != null && group.getName().equals(it.getName()))) {
				this.content.add(group);
			}
		});
		index();
	}

	public void validate() {
		index();
	}

	private void index() {
		this.indexedDemos.clear();
		this.content.forEach((group) -> indexDemo(group.getId(), group));
	}

	private void indexDemo(String id, DemoMeta demoMeta) {
		DemoMeta existing = this.indexedDemos.get(id);
		if (existing != null) {
			throw new IllegalArgumentException(
					"Could not register " + demoMeta + " another demo " + "has also the '" + id + "' id " + existing);
		}
		this.indexedDemos.put(id, demoMeta);
	}

}
