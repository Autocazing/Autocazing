from sqlalchemy import *
from sqlalchemy.sql import func
from db.mysql.base import Base

class PredictedSales(Base):
    __tablename__ = 'predicted_sales'
    year_month = Column(String(length=6), primary_key=True, index=True)

    login_id = Column(Integer, nullable=False)
    predicted_monthly_sales = Column(Integer, nullable=False)
