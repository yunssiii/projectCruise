package com.cruise.project_cruise.controller;

import com.cruise.project_cruise.dto.CrewBoardDTO;
import com.cruise.project_cruise.dto.CrewCommentDTO;
import com.cruise.project_cruise.service.CrewBoardService;
import com.cruise.project_cruise.service.CrewCommentService;
import com.cruise.project_cruise.util.CrewBoardUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;


@RestController
public class CrewBoardController {

	@Autowired
	private CrewBoardService crewBoardService;
	@Autowired
	private CrewCommentService crewCommentService;

	@Autowired
	CrewBoardUtil myUtil;

	@GetMapping("/board/created")
	public ModelAndView created() throws Exception {

		ModelAndView mav = new ModelAndView();

		mav.setViewName("board/created");

		return mav;
	}

	@PostMapping("/board/created")
	public ModelAndView created_ok(CrewBoardDTO dto,
								   HttpServletRequest request) throws Exception {

		HttpSession session = request.getSession();
		CrewBoardDTO userInfo = (CrewBoardDTO) session.getAttribute("userInfo");

		ModelAndView mav = new ModelAndView();

		int maxNum = crewBoardService.maxNum();

		dto.setBoard_num(maxNum + 1);

		if(userInfo == null) {
			mav.setViewName("redirect:/board/list");
			return mav;
		}

		dto.setCrew_num(userInfo.getCrew_num());
		dto.setEmail(userInfo.getEmail());
		dto.setName(userInfo.getName());

		crewBoardService.insertData(dto);


		mav.setViewName("redirect:/board/list");

		return mav;
	}

	@RequestMapping(value = "/board/list", method = {RequestMethod.POST,RequestMethod.GET})
	public ModelAndView list(HttpServletRequest request) throws Exception {

		/*
		 * Test: crew_num, name 설정
		 * */
		HttpSession session = request.getSession();

		CrewBoardDTO userInfo = new CrewBoardDTO();
		userInfo.setCrew_num(2);
		userInfo.setName("모임원");
		userInfo.setEmail("java@gmail.com");
		session.setAttribute("userInfo", userInfo);

		userInfo = (CrewBoardDTO) session.getAttribute("userInfo");

		String pageNum = request.getParameter("pageNum");

		int currentPage = 1;

		if(pageNum != null) {
			currentPage = Integer.parseInt(pageNum);
		}

		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");

		if(searchValue == null) {
			searchKey = "board_subject";
			searchValue = "";
		} else {
			if(request.getMethod().equalsIgnoreCase("GET")) {
				searchValue = URLDecoder.decode(searchValue, "UTF-8");
			}
		}

		int dataCount = crewBoardService.getDataCount(searchKey, searchValue, userInfo.getCrew_num());

		int numPerPage = 10;
		int totalPage = myUtil.getPageCount(numPerPage, dataCount);

		if(currentPage > totalPage)
			currentPage = totalPage;

//		int start = (currentPage - 1) * numPerPage + 1;
//		int end = currentPage * numPerPage;

		int start = dataCount - numPerPage * (currentPage - 1);
		int end = dataCount - (currentPage * numPerPage) + 1;

		List<CrewBoardDTO> lists = crewBoardService.getLists(start, end,
												searchKey, searchValue, userInfo.getCrew_num());

		String param = "";
		if(searchValue != null && !searchValue.equals("")) {
			param += "searchKey=" + searchKey;
			param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
		}

		String listUrl = "/board/list";

		if(!param.equals(""))
			listUrl = listUrl + "?" + param;

		String pageIndexList = myUtil.pageIndexList(currentPage, totalPage, listUrl);

		String articleUrl ="/board/article?pageNum=" + currentPage;

		if(!param.equals(""))
			articleUrl = articleUrl + "&" + param;


		ModelAndView mav = new ModelAndView();

		mav.setViewName("board/list");

		mav.addObject("crew_num", userInfo.getCrew_num());

		mav.addObject("lists", lists);
		mav.addObject("pageIndexList", pageIndexList);
		mav.addObject("dataCount", dataCount);
		mav.addObject("articleUrl", articleUrl);
		mav.addObject("pageNum", currentPage);

		return mav;
	}

