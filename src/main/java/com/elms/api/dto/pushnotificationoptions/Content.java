package com.elms.api.dto.pushnotificationoptions;

public class Content {
    private String en;

    public Content(String en) {
        this.en = en;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    @Override
    public String toString() {
        return "Content{" +
                "en='" + en + '\'' +
                '}';
    }
}
