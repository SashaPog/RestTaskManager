package com.example.resttaskmanager.dto;

import com.example.resttaskmanager.model.Priority;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequest {
    @Column(name = "name", nullable = false)
    @JsonProperty("name")
    private String name;

    @Column(name = "priority", nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonProperty("priority")
    private Priority priority;

    @JsonProperty("state")
    private String stateName;
}
