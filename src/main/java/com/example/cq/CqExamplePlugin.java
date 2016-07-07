package com.example.cq;

import com.cachequality.api.CacheQualityRulesDefinition;
import org.sonar.api.SonarPlugin;

import java.util.Collections;
import java.util.List;

/**
 * The plugin class
 *
 * <p>This is the class which you declare in the build.gradle's pluginClass. It
 * will always extend {@link SonarPlugin}.</p>
 *
 * <p>The {@link #getExtensions()} method returns the list of extension classes.
 * Since we do a plugin with new rules, we only have a single class extending
 * {@link CacheQualityRulesDefinition} in here.</p>
 */
public final class CqExamplePlugin
    extends SonarPlugin
{
    @Override
    public List getExtensions()
    {
        return Collections.singletonList(CqExampleRules.class);
    }
}
