package com.cruise.project_cruise.mapper;

import com.cruise.project_cruise.dto.TemplateDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CrewDetailMapper {
    public void deleteCrewMember
            (@Param("memberEmail") String cmemEmail, @Param("crewNum") int crewNum) throws Exception;

}
