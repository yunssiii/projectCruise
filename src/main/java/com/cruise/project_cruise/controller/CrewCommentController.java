package com.cruise.project_cruise.controller;

import com.cruise.project_cruise.dto.CrewBoardDTO;
import com.cruise.project_cruise.dto.CrewCommentDTO;
import com.cruise.project_cruise.service.CrewBoardService;
import com.cruise.project_cruise.service.CrewCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequestMapping("/board/comment/")
@RestController
public class CrewCommentController {

    @Autowired
    private CrewCommentService crewCommentService;
    @Autowired
    private CrewBoardService crewBoardService;


    @PostMapping("create")
    public String insertComment(CrewCommentDTO dto, HttpServletRequest request) throws Exception {

        int num = Integer.parseInt(request.getParameter("num"));

        int maxNum = crewCommentService.maxNum();
        dto.setComment_num(maxNum + 1);

        HttpSession session = request.getSession();
        CrewBoardDTO userInfo = (CrewBoardDTO) session.getAttribute("userInfo");

        if(userInfo != null) {
            dto.setCrew_num(userInfo.getCrew_num());
            dto.setBoard_num(num);
            dto.setEmail(userInfo.getEmail());
            dto.setName(userInfo.getName());

            crewCommentService.insertData(dto);

            return "userInfoOK";
        } else {
            return "userInfoNull";
        }

    }

    @PostMapping("delete")
    public String deleteComment(HttpServletRequest request) throws Exception {

        int comment_num = Integer.parseInt(request.getParameter("comment_num"));

        HttpSession session = request.getSession();
        CrewBoardDTO userInfo = (CrewBoardDTO) session.getAttribute("userInfo");

        if(userInfo != null) {
            crewCommentService.deleteData(comment_num);

            return "userInfoOK";
        } else {
            return "userInfoNull";
        }
    }

    @PostMapping("update")
    public String updateComment(CrewCommentDTO dto, HttpServletRequest request) throws Exception {

        int num = Integer.parseInt(request.getParameter("commentNum"));
        String content = request.getParameter("commentContent");

        HttpSession session = request.getSession();
        CrewBoardDTO userInfo = (CrewBoardDTO) session.getAttribute("userInfo");

        if(userInfo != null) {
            dto.setComment_num(num);
            dto.setComment_content(content);
            crewCommentService.updateData(dto);

            return "userInfoOK";
        } else {
            return "userInfoNull";
        }

    }
}
