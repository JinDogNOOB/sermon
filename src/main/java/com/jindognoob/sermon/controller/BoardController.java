package com.jindognoob.sermon.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.jindognoob.sermon.dto.AnswerDTO;
import com.jindognoob.sermon.dto.Paging;
import com.jindognoob.sermon.dto.QuestionDTO;
import com.jindognoob.sermon.service.BoardService;
import com.jindognoob.sermon.service.exceptions.AnswerDoesNotBelongsToQuestionException;
import com.jindognoob.sermon.service.exceptions.ContentAuthorizationViolationException;
import com.jindognoob.sermon.service.exceptions.QuestionStatusRuleViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RestController
@RequestMapping(value = "/board")
public class BoardController {
    @Autowired
    BoardService boardService;

    // ######################################################################

    // 질문 리스트
    @RequestMapping(value = "/question", method = RequestMethod.GET)
    public List<QuestionDTO> getQuestions(Paging paging, @RequestParam("hashTags") String hashTags) {
        log.info("paging.number : " + paging.getPageNumber());
        log.info("paging.size : " + paging.getPageSize());
        return boardService.getQuestions(paging, hashTags);
    }

    // 질문 리스트
    @RequestMapping(value = "/question/more", method = RequestMethod.GET)
    public List<QuestionDTO> getQuestionsMore(Paging paging, @RequestParam("lastIndex")long lastIndex, @RequestParam("hashTags") String hashTags) {
        log.info("paging.number : " + paging.getPageNumber());
        log.info("paging.size : " + paging.getPageSize());
        return boardService.getQuestions(paging, lastIndex, hashTags);
    }

    // 질문 등록
    @RequestMapping(value = "/question", method = RequestMethod.POST)
    public void addQuestion(@RequestParam("title") String title, @RequestParam("content") String content, @RequestParam("hashTags") String hashTags) {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boardService.addQuestion(principal, title, content, hashTags);
        return;
    }

    // ######################################################################

    // 질문 디테일 조회
    @RequestMapping(value = "/question/{questionId}", method = RequestMethod.GET)
    public QuestionDTO getQuestionDetail(@PathVariable long questionId) {
        return boardService.getQuestion(questionId);
    }

    // 답변 채택
    @RequestMapping(value = "/question/{questionId}", method = RequestMethod.POST)
    public void adoptAnswer(@PathVariable long questionId, @RequestParam("answerId") long answerId,
            HttpServletResponse response) {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            boardService.adoptAnswer(principal, questionId, answerId);
        } catch (ContentAuthorizationViolationException e) {
            response.setStatus(400);
        } catch (QuestionStatusRuleViolationException e) {
            response.setStatus(400);
        } catch (AnswerDoesNotBelongsToQuestionException e) {
            response.setStatus(400);
        }
        return;
    }

    // 질문 수정
    @RequestMapping(value = "/question/{questionId}", method = RequestMethod.PUT)
    public void modifyQuestion(@PathVariable long questionId, @RequestParam("title") String title,
            @RequestParam("content") String content, HttpServletResponse response) {
        String principal = getPrincipal();
        try {
            boardService.modifyQuestion(principal, questionId, title, content);
        } catch (ContentAuthorizationViolationException e) {
            response.setStatus(400);
        }
        return;
    }

    // 질문 삭제
    @RequestMapping(value = "/question/{questionId}", method = RequestMethod.DELETE)
    public void deleteQuestion(@PathVariable long questionId, HttpServletResponse response) {
        String principal = getPrincipal();
        try {
            boardService.deleteQuestion(principal, questionId);
        } catch (ContentAuthorizationViolationException e) {
            response.setStatus(400);
        } catch (QuestionStatusRuleViolationException e) {
            response.setStatus(400);
        }
        return;
    }

    // ######################################################################

    // 답변 리스트 조회
    @RequestMapping(value = "/question/{questionId}/answer", method = RequestMethod.GET)
    public List<AnswerDTO> getAnswerList(@PathVariable long questionId) {
        return boardService.getAnswersOfQuestion(questionId);
    }

    // 답변 등록
    @RequestMapping(value = "/question/{questionId}/answer", method = RequestMethod.POST)
    public void addAnswer(@PathVariable long questionId, @RequestParam("title") String title,
            @RequestParam("content") String content, HttpServletResponse response) {
        String principal = getPrincipal();
        try {
            boardService.addAnswer(principal, title, content, questionId);
        } catch (QuestionStatusRuleViolationException e) {
            response.setStatus(400);
        }
        return;
    }

    // ######################################################################

    // 답변 디테일 조회
    @RequestMapping(value = "/question/{questionId}/answer/{answerId}", method = RequestMethod.GET)
    public AnswerDTO getAnswerDetail(@PathVariable long questionId, @PathVariable long answerId) {
        return boardService.getAnswer(answerId);
    }

    // 답변 추천
    @RequestMapping(value = "/question/{questionId}/answer/{answerId}", method = RequestMethod.POST)
    public void thumbsUpToAnswer(@PathVariable long questionId, @PathVariable long answerId) {
        boardService.thumbsUpToAnswer(answerId);
        return;
    }

    // 답변 수정
    @RequestMapping(value = "/question/{questionId}/answer/{answerId}", method = RequestMethod.PUT)
    public void modifyAnswer(@PathVariable long questionId, @PathVariable long answerId,
            @RequestParam("title") String title, @RequestParam("content") String content,
            HttpServletResponse response) {
        String principal = getPrincipal();
        try {
            boardService.modifyAnswer(principal, answerId, title, content);
        } catch (ContentAuthorizationViolationException e) {
            response.setStatus(400);
        }
        return;
    }

    // 답변 삭제
    @RequestMapping(value = "/question/{questionId}/answer/{answerId}", method = RequestMethod.DELETE)
    public void deleteAnswer(@PathVariable long questionId, @PathVariable long answerId, HttpServletResponse response) {
        String principal = getPrincipal();
        try {
            boardService.deleteAnswer(principal, answerId);
        } catch (ContentAuthorizationViolationException | QuestionStatusRuleViolationException e) {
            response.setStatus(400);
        }
        return;
    }



    @RequestMapping(value="/hashtag", method=RequestMethod.GET)
    public List<String> getRelatedHashTag(@RequestParam("letter") String letter) {
        return boardService.findCandidateHashTags(letter);
    }
    






    private String getPrincipal() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


    

}
