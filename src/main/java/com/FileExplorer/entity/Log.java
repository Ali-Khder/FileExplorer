package com.FileExplorer.entity;

import jakarta.persistence.*;

@Entity
@Table
public class Log {
    @Id
    @SequenceGenerator(
            name = "log_sequence",
            sequenceName = "log_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "log_sequence"
    )
    private Long id;
    private String method;
    private String uri;
    private Long time;

    @Column(name = "request_body",
            columnDefinition = "TEXT")
    private String requestBody;

    @Column(name = "response_body",
            columnDefinition = "TEXT")
    private String responseBody;

    @Column(name = "status_code")
    private int statusCode;

    public Log() {
    }

    public Log(String method,
               String uri,
               String requestBody,
               int statusCode,
               String responseBody,
               Long time) {
        this.method = method;
        this.uri = uri;
        this.time = time;
        this.requestBody = requestBody;
        this.responseBody = responseBody;
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return "Log{" +
                "id=" + id +
                ", method='" + method + '\'' +
                ", uri='" + uri + '\'' +
                ", time=" + time +
                ", requestBody='" + requestBody + '\'' +
                ", responseBody='" + responseBody + '\'' +
                ", statusCode=" + statusCode +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
