package com.spring.eventmanagement.utils;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchParameter {
    @NotNull
    private String value;
    @NotNull
    private Boolean regex;


}
