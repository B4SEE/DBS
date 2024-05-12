package cs.cvut.fel.dbs.logging;

import org.apache.log4j.PatternLayout;

public class Layout extends PatternLayout {

    @Override
    public String getHeader() {
        return """
                ---------------------------------
                Semester project for DBS (Database systems) summer course 2023/2024
                Author: virycele@fel.cvut.cz
                ---------------------------------



                """;
    }

}

