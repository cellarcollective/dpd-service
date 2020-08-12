package co.cellarcollective.tools.chronopostapiemu;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WevServiceConfig extends WsConfigurerAdapter {

    String targetNamespace = "http://webservice.trace.chronopost.com";
    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(servlet, "/ws/*");
    }

    @Bean(name = "deliveries")
    public DefaultWsdl11Definition deliveriesWsdl11Definition(XsdSchema deliveriesSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("DeliveriesPort");
        wsdl11Definition.setLocationUri("/ws");

        wsdl11Definition.setTargetNamespace(targetNamespace);
        wsdl11Definition.setSchema(deliveriesSchema);
        return wsdl11Definition;
    }

    @Bean(name = "tracking")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema trackingSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("TrackingPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace(targetNamespace);
        wsdl11Definition.setSchema(trackingSchema);

        return wsdl11Definition;
    }

    @Bean
    public XsdSchema deliveriesSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/TestXSD.xml"));
    }

    @Bean
    public XsdSchema trackingSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/ChronopostTrackingXSD.xml"));
    }
}
