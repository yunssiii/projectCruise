package com.cruise.project_cruise.controller.board;

import com.cruise.project_cruise.dto.CrewCommentDTO;
import com.cruise.project_cruise.service.CrewBoardService;
import com.cruise.project_cruise.service.CrewCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RequestMapping("/board/comment/")
@RestController
public class CrewCommentController {

    @Autowired
    private CrewCommentService crewCommentService;
    @Autowired
    private CrewBoardService crewBoardService;

    @GetMapping("list")
    public List<CrewCommentDTO> commentList(@RequestParam("num") int num) throws Exception {
        return crewCommentService.getLists(num);
    }

    @GetMapping("count")
    public int commentCount(@RequestParam("num") int num) throws Exception {
        return crewCommentService.getDataCount(num);
    }

    @PostMapping("create")
    public String createComment(CrewCommentDTO dto, HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();
        String userEmail = (String)session.getAttribute("email");

        int maxNum = crewCommentService.maxNum();
        String userName = crewBoardService.getUserName(userEmail);

        if(userEmail != null) {
            dto.setComment_num(maxNum + 1);
            dto.setCrew_num(dto.getCrew_num());
            dto.setBoard_num(dto.getBoard_num());
            dto.setEmail(userEmail);
            dto.setName(userName);

            crewCommentService.insertData(dto);

            return "InsertComment";
        } else {
            return "InsertCommentFail";
        }

    }

    @PostMapping("delete")
    public String deleteComment(CrewCommentDTO dto) throws Exception {
        crewCommentService.deleteData(dto.getComment_num());

        return "DeleteComment";
    }

    @PostMapping("update")
    public String updateComment(CrewCommentDTO dto) throws Exception {
        dto.setComment_num(dto.getComment_num());
        dto.setComment_content(dto.getComment_content());
        crewCommentService.updateData(dto);

        return "UpdateComment";
    }

    @PostMapping("insertReply")
    public String insertCommentReply(CrewCommentDTO dto, HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();
        String userEmail = (String)session.getAttribute("email");

        String userName = crewBoardService.getUserName(userEmail);

        if(userEmail != null) {
            int maxNum = crewCommentService.maxNum();
            dto.setComment_num(maxNum + 1);
            dto.setCrew_num(dto.getCrew_num());
            dto.setBoard_num(dto.getBoard_num());
            dto.setEmail(userEmail);
            dto.setName(userName);
            dto.setComment_content(dto.getComment_content());
            dto.setRef_no(dto.getRef_no());

            crewCommentService.insertCommentReply(dto);

            return "InsertReply";
        } else {
            return "InsertReplyFail";
        }

    }
}
