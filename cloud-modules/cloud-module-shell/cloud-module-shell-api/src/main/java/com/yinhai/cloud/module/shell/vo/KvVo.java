package com.yinhai.cloud.module.shell.vo;

/**
 * @author jianglw
 */
public class KvVo {
    private String key;
    private String value;

    public KvVo() {
    }

    private KvVo(final String key, final String value) {
        this.key = key;
        this.value = value;
    }

    public static KvVo getKvVo(final String key, final String value) {
        return new KvVo(key, value);
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }
}
