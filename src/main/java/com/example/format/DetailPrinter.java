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

import com.github.javaparser.ast.TypeParameter;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ModifierSet;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitor;

import java.util.List;

public interface DetailPrinter {

    default void printModifiers(int modifiers, StringPrinter printer) {
        if (ModifierSet.isPrivate(modifiers)) {
            printer.print("private ");
        }
        if (ModifierSet.isProtected(modifiers)) {
            printer.print("protected ");
        }
        if (ModifierSet.isPublic(modifiers)) {
            printer.print("public ");
        }
        if (ModifierSet.isAbstract(modifiers)) {
            printer.print("abstract ");
        }
        if (ModifierSet.isStatic(modifiers)) {
            printer.print("static ");
        }
        if (ModifierSet.isFinal(modifiers)) {
            printer.print("final ");
        }
        if (ModifierSet.isNative(modifiers)) {
            printer.print("native ");
        }
        if (ModifierSet.isStrictfp(modifiers)) {
            printer.print("strictfp ");
        }
        if (ModifierSet.isSynchronized(modifiers)) {
            printer.print("synchronized ");
        }
        if (ModifierSet.isTransient(modifiers)) {
            printer.print("transient ");
        }
        if (ModifierSet.isVolatile(modifiers)) {
            printer.print("volatile ");
        }
    }

    void printMembers(VoidVisitor<Object> visitor
            , StringPrinter printer
            , List<BodyDeclaration> members
            , Object arg);

    void printMemberAnnotations(VoidVisitor<Object> visitor
            , StringPrinter printer
            , List<AnnotationExpr> annotations
            , Object arg);

    void printAnnotations(VoidVisitor<Object> visitor
            , StringPrinter printer
            , List<AnnotationExpr> annotations
            , Object arg);

    void printTypeArgs(VoidVisitor<Object> visitor
            , StringPrinter printer
            , List<Type> args
            , Object arg);

    void printTypeParameters(VoidVisitor<Object> visitor
            , StringPrinter printer
            , List<TypeParameter> args
            , Object arg);

    void printArguments(VoidVisitor<Object> visitor
            , StringPrinter printer
            , List<Expression> args
            , Object arg);

    void printJavadoc(VoidVisitor<Object> visitor
            , StringPrinter printer
            , JavadocComment javadoc
            , Object arg);

    void printJavaComment(VoidVisitor<Object> visitor
            , StringPrinter printer
            , Comment comment
            , Object arg);
}
