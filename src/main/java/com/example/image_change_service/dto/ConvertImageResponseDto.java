package com.example.image_change_service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConvertImageResponseDto {
    private String filename;
    private String type;
    private String pos;
}
