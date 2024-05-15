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

def solve_ingredient_solution(menu_ids: List[int], ingredient_id: str) -> IngredientSolutionResponse:
    # 메뉴별 이익과 재료 사용량 정의 (예시 데이터)
    profit_per_unit = {
        1: 2.5,  # menu_id 1: Frappuccino
        2: 2.0,  # menu_id 2: CafeLatte
        3: 2.5   # menu_id 3: Sandwich
    }
    
    ingredient_usage = {
        1: {"milk": 0.2},  # menu_id 1: Frappuccino
        2: {"milk": 0.15}, # menu_id 2: CafeLatte
        3: {"bread": 0.1}  # menu_id 3: Sandwich
    }
    
    # 문제 정의
    model = LpProblem(name="Cafe_Product_Mix", sense=LpMaximize)
    # 변수 정의
    variables = {menu_id: LpVariable(name=f"menu_{menu_id}", lowBound=0) for menu_id in menu_ids}
    # 목적 함수
    model += lpSum([profit_per_unit[menu_id] * variables[menu_id] for menu_id in menu_ids])
    # 제약 조건 (예시: 해당 재료의 총 사용량이 1000을 넘지 않도록)
    model += lpSum([ingredient_usage[menu_id][ingredient_id] * variables[menu_id] for menu_id in menu_ids if ingredient_id in ingredient_usage[menu_id]]) <= 1000, f"{ingredient_id}_Usage"
    # 문제 풀기
    model.solve()
    # 결과 출력
    optimal_sales = {f"menu_{menu_id}": value(variables[menu_id]) for menu_id in menu_ids}
    
    ingredient_solution = IngredientSolutionResponse(
        status=LpStatus[model.status],
        optimal_sales=optimal_sales
    )
    
    return ingredient_solution
