package com.ems.alga.posts.post.service.common;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochGenerator;

import java.util.UUID;

public class UuidGenerator {

    private static final TimeBasedEpochGenerator TIME_BASED_EPOCH_GENERATOR = Generators.timeBasedEpochGenerator();

    private UuidGenerator() {}

    public static UUID generateTimeBasedUUID() {
        return TIME_BASED_EPOCH_GENERATOR.generate();
    }

}
