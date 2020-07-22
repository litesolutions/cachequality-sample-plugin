package com.example.cq.checks;

import com.objectscriptquality.api.check.ObjectScriptCheck;
import com.objectscriptquality.helpers.ViolationList;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class VariableNameLengthCheckTest
    extends CqExampleCheckTest
{

    protected VariableNameLengthCheckTest()
        throws IOException, URISyntaxException
    {
        super(VariableNameLengthCheck.KEY);
    }

    @Override
    protected ObjectScriptCheck getCheck()
    {
        return new VariableNameLengthCheck();
    }

    @Override
    protected Iterator<Object[]> checkData()
    {
        final List<Object[]> list = new ArrayList<>();

        String resourceName;
        ViolationList violationList;

        resourceName = "C1.cls";
        violationList = ViolationList.create()
            .add(5, VariableNameLengthCheck.MESSAGE)
            .build();
        list.add(new Object[] { resourceName, violationList });

      
        return list.iterator();
    }
}
