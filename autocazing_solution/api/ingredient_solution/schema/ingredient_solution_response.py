from pydantic import BaseModel
from typing import Dict

class IngredientSolutionResponse(BaseModel):
    status: str
    optimal_sales: Dict[str, float]