from fastapi import APIRouter
from api.monthly_sales.schema.monthly_sales_response import PredictedMonthlySalesSchema

monthly_sales_router = APIRouter(prefix="/predicted-monthly-sales")

@monthly_sales_router.get("", tags=["monthly_sales_router"], response_model=PredictedMonthlySalesSchema)
async def get_monthly_sales():
    
    return PredictedMonthlySalesSchema(predicted_monthly_sales=300_000)