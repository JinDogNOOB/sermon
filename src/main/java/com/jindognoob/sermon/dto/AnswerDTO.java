package com.jindognoob.sermon.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.jindognoob.sermon.domain.Answer;
import com.jindognoob.sermon.utils.ModelMapperUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerDTO {
    private Long id;

    private String title;
    private String content;
    private Date createdDate;

    private Long accountId;
    private Long questionId;

    private boolean isAdopted;

    private Long star;
    private String nickname;


    public static AnswerDTO of(Answer answer){
        if(answer == null) return null;
        AnswerDTO answerDTO = ModelMapperUtils.getInstance().map(answer, AnswerDTO.class);
        
        answerDTO.setAccountId(answer.getAccount().getId());
        answerDTO.setQuestionId(answer.getQuestion().getId());
        answerDTO.setNickname(answer.getAccount().getNickname());
        
        return answerDTO;
    }

    // 아마 accountID는 포함안될거임 실제 보고 필요하면 추가 
    public static List<AnswerDTO> of(List<Answer> answers){
        if(answers == null) return null;
       /*  return answers.stream()
        .map(answerEntity -> ModelMapperUtils.getInstance().map(answerEntity, AnswerDTO.class))
        .collect(Collectors.toList()); */
        List<AnswerDTO> result = new ArrayList<AnswerDTO>();
        for(Answer answer : answers){
            result.add(AnswerDTO.of(answer));
        }
        return result;
    }
}
