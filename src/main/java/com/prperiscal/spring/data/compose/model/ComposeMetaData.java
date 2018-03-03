package com.prperiscal.spring.data.compose.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * <p>Meta data structure to map the compose metadata.
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
