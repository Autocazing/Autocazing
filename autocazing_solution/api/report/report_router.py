from fastapi import APIRouter
from api.monthly_sales.schema.monthly_sales_response import PredictedMonthlySalesSchema

report_router = APIRouter(prefix="/report")

@report_router.get("", tags=["report_router"])
async def get_reports():
    
    return "test"

@report_router.get("/{report_id}", tags=["report_router"])
async def get_report_details(report_id: int):
    
    return "test"