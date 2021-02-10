package io.riskscanner.commons.constants;

public enum CsRequestBodyType {


    KV("KeyValue"), FORM_DATA("Form Data"), RAW("Raw");

    private String value;

    CsRequestBodyType(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
