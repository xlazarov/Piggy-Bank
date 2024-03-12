package ui;

import java.util.ResourceBundle;

final class I18N {

    private final ResourceBundle bundle;
    private final String prefix;

    public I18N(Class<?> clazz) {
        var packagePath = clazz.getPackageName().replace(".", "/") + '/';
        bundle = ResourceBundle.getBundle(packagePath + "i18n_sk");
        prefix = clazz.getSimpleName() + ".";
    }

    String getString(String key) {
        return bundle.getString(prefix + key);
    }

    <E extends Enum<E>> String getString(E key) {
        return bundle.getString(key.getClass().getSimpleName() + "." + key.name());
    }
}
