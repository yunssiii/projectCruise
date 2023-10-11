package com.cruise.project_cruise.service;

import com.cruise.project_cruise.dto.TemplateDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CrewDetailService {
    // Mapper Interface를 그대로 가져와주기.

    /**
     * 회원이 크루를 탈퇴할 때, 선원테이블에서 해당 회원의 데이터를 삭제함.
     */
    public void deleteCrewMember
            (@Param("memberEmail") String cmemEmail, @Param("crewNum") int crewNum) throws Exception;

}
