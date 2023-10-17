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

        int num = Integer.parseInt(request.getParameter("num"));
        int crewNum = Integer.parseInt(request.getParameter("crewNum"));
        String userName = crewBoardService.getUserName(userEmail);

        if(userEmail != null) {
            int maxNum = crewCommentService.maxNum();
            dto.setComment_num(maxNum + 1);
            dto.setCrew_num(crewNum);
            dto.setBoard_num(num);
            dto.setEmail(userEmail);
            dto.setName(userName);

            crewCommentService.insertData(dto);

            return "InsertComment";
        } else {
            return "InsertCommentFail";
        }

    }

    @PostMapping("delete")
    public String deleteComment(HttpServletRequest request) throws Exception {

        int comment_num = Integer.parseInt(request.getParameter("comment_num"));

        crewCommentService.deleteData(comment_num);

        return "DeleteComment";
    }

    @PostMapping("update")
    public String updateComment(CrewCommentDTO dto, HttpServletRequest request) throws Exception {

        int num = Integer.parseInt(request.getParameter("commentNum"));
        String content = request.getParameter("commentContent");

        dto.setComment_num(num);
        dto.setComment_content(content);
        crewCommentService.updateData(dto);

        return "UpdateComment";
    }

    @PostMapping("insertReply")
    public String insertCommentReply(CrewCommentDTO dto, HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();
        String userEmail = (String)session.getAttribute("email");

        int num = Integer.parseInt(request.getParameter("num"));
        int crewNum = Integer.parseInt(request.getParameter("crewNum"));
        String userName = crewBoardService.getUserName(userEmail);

        int CommentNum = Integer.parseInt(request.getParameter("commentNum"));
        String content = request.getParameter("replyContent");

        if(userEmail != null) {
            int maxNum = crewCommentService.maxNum();
            dto.setComment_num(maxNum + 1);
            dto.setCrew_num(crewNum);
            dto.setBoard_num(num);
            dto.setEmail(userEmail);
            dto.setName(userName);
            dto.setComment_content(content);
            dto.setRef_no(CommentNum);

            crewCommentService.insertCommentReply(dto);

            return "InsertReply";
        } else {
            return "InsertReplyFail";
        }

    }
}
