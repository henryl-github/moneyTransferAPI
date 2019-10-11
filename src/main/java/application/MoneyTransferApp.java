package application;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import java.io.IOException;
import java.net.URI;

public class MoneyTransferApp {

    public static final String BASE_URI = "http://localhost:8090/";
    private static HttpServer server;

    public static void startServer() {
        if (server == null) {
            final ResourceConfig rc = new ResourceConfig().packages("api");
            rc.property(ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR, "true");
            server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
        }
    }

    public static void shutDownServer() {
        if (server != null)
            server.shutdown();
    }


    public static void main(String[] args) throws IOException {
        startServer();
        System.out.println("Money transfer API running at " + BASE_URI);
        System.in.read();
        shutDownServer();
    }
}