package org.jumpmind.symmetric.io.data.transform;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jumpmind.db.platform.IDatabasePlatform;
import org.jumpmind.extension.IBuiltInExtensionPoint;
import org.jumpmind.symmetric.io.data.DataContext;
import org.jumpmind.symmetric.io.data.DataEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IsNullTransform implements ISingleNewAndOldValueColumnTransform, IBuiltInExtensionPoint {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public static final String NAME = "isNull";

    public String getName() {
        return NAME;
    }
    
    @Override
    public NewAndOldValue transform(IDatabasePlatform platform,
            DataContext context,
            TransformColumn column, TransformedData data, Map<String, String> sourceValues,
            String newValue, String oldValue) throws IgnoreColumnException, IgnoreRowException {

        String value = data.getSourceDmlType().equals(DataEventType.DELETE) ? oldValue : newValue;
        
        if (value == null) {
            String expression = column.getTransformExpression();
            if (StringUtils.isNotEmpty(expression)) {
                value = expression;
            }
        }

        return new NewAndOldValue(column, data, value);
    }

    @Override
    public boolean isExtractColumnTransform() {
        return true;
    }

    @Override
    public boolean isLoadColumnTransform() {
        return true;
    }

}
