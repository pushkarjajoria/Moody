package com.example.moody.metadata;

import java.util.Optional;

public interface ActivityQueue {

    public boolean addActivity(KeyboardActivity keyboardActivity);

    public Optional<KeyboardActivity> getActivity();

    public Optional<KeyboardActivity> peekActivity();
}

