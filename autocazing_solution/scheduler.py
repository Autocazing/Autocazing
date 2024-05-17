from sqlalchemy.orm import Session
from db.mysql.session import mysqlSession
from apscheduler.schedulers.background import BackgroundScheduler
import requests
from datetime import datetime
from sqlalchemy import func
from db.mysql.models.stores import Stores
from db.mysql.models.orders import Orders, OrderSpecifics
from db.mysql.models.reports import Reports
from db.mysql.models.predicted_sales import PredictedSales

sched = BackgroundScheduler(timezone='Asia/Seoul')

def get_db_session() -> Session:
    return next(mysqlSession.get_db())

@sched.scheduled_job('cron', month='1', day='1', hour='00', minute='00', id='predict_monthly_sales')  # 월 매출 예상하는 코드 돌리는 스케줄러
def predict_monthly_sales():
    db = get_db_session()
    # 모든 상점을 조회
    stores = db.query(Stores).all()

    # 각 상점에 대해 매출 예측 수행
    for store in stores:
        login_id = store.login_id

        # API 요청을 통해 데이터 가져오기
        response = requests.post('http://70.12.130.111:8088/predict/revenue', params={'login_id': login_id})
        data = response.json()

        # 데이터베이스에 저장
        for item in data:
            year_month = item['Date']
            predicted_sales = item['Value']
            
            predicted_sale = PredictedSales(
                year_month=year_month,
                login_id=login_id,
                predicted_monthly_sales=predicted_sales
            )
            db.add(predicted_sale)
        db.commit()
        print(f"Predicted monthly sales for login_id {login_id}: {data}")

    db.close()

@sched.scheduled_job('cron', hour='12', minute='40', id='make_report')  # 01시 00분에 리포트 만드는 스케줄러
def make_report():
    db = get_db_session()
    run_make_report(db)
    db.close()

# 리포트를 생성하는 작업 함수
def run_make_report(db: Session):
    today = datetime.now().strftime('%Y-%m-%d')
    year_month = datetime.now().strftime('%Y%m')
    stores = db.query(Stores).all()

    # 각 상점에 대해 보고서 생성 또는 존재 여부 확인
    for store in stores:
        login_id = store.login_id

        report_exists = db.query(Reports).filter(
            func.date(Reports.created_at) == today,
            Reports.login_id == login_id
        ).first()
        
        if not report_exists:
            # expected_monthly_sales 가져오기
            expected_sales = db.query(PredictedSales).filter(
                PredictedSales.year_month == year_month,
                PredictedSales.login_id == login_id
            ).first()
            expected_monthly_sales = expected_sales.predicted_monthly_sales if expected_sales else 0

            # current_monthly_sales 계산
            current_monthly_sales = db.query(
                func.sum(OrderSpecifics.menu_price * OrderSpecifics.menu_quantity)
            ).join(Orders).filter(
                Orders.login_id == login_id,
                func.date_format(Orders.created_at, '%Y%m') == year_month
            ).scalar() or 0

            report = Reports(
                login_id=login_id,
                expected_monthly_sales=expected_monthly_sales,
                current_monthly_sales=current_monthly_sales
            )
            db.add(report)
            db.commit()
            print(f"Report created for login_id {login_id} on {today}")
        else:
            print(f"Report already exists for login_id {login_id} on {today}")

def start_image_scheduler():
    sched.start()

if __name__ == "__main__":
    start_image_scheduler()