package com.different.dashboard.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.different.dashboard.dao.BoardDao;
import com.different.dashboard.dto.BoardDto;
import com.different.dashboard.dto.DetailDetailDto;
import com.different.dashboard.form.BoardForm;
import com.different.dashboard.form.DetailDetailForm;
import com.different.dashboard.dto.DetailDto;
import com.different.dashboard.form.DetailForm;

@Service
public class BoardService {

   @Autowired
   private BoardDao boardDao;

   //학생 목록 조회
   public List<BoardDto> getBoardList(BoardForm boardForm) throws Exception {

       return boardDao.getBoardList(boardForm);
   }
   
   //학생 학습기록 조회
 /*  public BoardDto getStudiesList(BoardForm boardForm) throws Exception {
	   
       BoardDto boardDto = new BoardDto();

       String searchType = boardForm.getSearch_type();

       if("S".equals(searchType)){
           
           int updateCnt = boardDao.updateBoardHits(boardForm);
       
           if (updateCnt > 0) {
               boardDto = boardDao.getBoardDetail(boardForm);
           }
           
       } else {
           
           boardDto = boardDao.getBoardDetail(boardForm);
       }

       return boardDto;
   }*/
   
   
   //학습 상세기록 조회
   //학생 목록 조회
   public List<DetailDto> getDetails(DetailForm DetailForm) throws Exception {
	   
       return boardDao.getDetails(DetailForm);
       
   }
   
   public List<DetailDetailDto> getDetailDetails(DetailDetailForm detailDetailForm) throws Exception {
	   
       return boardDao.getDetailDetails(detailDetailForm);
       
   }
   
}
