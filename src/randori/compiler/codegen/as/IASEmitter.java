/***
 * Copyright 2013 Teoti Graphix, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * @author Michael Schmalle <mschmalle@teotigraphix.com>
 */

package randori.compiler.codegen.as;

import java.io.Writer;
import java.util.Collection;

import org.apache.flex.compiler.definitions.IPackageDefinition;
import org.apache.flex.compiler.internal.tree.as.LabeledStatementNode;
import org.apache.flex.compiler.internal.tree.as.NamespaceAccessExpressionNode;
import org.apache.flex.compiler.problems.ICompilerProblem;
import org.apache.flex.compiler.tree.as.IASNode;
import org.apache.flex.compiler.tree.as.IBinaryOperatorNode;
import org.apache.flex.compiler.tree.as.IBlockNode;
import org.apache.flex.compiler.tree.as.ICatchNode;
import org.apache.flex.compiler.tree.as.IClassNode;
import org.apache.flex.compiler.tree.as.IDynamicAccessNode;
import org.apache.flex.compiler.tree.as.IExpressionNode;
import org.apache.flex.compiler.tree.as.IForLoopNode;
import org.apache.flex.compiler.tree.as.IFunctionCallNode;
import org.apache.flex.compiler.tree.as.IFunctionNode;
import org.apache.flex.compiler.tree.as.IFunctionObjectNode;
import org.apache.flex.compiler.tree.as.IGetterNode;
import org.apache.flex.compiler.tree.as.IIdentifierNode;
import org.apache.flex.compiler.tree.as.IIfNode;
import org.apache.flex.compiler.tree.as.IImportNode;
import org.apache.flex.compiler.tree.as.IInterfaceNode;
import org.apache.flex.compiler.tree.as.IIterationFlowNode;
import org.apache.flex.compiler.tree.as.IKeywordNode;
import org.apache.flex.compiler.tree.as.ILanguageIdentifierNode;
import org.apache.flex.compiler.tree.as.ILiteralContainerNode;
import org.apache.flex.compiler.tree.as.ILiteralNode;
import org.apache.flex.compiler.tree.as.IMemberAccessExpressionNode;
import org.apache.flex.compiler.tree.as.INamespaceNode;
import org.apache.flex.compiler.tree.as.INumericLiteralNode;
import org.apache.flex.compiler.tree.as.IObjectLiteralValuePairNode;
import org.apache.flex.compiler.tree.as.IParameterNode;
import org.apache.flex.compiler.tree.as.IReturnNode;
import org.apache.flex.compiler.tree.as.ISetterNode;
import org.apache.flex.compiler.tree.as.ISwitchNode;
import org.apache.flex.compiler.tree.as.ITernaryOperatorNode;
import org.apache.flex.compiler.tree.as.IThrowNode;
import org.apache.flex.compiler.tree.as.ITryNode;
import org.apache.flex.compiler.tree.as.ITypedExpressionNode;
import org.apache.flex.compiler.tree.as.IUnaryOperatorNode;
import org.apache.flex.compiler.tree.as.IVariableExpressionNode;
import org.apache.flex.compiler.tree.as.IVariableNode;
import org.apache.flex.compiler.tree.as.IWhileLoopNode;
import org.apache.flex.compiler.tree.as.IWithNode;
import org.apache.flex.compiler.tree.metadata.IMetaTagNode;

import randori.compiler.codegen.IEmitter;
import randori.compiler.visitor.as.IASBlockWalker;
import randori.compiler.visitor.as.IASNodeStrategy;

/**
 * The {@link IASEmitter} interface allows abstraction between the
 * {@link IASNodeStrategy} and the current output buffer {@link Writer}.
 * 
 * @author Michael Schmalle
 */
public interface IASEmitter extends IEmitter
{
    /**
     * The emitter's parent visitor that is an {@link IASBlockWalker}.
     */
    IASBlockWalker getWalker();

