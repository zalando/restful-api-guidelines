package de.zalando.zally;

import java.beans.PropertyEditorSupport;
import org.springframework.util.StringUtils;

public class ViolationTypeBinder extends PropertyEditorSupport {

    @Override
    public void setAsText(final String text) throws IllegalArgumentException {
        final ViolationType value = StringUtils.hasText(text) ? ViolationType.valueOf(text.toUpperCase()) : null;
        setValue(value);
    }
}
