package com.example.cq;

import com.example.cq.checks.VariableNameLengthCheck;
import com.objectscriptquality.api.ObjectScriptQualityRulesDefinition;
import com.objectscriptquality.api.check.ObjectScriptCheck;
import org.sonar.api.config.Settings;

import java.util.Collections;
import java.util.List;

/**
 * The extension which you need to implement in order to register as a SonarQube
 * plugin
 *
 */
public final class CqExampleRules
    extends ObjectScriptQualityRulesDefinition
{
    /**
     * The name of the repository
     *
     * <p>This and the key must be unique across all plugins.</p>
     *
     * @return the name, as a string
     */
    @Override
    public String repositoryName()
    {
        return PluginConstants.RULEREPO_NAME;
    }

    /**
     * The key of the repository
     *
     * <p>This and the name must be unique across all plugins.</p>
     *
     * <p>Note that there are restrictions in the key. Basically, only use
     * letters and hyphens (-) in the name (with a letter first) and you'll be
     * safe.</p>
     *
     * @return the key, as a string
     */
    @Override
    public String repositoryKey()
    {
        return PluginConstants.RULEREPO_KEY;
    }

    /**
     * List of all check classes
     *
     * <p>This list is supposed to return a list of all check classes,
     * regardless of whether such classes have a no argument constructor or a
     * constructor with a {@link Settings} instance as an argument.</p>
     *
     * @return a list of all check classes
     */
    @Override
    public List<Class> checkClasses()
    {
        return Collections.singletonList(VariableNameLengthCheck.class);
    }

    /**
     * List of all check classes with an argumentless constructor
     *
     * <p>If the check class has an argumentless constructor, then Caché Quality
     * will take care of instansiating the checks.</p>
     *
     * @return see description
     */
    @Override
    public List<Class<? extends ObjectScriptCheck>> noargChecks()
    {
        return Collections.singletonList(VariableNameLengthCheck.class);
    }

    /**
     * List of all checks 
     *
     * <p>Here, it is up to the user to provide an instance of the check by
     * calling the constructor.</p>
     *
     * @return see description
     */
    @Override
    public List<ObjectScriptCheck> argChecks()
    {
        return Collections.emptyList();
    }
}
