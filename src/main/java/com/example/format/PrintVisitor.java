/*
 * Copyright 2017 Shinya Mochida
 * 
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.format;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.TypeParameter;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.AnnotationMemberDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EmptyMemberDeclaration;
import com.github.javaparser.ast.body.EmptyTypeDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.MultiTypeParameter;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.body.VariableDeclaratorId;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.InstanceOfExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.IntegerLiteralMinValueExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.LongLiteralExpr;
import com.github.javaparser.ast.expr.LongLiteralMinValueExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.QualifiedNameExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.expr.SuperExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.expr.TypeExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.AssertStmt;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.BreakStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.ContinueStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.EmptyStmt;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.LabeledStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.SwitchEntryStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.SynchronizedStmt;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.stmt.TypeDeclarationStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.IntersectionType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.UnionType;
import com.github.javaparser.ast.type.UnknownType;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.type.WildcardType;
import com.github.javaparser.ast.visitor.VoidVisitor;

public class PrintVisitor implements VoidVisitor<Object> {

    private final VoidVisitor<Object> delegate;

    public PrintVisitor(VoidVisitor<Object> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void visit(CompilationUnit n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(PackageDeclaration n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(ImportDeclaration n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(TypeParameter n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(LineComment n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(BlockComment n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(EnumDeclaration n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(EmptyTypeDeclaration n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(EnumConstantDeclaration n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(AnnotationDeclaration n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(AnnotationMemberDeclaration n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(FieldDeclaration n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(VariableDeclarator n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(VariableDeclaratorId n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(ConstructorDeclaration n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(MethodDeclaration n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(Parameter n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(MultiTypeParameter n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(EmptyMemberDeclaration n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(InitializerDeclaration n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(JavadocComment n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(ClassOrInterfaceType n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(PrimitiveType n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(ReferenceType n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(IntersectionType n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(UnionType n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(VoidType n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(WildcardType n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(UnknownType n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(ArrayAccessExpr n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(ArrayCreationExpr n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(ArrayInitializerExpr n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(AssignExpr n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(BinaryExpr n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(CastExpr n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(ClassExpr n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(ConditionalExpr n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(EnclosedExpr n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(FieldAccessExpr n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(InstanceOfExpr n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(StringLiteralExpr n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(IntegerLiteralExpr n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(LongLiteralExpr n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(IntegerLiteralMinValueExpr n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(LongLiteralMinValueExpr n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(CharLiteralExpr n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(DoubleLiteralExpr n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(BooleanLiteralExpr n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(NullLiteralExpr n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(MethodCallExpr n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(NameExpr n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(ObjectCreationExpr n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(QualifiedNameExpr n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(ThisExpr n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(SuperExpr n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(UnaryExpr n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(VariableDeclarationExpr n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(MarkerAnnotationExpr n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(SingleMemberAnnotationExpr n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(NormalAnnotationExpr n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(MemberValuePair n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(ExplicitConstructorInvocationStmt n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(TypeDeclarationStmt n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(AssertStmt n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(BlockStmt n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(LabeledStmt n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(EmptyStmt n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(ExpressionStmt n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(SwitchStmt n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(SwitchEntryStmt n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(BreakStmt n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(ReturnStmt n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(IfStmt n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(WhileStmt n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(ContinueStmt n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(DoStmt n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(ForeachStmt n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(ForStmt n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(ThrowStmt n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(SynchronizedStmt n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(TryStmt n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(CatchClause n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(LambdaExpr n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(MethodReferenceExpr n, Object arg) {
        delegate.visit(n, arg);
    }

    @Override
    public void visit(TypeExpr n, Object arg) {
        delegate.visit(n, arg);
    }
}
