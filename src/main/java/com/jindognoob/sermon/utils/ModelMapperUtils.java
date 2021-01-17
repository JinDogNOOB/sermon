package com.jindognoob.sermon.utils;

import org.modelmapper.ModelMapper;

public class ModelMapperUtils {
    private static ModelMapper modelMapper = new ModelMapper();
    public static ModelMapper getInstance(){
        return modelMapper;
    }
    
}
