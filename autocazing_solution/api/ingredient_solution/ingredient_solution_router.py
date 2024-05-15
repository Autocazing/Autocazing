from fastapi import Request, Query, Depends, HTTPException
from typing import Union
from typing import List
from fastapi import APIRouter
from sqlalchemy.orm import Session
from sqlalchemy import extract
from core.dependencies import get_db
from api.ingredient_solution.schema.ingredient_solution_response import IngredientSolutionResponse
from pulp import LpMaximize, LpProblem, LpVariable, lpSum, LpStatus, value


report_router = APIRouter(prefix="/ingredient-solution")

@report_router.get("/{ingredient_id}", tags=["ingreient_router"], response_model=IngredientSolutionResponse)
async def get_report_details(request: Request, ingredient_id: int, db: Session = Depends(get_db)):
    login_id = request.headers.get("loginId")
    if login_id is None:
        raise HTTPException(status_code=400, detail="loginId header is required")
    
    ingredient_solution = IngredientSolutionResponse()
    
    return ingredient_solution
