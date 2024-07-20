package com.example.resttaskmanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class ToDoRequest {

    @NotBlank
    @JsonProperty("title")
    private String title;


    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    public ToDoRequest(String title, LocalDateTime createdAt) {
        this.title = title;
        this.createdAt = createdAt;
    }
}
