/*
 * Copyright (C) 2007-2010 Júlio Vilmar Gesser.
 * Copyright (C) 2011, 2013-2016 The JavaParser Team.
 *
 * This file is part of JavaParser.
 *
 * JavaParser can be used either under the terms of
 * a) the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * b) the terms of the Apache License
 *
 * You should have received a copy of both licenses in LICENCE.LGPL and
 * LICENCE.APACHE. Please refer to those files for details.
 *
 * JavaParser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 */

package com.example.format;

import com.example.format.config.FormatConfig;
import com.github.javaparser.ast.TypeParameter;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Collections.emptyList;

public class DetailPrinterImpl implements DetailPrinter {

    private final FormatConfig config;

    public DetailPrinterImpl(FormatConfig config) {
        this.config = config;
    }

    private static final Object MOD = new Object();

    @Contract(value = "null -> false; !null -> true", pure = true)
    private static boolean notNull(Object o) {
        return o != null;
    }

    private static <T> boolean notEmpty(Collection<T> c) {
        return notNull(c) && c.size() > 0;
    }

    @Contract("null -> fail")
    private static void consume(Consumer<Object> consumer) {
        Objects.requireNonNull(consumer);
        consumer.accept(MOD);
    }

    private static <T> void iterateOverSeparator(
            @NotNull Iterable<T> iterable
            , @NotNull Consumer<? super T> always
            , @NotNull Runnable onHaveNext
    ) {
        Objects.requireNonNull(iterable);
        Objects.requireNonNull(always);
        Objects.requireNonNull(onHaveNext);

        final Iterator<T> iter = iterable.iterator();
        while (iter.hasNext()) {
            always.accept(iter.next());
            if (iter.hasNext()) {
                onHaveNext.run();
            }
        }
    }

    @NotNull
    @Contract("null->fail")
    private static <T>Consumer<T> newLine(StringPrinter printer) {
        Objects.requireNonNull(printer);
        return t -> printer.newLine();
    }

    @NotNull
    @Contract("null->fail")
    private static <T> Consumer<T> blank(StringPrinter printer) {
        Objects.requireNonNull(printer);
        return t -> printer.print(" ");
    }

    @NotNull
    @Contract("null->fail")
    private static <T> Consumer<T> consumer(Consumer<T> consumer) {
        Objects.requireNonNull(consumer);
        return consumer;
    }

    @NotNull
    @Contract("null -> fail")
    private static <T> Consumer<T> separator(StringPrinter printer) {
        Objects.requireNonNull(printer);
        return t -> printer.print(", ");
    }

    @NotNull
    @Contract("null -> fail")
    private static <T> Consumer<T> typeLeftParenthesis(StringPrinter printer) {
        Objects.requireNonNull(printer);
        return t -> printer.print("<");
    }

    @NotNull
    @Contract("null -> fail")
    private static <T> Consumer<T> typeRightParenthesis(StringPrinter printer) {
        Objects.requireNonNull(printer);
        return t -> printer.print(">");
    }

    @NotNull
    @Contract("null -> fail")
    private static <T> Consumer<T> leftParenthesis(StringPrinter printer) {
        Objects.requireNonNull(printer);
        return t -> printer.print("(");
    }

    @NotNull
    @Contract("null -> fail")
    private static <T> Consumer<T> rightParenthesis(StringPrinter printer) {
        Objects.requireNonNull(printer);
        return t -> printer.print(")");
    }

    @Override
    public void printMembers(
            VoidVisitor<Object> visitor
            , StringPrinter printer
            , List<BodyDeclaration> members
            , Object arg) {
        members.forEach(
                DetailPrinterImpl.<BodyDeclaration>newLine(printer)
                .andThen(m -> m.accept(visitor, arg))
                .andThen(newLine(printer))
        );
    }

    @Override
    public void printMemberAnnotations(
            VoidVisitor<Object> visitor
            , StringPrinter printer
            , List<AnnotationExpr> annotations
            , Object arg) {
        Optional.ofNullable(annotations)
                .orElse(emptyList())
                .forEach(DetailPrinterImpl
                        .<AnnotationExpr>consumer(a -> a.accept(visitor, arg))
                        .andThen(newLine(printer)));
    }

    @Override
    public void printAnnotations(
            VoidVisitor<Object> visitor
            , StringPrinter printer
            , List<AnnotationExpr> annotations
            , Object arg) {
        Optional.ofNullable(annotations)
                .orElse(emptyList())
                .forEach(DetailPrinterImpl
                        .<AnnotationExpr>consumer(a -> a.accept(visitor, arg))
                        .andThen(blank(printer)));
    }

    @Override
    public void printTypeArgs(
            VoidVisitor<Object> visitor
            , StringPrinter printer
            , List<Type> args
            , Object arg) {
        if (notEmpty(args)) {
            typeLeftParenthesis(printer).accept("");
            final Iterator<Type> iter = args.iterator();
            while (iter.hasNext()) {
                iter.next().accept(visitor, arg);
                if (iter.hasNext()) {
                    separator(printer);
                }
            }
            typeRightParenthesis(printer).accept("");
        }
    }

    @Override
    public void printTypeParameters(
            VoidVisitor<Object> visitor
            , StringPrinter printer
            , List<TypeParameter> args
            , Object arg) {
        if (notEmpty(args)) {
            consume(typeLeftParenthesis(printer));
            final Iterator<TypeParameter> iter = args.iterator();
            while (iter.hasNext()) {
                iter.next().accept(visitor, arg);
                if (iter.hasNext()) {
                    separator(printer);
                }
            }
            consume(typeRightParenthesis(printer));
        }
    }

    @Override
    public void printArguments(
            VoidVisitor<Object> visitor
            , StringPrinter printer
            , List<Expression> args
            , Object arg) {
        consume(leftParenthesis(printer));
        iterateOverSeparator(args
                , e -> e.accept(visitor, arg)
                , () -> separator(printer));
        consume(rightParenthesis(printer));
    }

    @Override
    public void printJavadoc(
            VoidVisitor<Object> visitor
            , StringPrinter printer
            , JavadocComment javadoc
            , Object arg) {

    }

    @Override
    public void printJavaComment(
            VoidVisitor<Object> visitor
            , StringPrinter printer
            , Comment comment
            , Object arg) {

    }
}
