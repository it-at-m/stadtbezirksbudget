package de.muenchen.stadtbezirksbudget.cit_eai;

import lombok.RequiredArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EaiRouteBuilder extends RouteBuilder {

    public static final String DIRECT_ROUTE = "direct:eai-route";
    @Value("${output}")
    private String outputRoute;

    @Override
    public void configure() {
        onException(Exception.class).handled(true).log(LoggingLevel.ERROR, "${exception}");

        from(DIRECT_ROUTE)
                .routeId("eai-route")
                .log(LoggingLevel.DEBUG, "de.muenchen",
                        "Add camel routing... (https://camel.apache.org/components/latest/eips/enterprise-integration-patterns.html).")
                .to(outputRoute);
    }

}
