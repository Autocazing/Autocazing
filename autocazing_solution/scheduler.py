from sqlalchemy.orm import Session
from db.mysql.session import mysqlSession
from apscheduler.schedulers.background import BackgroundScheduler
import requests
import json
from datetime import datetime
from sqlalchemy import func
from db.mysql.models.reports import Reports
from db.mysql.models.predicted_sales import PredictedSales

sched = BackgroundScheduler(timezone='Asia/Seoul')

def get_db_session() -> Session:
    return next(mysqlSession.get_db())

@sched.scheduled_job('cron', month='1', day='1', hour='00', minute='00', id='predict_monthly_sales')  # 월 매출 예상하는 코드 돌리는 스케줄러
def predict_monthly_sales():
    db = get_db_session()
    # API 요청을 통해 데이터 가져오기
    response = requests.post('http://70.12.130.111:8088/predict/revenue')
    data = response.json()
    
    # 데이터베이스에 저장
    
    for item in data:
        year_month = item['Date']
        predicted_sales = item['Value']
        
        predicted_sale = PredictedSales(
            year_month=year_month,
            login_id=1,
            predicted_monthly_sales=predicted_sales
        )
        db.add(predicted_sale)
    db.commit()
    print(f"Predicted monthly sales: {data}")
    db.close()


@sched.scheduled_job('cron', hour='01', minute='00', id='make_report')  # 00시 05분에 리포트 만드는 스케줄러
def make_report():
    db = get_db_session()
    run_make_report(db)
    db.close()

# 리포트를 생성하는 작업 함수
def run_make_report(db: Session):
    today = datetime.now().strftime('%Y-%m-%d')
    report_exists = db.query(Reports).filter(func.date(Reports.created_at) == today).first()
    
    if not report_exists:
        report = Reports(
            login_id=1,  # 적절한 login_id 값을 설정해야 합니다.
            expected_monthly_sales=1000,  # 예시 값입니다. 실제 값을 설정해야 합니다.
            current_monthly_sales=500  # 예시 값입니다. 실제 값을 설정해야 합니다.
        )
        db.add(report)
        db.commit()
        print(f"Report created for {today}")
    else:
        print(f"Report already exists for {today}")


def start_image_scheduler():
    sched.start()