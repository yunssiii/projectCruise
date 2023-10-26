package com.cruise.project_cruise.controller.board;

import com.cruise.project_cruise.dto.CrewBoardDTO;
import com.cruise.project_cruise.dto.CrewCommentDTO;
import com.cruise.project_cruise.service.CrewBoardService;
import com.cruise.project_cruise.service.CrewCommentService;
import com.cruise.project_cruise.service.CrewSettingService;
import com.cruise.project_cruise.service.MypageService;
import com.cruise.project_cruise.util.CrewBoardUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RequestMapping("/board/")
@RestController
public class CrewBoardController {

	@Autowired
	private CrewBoardService crewBoardService;
	@Autowired
	private CrewCommentService crewCommentService;
	@Autowired
	private MypageService mypageService;
	@Autowired
	private CrewSettingService crewSettingService;

	@Autowired
	CrewBoardUtil myUtil;

	@PostMapping("created")
	public ModelAndView created_ok(CrewBoardDTO dto,
					   @RequestParam(value = "files", required = false) MultipartFile files,
								   HttpServletRequest request) throws Exception {

		// 세션에서 이메일 가져오기
		HttpSession session = request.getSession();
		String userEmail = (String)session.getAttribute("email");

		ModelAndView mav = new ModelAndView();

		if(userEmail == null) {
			mav.setViewName("redirect:/");
			return mav;
		}

		if (!files.isEmpty()) {
			// 이미지 저장 경로
			String upload_path = request.getServletContext().getRealPath("/images");
			System.out.println("upload_path: " + upload_path);
			String originalName = files.getOriginalFilename();

			try {
				// 파일 저장 디렉토리 생성
				File uploadDir = new File(upload_path);
				if (!uploadDir.exists()) {
					uploadDir.mkdirs();
				}

				// 파일명 설정
				String saveFileName = System.currentTimeMillis() + "-" + originalName;
				File saveFile = new File(uploadDir, saveFileName);

				// 파일 저장
				files.transferTo(saveFile);

				// 파일 업로드 성공 메시지 또는 데이터베이스에 파일 정보 저장 등의 추가 작업 수행
				dto.setSavedFile(saveFileName);

			} catch (IOException e) {
				System.out.println("파일 업로드 실패: " + e.getMessage());
			}
		} else {
			dto.setSavedFile("");
		}

		String userName = crewBoardService.getUserName(userEmail);
		int notice = Integer.parseInt(request.getParameter("notice"));
		int maxNum = crewBoardService.maxNum();

		dto.setBoard_num(maxNum + 1);
		dto.setCrew_num(dto.getCrew_num());
		dto.setEmail(userEmail);
		dto.setName(userName);

		if (notice == 1) {	// 공지글일 때
			dto.setNotice(1);

			// 공지 알림
			String crewName = crewBoardService.getCrewName(dto.getCrew_num());
			String alertContent = "[" + crewName + "]" + " 새 공지가 등록되었습니다.";
			Date today = new Date();
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
			List<Map<String, String>> crewMember = crewSettingService.getCrewMemberList(dto.getCrew_num());

			// 크루 멤버수만큼 my_alert 테이블에 insert
            for (Map<String, String> stringStringMap : crewMember) {
                int alertMaxNum = mypageService.maxMyalertNum();
                mypageService.insertMyAlert(alertMaxNum + 1, "공지",
                        alertContent, date.format(today), stringStringMap.get("MEM_EMAIL"), dto.getCrew_num());
            }

			// crew_alert 테이블에 insert
			mypageService.insertCrewAlert(mypageService.maxCalertNum() + 1, dto.getCrew_num(),
					"공지", alertContent, date.format(today));

		} else {	// 일반 게시글일 때
			dto.setNotice(0);
		}

		crewBoardService.insertData(dto);

		mav.setViewName("redirect:/board/list?crewNum=" + dto.getCrew_num());

		return mav;
	}

