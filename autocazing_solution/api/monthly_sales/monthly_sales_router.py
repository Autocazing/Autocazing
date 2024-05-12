from fastapi import APIRouter
from api.monthly_sales.schema.monthly_sales_response import PredictedMonthlySalesDto

monthly_sales_router = APIRouter(prefix="/predicted-monthly-sales")

@monthly_sales_router.get("", tags=["monthly_sales_router"], response_model=PredictedMonthlySalesDto)
async def get_monthly_sales():
    
    return PredictedMonthlySalesDto(predicted_monthly_sales=300_000)