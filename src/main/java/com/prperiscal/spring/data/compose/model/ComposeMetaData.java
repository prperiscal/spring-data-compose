package com.prperiscal.spring.data.compose.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author Pablo Rey Periscal
 */
@Data
@NoArgsConstructor
public class ComposeMetaData {

    @NonNull
    private String _class;

    @NonNull
    private String repository;

    @NonNull
    private String id;

}
