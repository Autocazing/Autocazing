from typing import List
from pydantic import BaseModel
from datetime import datetime

class IngredientSolution(BaseModel):
    ingredient_id: int
    menu_name: str
    sale_quantity: int

class ExpirationSpecifics(BaseModel):
    ingredient_id: int
    ingredient_name: str

class OnDeliveryIngredients(BaseModel):
    ingredient_name: str

class ReportResponseSchema(BaseModel):
    report_id: int
    expected_monthly_sales: int
    current_monthly_sales: int
    expiration_specifics: List[ExpirationSpecifics]
    on_delivery_ingredients: List[OnDeliveryIngredients]
    ingredient_solutions: List[IngredientSolution]
    created_at: datetime