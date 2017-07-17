package com.developmentontheedge.testbe5app;

import com.developmentontheedge.be5.operation.Operation;
import com.developmentontheedge.be5.operation.OperationContext;
import com.developmentontheedge.be5.operation.OperationSupport;
import com.developmentontheedge.beans.DynamicProperty;
import java.util.Map;


public class TestOperation extends OperationSupport implements Operation
{

    @Override
    public Object getParameters(Map<String, String> presetValues) throws Exception
    {
        parameters.add(new DynamicProperty("name", "Name", String.class,
                presetValues.getOrDefault("name", "")));

        parameters.add(new DynamicProperty("value", "Value", Integer.class,
                presetValues.getOrDefault("value", "1")));

        return parameters;
    }

    @Override
    public void invoke(Object parameters, OperationContext context) throws Exception
    {
        db.insert("insert into testtable (name, value) VALUES (?, ?)",
                this.parameters.getProperty("name").getValue(), this.parameters.getProperty("value").getValue());
    }

}
