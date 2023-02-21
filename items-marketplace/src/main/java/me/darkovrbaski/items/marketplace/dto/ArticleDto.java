package me.darkovrbaski.items.marketplace.dto;

import jakarta.validation.constraints.NotBlank;

public record ArticleDto(

    Long id,

    @NotBlank(message = "Name is mandatory")
    String name,

    String description,

    String image

) {

}
