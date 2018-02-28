package com.prperiscal.spring.data.compose.model;

import java.util.List;
import java.util.Map;

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
public class ComposeData {

    @NonNull
    private Map<String,ComposeMetaData> metadata;

    @NonNull
    private Map<String, List<Map<String,Object>>> entities;
}
