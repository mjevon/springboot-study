package cn.leon.harbor.model;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangxueying on 2016/11/14.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "project", "repository" })
public class Search {

	@JsonProperty("project")
	private List<Object> project = new ArrayList<Object>();
	@JsonProperty("repository")
	private List<Repository> repository = new ArrayList<Repository>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 *
	 * @return The project
	 */
	@JsonProperty("project")
	public List<Object> getProject() {
		return project;
	}

	/**
	 *
	 * @param project
	 *            The project
	 */
	@JsonProperty("project")
	public void setProject(List<Object> project) {
		this.project = project;
	}

	/**
	 *
	 * @return The repository
	 */
	@JsonProperty("repository")
	public List<Repository> getRepository() {
		return repository;
	}

	/**
	 *
	 * @param repository
	 *            The repository
	 */
	@JsonProperty("repository")
	public void setRepository(List<Repository> repository) {
		this.repository = repository;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	@Override
	public String toString() {
		return "Search{" + "project=" + project + ", repository=" + repository + ", additionalProperties="
				+ additionalProperties + '}';
	}
}
