package com.example.moody.persistence;

import com.example.moody.metadata.Metadata;

public interface PersistanceService {

    boolean persistMetadata(Metadata metadata);

}
