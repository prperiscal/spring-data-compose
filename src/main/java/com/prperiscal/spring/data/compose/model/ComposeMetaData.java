package com.prperiscal.spring.data.compose.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 *
 *
 * @author <a href="mailto:prperiscal@gmail.com">Pablo Rey Periscal</a>
 * @since 1.0.0
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
