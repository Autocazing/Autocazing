from fastapi import Request, Query, Depends, HTTPException
from fastapi import APIRouter
from sqlalchemy.orm import Session
from core.dependencies import get_db
from api.ingredient_solution.schema.ingredient_solution_response import IngredientSolutionResponse
from db.mysql.models.reports import IngredientSolution


ingredient_solution_router = APIRouter(prefix="/{report_id}/ingredient-solution")

@ingredient_solution_router.get("/{ingredient_id}", tags=["ingreient_solution_router"], response_model=IngredientSolutionResponse)
async def get_ingredient_solution(request: Request, report_id: int, ingredient_id: int, db: Session = Depends(get_db)):
    login_id = request.headers.get("loginId")
    if login_id is None:
        raise HTTPException(status_code=400, detail="loginId header is required")
    
    # 주어진 report_id와 ingredient_id로 최적화 결과 조회
    ingredient_solutions = db.query(IngredientSolution).filter(
        IngredientSolution.report_id == report_id,
        IngredientSolution.ingredient_id == ingredient_id
    ).all()

    if not ingredient_solutions:
        raise HTTPException(status_code=404, detail="No ingredient solution found for the given report_id and ingredient_id")

    optimal_sales = {solution.menu_name: solution.sale_quantity for solution in ingredient_solutions}

    ingredient_solution = IngredientSolutionResponse(
        status="Optimal",
        optimal_sales=optimal_sales
    )

    return ingredient_solution
