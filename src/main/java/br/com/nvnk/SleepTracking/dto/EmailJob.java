package br.com.nvnk.SleepTracking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailJob implements Serializable {
    public enum Type { VERIFICATION, PASSWORD_RESET, EMAIL_CHANGE, GENERIC }

    private String to;
    private String subject;
    private String body;
    private Type type;
    private String userId;
    private Map<String, Object> meta;
}
