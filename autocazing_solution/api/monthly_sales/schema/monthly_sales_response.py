from pydantic import BaseModel

class PredictedMonthlySalesSchema(BaseModel):
    predicted_monthly_sales: int