package com.example.cq;

import java.util.Locale;

import org.sonar.api.Plugin;

import com.objectscriptquality.api.ObjectScriptQualityRulesDefinition;

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
implements Plugin {
    static {
        Locale.setDefault(Locale.ENGLISH);
    }

    @Override
    public void define(Context context) {

    	
        context.addExtension(
        		CqExampleRules.class
        );
    }
}
