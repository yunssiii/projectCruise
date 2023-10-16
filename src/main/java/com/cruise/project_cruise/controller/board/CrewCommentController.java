package com.cruise.project_cruise.controller.board;

import com.cruise.project_cruise.dto.CrewBoardDTO;
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

    @PostMapping("insertReply")
    public String insertCommentReply(CrewCommentDTO dto, HttpServletRequest request) throws Exception {

        int num = Integer.parseInt(request.getParameter("num"));
        int CommentNum = Integer.parseInt(request.getParameter("commentNum"));
        String content = request.getParameter("replyContent");

        HttpSession session = request.getSession();
        CrewBoardDTO userInfo = (CrewBoardDTO) session.getAttribute("userInfo");

        if(userInfo != null) {
            int maxNum = crewCommentService.maxNum();

            dto.setComment_num(maxNum + 1);
            dto.setCrew_num(userInfo.getCrew_num());
            dto.setBoard_num(num);
            dto.setEmail(userInfo.getEmail());
            dto.setName(userInfo.getName());
            dto.setComment_content(content);
            dto.setRef_no(CommentNum);

            crewCommentService.insertCommentReply(dto);

            return "userInfoOK";
        } else {
            return "userInfoNull";
        }

    }
}
