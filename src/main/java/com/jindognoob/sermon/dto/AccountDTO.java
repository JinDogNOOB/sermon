package com.jindognoob.sermon.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.jindognoob.sermon.domain.Account;
import com.jindognoob.sermon.domain.etypes.AccountRoleType;
import com.jindognoob.sermon.domain.etypes.AccountSignupType;
import com.jindognoob.sermon.utils.ModelMapperUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    private Long id;
    private String email;
    private String nickname;
    // private String password;
    private AccountSignupType signupType;
    private AccountRoleType roleType;
    private Long pointAmount;


    /**
     * ModelMapper보다 BeanUtil.copyProperties가 더 빠름
     */
    public static AccountDTO of(Account account){
        if(account ==null) return null;
        // 필드명 동일할시 그냥 map (src, target.class) https://dbbymoon.tistory.com/4 참고
        AccountDTO accountDTO = ModelMapperUtils.getInstance().map(account, AccountDTO.class);
        // 이름이 다른 필드는 직접 Set을 통해 매핑
        
        accountDTO.setPointAmount(account.getPoint().getAmount());
        return accountDTO;
    }

    public static List<AccountDTO> of(List<Account> accounts){
        if(accounts == null) return null;
        return accounts.stream()
        .map(accountEntity -> ModelMapperUtils.getInstance().map(accountEntity, AccountDTO.class))
        .collect(Collectors.toList());
    }

}
