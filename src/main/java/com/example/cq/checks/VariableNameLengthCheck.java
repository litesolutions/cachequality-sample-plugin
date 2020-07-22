package com.example.cq.checks;

import com.objectscriptquality.api.IssueTags;
import com.objectscriptquality.api.ast.AstUtil;
import com.objectscriptquality.api.ast.grammars.arguments.MethodCallArgumentsGrammar;
import com.objectscriptquality.api.ast.grammars.cacheclass.ClassGrammar;
import com.objectscriptquality.api.ast.grammars.objectscript.ref.HashReferenceGrammar;
import com.objectscriptquality.api.ast.grammars.objectscript.statements.commands.CommandsGrammar;
import com.objectscriptquality.api.ast.tokens.classcode.ClassElements;
import com.objectscriptquality.api.ast.tokens.operators.Symbols;
import com.objectscriptquality.api.ast.tokens.references.References;
import com.objectscriptquality.api.ast.tokens.references.Variables;
import com.objectscriptquality.api.check.ObjectScriptCheck;
import com.objectscriptquality.api.check.ObjectScriptMethodCheck;
import com.objectscriptquality.api.sourcecode.ObjectScriptMethod;
import com.example.cq.PluginConstants;
import com.google.common.annotations.VisibleForTesting;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.api.CheckMessage;
import org.sonar.squidbridge.api.SourceCode;

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
 *
 * <p><em>Important:</em> do not forget the HTML description. It is in the
 * resources directory ({@code src/main/resources}) and must be named {@code
 * org/sonar/l10n/objectscripts/rule/repokey/rulekey.html}, where {@code
 * repokey} is the repository name (defined by {@link
 * PluginConstants#RULEREPO_KEY} in this project) and {@code rulekey} is the key
 * of this rule (see the {@link #KEY} constant defined below).</p>
 */

/*
 * The @Rule annotation specifies different parameters for the check; see its
 * javadoc for more details.
 */
@Rule(
    key = VariableNameLengthCheck.KEY,
    name = VariableNameLengthCheck.NAME,
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
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.LOGIC_RELIABILITY)
@SqaleConstantRemediation("2h")
@ParametersAreNonnullByDefault
@ActivatedByDefault
public final class VariableNameLengthCheck
    extends ObjectScriptMethodCheck
{
    /*
     * The key of the check. It must be unique across all checks for a given
     * language; it is therefore recommended that you use, for instance, the
     * plugin key as a base for generating keys.
     *
     * Here, CQEX is an abbreviation for "cqexample".
     */
    static final String KEY = "VNL0001";

    /*
     * The name of the check. This will appear in the check list in SonarQube,
     * and is searchable.
     */
    static final String NAME = "AAAAAADetect whether a _local_ variable name has a length >= 8 characters";

    /*
     * The message associated with the check.
     *
     * If the message is parameterized, use format specifiers as below, and use
     * String.format() to generate the message.
     */
    @VisibleForTesting
    static final String MESSAGE = "AAAAAdetect  %s whether a _local_ variable name has a length >= 8 characters ";

    private final ArrayList<MethodInfo> nameMethods = new ArrayList<>();

    private final Map<AstNode, SourceCode> methodBodies = new IdentityHashMap<>();

    @Override
    public void leaveNode(final AstNode astNode) {
        final ObjectScriptMethod method = getMethod();

        final String methodName = method.getMethodName();

        final String className = astNode.getFirstAncestor(ClassGrammar.CLASS)
                .getFirstChild(ClassElements.CLASS).getTokenValue();

        if (method.getReturnType() == null)
            nameMethods.add(new MethodInfo(className, methodName));

        methodBodies.put(astNode, getEnclosingFile());

    }

    @Override
    public void destroy() {
        for (final Map.Entry<AstNode, SourceCode> entry : methodBodies
                .entrySet())
            checkMethod(entry.getKey(), entry.getValue());
    }

    private void checkMethod(final AstNode body, final SourceCode sourceCode) {
        final List<AstNode> methodCalls = body.getDescendants(
                MethodCallArgumentsGrammar.METHOD_CALL_ARGUMENTS);

        AstNode caller;

        for (final AstNode methodCall : methodCalls) {
            /*
             * Pick our previous sibling
             */
            caller = methodCall.getPreviousSibling();

            final String methodName = caller.getTokenValue();

            /*
             * Now it depends on what the caller is...
             */

            AstNode isDo = caller.getFirstAncestor(CommandsGrammar.DO_COMMAND);

            if (isDo != null
                    && (isDo.getTokenLine() == caller.getTokenLine())) {
                continue;
            } else {
                if (caller.is(References.SELF_METHOD)) {
                    final String className = caller
                            .getFirstAncestor(ClassGrammar.CLASS)
                            .getFirstChild(ClassElements.CLASS)
                            .getTokenValue();
                    if (nameMethods
                            .contains(new MethodInfo(className, methodName))) {
                        generateViolation(methodName, sourceCode,
                                caller.getTokenLine());
                    }
                }

                else if (caller.is(References.METHOD)) {
                    final AstNode parent = caller.getParent();
                    if (parent.is(HashReferenceGrammar.CLASS)
                            && parent.getChildren(Symbols.DOT).size() == 1) {
                        final String className = AstUtil.getFullClassName(
                                parent.getFirstChild(References.CLASS));
                        if (nameMethods.contains(
                                new MethodInfo(className, methodName))) {
                            generateViolation(methodName, sourceCode,
                                    caller.getTokenLine());
                        }

                    }

                }
            }
        }

    }

    private void generateViolation(String methodName,
            final SourceCode sourceCode, int line) {
        final String msg = String.format(MESSAGE, methodName);
        final CheckMessage message = new CheckMessage((Object) this, msg);
        message.setLine(line);
        sourceCode.log(message);
    }

    private class MethodInfo {

        private String name;

        private String className;

        public MethodInfo(String name, String className) {
            super();
            this.name = name;
            this.className = className;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result
                    + ((className == null) ? 0 : className.hashCode());
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            MethodInfo other = (MethodInfo) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (className == null) {
                if (other.className != null)
                    return false;
            } else if (!className.equals(other.className))
                return false;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            return true;
        }

        private VariableNameLengthCheck getOuterType() {
            return VariableNameLengthCheck.this;
        }

    }

}
