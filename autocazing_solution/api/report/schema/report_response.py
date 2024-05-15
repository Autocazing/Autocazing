from typing import List
from pydantic import BaseModel
from datetime import datetime

class ExpirationSpecifics(BaseModel):
    ingredient_name: str

class OnDeliveryIngredients(BaseModel):
    ingredient_name: str

class ReportResponseSchema(BaseModel):
    report_id: int
    store_id: int
    expected_monthly_sales: int
    current_monthly_sales: int
    expiration_specifics: List[ExpirationSpecifics]
    on_delivery_ingredients: List[OnDeliveryIngredients]
    created_at: datetime