package com.prperiscal.spring.data.compose.composer;

import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.lang3.StringUtils.replace;

import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.net.URL;

import com.prperiscal.spring.data.compose.exception.FileInvalidException;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

/**
 * <p>Util class for composing data.
 *
 * @author <a href="mailto:prperiscal@gmail.com">Pablo Rey Periscal</a>
 * @since 1.0.0
 */
@NoArgsConstructor(access = PRIVATE)
final class ComposerUtils {

    private static final String FILE_EXTENSION_JSON = "json";

    /**
     * <p>Returns the {@code Class} object associated with the class or interface with the given string name.
     *
     * @param className the fully qualified name of the desired class.
     *
     * @return the {@code Class} object for the class with the specified name.
     * @throws ClassNotFoundException if the class cannot be located
     * @since 1.0.0
     */
    static Class<?> getEntityClass(String className) throws ClassNotFoundException {
        return Class.forName(className);
    }

    /**
     * @param testClass
     * @param composeResource
     *
     * @return
     * @throws FileNotFoundException
     * @since 1.0.0
     */
    static URL getResource(Class<?> testClass, String composeResource) throws FileNotFoundException {
        Long dots = composeResource.chars().filter(character -> character == '.').count();
        String packagePath = testClass.getPackage().getName();
        String path;

        if(dots == 1) {
            if(composeResource.endsWith("." + FILE_EXTENSION_JSON)) {
                path = getValidPackagePath(packagePath).concat(composeResource);
            } else {
                throw new FileInvalidException(composeResource);
            }
        } else if(dots > 1) {
            path = replace(composeResource, ".", "/");
        } else {
            path = getValidPackagePath(packagePath).concat(composeResource).concat(FILE_EXTENSION_JSON);
        }

        return testClass.getResource(path);
    }

    /**
     * @param packagePath
     *
     * @return
     * @since 1.0.0
     */
    static String getValidPackagePath(String packagePath) {
        return "/".concat(replace(packagePath, ".", "/").concat("/"));
    }

    /**
     * @param object
     * @param fieldName
     *
     * @return
     * @throws NoSuchMethodException
     * @since 1.0.0
     */
    static Method getGetter(Object object, String fieldName) throws NoSuchMethodException {
        return object.getClass().getMethod("get" + StringUtils.capitalize(fieldName));
    }

}
