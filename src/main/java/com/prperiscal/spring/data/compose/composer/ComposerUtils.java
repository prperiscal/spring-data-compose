package com.prperiscal.spring.data.compose.composer;

import static lombok.AccessLevel.PRIVATE;

import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.net.URL;

import com.prperiscal.spring.data.compose.exception.FileInvalidException;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

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
     * <p>Method to retrieve a compose resource. The resource name can be given with full path name or only the simple name.
     * In this case the resource will be fetched form the package level of the testClass.
     *
     * @param testClass       The test Class that wants to compose data
     * @param composeResource The resource name
     *
     * @return {@link URL} of the resource.
     * @throws FileNotFoundException if the resource is not available or accessible
     * @since 1.0.0
     */
    static URL getResource(Class<?> testClass, String composeResource) throws FileNotFoundException {
        if(!composeResource.endsWith("." + FILE_EXTENSION_JSON)) {
            throw new FileInvalidException(composeResource);
        }

        Long dots = composeResource.chars().filter(character -> character == '.').count();
        if(dots > 1) {
            throw new FileInvalidException(composeResource);
        }

        String path;
        if(composeResource.contains("/")) {
            path = composeResource.startsWith("/") ? composeResource : "/".concat(composeResource);
        } else {
            String packagePath = testClass.getPackage().getName();
            path = getValidPackagePath(packagePath).concat(composeResource);
        }

        return testClass.getResource(path);
    }

    /**
     * <p>Returns a {@code Method} object that reflects the specified get public member method of the class or interface
     * represented by the given {@code Class} object.
     *
     * @param object    Object from where the Getter method should be retrieved
     * @param fieldName Name of the parameter for the getter
     *
     * @return the {@code Method} object that matches the specified {@code name} and {@code parameterTypes}
     * @throws NoSuchMethodException if a matching method is not found or if the name is "&lt;init&gt;"or "&lt;clinit&gt;".
     * @throws NullPointerException  if {@code name} is {@code null}
     * @throws SecurityException     If a security manager, <i>s</i>, is present and the caller's class loader is not the
     *                               same as or an ancestor of the class loader for the current class and invocation of {@link SecurityManager#checkPackageAccess s.checkPackageAccess()}
     *                               denies access to the package of this class.
     * @since 1.0.0
     */
    static Method getGetter(Object object, String fieldName) throws NoSuchMethodException {
        return object.getClass().getMethod("get" + StringUtils.capitalize(fieldName));
    }

    /**
     * <p>Convert the given packageName to a valid package path name.
     *
     * @param packagePath The package name (with "." as separators)
     *
     * @return A valid package path name (with "/" as separator)
     * @since 1.0.0
     */
    private static String getValidPackagePath(String packagePath) {
        String packageValidName = StringUtils.replace(packagePath, ".", "/");
        return StringUtils.join("/", packageValidName, "/");
    }

}