	@RequestMapping(value = "/board/article", method = {RequestMethod.POST,RequestMethod.GET})
	public ModelAndView article(HttpServletRequest request) throws Exception {

		HttpSession session = request.getSession();
		CrewBoardDTO userInfo = (CrewBoardDTO) session.getAttribute("userInfo");

		int num = Integer.parseInt(request.getParameter("num"));
		String pageNum = request.getParameter("pageNum");
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");

		if(searchValue != null) {
			searchValue = URLDecoder.decode(searchValue, "UTF-8");
		} else {
			searchKey = "board_subject";
			searchValue = "";
		}

		crewBoardService.updateHitCount(num);	// 조회수 증가

		CrewBoardDTO dto = crewBoardService.getReadData(num);

		if(dto == null) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("redirect:/board/list?pageNum=" + pageNum);

			return mav;
		}

		String param = "pageNum=" + pageNum;
		if(searchValue != null && !searchValue.equals("")) {
			param += "&searchKey=" + searchKey;
			param += "&searchValue=" + URLEncoder.encode(searchValue,"UTF-8");
		}

		ModelAndView mav = new ModelAndView();

		mav.addObject("userInfo", userInfo);

		mav.addObject("dto", dto);
		mav.addObject("params", param);

		// 댓글 불러오기------------------------------
		List<CrewCommentDTO> lists = crewCommentService.getLists(dto.getBoard_num());

		mav.addObject("lists", lists);
		// ------------------------------댓글 불러오기

		mav.setViewName("board/article");

		return mav;

	}

	@RequestMapping(value = "/board/updated",
			method = {RequestMethod.POST,RequestMethod.GET})
	public ModelAndView updated(HttpServletRequest request) throws Exception {

		int num = Integer.parseInt(request.getParameter("board_num"));
		String pageNum = request.getParameter("pageNum");

		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");

		if(searchValue != null) {
			searchValue = URLDecoder.decode(searchValue, "UTF-8");
		}

		CrewBoardDTO dto = crewBoardService.getReadData(num);

		if(dto == null) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("redirect:/board/list?pageNum=" + pageNum);

			return mav;
		}

		String param = "pageNum=" + pageNum;
		if(searchValue != null && !searchValue.equals("")) {
			param += "&searchKey=" + searchKey;
			param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
		}

		ModelAndView mav = new ModelAndView();
		mav.setViewName("board/updated");

		mav.addObject("dto", dto);
		mav.addObject("pageNum", pageNum);
		mav.addObject("params", param);
		mav.addObject("searchKey", searchKey);
		mav.addObject("searchValue", searchValue);


		return mav;
	}

	@RequestMapping(value = "/board/updated_ok",
			method = RequestMethod.POST)
	public ModelAndView updated_ok(CrewBoardDTO dto, HttpServletRequest request) throws Exception {

		HttpSession session = request.getSession();
		CrewBoardDTO userInfo = (CrewBoardDTO) session.getAttribute("userInfo");

		ModelAndView mav = new ModelAndView();

		if(userInfo == null) {
			mav.setViewName("redirect:/board/list");
			return mav;
		}

		String pageNum = request.getParameter("pageNum");
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");

		String param = "pageNum=" + pageNum;

		if(searchValue != null && !searchValue.equals("")) {
			param += "&searchKey=" + searchKey;
			param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
		}

		dto.setCrew_num(userInfo.getCrew_num());
		dto.setEmail(userInfo.getEmail());
		dto.setName(userInfo.getName());

		crewBoardService.updateData(dto);

		mav.setViewName("redirect:/board/list?" + param);

		return mav;

	}

	@RequestMapping(value = "/board/deleted_ok",
			method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView deleted_ok(HttpServletRequest request) throws Exception {

		HttpSession session = request.getSession();
		CrewBoardDTO userInfo = (CrewBoardDTO) session.getAttribute("userInfo");

		int num = Integer.parseInt(request.getParameter("board_num"));
		String pageNum = request.getParameter("pageNum");
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");

		crewBoardService.deleteData(num);

		String param = "pageNum=" + pageNum;

		if(searchValue != null && !searchValue.equals("")) {
			param += "&searchKey=" + searchKey;
			param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
		}

		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/board/list?" + param);

		return mav;

	}

}
