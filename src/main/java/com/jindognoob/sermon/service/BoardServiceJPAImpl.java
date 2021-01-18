package com.jindognoob.sermon.service;

import java.util.Calendar;
import java.util.List;

import com.jindognoob.sermon.domain.Account;
import com.jindognoob.sermon.domain.Answer;
import com.jindognoob.sermon.domain.Question;
import com.jindognoob.sermon.domain.etypes.QuestionStatusType;
import com.jindognoob.sermon.dto.Paging;
import com.jindognoob.sermon.repository.AccountRepository;
import com.jindognoob.sermon.repository.AnswerRepository;
import com.jindognoob.sermon.repository.QuestionRepository;
import com.jindognoob.sermon.service.constants.PointRule;
import com.jindognoob.sermon.service.exceptions.ContentAuthorizationViolationException;
import com.jindognoob.sermon.service.exceptions.PasswordPolicyViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BoardServiceJPAImpl implements BoardService{
    @Autowired AnswerRepository answerRepository;
    @Autowired QuestionRepository questionRepository;
    @Autowired AccountRepository accountRepository;
    
    @Override
    public Long addQuestion(String principal, String title, String content){
        Account account = accountRepository.findOneByEmail(principal);

        Question question = new Question();
        question.setAccount(account);
        question.setTitle(title);
        question.setContent(content);
        question.setStatus(QuestionStatusType.ACTIVE);
        question.setCreatedDate(Calendar.getInstance().getTime());

        questionRepository.save(question);
        return question.getId();
    }
    

    @Override
    public void deleteQuestion(String principal, Long questionId) throws ContentAuthorizationViolationException, Exception{
        Account account = accountRepository.findOneByEmail(principal);
        Question question = questionRepository.findOne(questionId);
        if(account.getId() != question.getAccount().getId()) 
            throw new ContentAuthorizationViolationException("컨텐트 권한자가 아님");
        if(question.getStatus() == QuestionStatusType.CLOSED)
            throw new Exception("CLOSED된 QUESTION은 삭제할 수 없음");
        questionRepository.delete(question);
    }
    @Override
    public Question getQuestion(Long id){
        return questionRepository.findOne(id);
    }

    @Override
    public Long addAnswer(String principal, String title, String content, Long questionId) throws Exception {
        Account account = accountRepository.findOneByEmail(principal);
        Question question = questionRepository.findOne(questionId);

        if(question.getStatus() == QuestionStatusType.CLOSED)
            throw new Exception("CLOSED된 Quetion에는 Answer를 등록할 수 없음");

        Answer answer = new Answer();
        answer.setAccount(account);
        answer.setQuestion(question);
        answer.setTitle(title);
        answer.setContent(content);
        answer.setAdopted(false);
        answer.setCreatedDate(Calendar.getInstance().getTime());

        answerRepository.save(answer);
        return answer.getId();
    }
    
    @Override
    public void deleteAnswer(String principal, Long answerId) throws ContentAuthorizationViolationException, Exception {
        Account account = accountRepository.findOneByEmail(principal);
        Answer answer = answerRepository.findOne(answerId);
        if(answer.getAccount().getId() != account.getId())
            throw new ContentAuthorizationViolationException("컨텐트 권한자가 아님");
        if(answer.getQuestion().getStatus() == QuestionStatusType.CLOSED)
            throw new Exception("CLOSED된 Quetion에 등록된 Answer는 삭제할 수 없음");
        answerRepository.delete(answer);
    }
    @Override
    public Answer getAnswer(Long id){
        return answerRepository.findOne(id);
    }

    @Override
    public List<Question> getQuestions(Paging paging){
        return questionRepository.findPage(paging);
    }
    @Override
    public List<Question> getQuestions(Paging paging, QuestionStatusType type){
        return questionRepository.findPage(paging, type);
    }
    @Override
    public List<Question> getMyQuestions(String principal, Paging paging){
        Account account = accountRepository.findOneByEmail(principal);
        return questionRepository.findMyQuestionAsPage(account, paging);
    }
    @Override
    public List<Question> getMyQuestions(String principal, Paging paging, QuestionStatusType type){
        Account account = accountRepository.findOneByEmail(principal);
        return questionRepository.findMyQuestionAsPage(account, paging, type);
    }

    @Override
    public List<Answer> getAnswersOfQuestion(Long questionId){
        Question question = questionRepository.findOne(questionId);
        return answerRepository.findAllByQuestion(question);
    }

    /**
     * 답변 채택
     * 
     * @throws Exception
     */
    @Override
    public void adoptAnswer(String principal, Long questionId, Long answerId) throws Exception, ContentAuthorizationViolationException{
        Account account = accountRepository.findOneByEmail(principal);
        Question question = questionRepository.findOne(questionId);
        Answer answer = answerRepository.findOne(answerId);

        if(account.getId() != question.getAccount().getId()) 
            throw new ContentAuthorizationViolationException("컨텐트 권한자가 아님");
        if(question.getStatus() == QuestionStatusType.CLOSED)
            throw new Exception("CLOSED된 QUESTION에서는 더이상 채택기능을 사용할 수 없음");
        if(answer.getQuestion() != question)
            throw new Exception("채택하려고 하는 답변이 해당 질문에 등록된 것이 아님");

        question.setStatus(QuestionStatusType.CLOSED);
        answer.setAdopted(true);
        answer.getAccount().getPoint().addPoint(PointRule.pointForAnswerWriter);
        account.getPoint().addPoint(PointRule.pointForQuetionWriter);
    }
}
