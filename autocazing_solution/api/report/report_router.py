from typing import List
from fastapi import APIRouter
from api.report.schema.report_response import ReportResponseSchema

report_router = APIRouter(prefix="/report")

@report_router.get("", tags=["report_router"], response_model=List[ReportResponseSchema])
async def get_reports():
    
    return []

@report_router.get("/{report_id}", tags=["report_router"], response_model=ReportResponseSchema)
async def get_report_details(report_id: int):
    
    return ReportResponseSchema