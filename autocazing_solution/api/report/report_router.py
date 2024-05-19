from fastapi import Request, Query, Depends, HTTPException
from typing import Union
from typing import List
from fastapi import APIRouter
from api.report.schema.report_response import ReportResponseSchema
from sqlalchemy.orm import Session
from sqlalchemy import extract
from db.mysql.models import reports
from core.dependencies import get_db

report_router = APIRouter(prefix="/report")

@report_router.get("", tags=["report_router"], response_model=List[ReportResponseSchema])
async def get_reports(request: Request, year: Union[int, None] = Query(None), month: Union[int, None] = Query(None, ge=1, le=12), day: Union[int, None] = Query(None, ge=1, le=31), db: Session = Depends(get_db)):
    login_id = request.headers.get("loginId")
    if login_id is None:
        raise HTTPException(status_code=400, detail="loginId header is required")
    query = db.query(reports.Reports).filter(reports.Reports.login_id == login_id)  # 가게가 가진 모든 리포트 조회
    if year is not None:    # 년도 필터링
        query = query.filter(extract('year', reports.Reports.created_at) == year)
    if month is not None:   # 월 필터링
        query = query.filter(extract('month', reports.Reports.created_at) == month)
    if day is not None: # 일 필터링
        query = query.filter(extract('day', reports.Reports.created_at) == day)
    
    store_reports = query.all()
    store_reports.sort(key=lambda report: report.created_at.day)

    return store_reports

@report_router.get("/{report_id}", tags=["report_router"], response_model=ReportResponseSchema)
async def get_report_details(request: Request, report_id: int, db: Session = Depends(get_db)):
    login_id = request.headers.get("loginId")
    if login_id is None:
        raise HTTPException(status_code=400, detail="loginId header is required")
    store_report_detail = db.query(reports.Reports).filter(reports.Reports.login_id == login_id).filter(reports.Reports.report_id == report_id).first()
    if store_report_detail is None:
        raise HTTPException(status_code=404, detail="Report not found")
    
    return store_report_detail