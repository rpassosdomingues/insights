package sql;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;

public class Neo4jConnector {
    private final Driver driver;

    public Neo4jConnector(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    public String searchWords(String keyword) {
        try (Session session = driver.session()) {
            return session.writeTransaction(tx -> {
                String query = "MATCH (n) WHERE n.name CONTAINS $keyword RETURN n.name";
                return tx.run(query, org.neo4j.driver.Values.parameters("keyword", keyword))
                         .list(record -> record.get(0).asString()).toString();
            });
        }
    }

    public void close() {
        driver.close();
    }
}
