package com.different.dashboard.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.different.dashboard.dao.*;
import com.different.dashboard.dto.*;
import com.different.dashboard.form.*;

@Service
public class BoardService {

   @Autowired
   private BoardDao boardDao;
   
   @Autowired
   private ResultDao resultDao;

   //학생 목록 조회
   public List<BoardDto> getBoardList(BoardForm boardForm) throws Exception {

       return boardDao.getBoardList(boardForm);
   }
   
   public BoardDto getStuInfo(String id) throws Exception{
	   
	   return boardDao.getStuInfo(id);
   }
   public List<ResultDto> getResultList(String id) throws Exception{

	   return resultDao.getResultList(id);
   }
   
   public List<ResultDto> getScorePerDay(String id) throws Exception{
	   
	   return resultDao.getScorePerDay(id);
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
   
}
