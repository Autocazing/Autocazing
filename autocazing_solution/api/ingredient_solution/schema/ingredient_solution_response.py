from pydantic import BaseModel
from typing import Dict

class IngredientSolutionResponse:
    status: str
    optimal_sales: Dict[str, float]