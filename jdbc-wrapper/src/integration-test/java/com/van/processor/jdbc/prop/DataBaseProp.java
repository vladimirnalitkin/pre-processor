package com.van.processor.jdbc.prop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataBaseProp {
    private Datasource datasource;

    @Data
    @AllArgsConstructor
	@NoArgsConstructor
    public static class Datasource {
        private Map<String, String> hikari;
        private String driverclassname;
        private String url;
        private String username;
        private String password;
        private String initializationmode;
        private List<String> initsql;
    }

}
