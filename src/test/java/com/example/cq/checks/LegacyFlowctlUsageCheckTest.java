package com.example.cq.checks;

import com.cachequality.api.check.ObjectScriptCheck;
import com.cachequality.helpers.ViolationList;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class LegacyFlowctlUsageCheckTest
    extends CqExampleCheckTest
{

    protected LegacyFlowctlUsageCheckTest()
        throws IOException, URISyntaxException
    {
        super(LegacyFlowctlUsageCheck.KEY);
    }

    @Override
    protected ObjectScriptCheck getCheck()
    {
        return new LegacyFlowctlUsageCheck();
    }

    @Override
    protected Iterator<Object[]> checkData()
    {
        final List<Object[]> list = new ArrayList<>();

        String resourceName;
        ViolationList violationList;

        resourceName = "If.cls";
        violationList = ViolationList.create()
            .add(5, LegacyFlowctlUsageCheck.MESSAGE)
            .build();
        list.add(new Object[] { resourceName, violationList });

        resourceName = "Else.cls";
        violationList = ViolationList.create()
            .add(5, LegacyFlowctlUsageCheck.MESSAGE)
            .build();
        list.add(new Object[] { resourceName, violationList });

        resourceName = "For.cls";
        violationList = ViolationList.create()
            .add(5, LegacyFlowctlUsageCheck.MESSAGE)
            .build();
        list.add(new Object[] { resourceName, violationList });

        return list.iterator();
    }
}
