from pydantic import BaseModel

class PredictedMonthlySalesDto(BaseModel):
    predicted_monthly_sales: int