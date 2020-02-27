package com.mycompany.application.module.bus;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@RefreshScope
@Component
public class BusService {
	
    @Value("${service.prop:notset}")
    private String aRefreshableProperty;
    @Value("${service.prop.2:notset}")
    private String aRefreshableProperty2;
    @Value("${service.prop.3:notset}")
    private String aRefreshableProperty3;

    public String getProperty() {
        return this.aRefreshableProperty;
    }
    public String getProperty2() {
    	return this.aRefreshableProperty2;
    }
    public String getProperty3() {
    	return this.aRefreshableProperty3;
    }
	
}
