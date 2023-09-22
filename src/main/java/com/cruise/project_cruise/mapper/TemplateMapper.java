package com.cruise.project_cruise.mapper;

import com.cruise.project_cruise.dto.TemplateDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TemplateMapper {

    public int selectMethod() throws Exception;
    public void insertMethod(TemplateDTO dto) throws Exception;

}
