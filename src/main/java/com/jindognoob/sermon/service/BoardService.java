package com.jindognoob.sermon.service;

import java.util.List;

import com.jindognoob.sermon.domain.Answer;
import com.jindognoob.sermon.domain.Question;
import com.jindognoob.sermon.domain.etypes.QuestionStatusType;
import com.jindognoob.sermon.dto.Paging;
import com.jindognoob.sermon.service.exceptions.AnswerDoesNotBelongsToQuestionException;
import com.jindognoob.sermon.service.exceptions.ContentAuthorizationViolationException;
import com.jindognoob.sermon.service.exceptions.QuestionStatusRuleViolationException;

public interface BoardService {
    
    public Long addQuestion(String principal, String title, String content);

    public void deleteQuestion(String principal, Long questionId) throws ContentAuthorizationViolationException, QuestionStatusRuleViolationException;
    public Question getQuestion(Long id);

    public Long addAnswer(String principal, String title, String content, Long questionId) throws QuestionStatusRuleViolationException ;

    public void deleteAnswer(String principal, Long answerId) throws ContentAuthorizationViolationException, QuestionStatusRuleViolationException;
    public Answer getAnswer(Long id);


    public List<Question> getQuestions(Paging paging);
    public List<Question> getQuestions(Paging paging, QuestionStatusType type);
    public List<Question> getMyQuestions(String principal, Paging paging);
    public List<Question> getMyQuestions(String principal, Paging paging, QuestionStatusType type);

    public List<Answer> getAnswersOfQuestion(Long questionId);


    public void adoptAnswer(String principal, Long questionId, Long answerId) throws ContentAuthorizationViolationException, QuestionStatusRuleViolationException, AnswerDoesNotBelongsToQuestionException;
}
