package com.cruise.project_cruise.controller.board;

import com.cruise.project_cruise.dto.CrewBoardDTO;
import com.cruise.project_cruise.dto.CrewCommentDTO;
import com.cruise.project_cruise.service.*;
import com.cruise.project_cruise.util.CrewBoardUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/board/")
@RestController
public class CrewBoardController {

	@Autowired
	private CrewAlertService crewAlertService;
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
	public JSONArray created_ok(CrewBoardDTO dto,
								   @RequestParam(value = "files", required = false) MultipartFile files,
								   HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 세션에서 이메일 가져오기
        HttpSession session = request.getSession();
        String userEmail = (String) session.getAttribute("email");

        //ModelAndView mav = new ModelAndView();

        if (userEmail == null) {
            //mav.setViewName("redirect:/");
            return null;
        }

        if (!files.isEmpty()) {
            // 이미지 저장 경로

            String upload_path = request.getServletContext().getRealPath("/images").replace("\\", "/");


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

        JSONArray jsonArray = null;

        if (notice == 1) {    // 공지글일 때
            dto.setNotice(1);

            // 공지 알림
            String crewName = crewBoardService.getCrewName(dto.getCrew_num());
            String content = "[" + crewName + "]" + " 새 공지가 등록되었습니다.";
            // 알림 날짜 설정
            Date today = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String todayStr = dateFormat.format(today);

            List<Map<String, String>> crewMember = crewSettingService.getCrewMemberList(dto.getCrew_num());

            crewBoardService.insertData(dto);

            // 은지 : crewBoardService에 insert를 성공하면 alert 테이블에 insert 되도록 수정했어..!
            // 크루 멤버수만큼 my_alert 테이블에 insert
            // 윤하 : 언니..! crewNum도 같이 insert해서 추가했어!!

            JSONObject jsonResponse = new JSONObject();
            jsonArray = new JSONArray();
            HashMap<String, Object> hashMap = new HashMap<>();

            for (Map<String, String> stringStringMap : crewMember) {
                int alertNum = mypageService.maxMyalertNum() + 1;
                mypageService.insertMyAlert(alertNum, dto.getCrew_num(), "공지",
                        content, todayStr, stringStringMap.get("MEM_EMAIL"),dto.getBoard_num());

                hashMap.put("alertEmailsList", stringStringMap.get("MEM_EMAIL"));

                jsonResponse = new JSONObject(hashMap);
                jsonArray.add(jsonResponse);
            }

            System.out.println("알림보내야 할 이메일들 >>>>>>>>> " + jsonArray);
            System.out.println("알림보내야 할 이메일들222 >>>>>>>>> " + jsonArray.toString());

            // crew_alert 테이블에 insert
            // 은지 : 공지 알림 형식 수정할게요~!
            String articleSubjectSub = dto.getBoard_subject().substring(0, 4) + "...";
            String crewAlertContent = "\"" + articleSubjectSub + "\" 가 등록되었습니다.";
            crewAlertService.insertCrewAlert(crewAlertService.cAlertMaxNum() + 1, dto.getCrew_num(),
                    "공지", crewAlertContent, todayStr);

            // 윤하 : 알림가야할 이메일들 클라이언트로 보내기

            //mav.setViewName("redirect:/board/list?crewNum=" + dto.getCrew_num());

        } else {    // 일반 게시글일 때
            dto.setNotice(0);
			crewBoardService.insertData(dto);
        }

        return jsonArray;
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
		System.out.println(lists);

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
		String crewName = crewBoardService.getCrewName(dto.getCrew_num());

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
		mav.addObject("crewName", crewName);

		return mav;
	}

	@RequestMapping(value = "updated_ok",
			method = RequestMethod.POST)
	public ModelAndView updated_ok(CrewBoardDTO dto,
								   @RequestParam(value = "files", required = false) MultipartFile files,
								   HttpServletRequest request) throws Exception {

		HttpSession session = request.getSession();
		String userEmail = (String)session.getAttribute("email");

		ModelAndView mav = new ModelAndView();

		if(userEmail == null) {
			mav.setViewName("redirect:/");
			return mav;
		}

		String fileName = crewBoardService.getFileName(dto.getBoard_num());

		// 이미지 없는 글에서 이미지 추가 (created와 동일)
		if (fileName == null || fileName.isEmpty()) {
			if (!files.isEmpty()) {
				// 이미지 저장 경로
				String upload_path = request.getServletContext().getRealPath("/images").replace("\\", "/");
				System.out.println("upload_path: " + upload_path);

				String originalName = files.getOriginalFilename();

				try {
					// 파일 저장 디렉토리 생성
					File uploadDir = new File(upload_path);
					if (!uploadDir.exists()) {
						uploadDir.mkdirs();
					}

					// 파일명 설정
					String saveFileName = System.currentTimeMillis() + "_" + originalName;
					File saveFile = new File(uploadDir, saveFileName);

					// 파일 저장
					files.transferTo(saveFile);

					dto.setSavedFile(saveFileName);

				} catch (IOException e) {
					System.out.println("파일 업로드 실패: " + e.getMessage());
				}
			} else {	// 이미지 없는 글에서 글만 수정
				dto.setSavedFile("");
			}
		}

		// 이미지 있는 글에서 이미지 변경: 기존 파일 삭제 후 새 파일 추가
		if (fileName != null && !files.isEmpty()) {
			// 이미지 저장 경로
			String upload_path = request.getServletContext().getRealPath("/images").replace("\\", "/");
			String originalName = files.getOriginalFilename();

			try {
				// 파일 풀네임 불러오기
				File file = new File(upload_path + File.separator + fileName);
				// 파일이 존재하는지 확인 (기존 파일 삭제)
				if (file.exists()) {
					boolean deleted = file.delete();
					if (deleted) {
						System.out.println("파일 삭제 성공: " + file);
					} else {
						System.out.println("파일 삭제 실패: " + file);
					}
				} else {
					System.out.println("존재하지 않는 파일: " + file);
				}

				// 파일 저장 디렉토리 생성
				File uploadDir = new File(upload_path);

				// 파일명 설정
				String saveFileName = System.currentTimeMillis() + "_" + originalName;
				File saveFile = new File(uploadDir, saveFileName);

				// 파일 저장
				files.transferTo(saveFile);

				dto.setSavedFile(saveFileName);

			} catch (IOException e) {
				System.out.println("파일 업로드 실패: " + e.getMessage());
			}

			// 이미지 있는 글에서 글만 수정
		} else if (fileName != null && files.isEmpty()) {
			dto.setSavedFile(fileName);
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
			File file = new File(upload_path + File.separator + fileName);

			// 파일이 존재하는지 확인
			if (file.exists()) {
				boolean deleted = file.delete();
				if (deleted) {
					System.out.println("파일 삭제 성공: " + file);
				} else {
					System.out.println("파일 삭제 실패: " + file);
				}
			} else {
				System.out.println("존재하지 않는 파일: " + file);
			}
		}

		crewBoardService.deleteData(num);	// crew_board 테이블에서 게시글 삭제

		return "DeleteSuccess";
	}

}
