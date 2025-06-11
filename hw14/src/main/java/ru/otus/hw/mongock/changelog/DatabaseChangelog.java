package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;

@ChangeLog
public class DatabaseChangelog {

    private static final String ALEXA_NOVIKOVA = "Alexandra Novikova";

    @ChangeSet(order = "001", id = "dropDb", author = ALEXA_NOVIKOVA, runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

}