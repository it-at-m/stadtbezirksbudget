package de.muenchen.stadtbezirksbudget.backend;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings({ "PMD.TestClassWithoutTestCases", "PMD.DataClass" })
public final class TestConstants {

    public static final String SPRING_TEST_PROFILE = "test";

    public static final String SPRING_NO_SECURITY_PROFILE = "no-security";

    public static final String TESTCONTAINERS_POSTGRES_IMAGE = "postgres:17.4-alpine3.21";

    public static final String TESTCONTAINERS_KAFKA_IMAGE = "confluentinc/cp-kafka:7.9.5";

}
