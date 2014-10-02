/*
 * Copied verbatim from
 * http://download.oracle.com/docs/cd/E14571_01/web.1111/e13728/accesswls.htm#i1116394
 * Compile and execute:
 * java -classpath ... MonitorServlets host port username password
 */


import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Hashtable;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;

public class MonitorServlets {
    private static MBeanServerConnection connection;
    private static JMXConnector connector;
    private static final ObjectName service;

    // Initializing the object name for DomainRuntimeServiceMBean
    // so it can be used throughout the class.
    static {
        try {
            service = new ObjectName(
                    "com.bea:Name=DomainRuntimeService,Type=weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean");
        } catch (MalformedObjectNameException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    /*
     * Initialize connection to the Domain Runtime MBean Server
     */
    public static void initConnection(String hostname, String portString,
                                      String username, String password) throws IOException,
            MalformedURLException {
        String protocol = "t3";
        Integer portInteger = Integer.valueOf(portString);
        int port = portInteger.intValue();
        String jndiroot = "/jndi/";
        String mserver = "weblogic.management.mbeanservers.domainruntime";

        JMXServiceURL serviceURL = new JMXServiceURL(protocol, hostname, port,
                jndiroot + mserver);
        Hashtable h = new Hashtable();
        h.put(Context.SECURITY_PRINCIPAL, username);
        h.put(Context.SECURITY_CREDENTIALS, password);
        h.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES,
                "weblogic.management.remote");
        connector = JMXConnectorFactory.connect(serviceURL, h);
        connection = connector.getMBeanServerConnection();
    }

    /*
     * Get an array of ServerRuntimeMBeans
     */
    public static ObjectName[] getServerRuntimes() throws Exception {
        return (ObjectName[]) connection
                .getAttribute(service, "ServerRuntimes");
    }

    /*
     * Get an array of WebAppComponentRuntimeMBeans
     */
    public void getServletData() throws Exception {
        ObjectName[] serverRT = getServerRuntimes();
        int length = (int) serverRT.length;
        for (int i = 0; i < length; i++) {
            ObjectName[] appRT = (ObjectName[]) connection.getAttribute(
                    serverRT[i], "ApplicationRuntimes");
            int appLength = (int) appRT.length;
            for (int x = 0; x < appLength; x++) {
                System.out.println("Application name: "
                        + (String) connection.getAttribute(appRT[x], "Name"));
                ObjectName[] compRT = (ObjectName[]) connection.getAttribute(
                        appRT[x], "ComponentRuntimes");
                int compLength = (int) compRT.length;
                for (int y = 0; y < compLength; y++) {
                    System.out.println("  Component name: "
                            + (String) connection.getAttribute(compRT[y],
                            "Name"));
                    String componentType = (String) connection.getAttribute(
                            compRT[y], "Type");
                    System.out.println(componentType.toString());
                    if (componentType.toString().equals(
                            "WebAppComponentRuntime")) {
                        ObjectName[] servletRTs = (ObjectName[]) connection
                                .getAttribute(compRT[y], "Servlets");
                        int servletLength = (int) servletRTs.length;
                        for (int z = 0; z < servletLength; z++) {
                            System.out.println("    Servlet name: "
                                    + (String) connection.getAttribute(
                                    servletRTs[z], "Name"));
                            System.out.println("       Servlet context path: "
                                    + (String) connection.getAttribute(
                                    servletRTs[z], "ContextPath"));
                            System.out
                                    .println("       Invocation Total Count : "
                                            + (Object) connection.getAttribute(
                                            servletRTs[z],
                                            "InvocationTotalCount"));
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        String hostname = args[0];
        String portString = args[1];
        String username = args[2];
        String password = args[3];

        MonitorServlets s = new MonitorServlets();
        initConnection(hostname, portString, username, password);
        s.getServletData();
        connector.close();
    }
}
