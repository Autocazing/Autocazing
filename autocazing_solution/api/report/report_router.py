from fastapi import Depends
from typing import List
from fastapi import APIRouter
from api.report.schema.report_response import ReportResponseSchema
from sqlalchemy.orm import Session
from main import get_db

report_router = APIRouter(prefix="/report")

@report_router.get("", tags=["report_router"], response_model=List[ReportResponseSchema])
async def get_reports(db: Session = Depends(get_db)):
    
    return []

@report_router.get("/{report_id}", tags=["report_router"], response_model=ReportResponseSchema)
async def get_report_details(report_id: int, db: Session = Depends(get_db)):

    
    return ReportResponseSchema