package com.jindognoob.sermon.service;

import java.util.List;

import com.jindognoob.sermon.dto.AnswerDTO;
import com.jindognoob.sermon.dto.QuestionDTO;
import com.jindognoob.sermon.domain.etypes.QuestionStatusType;
import com.jindognoob.sermon.dto.Paging;
import com.jindognoob.sermon.service.exceptions.AnswerDoesNotBelongsToQuestionException;
import com.jindognoob.sermon.service.exceptions.ContentAuthorizationViolationException;
import com.jindognoob.sermon.service.exceptions.QuestionStatusRuleViolationException;

public interface BoardService {
    
    public Long addQuestion(String principal, String title, String content, String hashTags);

    public void deleteQuestion(String principal, Long questionId) throws ContentAuthorizationViolationException, QuestionStatusRuleViolationException;
    public QuestionDTO getQuestion(Long id);

    public Long addAnswer(String principal, String title, String content, Long questionId) throws QuestionStatusRuleViolationException ;

    public void deleteAnswer(String principal, Long answerId) throws ContentAuthorizationViolationException, QuestionStatusRuleViolationException;
    public AnswerDTO getAnswer(Long id);


    public List<QuestionDTO> getQuestions(Paging paging, String hashTags);
    public List<QuestionDTO> getQuestions(Paging paging, long lastIndex, String hashTags);
    public List<QuestionDTO> getQuestions(Paging paging, QuestionStatusType type);
    public List<QuestionDTO> getMyQuestions(String principal, Paging paging);
    public List<QuestionDTO> getMyQuestions(String principal, Paging paging, QuestionStatusType type);

    public List<AnswerDTO> getAnswersOfQuestion(Long questionId);


    public void adoptAnswer(String principal, Long questionId, Long answerId) throws ContentAuthorizationViolationException, QuestionStatusRuleViolationException, AnswerDoesNotBelongsToQuestionException;
    
    public void thumbsUpToAnswer(Long answerId);
    public void modifyQuestion(String principal, Long questionId, String title, String content) throws ContentAuthorizationViolationException;
    public void modifyAnswer(String principal, Long answerId, String title, String content) throws ContentAuthorizationViolationException;

    public List<String> findCandidateHashTags(String letter);
}
