package de.muenchen.stadtbezirksbudget.backend.common;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(CLASS)

/**
 * Marker annotation to exclude classes, methods, or constructors from JaCoCo coverage reports.
 * <p>
 * This annotation should be used sparingly and only for:
 * <ul>
 * <li>Infrastructure/configuration code that is difficult to test in isolation</li>
 * <li>Generated code (e.g., MapStruct implementations)</li>
 * <li>Code temporarily excluded with plans to add tests later (include TODO with issue
 * reference)</li>
 * </ul>
 */
@Target({ TYPE, METHOD, CONSTRUCTOR })
public @interface ExcludedFromGeneratedCoverage {

}
