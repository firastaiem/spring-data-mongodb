/*
 * Copyright 2010-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.document.mongodb;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.data.document.UncategorizedDocumentStoreException;

import com.mongodb.MongoException;
import com.mongodb.MongoException.DuplicateKey;
import com.mongodb.MongoException.Network;

/**
 * Simple {@link PersistenceExceptionTranslator} for Mongo. Convert the given runtime exception to an appropriate
 * exception from the {@code org.springframework.dao} hierarchy. Return {@literal null} if no translation is
 * appropriate: any other exception may have resulted from user code, and should not be translated.
 * @param ex runtime exception that occurred
 * @return the corresponding DataAccessException instance, or {@literal null} if the exception should not be translated
 * 
 * 
 * @author Oliver Gierke
 */
public class MongoExceptionTranslator implements PersistenceExceptionTranslator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.dao.support.PersistenceExceptionTranslator#
	 * translateExceptionIfPossible(java.lang.RuntimeException)
	 */
	public DataAccessException translateExceptionIfPossible(RuntimeException ex) {

		// Check for well-known MongoException subclasses.

		// All other MongoExceptions
		if (ex instanceof DuplicateKey) {
			return new DataIntegrityViolationException(ex.getMessage(), ex);
		}
		if (ex instanceof Network) {
			return new DataAccessResourceFailureException(ex.getMessage(), ex);
		}
		if (ex instanceof MongoException) {
			return new UncategorizedDocumentStoreException(ex.getMessage(), ex);
		}

		// If we get here, we have an exception that resulted from user code,
		// rather than the persistence provider, so we return null to indicate
		// that translation should not occur.
		return null;
	}
}
