package com.jindognoob.sermon.service;

import java.util.List;

import com.jindognoob.sermon.domain.HashTag;
import com.jindognoob.sermon.repository.HashTagRepositoty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class HashTagServiceJPAImpl implements HashTagService {

    @Autowired HashTagRepositoty hashTagRepositoty;
    @Override
    public List<HashTag> findCandidateTags(String letter) {
        return hashTagRepositoty.findCandidateTags(letter);
    }  
}
