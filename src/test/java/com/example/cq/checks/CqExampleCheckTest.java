package com.example.cq.checks;

import com.cachequality.api.CacheQualityRulesDefinition;
import com.cachequality.checks.CheckTest;
import com.example.cq.CqExampleRules;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;

@Test
public abstract class CqExampleCheckTest
    extends CheckTest
{
    private static final CacheQualityRulesDefinition DEF
        = new CqExampleRules();

    protected CqExampleCheckTest(final String key)
        throws IOException, URISyntaxException
    {
        super(DEF.repositoryKey(), DEF.checkClasses(), key);
    }
}
