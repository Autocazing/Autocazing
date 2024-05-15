from fastapi import Request, Query, Depends, HTTPException
from typing import Union
from typing import List
from fastapi import APIRouter
from sqlalchemy.orm import Session
from sqlalchemy import extract
from core.dependencies import get_db
from api.ingredient_solution.schema.ingredient_solution_response import IngredientSolutionResponse
from db.mysql.models.menu_ingredients import MenuIngredients
from db.mysql.models.menus import Menus
from pulp import LpMaximize, LpProblem, LpVariable, lpSum, LpStatus, value


report_router = APIRouter(prefix="/ingredient-solution")

@report_router.get("/{ingredient_id}", tags=["ingreient_router"], response_model=IngredientSolutionResponse)
async def get_report_details(request: Request, ingredient_id: int, db: Session = Depends(get_db)):
    login_id = request.headers.get("loginId")
    if login_id is None:
        raise HTTPException(status_code=400, detail="loginId header is required")
    # 주어진 ingredient_id로 메뉴를 검색
    menus = db.query(MenuIngredients.menu_id).filter(MenuIngredients.ingredient_id == ingredient_id).all()
    if not menus:
        raise HTTPException(status_code=404, detail="No menus found for the given ingredient_id")
    menu_ids = [menu.menu_id for menu in menus]
    # 최적화 문제 해결
    ingredient_solution = solve_ingredient_solution(menu_ids, ingredient_id, db)

    return ingredient_solution

def solve_ingredient_solution(menu_ids: List[int], ingredient_id: str, db: Session) -> IngredientSolutionResponse:
    # 메뉴별 가격 가져오기
    menu_prices = {menu.id: menu.price for menu in db.query(Menu).filter(Menu.id.in_(menu_ids)).all()}
    
    # 재료 사용량 가져오기
    ingredient_usage = {
        menu.menu_id: menu.quantity for menu in db.query(MenuIngredients).filter(MenuIngredients.menu_id.in_(menu_ids), MenuIngredients.ingredient_id == ingredient_id).all()
    }
    
    # 문제 정의
    model = LpProblem(name="Cafe_Product_Mix", sense=LpMaximize)
    # 변수 정의
    variables = {menu_id: LpVariable(name=f"menu_{menu_id}", lowBound=0) for menu_id in menu_ids}
    # 목적 함수 (가격 기반)
    model += lpSum([menu_prices[menu_id] * variables[menu_id] for menu_id in menu_ids])
    # 제약 조건 (해당 재료의 총 사용량이 1000을 넘지 않도록)
    model += lpSum([ingredient_usage[menu_id] * variables[menu_id] for menu_id in menu_ids]) <= 1000, f"{ingredient_id}_Usage"
    # 문제 풀기
    model.solve()
    # 결과 출력
    optimal_sales = {f"menu_{menu_id}": value(variables[menu_id]) for menu_id in menu_ids}
    
    ingredient_solution = IngredientSolutionResponse(
        status=LpStatus[model.status],
        optimal_sales=optimal_sales
    )
    
    return ingredient_solution
