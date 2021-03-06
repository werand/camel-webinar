package org.talend.example;

import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class CamelEipFilterTest extends CamelTestSupport {
	@EndpointInject(uri="mock:result")
	MockEndpoint mock;

    @Test
    public void testFilter() throws Exception {
        mock.expectedMessageCount(4);

        template.sendBodyAndHeader("direct:start", "Athens", "rank", "1");
        template.sendBodyAndHeader("direct:start", "Toronto", "rank", "2");
        template.sendBodyAndHeader("direct:start", "Moscow", "rank", "2");
        template.sendBodyAndHeader("direct:start", "San Diego", "rank", "3");
        template.sendBodyAndHeader("direct:start", "Aruba", "rank", "4");
        template.sendBodyAndHeader("direct:start", "London", "rank", "4");
        template.sendBodyAndHeader("direct:start", "Venice", "rank", "4");
        template.sendBodyAndHeader("direct:start", "Cancun", "rank", "5");

        assertMockEndpointsSatisfied();
        assertEquals("Aruba", mock.getExchanges().get(0).getIn().getBody());
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
            	getContext().setTracing(true);
            	
                from("direct:start")
	                .filter(header("rank").isGreaterThan("3"))
	                .to("log:interesting")
	                .to("mock:result");
            }
        };
    }
}
