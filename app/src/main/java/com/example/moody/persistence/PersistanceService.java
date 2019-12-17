package com.example.moody.persistence;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.moody.metadata.Metadata;

public interface PersistanceService {

    long persistMetadata(Metadata metadata, SQLiteDatabase database);

}
