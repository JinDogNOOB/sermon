package com.jindognoob.sermon.service;

import java.util.List;

import com.jindognoob.sermon.domain.HashTag;

public interface HashTagService {
    public List<HashTag> findCandidateTags(String letter);
}
