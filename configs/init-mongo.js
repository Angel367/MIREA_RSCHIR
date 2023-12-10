db = db.getSiblingDB('filestorage');

db.createUser({
    user: "admin",
    pwd: "admin",
    roles: [{ role: "readWrite", db: "filestorage" }]
});

db.createCollection("files");
db.createCollection("fileInfo");
