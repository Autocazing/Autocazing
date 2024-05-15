from fastapi import Header, Depends
from typing import Union
from typing import List
from fastapi import APIRouter
from api.report.schema.report_response import ReportResponseSchema
from sqlalchemy.orm import Session
from db.mysql.models import reports
from core.dependencies import get_db

report_router = APIRouter(prefix="/report")

@report_router.get("", tags=["report_router"], response_model=List[ReportResponseSchema])
async def get_reports(store_id: Union[int, None] = Header(default=None), db: Session = Depends(get_db)):
    db = next(db)  # next()를 사용하여 제너레이터에서 세션을 가져옵니다.
    store_reports = db.query(reports.Reports).filter(reports.Reports.store_id == store_id).all()

    return store_reports

@report_router.get("/{report_id}", tags=["report_router"], response_model=ReportResponseSchema)
async def get_report_details(report_id: int,  store_id: Union[int, None] = Header(default=None), db: Session = Depends(get_db)):
    db = next(db)  # next()를 사용하여 제너레이터에서 세션을 가져옵니다.
    store_report_detail = db.query(reports.Reports).filter(reports.Reports.store_id == store_id).filter(reports.Reports.report_id == report_id).first()
    
    return store_report_detail