    void setWalker(IASBlockWalker asBlockWalker);

    /**
     * The current collection of {@link ICompilerProblem}s for the
     * {@link IASEmitter}.
     */
    Collection<ICompilerProblem> getProblems();

    void emitImport(IImportNode node);

    void emitPackageHeader(IPackageDefinition definition);

    void emitPackageHeaderContents(IPackageDefinition definition);

    void emitPackageContents(IPackageDefinition definition);

    void emitPackageFooter(IPackageDefinition definition);

    /**
     * Emit a Class.
     * 
     * @param node The {@link IClassNode} class.
     */
    void emitClass(IClassNode node);

    /**
     * Emit an Interface.
     * 
     * @param node The {@link IInterfaceNode} class.
     */
    void emitInterface(IInterfaceNode node);

    /**
     * Emit a documentation comment for a Class field or constant
     * {@link IVariableNode}.
     * 
     * @param node The {@link IVariableNode} class field member.
     */
    void emitFieldDocumentation(IVariableNode node);

    /**
     * Emit a full Class field member.
     * 
     * @param node The {@link IVariableNode} class field member.
     */
    void emitField(IVariableNode node);

    /**
     * Emit a method scope with braces.
     * 
     * @param node The {@link IFunctionNode} member.
     */
    void emitMethodScope(IFunctionNode node);

    /**
     * Emit parameters for a function.
     * 
     * @param node The {@link IFunctionNode} node.
     */
    void emitParamters(IFunctionNode node);

    /**
     * Emit a documentation comment for a Class method {@link IFunctionNode}.
     * 
     * @param node The {@link IFunctionNode} class method member.
     */
    void emitMethodDocumentation(IFunctionNode node);

    /**
     * Emit a full Class or Interface method member.
     * 
     * @param node The {@link IFunctionNode} class method member.
     */
    void emitMethod(IFunctionNode node);

    /**
     * Emit a documentation comment for a Class method {@link IGetterNode}.
     * 
     * @param node The {@link IGetterNode} class accessor member.
     */
    void emitGetAccessorDocumentation(IGetterNode node);

    /**
     * Emit a full Class getter member.
     * 
     * @param node The {@link IVariableNode} class getter member.
     */
    void emitGetAccessor(IGetterNode node);

    /**
     * Emit a documentation comment for a Class accessor {@link IGetterNode}.
     * 
     * @param node The {@link ISetterNode} class accessor member.
     */
    void emitSetAccessorDocumentation(ISetterNode node);

    /**
     * Emit a full Class setter member.
     * 
     * @param node The {@link ISetterNode} class setter member.
     */
    void emitSetAccessor(ISetterNode node);

    void emitParameter(IParameterNode node);

    /**
     * Emit a namespace member.
     * 
     * @param node The {@link INamespaceNode} class member.
     */
    void emitNamespace(INamespaceNode node);

    //--------------------------------------------------------------------------
    // Statements
    //--------------------------------------------------------------------------

    /**
     * Emit a statement found within an {@link IBlockNode}.
     * 
     * @param node The {@link IASNode} statement.
     */
    void emitStatement(IASNode node);

    /**
     * Emit a <code>if(){}else if(){}else{}</code> statement.
     * 
     * @param node The {@link IIfNode} node.
     */
    void emitIf(IIfNode node);

    /**
     * Emit a <code>for each</code> statement.
     * 
     * @param node The {@link IForLoopNode} node.
     */
    void emitForEachLoop(IForLoopNode node);

    /**
     * Emit a <code>for</code> statement.
     * 
     * @param node The {@link IForLoopNode} node.
     */
    void emitForLoop(IForLoopNode node);

    /**
     * Emit a <code>switch(){}</code> statement.
     * 
     * @param node The {@link ISwitchNode} node.
     */
    void emitSwitch(ISwitchNode node);

    /**
     * Emit a <code>while(){}</code> statement.
     * 
     * @param node The {@link IWhileLoopNode} node.
     */
    void emitWhileLoop(IWhileLoopNode node);

