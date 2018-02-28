package com.prperiscal.spring.data.compose.model;

import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author Pablo Rey Periscal
 */
@Data
@NoArgsConstructor
public class ComposeData {

    @NonNull
    private Map<String,ComposeMetaData> metadata;

    @NonNull
    private Map<String, List<Map<String,Object>>> entities;
}
