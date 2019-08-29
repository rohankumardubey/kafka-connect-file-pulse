/*
 * Copyright 2019 StreamThoughts.
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.streamthoughts.kafka.connect.filepulse.expression.function.impl;

import io.streamthoughts.kafka.connect.filepulse.data.TypedValue;
import io.streamthoughts.kafka.connect.filepulse.expression.function.ArgumentValue;
import io.streamthoughts.kafka.connect.filepulse.expression.function.ExpressionFunction;
import io.streamthoughts.kafka.connect.filepulse.expression.function.MissingArgumentValue;
import io.streamthoughts.kafka.connect.filepulse.expression.function.SimpleArguments;

import java.util.regex.Pattern;

/**
 * Replaces every subsequence of the input sequence that matches the
 * pattern with the given replacement string.
 */
public class ReplaceAll implements ExpressionFunction<SimpleArguments> {

    private static final String PATTERN_ARG = "pattern";
    private static final String REPLACEMENT_ARG = "replacement";

    /**
     * {@inheritDoc}
     */
    @Override
    public SimpleArguments prepare(final TypedValue[] args) {
        if (args.length < 2) {
            return new SimpleArguments()
                 .withArg(new MissingArgumentValue(PATTERN_ARG))
                 .withArg(new MissingArgumentValue(REPLACEMENT_ARG));
        }
        String pattern = args[0].getString();
        String replacement = args[1].getString();
        return new SimpleArguments()
                .withArg(new ArgumentValue(PATTERN_ARG,  Pattern.compile(pattern)))
                .withArg(new ArgumentValue(REPLACEMENT_ARG,  replacement));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypedValue apply(final TypedValue field, final SimpleArguments args) {
        final Pattern pattern = args.valueOf(PATTERN_ARG);
        final String replacement = args.valueOf(REPLACEMENT_ARG);
        final String value = field.value();

        return TypedValue.string(pattern.matcher(value).replaceAll(replacement));
    }
}
