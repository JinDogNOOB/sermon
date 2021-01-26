package com.jindognoob.sermon.service;

import java.util.Calendar;
import java.util.List;

import com.jindognoob.sermon.domain.Account;
import com.jindognoob.sermon.domain.Answer;
import com.jindognoob.sermon.domain.HashTag;
import com.jindognoob.sermon.domain.Question;
import com.jindognoob.sermon.domain.QuestionHashTag;
import com.jindognoob.sermon.domain.etypes.QuestionStatusType;
import com.jindognoob.sermon.dto.AnswerDTO;
import com.jindognoob.sermon.dto.Paging;
import com.jindognoob.sermon.dto.QuestionDTO;
import com.jindognoob.sermon.repository.AccountRepository;
import com.jindognoob.sermon.repository.AnswerRepository;
import com.jindognoob.sermon.repository.HashTagRepositoty;
import com.jindognoob.sermon.repository.QuestionHashTagRepository;
import com.jindognoob.sermon.repository.QuestionRepository;
import com.jindognoob.sermon.service.constants.PointRule;
import com.jindognoob.sermon.service.exceptions.AnswerDoesNotBelongsToQuestionException;
import com.jindognoob.sermon.service.exceptions.ContentAuthorizationViolationException;
import com.jindognoob.sermon.service.exceptions.QuestionStatusRuleViolationException;
import com.jindognoob.sermon.utils.HashTagParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class BoardServiceJPAImpl implements BoardService{
    @Autowired AnswerRepository answerRepository;
    @Autowired QuestionRepository questionRepository;
    @Autowired AccountRepository accountRepository;
    @Autowired HashTagRepositoty hashTagRepositoty;
    @Autowired QuestionHashTagRepository questionHashTagRepository;

    @Override
    public Long addQuestion(String principal, String title, String content, String hashTags){
        Account account = accountRepository.findOneByEmail(principal);

        Question question = new Question();
        question.setAccount(account);
        question.setTitle(title);
        question.setContent(content);
        question.setStatus(QuestionStatusType.ACTIVE);
        question.setCreatedDate(Calendar.getInstance().getTime());
        question.setViewCount((long)0);
        questionRepository.save(question);

        // hashtag 등록 
        List<String> hashTagList = HashTagParser.parseHashTagsString(hashTags);
        for(String s : hashTagList){
            HashTag hashTag = new HashTag();
            hashTag.setTag(s);
            hashTagRepositoty.save(hashTag);

            QuestionHashTag questionHashTag = new QuestionHashTag();
            questionHashTag.setHashTag(hashTag);
            questionHashTag.setQuestion(question);
            questionHashTagRepository.save(questionHashTag);
        }

        return question.getId();
    }
    

    @Override
    public void deleteQuestion(String principal, Long questionId) throws ContentAuthorizationViolationException,
            QuestionStatusRuleViolationException {
        Account account = accountRepository.findOneByEmail(principal);
        Question question = questionRepository.findOne(questionId);
        if(account.getId() != question.getAccount().getId()) 
            throw new ContentAuthorizationViolationException("컨텐트 권한자가 아님");
        if(question.getStatus() == QuestionStatusType.CLOSED)
            throw new QuestionStatusRuleViolationException("CLOSED된 QUESTION은 삭제할 수 없음");
        questionRepository.delete(question);
    }
    @Override
    public QuestionDTO getQuestion(Long id){
        Question question = questionRepository.findOne(id);
        if(question != null)
            question.setViewCount(question.getViewCount()+1);
        return QuestionDTO.of(question);
    }

    @Override
    public Long addAnswer(String principal, String title, String content, Long questionId)
            throws QuestionStatusRuleViolationException {
        Account account = accountRepository.findOneByEmail(principal);
        Question question = questionRepository.findOne(questionId);

        if(question.getStatus() == QuestionStatusType.CLOSED)
            throw new QuestionStatusRuleViolationException("CLOSED된 Quetion에는 Answer를 등록할 수 없음");
        if(question.getAccount() == account)
            throw new IllegalStateException("자문자답할 수 없음");

        Answer answer = new Answer();
        answer.setAccount(account);
        answer.setQuestion(question);
        answer.setTitle(title);
        answer.setContent(content);
        answer.setAdopted(false);
        answer.setCreatedDate(Calendar.getInstance().getTime());
        answer.setStar((long)0);

        answerRepository.save(answer);
        return answer.getId();
    }
    
    @Override
    public void deleteAnswer(String principal, Long answerId) throws ContentAuthorizationViolationException,
            QuestionStatusRuleViolationException {
        Account account = accountRepository.findOneByEmail(principal);
        Answer answer = answerRepository.findOne(answerId);
        if(answer.getAccount().getId() != account.getId())
            throw new ContentAuthorizationViolationException("컨텐트 권한자가 아님");
        if(answer.getQuestion().getStatus() == QuestionStatusType.CLOSED)
            throw new QuestionStatusRuleViolationException("CLOSED된 Quetion에 등록된 Answer는 삭제할 수 없음");
        answerRepository.delete(answer);
    }
    @Override
    public AnswerDTO getAnswer(Long id){
        return AnswerDTO.of(answerRepository.findOne(id));
    }

    @Override
    public List<QuestionDTO> getQuestions(Paging paging, String hashTags){
        return QuestionDTO.of(questionRepository.findPage(paging, HashTagParser.parseHashTagsString(hashTags)));
    }
    @Override
    public List<QuestionDTO> getQuestions(Paging paging, long lastIndex, String hashTags){
        return QuestionDTO.of(questionRepository.findPage(paging, lastIndex, HashTagParser.parseHashTagsString(hashTags)));
    }
    @Override
    public List<QuestionDTO> getQuestions(Paging paging, QuestionStatusType type){
        return QuestionDTO.of(questionRepository.findPage(paging, type));
    }
    @Override
    public List<QuestionDTO> getMyQuestions(String principal, Paging paging){
        Account account = accountRepository.findOneByEmail(principal);
        return QuestionDTO.of(questionRepository.findMyQuestionAsPage(account, paging));
    }
    @Override
    public List<QuestionDTO> getMyQuestions(String principal, Paging paging, QuestionStatusType type){
        Account account = accountRepository.findOneByEmail(principal);
        return QuestionDTO.of(questionRepository.findMyQuestionAsPage(account, paging, type));
    }

    @Override
    public List<AnswerDTO> getAnswersOfQuestion(Long questionId){
        Question question = questionRepository.findOne(questionId);
        return AnswerDTO.of(answerRepository.findAllByQuestion(question));
    }

    /**
     * 답변 채택
     * 
     * @throws QuestionStatusRuleViolationException
     * @throws AnswerDoesNotBelongsToQuestionException
     * 
     * @throws Exception
     */
    @Override
    public void adoptAnswer(String principal, Long questionId, Long answerId)
            throws ContentAuthorizationViolationException, QuestionStatusRuleViolationException,
            AnswerDoesNotBelongsToQuestionException {
        Account account = accountRepository.findOneByEmail(principal);
        Question question = questionRepository.findOne(questionId);
        Answer answer = answerRepository.findOne(answerId);

        log.info("questionId" + questionId);
        log.info("accountId" + account.getId());
        log.info("question.getAccount().getId()" + question.getAccount().getId());


        if(account.getId() != question.getAccount().getId()) 
            throw new ContentAuthorizationViolationException("컨텐트 권한자가 아님");
        if(question.getStatus() == QuestionStatusType.CLOSED)
            throw new QuestionStatusRuleViolationException("CLOSED된 QUESTION에서는 더이상 채택기능을 사용할 수 없음");
        if(answer.getQuestion() != question)
            throw new AnswerDoesNotBelongsToQuestionException("채택하려고 하는 답변이 해당 질문에 등록된 것이 아님");

        question.setStatus(QuestionStatusType.CLOSED);
        answer.setAdopted(true);
        answer.getAccount().getPoint().addPoint(PointRule.pointForAnswerWriter);
        account.getPoint().addPoint(PointRule.pointForQuetionWriter);
    }

    @Override
    public void thumbsUpToAnswer(Long answerId){
        Answer answer = answerRepository.findOne(answerId);
        answer.setStar(answer.getStar() + 1);
        return;
    }
    @Override
    public void modifyQuestion(String principal, Long questionId, String title, String content)
            throws ContentAuthorizationViolationException {
        Account account = accountRepository.findOneByEmail(principal);
        Question question = questionRepository.findOne(questionId);
        if(account.getId() != question.getAccount().getId()) 
            throw new ContentAuthorizationViolationException("컨텐트 권한자가 아님");
        question.setTitle(title);
        question.setContent(content);
    }
    @Override
    public void modifyAnswer(String principal, Long answerId, String title, String content)
            throws ContentAuthorizationViolationException {
        Account account = accountRepository.findOneByEmail(principal);
        Answer answer = answerRepository.findOne(answerId);
        if(account.getId() != answer.getAccount().getId()) 
            throw new ContentAuthorizationViolationException("컨텐트 권한자가 아님");
        answer.setTitle(title);
        answer.setContent(content);
    }
}
