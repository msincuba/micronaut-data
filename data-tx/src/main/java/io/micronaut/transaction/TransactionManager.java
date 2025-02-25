/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.micronaut.transaction;

/**
 * NOTICE: This is a fork of Spring's {@code TransactionManager} modernizing it
 * to use enums, Slf4j and decoupling from Spring.
 *
 * Marker interface for transaction manager implementations,
 * either traditional or reactive.
 *
 * @author Juergen Hoeller
 * @author graemerocher
 * @since 1.0
 * @see SynchronousTransactionManager
 * @see ReactiveTransactionManager
 */
public interface TransactionManager {

}

