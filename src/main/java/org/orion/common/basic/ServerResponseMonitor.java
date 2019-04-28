package org.orion.common.basic;

import java.io.Serializable;
import java.sql.Timestamp;

public class ServerResponseMonitor implements Serializable {

    private long responseTime;
    private Timestamp requestStart;
    private Timestamp responseEnd;
    private String responseCode;


}
