package com.example.cq.checks;

import com.cachequality.api.IssueTags;
import com.cachequality.api.ast.grammars.statements.FlowctlGrammar;
import com.cachequality.api.check.ObjectScriptCheck;
import com.google.common.annotations.VisibleForTesting;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;

/**
 * Example of a "raw" rule
 *
 * <p>A rule will always extend {@link ObjectScriptCheck}.</p>
 *
 * <p>With this kind of rule, you will be able to specify which nodes in the AST
 * you want to {@link #subscribeTo(AstNodeType...) subscribe to} (this is done
 * in the {@link #init()} method).</p>
 *
 * <p>A visitor pattern is used; you will be able to perform the check either
 * when the node is visited (using {@link #visitNode(AstNode)}) or after the
 * node has been visited (using {@link #leaveNode(AstNode)}).</p>
 */

/*
 * The @Rule annotation specifies different parameters for the check; see its
 * javadoc for more details.
 */
@Rule(
    key = LegacyFlowctlUsageCheck.KEY,
    name = LegacyFlowctlUsageCheck.NAME,
    priority = Priority.MAJOR,
    tags = { IssueTags.CODING_GUIDELINES, IssueTags.DEPRECATION }
)
/*
 * The two annotations below define the SQALE characteristics associated to this
 * check.
 *
 * If this check is not meant to contribute to the technical debt, replace the
 * two annotations below with @NoSqale.
 */
@SqaleConstantRemediation("5min")
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.MAINTAINABILITY_COMPLIANCE)
public final class LegacyFlowctlUsageCheck
    extends ObjectScriptCheck
{
    /*
     * The key of the check. It must be unique across all checks for a given
     * language; it is therefore recommended that you use, for instance, the
     * plugin key as a base for generating keys.
     *
     * Here, CQEX is an abbreviation for "cqexample".
     */
    static final String KEY = "CQEX0001";

    /*
     * The name of the check. This will appear in the check list in SonarQube,
     * and is searchable.
     */
    static final String NAME = "Usage of legacy flow control statements";

    /*
     * The message associated with the check.
     *
     * If the message is parameterized, use format specifiers as below, and use
     * String.format() to generate the message.
     */
    @VisibleForTesting
    static final String MESSAGE = "Avoid using legacy flow control statements";

    @Override
    public void init()
    {
        subscribeTo(FlowctlGrammar.LEGACY);
    }

    @Override
    public void visitNode(final AstNode astNode)
    {
        final String what = astNode.getTokenValue();

        /*
         * This is how you generate an issue in the code.
         *
         * This example is simple: the issue is raised unconditionally.
         *
         * If this is not the case, you should explore the AST by querying it
         * using SonarQube's AstNode class, which is the argument to this
         * method.
         */
        getContext().createLineViolation(this, MESSAGE, astNode);
    }
}