    /**
     * Emit a <code>do{}while()</code> statement.
     * 
     * @param node The {@link IWhileLoopNode} node.
     */
    void emitDoLoop(IWhileLoopNode node);

    /**
     * Emit a <code>with(){}</code> statement.
     * 
     * @param node The {@link IWithNode} node.
     */
    void emitWith(IWithNode node);

    /**
     * Emit a <code>throw</code> statement.
     * 
     * @param node The {@link IThrowNode} node.
     */
    void emitThrow(IThrowNode node);

    /**
     * Emit a <code>try{}</code> statement.
     * 
     * @param node The {@link ITryNode} node.
     */
    void emitTry(ITryNode node);

    /**
     * Emit a <code>catch(){}</code> statement.
     * 
     * @param node The {@link ICatchNode} node.
     */
    void emitCatch(ICatchNode node);

    /**
     * Emit a <code>foo:{}</code> statement.
     * 
     * @param node The {@link LabeledStatementNode} node.
     */
    void emitLabelStatement(LabeledStatementNode node);

    void emitReturn(IReturnNode node);

    //--------------------------------------------------------------------------
    // Expressions
    //--------------------------------------------------------------------------

    /**
     * Emit a variable declaration found in expression statements within scoped
     * blocks.
     * 
     * @param node The {@link IVariableNode} or chain of variable nodes.
     */
    void emitVarDeclaration(IVariableNode node);

    /**
     * Emit an anonymous {@link IFunctionObjectNode}.
     * 
     * @param node The anonymous {@link IFunctionObjectNode}.
     */
    void emitFunctionObject(IFunctionObjectNode node);

    /**
     * Emit a header at the start of a function block.
     * 
     * @param node The {@link IFunctionNode} node.
     */
    void emitFunctionBlockHeader(IFunctionNode node);

    /**
     * Emit a function call like <code>new Foo()</code> or <code>foo(42)</code>.
     * 
     * @param node The {@link IFunctionCallNode} node.
     */
    void emitFunctionCall(IFunctionCallNode node);

    void emitIterationFlow(IIterationFlowNode node);

    void emitNamespaceAccessExpression(NamespaceAccessExpressionNode node);

    void emitMemberAccessExpression(IMemberAccessExpressionNode node);

    void emitVariableExpression(IVariableExpressionNode node);

    void emitDynamicAccess(IDynamicAccessNode node);

    void emitTypedExpression(ITypedExpressionNode node);

    void emitObjectLiteralValuePair(IObjectLiteralValuePairNode node);

    void emitIdentifier(IIdentifierNode node);

    void emitLiteral(ILiteralNode node);

    void emitLiteralContainer(ILiteralContainerNode node);

    void emitNumericLiteral(INumericLiteralNode node);

    //--------------------------------------------------------------------------
    // Operators
    //--------------------------------------------------------------------------

    void emitUnaryOperator(IUnaryOperatorNode node);

    void emitAsOperator(IBinaryOperatorNode node);

    void emitIsOperator(IBinaryOperatorNode node);

    /**
     * Emit an operator statement.
     * 
     * @param node The {@link IBinaryOperatorNode} or chain of variable nodes.
     */
    void emitBinaryOperator(IBinaryOperatorNode node);

    void emitTernaryOperator(ITernaryOperatorNode node);

    //--------------------------------------------------------------------------
    // Node
    //--------------------------------------------------------------------------

    void emitKeyword(IKeywordNode node);

    void emitLanguageIdentifier(ILanguageIdentifierNode node);

    void emitMetaTag(IMetaTagNode node);

    /**
     * Will swap the current write buffer with a {@link StringBuilder}, walk the
     * node passed and then return the String produced.
     * <p>
     * Important to note that the String is not actually written to the out
     * buffer.
     * 
     * @param node The node to stringify.
     */
    String toNodeString(IExpressionNode node);
}
