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
async def get_reports(request: Request, month: Union[int, None] = Query(None, ge=1, le=12), db: Session = Depends(get_db)):
    db = next(db)
    login_id = request.headers.get("loginId")
    if login_id is None:
        raise HTTPException(status_code=400, detail="store_id header is required")
    query = db.query(reports.Reports).filter(reports.Reports.store_id == login_id)  # 가게가 가진 모든 리포트 조회
    if month is not None:
        query = query.filter(extract('month', reports.Reports.created_at) == month)
    
    store_reports = query.all()


    return store_reports

@report_router.get("/{report_id}", tags=["report_router"], response_model=ReportResponseSchema)
async def get_report_details(request: Request, report_id: int, db: Session = Depends(get_db)):
    db = next(db)
    login_id = request.headers.get("loginId")
    if login_id is None:
        raise HTTPException(status_code=400, detail="store_id header is required")
    store_report_detail = db.query(reports.Reports).filter(reports.Reports.store_id == login_id).filter(reports.Reports.report_id == report_id).first()
    if store_report_detail is None:
        raise HTTPException(status_code=404, detail="Report not found")
    
    return store_report_detail