/**
 * Created by manusant
 */

// Notification MongoDB for WineMarket platform
// available at: mongodb://localhost:27017/dpd-service
//--------------------------------------------
use dpd-service

db.info.save({
    name:"dpd-service",
    description:"Mongo Database for WineMarket Notification micro-services"
});

db.createUser(
    {
        user: "cellarcollective",
        pwd: "mongo123QWEasd",
        roles: ["readWrite","dbAdmin"]
    }
);
