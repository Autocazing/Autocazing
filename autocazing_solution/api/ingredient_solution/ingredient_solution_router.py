from fastapi import Request, Query, Depends, HTTPException
from typing import Union
from typing import List
from fastapi import APIRouter
from sqlalchemy.orm import Session
from sqlalchemy import extract, func
from core.dependencies import get_db
from api.ingredient_solution.schema.ingredient_solution_response import IngredientSolutionResponse
from db.mysql.models.menu_ingredients import MenuIngredients
from db.mysql.models.menus import Menus
from db.mysql.models.reports import Reports, ExpirationSpecifics
from pulp import LpMaximize, LpProblem, LpVariable, lpSum, LpStatus, value


ingredient_solution_router = APIRouter(prefix="/ingredient-solution")

@ingredient_solution_router.get("/{ingredient_id}", tags=["ingreient_solution_router"], response_model=IngredientSolutionResponse)
async def get_ingredient_solution(request: Request, ingredient_id: int, db: Session = Depends(get_db)):
    login_id = request.headers.get("loginId")
    if login_id is None:
        raise HTTPException(status_code=400, detail="loginId header is required")
    # 주어진 ingredient_id로 메뉴를 검색
    menus = db.query(MenuIngredients.menu_id).filter(MenuIngredients.ingredient_id == ingredient_id).all()
    if not menus:
        raise HTTPException(status_code=404, detail="No menus found for the given ingredient_id")
    menu_ids = [menu.menu_id for menu in menus]

    optimal_sales_temp = {
        "test1" : 30.0,
        "test2" : 40.0,
        "test3" : 60.0
    }
    
    ingredient_solution = IngredientSolutionResponse(
        status="Optimal",
        optimal_sales=optimal_sales_temp
    )

    return ingredient_solution

