package co.cellarcollective.tools.dpd.config;

import com.mongodb.MongoCommandException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.ArrayList;
import java.util.List;

@Profile(value = {"local","dev", "prod"})
@Configuration
@EnableMongoRepositories(basePackages = "co.cellarcollective.tools.dpd.repository")
public class MongoTransactionConfig extends AbstractMongoClientConfiguration {

    private static List<String> COLLECTIONS = new ArrayList<>();

    static {
        COLLECTIONS.add("trackings");
        COLLECTIONS.add("scenarios");
    }

    @Value("${spring.data.mongodb.uri}")
    private String dbUri;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;


    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Override
    protected boolean autoIndexCreation() {
        return false;
    }

    @Bean
    MongoTransactionManager transactionManager(MongoDbFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        final MongoTemplate mongoTemplate = new MongoTemplate(this.mongoDbFactory(), this.mappingMongoConverter());

        MappingMongoConverter mongoMapping = (MappingMongoConverter) mongoTemplate.getConverter();
        mongoMapping.setCustomConversions(customConversions());
        mongoMapping.afterPropertiesSet();
        return mongoTemplate;
    }

    /**
     * We need to create every mongo collection just in case,
     * because mongo db does not support collection creation on transactions
     * at this time,
     * any access to an uncreated collection would throw an exception
     *
     * @return Mongo Client
     */
    @Override
    public MongoClient mongoClient() {
        final MongoClient client = MongoClients.create(dbUri);
        MongoDatabase db = client.getDatabase(getDatabaseName());
        for (String collection : COLLECTIONS) {
            try {
                db.createCollection(collection);
            } catch (MongoCommandException e) {
                //NO OP collection already exists
            }
        }
        return client;
    }
}
