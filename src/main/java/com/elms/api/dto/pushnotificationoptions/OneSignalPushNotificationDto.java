package com.elms.api.dto.pushnotificationoptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class OneSignalPushNotificationDto {

    @JsonProperty("app_id")
    private String appId;
    @JsonProperty("include_player_ids")
    private List<String> includePlayerIds;
    private AdditionalData data;
    private Content headings;
    private Content contents;
    @JsonProperty("large_icon")
    private String largeIcon;

    public OneSignalPushNotificationDto(List<String> includePlayerIds, String title, String body, AdditionalData data) {
        this.includePlayerIds = includePlayerIds;
        this.headings = new Content(title);
        this.contents = new Content(body);
        this.data = data;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public List<String> getIncludePlayerIds() {
        return includePlayerIds;
    }

    public void setIncludePlayerIds(List<String> includePlayerIds) {
        this.includePlayerIds = includePlayerIds;
    }

    public AdditionalData getData() {
        return data;
    }

    public void setData(AdditionalData data) {
        this.data = data;
    }

    public Content getHeadings() {
        return headings;
    }

    public void setHeadings(Content headings) {
        this.headings = headings;
    }

    public Content getContents() {
        return contents;
    }

    public void setContents(Content contents) {
        this.contents = contents;
    }

    public String getLargeIcon() {
        return largeIcon;
    }

    public void setLargeIcon(String largeIcon) {
        this.largeIcon = largeIcon;
    }

    @Override
    public String toString() {
        return "PushNotificationWithOptionalData{" +
                "app_id='" + appId + '\'' +
                ", include_player_ids=" + includePlayerIds +
                ", data=" + data +
                ", headings=" + headings +
                ", contents=" + contents +
                ", large_icon='" + largeIcon + '\'' +
                '}';
    }


}
