package models.performers;

import com.fasterxml.jackson.annotation.*;

public class Meta {
    private Long currentPage;
    private Long from;
    private Long lastPage;
    private Link[] links;
    private String path;
    private Long perPage;
    private Long to;
    private Long total;
    private String title;
    private Long apiID;

    @JsonProperty("current_page")
    public Long getCurrentPage() { return currentPage; }
    @JsonProperty("current_page")
    public void setCurrentPage(Long value) { this.currentPage = value; }

    @JsonProperty("from")
    public Long getFrom() { return from; }
    @JsonProperty("from")
    public void setFrom(Long value) { this.from = value; }

    @JsonProperty("last_page")
    public Long getLastPage() { return lastPage; }
    @JsonProperty("last_page")
    public void setLastPage(Long value) { this.lastPage = value; }

    @JsonProperty("links")
    public Link[] getLinks() { return links; }
    @JsonProperty("links")
    public void setLinks(Link[] value) { this.links = value; }

    @JsonProperty("path")
    public String getPath() { return path; }
    @JsonProperty("path")
    public void setPath(String value) { this.path = value; }

    @JsonProperty("per_page")
    public Long getPerPage() { return perPage; }
    @JsonProperty("per_page")
    public void setPerPage(Long value) { this.perPage = value; }

    @JsonProperty("to")
    public Long getTo() { return to; }
    @JsonProperty("to")
    public void setTo(Long value) { this.to = value; }

    @JsonProperty("total")
    public Long getTotal() { return total; }
    @JsonProperty("total")
    public void setTotal(Long value) { this.total = value; }

    @JsonProperty("title")
    public String getTitle() { return title; }
    @JsonProperty("title")
    public void setTitle(String value) { this.title = value; }

    @JsonProperty("apiId")
    public Long getAPIID() { return apiID; }
    @JsonProperty("apiId")
    public void setAPIID(Long value) { this.apiID = value; }
}
