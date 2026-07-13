package com.rest.marketplace.infrastructure.utilities.parameters;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParametrosTest {

	@Test
	void debeSerUnaClaseUtility() {
		assertThat(Modifier.isFinal(Parametros.class.getModifiers())).isTrue();
	}

	@Test
	void constructorPrivadoNoDebePermitirInstanciacion() throws Exception {
		Constructor<Parametros> constructor = Parametros.class.getDeclaredConstructor();
		constructor.setAccessible(true);

		assertThrows(InvocationTargetException.class, constructor::newInstance);
	}
}

