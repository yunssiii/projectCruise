package com.cruise.project_cruise.service;

import com.cruise.project_cruise.dto.TemplateDTO;
import org.apache.ibatis.annotations.Mapper;

//@Service
public interface TemplateService {

    public int selectMethod() throws Exception;
    public void insertMethod(TemplateDTO dto) throws Exception;

    // Mapper Interface를 그대로 가져와주기.

}
