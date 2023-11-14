// init-mongo.js
db = db.getSiblingDB('filestorage');

db.createUser({
    user: "yourusername",
    pwd: "yourpassword",
    roles: [{ role: "readWrite", db: "filestorage" }]
});

db.createCollection("files");
db.createCollection("fileInfo");
