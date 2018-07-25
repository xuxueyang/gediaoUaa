package uaa.web.rest.util.excel.basel;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class ColumnWalkerImpl implements ColumnWalker {
    private static final Logger LOGGER = LoggerFactory.getLogger(ColumnWalkerImpl.class);
    protected int i = 0;

    public ColumnWalkerImpl() {
    }

    public boolean hasNext() {
        List<Field> actualFields = this.getActualFields();
        return this.i < actualFields.size();
    }

    public String next() {
        List<Field> actualFields = this.getActualFields();
        Field field = (Field)actualFields.get(this.i);
        String value = "";

        try {
            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), this.getClass());
            Method rM = pd.getReadMethod();
            value = (String)rM.invoke(this);
        } catch (IllegalAccessException var6) {
            LOGGER.error(var6.getMessage(), var6);
        } catch (IllegalArgumentException var7) {
            LOGGER.error(var7.getMessage(), var7);
        } catch (InvocationTargetException var8) {
            LOGGER.error(var8.getMessage(), var8);
        } catch (IntrospectionException var9) {
            LOGGER.error(var9.getMessage(), var9);
        }

        ++this.i;
        return value;
    }

    private List<Field> getActualFields() {
        Field[] fields = this.getClass().getDeclaredFields();
        List<Field> actualFields = new ArrayList();

        for(int i = 0; i < fields.length; ++i) {
            if (fields[i].getAnnotation(ExcelColumn.class) != null) {
                actualFields.add(fields[i]);
            }
        }

        return actualFields;
    }
}
