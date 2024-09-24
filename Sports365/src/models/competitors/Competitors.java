package models.competitors;

import com.fasterxml.jackson.annotation.*;

public class Competitors {
    private Datum[] data;
    private Links links;
    private Meta meta;
    private Boolean success;

    @JsonProperty("data")
    public Datum[] getData() { return data; }
    @JsonProperty("data")
    public void setData(Datum[] value) { this.data = value; }

    @JsonProperty("links")
    public Links getLinks() { return links; }
    @JsonProperty("links")
    public void setLinks(Links value) { this.links = value; }

    @JsonProperty("meta")
    public Meta getMeta() { return meta; }
    @JsonProperty("meta")
    public void setMeta(Meta value) { this.meta = value; }

    @JsonProperty("success")
    public Boolean getSuccess() { return success; }
    @JsonProperty("success")
    public void setSuccess(Boolean value) { this.success = value; }
}