	@RequestMapping(value = "list", method = {RequestMethod.POST,RequestMethod.GET})
	public ModelAndView list(@RequestParam("crewNum") int crewNum, HttpServletRequest request) throws Exception {

		HttpSession session = request.getSession();
		String userEmail = (String)session.getAttribute("email");

		ModelAndView mav = new ModelAndView();

		if(userEmail == null) {
			mav.setViewName("redirect:/");
			return mav;
		}

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

		int dataCount = crewBoardService.getDataCount(searchKey, searchValue, crewNum);

		int numPerPage = 10;	// 한 페이지당 10개의 게시글
		int totalPage = myUtil.getPageCount(numPerPage, dataCount);

		if(currentPage > totalPage)
			currentPage = totalPage;

		int start = dataCount - numPerPage * (currentPage - 1);
		int end = dataCount - (currentPage * numPerPage) + 1;

		List<CrewBoardDTO> lists = crewBoardService.getLists(start, end,
				searchKey, searchValue, crewNum,currentPage,totalPage);

		String param = "";
		if(searchValue != null && !searchValue.isEmpty()) {
			param += "searchKey=" + searchKey;
			param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
		}

		String listUrl = "/board/list?crewNum=" + crewNum;

		if(!param.isEmpty())
			listUrl = listUrl + "&" + param;

		String pageIndexList = myUtil.pageIndexList(currentPage, totalPage, listUrl);

		String articleUrl ="/board/article?crewNum=" + crewNum + "&pageNum=" + currentPage;

		if(!param.isEmpty())
			articleUrl = articleUrl + "&" + param;

		mav.setViewName("board/list");

		mav.addObject("crew_num", crewNum);
		mav.addObject("lists", lists);
		mav.addObject("pageIndexList", pageIndexList);
		mav.addObject("dataCount", dataCount);
		mav.addObject("articleUrl", articleUrl);
		mav.addObject("pageNum", currentPage);

		int crewMaxNum = crewBoardService.crewmaxNum(crewNum);

		mav.addObject("crewMaxNum",crewMaxNum);

		// 게시판 상단 Title-----------------------------------
		Map<String, Object> boardTitle = crewBoardService.boardTitle(crewNum);
		mav.addObject("boardTitle", boardTitle);
		// -----------------------------------게시판 상단 Title

		// 모임장(캡틴)인 경우 '공지' 버튼 보이게 하기
		String checkCaptain = crewBoardService.checkCaptain(userEmail, crewNum);
		mav.addObject("checkCaptain", checkCaptain);

		return mav;
	}

	@RequestMapping(value = "article", method = {RequestMethod.POST,RequestMethod.GET})
	public ModelAndView article(HttpServletRequest request) throws Exception {

		HttpSession session = request.getSession();
		String userEmail = (String)session.getAttribute("email");

		ModelAndView mav = new ModelAndView();

		if(userEmail == null) {
			mav.setViewName("redirect:/");
			return mav;
		}

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
			mav.setViewName("redirect:/board/list?pageNum=" + pageNum);
			return mav;
		}

		String param = "pageNum=" + pageNum;
		if(searchValue != null && !searchValue.isEmpty()) {
			param += "&searchKey=" + searchKey;
			param += "&searchValue=" + URLEncoder.encode(searchValue,"UTF-8");
		}

		mav.addObject("dto", dto);
		mav.addObject("params", param);
		mav.addObject("email", userEmail);

		// 이미지 불러오기------------------------------------------
		String fileName = crewBoardService.getFileName(num);
		if (fileName != null && !fileName.isEmpty()) {
			mav.addObject("filepath", fileName);
		}
		// ------------------------------------------이미지 불러오기

		// 댓글 불러오기------------------------------
		List<CrewCommentDTO> lists = crewCommentService.getLists(dto.getBoard_num());
		mav.addObject("lists", lists);
		// ------------------------------댓글 불러오기

		mav.setViewName("board/article");

		return mav;
	}

	@RequestMapping(value = "updated",
			method = {RequestMethod.POST,RequestMethod.GET})
	public ModelAndView updated(HttpServletRequest request) throws Exception {

		int num = Integer.parseInt(request.getParameter("board_num"));
		String pageNum = request.getParameter("pageNum");
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");

		ModelAndView mav = new ModelAndView();

		if(searchValue != null) {
			searchValue = URLDecoder.decode(searchValue, "UTF-8");
		}

		CrewBoardDTO dto = crewBoardService.getReadData(num);

		if(dto == null) {
			mav.setViewName("redirect:/");
			return mav;
		}

		String param = "pageNum=" + pageNum;
		if(searchValue != null && !searchValue.isEmpty()) {
			param += "&searchKey=" + searchKey;
			param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
		}

		mav.setViewName("board/updated");

		mav.addObject("dto", dto);
		mav.addObject("pageNum", pageNum);
		mav.addObject("params", param);
		mav.addObject("searchKey", searchKey);
		mav.addObject("searchValue", searchValue);

		return mav;
	}

	@RequestMapping(value = "updated_ok",
			method = RequestMethod.POST)
	public ModelAndView updated_ok(CrewBoardDTO dto, HttpServletRequest request) throws Exception {

		HttpSession session = request.getSession();
		String userEmail = (String)session.getAttribute("email");

		ModelAndView mav = new ModelAndView();

		if(userEmail == null) {
			mav.setViewName("redirect:/");
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

		crewBoardService.updateData(dto);

		mav.setViewName("redirect:/board/list?crewNum=" + dto.getCrew_num() + "&" + param);

		return mav;
	}

	@RequestMapping(value = "deleted_ok",
			method = {RequestMethod.POST, RequestMethod.GET})
	public String deleted_ok(HttpServletRequest request) throws Exception {

		int num = Integer.parseInt(request.getParameter("board_num"));

		String fileName = crewBoardService.getFileName(num);
		// 파일명이 있는 경우 파일 삭제
		if (fileName != null && !fileName.isEmpty()) {
			String upload_path = request.getServletContext().getRealPath("/images");
			File file = new File(upload_path + "\\" + fileName);
			System.out.println("삭제된 파일: " + file);
			file.delete();	// 파일 삭제
		}

		crewBoardService.deleteData(num);

		return "DeleteSuccess";
	}

}
