package com.jindognoob.sermon.utils;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;


public class ModelMapperUtils {
    private static class InstanceHolder{
        private static final ModelMapper instance = getModelMapper();

    }

    private static ModelMapper getModelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
            .setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

    public static ModelMapper getInstance(){
        return InstanceHolder.instance;
    }
    
}
