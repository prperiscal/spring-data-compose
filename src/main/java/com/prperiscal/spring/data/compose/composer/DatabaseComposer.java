package com.prperiscal.spring.data.compose.composer;

import static com.prperiscal.spring.data.compose.composer.ComposerUtils.getEntityClass;
import static com.prperiscal.spring.data.compose.composer.ComposerUtils.getGetter;
import static com.prperiscal.spring.data.compose.composer.ComposerUtils.getResource;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.prperiscal.spring.data.compose.exception.GenericComposeException;
import com.prperiscal.spring.data.compose.model.ComposeData;
import com.prperiscal.spring.data.compose.model.ComposeMetaData;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>Composer class which handles the importing data process.
 * <p>A Datasource must be configured and available for the compose process to success.
 *
 * @author <a href="mailto:prperiscal@gmail.com">Pablo Rey Periscal</a>
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class DatabaseComposer {

    private static final String FK_CHAR = "@";

    @PersistenceContext
    @NonNull
    private final EntityManager entityManager;

    @NonNull
    private final ApplicationContext appContext;

    /**
     *
     * @param testClass Test class from where the compose is executed
     * @param composeResource Json file name from where data will be read
     * @throws IOException
     */
    @Modifying
    @Transactional
    public void compose(Class<?> testClass, String composeResource) throws IOException {
        Validate.notNull(testClass, String.format("Element '%s' must not be null", "testClass"));
        URL resourcePath = getResource(testClass, composeResource);

        ObjectMapper objectMapper = new ObjectMapper();
        ComposeData composeData = objectMapper.readValue(resourcePath, ComposeData.class);

        //TODO validate composeData, each key in entities should have a metadata object associated

        importResource(composeData);
    }


    private Map<String, Object> processEntityGroup(Entry<String, List<Map<String, Object>>> groupEntry,
                                                   ComposeMetaData composeMetaData) {
        Map<String, Object> insertedEntities = Maps.newHashMap();

        for(Map<String, Object> entityData : groupEntry.getValue()) {
            String key = groupEntry.getKey() + FK_CHAR + entityData.get(composeMetaData.getId()).toString();
            insertedEntities.put(key, insertEntity(composeMetaData, entityData));
        }
        entityManager.flush();

        return insertedEntities;
    }

    private Object insertEntity(ComposeMetaData composeMetaData, Map<String, Object> entityResource) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            Object entity = objectMapper.convertValue(entityResource, getEntityClass(composeMetaData.get_class()));
            CrudRepository repository = (CrudRepository) appContext.getBean(composeMetaData.getRepository());
            return repository.save(entity);
        } catch (ClassNotFoundException e) {
            throw new GenericComposeException();
        }
    }

    private void importResource(ComposeData composeData) {
        Map<String, Object> insertedEntities = composeData.getEntities().entrySet().stream().map(
                entry -> processEntityGroup(entry, composeData.getMetadata().get(entry.getKey()))).flatMap(
                map -> map.entrySet().stream()).collect(Collectors.toMap(Entry::getKey, Entry::getValue));

        connect(composeData, insertedEntities);
    }

    private void connect(ComposeData composeData, Map<String, Object> insertedEntities) {
        composeData.getEntities().forEach(
                (key, value) -> connectEntityGroup(key, value, composeData.getMetadata().get(key), insertedEntities));

        insertedEntities.forEach((key,value) -> entityManager.refresh(value));
    }

    private void connectEntityGroup(String group, List<Map<String, Object>> entitiesData,
                                    ComposeMetaData composeMetaData, Map<String, Object> insertedEntities) {
        for(Map<String, Object> entityData : entitiesData) {
            entityData.entrySet().stream().filter(entry -> entry.getKey().startsWith(FK_CHAR)).forEach(
                    entry -> connectEntity(group, entry, entityData, composeMetaData, insertedEntities));
        }
        entityManager.flush();
    }

    private void connectEntity(String group, Entry<String, Object> entryField, Map<String, Object> entityData,
                               ComposeMetaData composeMetaData, Map<String, Object> entitiesData) {

        String parsedField = entryField.getKey().substring(1);

        Object thisEntity = entitiesData.get(group + FK_CHAR + entityData.get(composeMetaData.getId()));
        CrudRepository repository = (CrudRepository) appContext.getBean(composeMetaData.getRepository());

        Object foreigners = entryField.getValue();
        if(foreigners instanceof List) {
            for(Object foreign : (List) foreigners) {
                Object foreignEntity = entitiesData.get(foreign.toString());
                setInto(thisEntity, parsedField, foreignEntity);
                repository.save(thisEntity);
            }
        } else {
            Object foreignEntity = entitiesData.get(foreigners.toString());
            setInto(thisEntity, parsedField, foreignEntity);
            repository.save(thisEntity);
        }
    }

    private void setInto(Object entity, String fieldName, Object foreignEntity) {
        try {
            Method getter = getGetter(entity, fieldName);
            if(getter.getReturnType().isAssignableFrom(Set.class)) {
                Set<Object> ingredients = (Set<Object>) getter.invoke(entity);
                ingredients.add(foreignEntity);
                BeanUtils.setProperty(entity, fieldName, ingredients);
            } else {
                BeanUtils.setProperty(entity, fieldName, foreignEntity);
            }

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }


}
