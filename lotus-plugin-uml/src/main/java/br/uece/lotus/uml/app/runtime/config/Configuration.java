/**
 * The MIT License
 * Copyright © 2017 Davi Monteiro Barbosa
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.uece.lotus.uml.app.runtime.config;

import br.uece.lotus.Component;
import br.uece.lotus.uml.api.ds.ProjectDS;
import br.uece.lotus.uml.api.ds.StandardModeling;
import br.uece.lotus.uml.app.runtime.utils.checker.Property;
import br.uece.lotus.uml.designer.standardModeling.StandardModelingWindowImpl;


import java.util.List;

public class Configuration {
	
	public static final String FILE_EXTENSION = ".json";


	private String name;
	
	private String traceFile;
	
	private String projectFile;
	
	private Long milliseconds;
	
	private List<Property> properties;

	private ProjectDS projectDS;
	private Component parallelComponent;

	public Configuration() { }

	private Configuration(ConfigurationBuilder builder) {
		this.name = builder.name;
		this.traceFile = builder.traceFile;
		this.projectFile = builder.projectFile;
		this.milliseconds = builder.milliseconds;
		this.properties = builder.properties;
		this.projectDS = builder.projectDS;
		this.parallelComponent = builder.parallelComponent;
	}

	public void setProjectDS(ProjectDS projectDS) {
		this.projectDS = projectDS;
	}

	public void setParallelComponent(Component parallelComponent) {
		this.parallelComponent = parallelComponent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTraceFile() {
		return traceFile;
	}

	public void setTraceFile(String traceFile) {
		this.traceFile = traceFile;
	}

	public String getProjectFile() {
		return projectFile;
	}

	public void setProjectFile(String projectFile) {
		this.projectFile = projectFile;
	}

	public Long getMilliseconds() {
		return milliseconds;
	}

	public void setMilliseconds(Long milliseconds) {
		this.milliseconds = milliseconds;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public ProjectDS getProjectDS() {
		return projectDS;
	}

	public Component getParallelComponent() {
		return parallelComponent;
	}

	public static class ConfigurationBuilder {

		private Component parallelComponent;

		private String name;
		
		private String traceFile;
		
		private String projectFile;
		
		private Long milliseconds;
		
		private List<Property> properties;

		private ProjectDS projectDS;

		public ConfigurationBuilder() { }
		
		public ConfigurationBuilder name(String name) {
			this.name = name;
			return this;
		}

		public ConfigurationBuilder parallelComponent (Component parallelComponent){
			this.parallelComponent = parallelComponent;
			return this;
		}
		
		public ConfigurationBuilder traceFile(String traceFile) {
			this.traceFile = traceFile;
			return this;
		}
		
		public ConfigurationBuilder projectFile(String projectFile) {
			this.projectFile = projectFile;
			return this;
		}
		public ConfigurationBuilder project(ProjectDS projectDS){
			this.projectDS = projectDS;
			return this;
		}

		
		public ConfigurationBuilder milliseconds(Long milliseconds) {
			this.milliseconds = milliseconds;
			return this;
		}
		
		public ConfigurationBuilder properties(List<Property> properties) {
			this.properties = properties;
			return this;
		}
		
		public Configuration build() {
			return new Configuration(this);
		}



    }
	
}
